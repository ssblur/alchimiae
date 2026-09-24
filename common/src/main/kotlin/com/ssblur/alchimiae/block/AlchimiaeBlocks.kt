package com.ssblur.alchimiae.block

import com.ssblur.alchimiae.AlchimiaeMod
import com.ssblur.alchimiae.item.AlchimiaeItems
import com.ssblur.unfocused.tab.CreativeTabs.tab

object AlchimiaeBlocks {
  val BOILER = AlchimiaeMod.registerBlockWithItem("boiler") { BoilerBlock() }
  val ALEMBIC = AlchimiaeMod.registerBlockWithItem("alembic") { AlembicBlock() }
  val FILTER = AlchimiaeMod.registerBlockWithItem("filter") { FilterBlock() }

  fun register() {
    BOILER.second.tab(AlchimiaeItems.TAB)
    ALEMBIC.second.tab(AlchimiaeItems.TAB)
    FILTER.second.tab(AlchimiaeItems.TAB)
  }
}
