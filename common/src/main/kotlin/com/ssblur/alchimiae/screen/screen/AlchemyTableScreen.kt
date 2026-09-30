package com.ssblur.alchimiae.screen.screen

import com.ssblur.alchimiae.screen.menu.AlchemyTableMenu
import com.ssblur.unfocused.screen.UnfocusedScreen
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class AlchemyTableScreen(abstractContainerMenu: AlchemyTableMenu, inventory: Inventory, component: Component):
  UnfocusedScreen<AlchemyTableMenu>(abstractContainerMenu, inventory, component) {

}