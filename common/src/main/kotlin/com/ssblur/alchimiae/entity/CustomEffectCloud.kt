package com.ssblur.alchimiae.entity

import com.ssblur.alchimiae.alchemy.AlchemyHelper
import com.ssblur.alchimiae.data.CustomPotionEffects
import net.minecraft.world.entity.AreaEffectCloud
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.level.Level
import java.util.*

class CustomEffectCloud(entityType: EntityType<out AreaEffectCloud>, level: Level) :
  AreaEffectCloud(entityType, level) {
  var effects: CustomPotionEffects? = null
    set(value) {
      field = value
      setPotionContents(PotionContents(Optional.empty(), Optional.of(value?.color ?: 0), listOf()))
    }

  override fun tick() {
    if(level().gameTime % 5 == 0L && effects != null) {
      level().getEntitiesOfClass(LivingEntity::class.java, this.boundingBox).forEach{
        effects?.let { effect -> AlchemyHelper.applyEffects(effect, it) }
      }
      EntityType.AREA_EFFECT_CLOUD
    }
    super.tick()
  }


}