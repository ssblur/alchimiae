package com.ssblur.alchimiae.integration.emi;

import com.ssblur.alchimiae.alchemy.ClientAlchemyHelper;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Random;

public record IngredientEmiIngredient(int frequency) implements EmiIngredient {
  static int MAX_FREQUENCY = 9;

  @Override
  public List<EmiStack> getEmiStacks() {
    return ClientAlchemyHelper.INSTANCE.getEFFECTS().keySet().stream().map(ItemStack::new).map(EmiStack::of).toList();
  }

  @Override
  public EmiIngredient copy() {
    return new IngredientEmiIngredient(frequency);
  }

  @Override
  public long getAmount() {
    return 1;
  }

  @Override
  public EmiIngredient setAmount(long amount) {
    return this;
  }

  @Override
  public float getChance() {
    return 1.0f;
  }

  @Override
  public EmiIngredient setChance(float chance) {
    return this;
  }

  @Override
  public void render(GuiGraphics draw, int x, int y, float delta, int flags) {
    var level = Minecraft.getInstance().level;
    var second = 0L;
    if(level != null)
      second = level.getGameTime() / 20;
    var random = new Random(second + frequency).nextInt(ClientAlchemyHelper.INSTANCE.getEFFECTS().size());
    var item = (Item) ClientAlchemyHelper.INSTANCE.getEFFECTS().entrySet().toArray(new Map.Entry[] {})[random].getKey();

    if(second % MAX_FREQUENCY < frequency)
      draw.renderItem(new ItemStack(item), x, y);
  }

  @Override
  public List<ClientTooltipComponent> getTooltip() {
    return List.of(
      ClientTooltipComponent.create(FormattedCharSequence.forward(I18n.get("gui.alchimiae.any_ingredient"), Style.EMPTY)),
      ClientTooltipComponent.create(FormattedCharSequence.forward(I18n.get("lore.alchimiae.ingredient"), Style.EMPTY.withColor(ChatFormatting.AQUA)))
    );
  }
}
