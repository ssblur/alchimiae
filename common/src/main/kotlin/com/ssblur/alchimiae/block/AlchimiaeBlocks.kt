package com.ssblur.alchimiae.block

import com.ssblur.alchimiae.AlchimiaeMod
import com.ssblur.alchimiae.item.AlchimiaeItems
import com.ssblur.unfocused.tab.CreativeTabs.tab

object AlchimiaeBlocks {
  val BOILER = AlchimiaeMod.registerBlockWithItem("boiler") { BoilerBlock() }
  val ALEMBIC = AlchimiaeMod.registerBlockWithItem("alembic") { AlembicBlock() }
  val FILTER = AlchimiaeMod.registerBlockWithItem("filter") { FilterBlock() }
  val ALCHEMY_TABLE = AlchimiaeMod.registerBlockWithItem("alchemy_table") { AlchemyTableBlock() }

  fun register() {
    BOILER.second.tab(AlchimiaeItems.TAB)
    ALEMBIC.second.tab(AlchimiaeItems.TAB)
    FILTER.second.tab(AlchimiaeItems.TAB)
    ALCHEMY_TABLE.second.tab(AlchimiaeItems.TAB)
  }
}
