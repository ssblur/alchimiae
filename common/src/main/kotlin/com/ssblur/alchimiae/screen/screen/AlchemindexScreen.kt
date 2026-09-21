package com.ssblur.alchimiae.screen.screen

import com.ssblur.alchimiae.alchemy.ClientAlchemyHelper
import com.ssblur.alchimiae.screen.menu.AlchemindexMenu
import com.ssblur.alchimiae.screen.screen.widget.EffectButtonWidget
import com.ssblur.alchimiae.screen.screen.widget.ItemButtonWidget
import com.ssblur.unfocused.extension.SoundEventExtension.play
import com.ssblur.unfocused.screen.UnfocusedScreen
import com.ssblur.unfocused.screen.renderable.SinglePageBackground
import com.ssblur.unfocused.screen.widget.*
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.language.I18n
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
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
      leftPos + 24, topPos + 24, imageWidth - 48, 64, false
    ))

    var y = TOP_PADDING

    val font = Minecraft.getInstance().font
    var captionHeight = 0
    if(I18n.exists(effect!!.toLanguageKey("effect", "description"))) {
      captionHeight = font.splitter.splitLines(
        I18n.get((effect!!.toLanguageKey("effect", "description"))),
        imageWidth - 48,
        Style.EMPTY
      ).size * font.lineHeight
      add(
        PlainTextWidget(
          Component.translatable(effect!!.toLanguageKey("effect", "description"))
            .withStyle(ChatFormatting.GRAY),
          leftPos + 24, topPos + 50, imageWidth - 48, captionHeight, false
        )
      )
      y += captionHeight + 2
    }

    var x = LEFT_PADDING
    val w = imageWidth - RIGHT_PADDING
    val h = imageHeight - BOTTOM_PADDING
    var curPage = 0
    // filter entries to items which have this effect
    val items = ClientAlchemyHelper.EFFECTS.entries.filter { (key, value) ->
      key != Items.AIR && value?.contains(effect!!) ?: false
    }.map { // get the items
      it.key
    }.filterNotNull()
    for(i in items) {
      if(curPage == page) {
        add(ItemButtonWidget(leftPos + x, topPos + y, EFFECT_SIZE, i) {
          item = i
          effect = null
          page = 0
          rebuildWidgets()
          SoundEvents.UI_BUTTON_CLICK.play()
        })
      } else if(curPage > page) break
      x += ITEM_SIZE + ITEM_SPACING
      if((x + ITEM_SIZE) > w) {
        y += ITEM_SIZE + ITEM_SPACING
        x = LEFT_PADDING
        if((y + EFFECT_SIZE) > h) {
          curPage++
          y = TOP_PADDING + captionHeight + 2
        }
      }
    }

    addPageButtons(curPage)
    addBackButton()
  }

  fun initItemPage() {
    add(TitleWidget(
      ItemStack(item!!).hoverName,
      leftPos + 24, topPos + 24, imageWidth - 24, 64, false
    ))

    val effects = ClientAlchemyHelper.EFFECTS.entries.filter{ (k, _) ->
      k == item
    }.flatMap { (_, v) -> v?.toList() ?: listOf() }
//      .distinct() // actually for items it seems useful to show total number of effects
      .sortedBy {
        I18n.get(it.toLanguageKey("effect"))
      }
    var y = TOP_PADDING
    var x = LEFT_PADDING
    val w = imageWidth - RIGHT_PADDING
    val h = imageHeight - BOTTOM_PADDING
    var curPage = 0
    for(e in effects) {
      if(curPage == page) {
        add(EffectButtonWidget(leftPos + x, topPos + y, EFFECT_SIZE, e) {
          effect = e
          item = null
          page = 0
          rebuildWidgets()
          SoundEvents.UI_BUTTON_CLICK.play()
        })
      } else if(curPage > page) break

      x += EFFECT_SIZE + EFFECT_SPACING
      if((x + EFFECT_SIZE) > w) {
        y += EFFECT_SIZE + EFFECT_SPACING
        x = LEFT_PADDING
        if((y + EFFECT_SIZE) > h) {
          curPage++
          y = TOP_PADDING
        }
      }
    }

    addPageButtons(curPage)
    addBackButton()
  }

  fun initIndexPage() {
    val effects = ClientAlchemyHelper.EFFECTS.values.flatMap { it?.toList() ?: listOf() }
      .distinct()
      .sortedBy {
        I18n.get(it.toLanguageKey("effect"))
      }

    var headerDrawn = false
    var curPage = 0
    var y = TOP_PADDING
    var x = LEFT_PADDING
    val w = imageWidth - RIGHT_PADDING
    val h = imageHeight - BOTTOM_PADDING
    for(e in effects) {
      if(curPage == page) {
        if(!headerDrawn) {
          add(TitleWidget(
            Component.translatable("gui.alchimiae.effects"),
            leftPos + 24, topPos + 24, imageWidth - 48, 64, false
          ))
          headerDrawn = true
        }
        add(EffectButtonWidget(leftPos + x, topPos + y, EFFECT_SIZE, e) {
          effect = e
          item = null
          page = 0
          rebuildWidgets()
          SoundEvents.UI_BUTTON_CLICK.play()
        })
      } else if(curPage > page) break

      x += EFFECT_SIZE + EFFECT_SPACING
      if((x + EFFECT_SIZE) > w) {
        y += EFFECT_SIZE + EFFECT_SPACING
        x = LEFT_PADDING
        if((y + EFFECT_SIZE) > h) {
          curPage++
          y = TOP_PADDING
        }
      }
    }

    curPage++
    x = LEFT_PADDING
    y = TOP_PADDING
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
        add(ItemButtonWidget(leftPos + x, topPos + y, ITEM_SIZE, i) {
          item = i
          effect = null
          page = 0
          rebuildWidgets()
          SoundEvents.UI_BUTTON_CLICK.play()
        })
      } else if(curPage > page) break
      x += ITEM_SIZE + ITEM_SPACING
      if((x + ITEM_SIZE) > w) {
        y += ITEM_SIZE + ITEM_SPACING
        x = LEFT_PADDING
        if((y + EFFECT_SIZE) > h) {
          curPage++
          y = TOP_PADDING
        }
      }
    }

    addPageButtons(curPage)
  }

  fun addPageButtons(curPage: Int) {
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

  fun addBackButton() {
    val width = 46
    val half = width / 2
    add(ButtonWidget(
      leftPos + (imageWidth / 2) - half,
      topPos + imageHeight - 50,
      width,
      24,
      Component.translatable("gui.alchimiae.back")
    ) {
      item = null
      effect = null
      page = 0
      rebuildWidgets()
    })
  }

  companion object {
    const val EFFECT_SIZE = 16
    const val EFFECT_SPACING = 2
    const val ITEM_SIZE = 16
    const val ITEM_SPACING = 4
    const val LEFT_PADDING = 24
    const val RIGHT_PADDING = 24
    const val BOTTOM_PADDING = 48
    const val TOP_PADDING = 48
  }
}