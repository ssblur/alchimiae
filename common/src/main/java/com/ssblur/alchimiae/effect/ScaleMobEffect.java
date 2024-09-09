package com.ssblur.alchimiae.effect;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ScaleMobEffect extends MobEffect {
  double scale;
  ResourceLocation location;
  public ScaleMobEffect(double scale, ResourceLocation attributeLocation) {
    super(MobEffectCategory.NEUTRAL, 0xFF508743);

    this.scale = scale;
    this.location = attributeLocation;
    this.addAttributeModifier(Attributes.SCALE, attributeLocation.withSuffix("_att_scale"), scale, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    if(scale > 0) {
      this.addAttributeModifier(Attributes.MOVEMENT_SPEED, attributeLocation.withSuffix("_att_movement_speed"), scale / 1.5d, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
      this.addAttributeModifier(Attributes.SNEAKING_SPEED, attributeLocation.withSuffix("_att_sneaking_speed"), scale / 1.5d, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
      this.addAttributeModifier(Attributes.JUMP_STRENGTH, attributeLocation.withSuffix("_att_jump_strength"), scale / 3d, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
      this.addAttributeModifier(Attributes.STEP_HEIGHT, attributeLocation.withSuffix("_att_step_height"), scale / 1.5d, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
      this.addAttributeModifier(Attributes.WATER_MOVEMENT_EFFICIENCY, attributeLocation.withSuffix("water_movement"), scale / 1.5d, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
      this.addAttributeModifier(Attributes.BLOCK_INTERACTION_RANGE, attributeLocation.withSuffix("_att_block_range"), scale / 1.5d, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
      this.addAttributeModifier(Attributes.ENTITY_INTERACTION_RANGE, attributeLocation.withSuffix("_att_range"), scale / 1.5d, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    } else {
      this.addAttributeModifier(Attributes.SAFE_FALL_DISTANCE, attributeLocation.withSuffix("_att_fall_distance"), -scale, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
      this.addAttributeModifier(Attributes.OXYGEN_BONUS, attributeLocation.withSuffix("_att_oxygen"), -scale, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
      this.addAttributeModifier(Attributes.ENTITY_INTERACTION_RANGE, attributeLocation.withSuffix("_att_range"), -scale, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    }
  }

  @Override
  public boolean shouldApplyEffectTickThisTick(int i, int j) {
    return false;
  }
}
