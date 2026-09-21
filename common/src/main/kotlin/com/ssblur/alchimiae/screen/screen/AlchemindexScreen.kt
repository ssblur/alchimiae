package com.ssblur.alchimiae.screen.screen

import com.ssblur.alchimiae.alchemy.ClientAlchemyHelper
import com.ssblur.alchimiae.screen.menu.AlchemindexMenu
import com.ssblur.alchimiae.screen.screen.widget.EffectButtonWidget
import com.ssblur.alchimiae.screen.screen.widget.ItemButtonWidget
import com.ssblur.unfocused.extension.SoundEventExtension.play
import com.ssblur.unfocused.screen.UnfocusedScreen
import com.ssblur.unfocused.screen.renderable.SinglePageBackground
import com.ssblur.unfocused.screen.widget.NextPageWidget
import com.ssblur.unfocused.screen.widget.PlainTextWidget
import com.ssblur.unfocused.screen.widget.PrevPageWidget
import com.ssblur.unfocused.screen.widget.TitleWidget
import net.minecraft.ChatFormatting
import net.minecraft.client.resources.language.I18n
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

class AlchemindexScreen(abstractContainerMenu: AlchemindexMenu, inventory: Inventory, component: Component):
  UnfocusedScreen<AlchemindexMenu>(abstractContainerMenu, inventory, component) {
  var effect: ResourceLocation? = null
  var item: Item? = null
  var page = 0

  override fun init() {
    imageWidth = 265
    imageHeight = 220
    leftPos = (this.width - imageWidth) / 2
    topPos = (this.height - imageHeight) / 2

    add(SinglePageBackground(leftPos, topPos, imageWidth, imageHeight))

    if(effect != null) initEffectPage()
    else if(item != null) initItemPage()
    else initIndexPage()

    super.init()
  }

  fun initEffectPage() {
    // show items which have selected effect

    // title
    add(TitleWidget(
      Component.translatable(effect!!.toLanguageKey("effect")),
      leftPos + 24, topPos + 24, imageWidth - 24, 64, false
    ))

    var y = topPadding

    if(I18n.exists(effect!!.toLanguageKey("effect", "description"))) {
      add(
        PlainTextWidget(
          Component.translatable(effect!!.toLanguageKey("effect", "description"))
            .withStyle(ChatFormatting.GRAY),
          leftPos + 24, topPos + 50, imageWidth - 24, 64, false
        )
      )
      y += 18
    }

    var x = leftPadding
    val w = imageWidth - rightPadding
    val h = imageHeight - bottomPadding
    var curPage = 0
    // filter entries to items which have this effect
    val items = ClientAlchemyHelper.EFFECTS.entries.filter { (key, value) ->
      key != Items.AIR && value?.contains(effect!!) ?: false
    }.map { // get the items
      it.key
    }.filterNotNull()
    for(i in items) {
      if(curPage == page) {
        add(ItemButtonWidget(leftPos + x, topPos + y, effectSize, i) {
          item = i
          effect = null
          page = 0
          rebuildWidgets()
          SoundEvents.UI_BUTTON_CLICK.play()
        })
      } else if(curPage > page) break
      x += itemSize + itemSpacing
      if((x + itemSize) > w) {
        y += itemSize + itemSpacing
        x = leftPadding
        if((y + effectSize) > h) {
          curPage++
          y = topPadding
        }
      }
    }
  }

  fun initItemPage() {
    add(TitleWidget(
      ItemStack(item!!).hoverName,
      leftPos + 24, topPos + 24, imageWidth - 24, 64, false
    ))

    val effects = ClientAlchemyHelper.EFFECTS.entries.filter{ (k, _) ->
      k == item
    }.flatMap { (_, v) -> v?.toList() ?: listOf() }
      .distinct()
      .sortedBy {
        I18n.get(it.toLanguageKey("effect"))
      }
    var y = topPadding
    var x = leftPadding
    val w = imageWidth - rightPadding
    val h = imageHeight - bottomPadding
    var curPage = 0
    for(e in effects) {
      if(curPage == page) {
        add(EffectButtonWidget(leftPos + x, topPos + y, effectSize, e) {
          effect = e
          item = null
          page = 0
          rebuildWidgets()
          SoundEvents.UI_BUTTON_CLICK.play()
        })
      } else if(curPage > page) break

      x += effectSize + effectSpacing
      if((x + effectSize) > w) {
        y += effectSize + effectSpacing
        x = leftPadding
        if((y + effectSize) > h) {
          curPage++
          y = topPadding
        }
      }
    }
  }

  fun initIndexPage() {
    val effects = ClientAlchemyHelper.EFFECTS.values.flatMap { it?.toList() ?: listOf() }
      .distinct()
      .sortedBy {
        I18n.get(it.toLanguageKey("effect"))
      }

    var headerDrawn = false
    var curPage = 0
    var y = topPadding
    var x = leftPadding
    val w = imageWidth - rightPadding
    val h = imageHeight - bottomPadding
    for(e in effects) {
      if(curPage == page) {
        if(!headerDrawn) {
          add(TitleWidget(
            Component.translatable("gui.alchimiae.effects"),
            leftPos + 24, topPos + 24, imageWidth - 24, 64, false
          ))
          headerDrawn = true
        }
        add(EffectButtonWidget(leftPos + x, topPos + y, effectSize, e) {
          effect = e
          item = null
          page = 0
          rebuildWidgets()
          SoundEvents.UI_BUTTON_CLICK.play()
        })
      } else if(curPage > page) break

      x += effectSize + effectSpacing
      if((x + effectSize) > w) {
        y += effectSize + effectSpacing
        x = leftPadding
        if((y + effectSize) > h) {
          curPage++
          y = topPadding
        }
      }
    }

    curPage++
    x = leftPadding
    y = topPadding
    val items = ClientAlchemyHelper.EFFECTS.keys.filterNotNull().filter {
      item != Items.AIR
    }
    for(i in items) {
      if(curPage == page) {
        if(!headerDrawn) {
          add(TitleWidget(
            Component.translatable("gui.alchimiae.items"),
            leftPos + 24, topPos + 24, imageWidth - 24, 64, false
          ))
          headerDrawn = true
        }
        add(ItemButtonWidget(leftPos + x, topPos + y, effectSize, i) {
          item = i
          effect = null
          page = 0
          rebuildWidgets()
          SoundEvents.UI_BUTTON_CLICK.play()
        })
      } else if(curPage > page) break
      x += itemSize
      if((x + itemSize) > w) {
        y += itemSize + itemSpacing
        x = leftPadding
        if((y + effectSize) > h) {
          curPage++
          y = topPadding
        }
      }
    }

    if(curPage != page) {
      add(NextPageWidget(leftPos + imageWidth - 44, topPos + imageHeight - 35) {
        page++
        rebuildWidgets()
      })
    }

    if(page > 0) {
      add(PrevPageWidget(leftPos + 21, topPos + imageHeight - 35) {
        page--
        rebuildWidgets()
      })
    }
  }

  companion object {
    val effectSize = 16
    val effectSpacing = 2
    val itemSize = 16
    val itemSpacing = 2
    val leftPadding = 24
    val rightPadding = 24
    val bottomPadding = 36
    val topPadding = 48
  }
}