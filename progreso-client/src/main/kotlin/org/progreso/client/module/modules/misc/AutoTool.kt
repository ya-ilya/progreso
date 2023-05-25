package org.progreso.client.module.modules.misc

import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.init.Enchantments
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import org.progreso.client.events.block.DamageBlockEvent
import org.progreso.client.events.safeEventListener
import org.progreso.client.module.Category
import org.progreso.client.module.Module
import org.progreso.client.util.InventoryUtil

object AutoTool : Module("AutoTool", Category.Misc) {
    init {
        safeEventListener<DamageBlockEvent> { event ->
            val best = InventoryUtil.hotbar.maxByOrNull { slot -> getDestroySpeedForBlock(slot.stack, event.pos) }

            if (best != null && getDestroySpeedForBlock(
                    best.stack,
                    event.pos
                ) > getDestroySpeedForBlock(InventoryUtil.currentItemStack, event.pos)
            ) {
                InventoryUtil.currentItemIndex = best.slotNumber - 36
            }
        }
    }

    private fun getDestroySpeedForBlock(stack: ItemStack, pos: BlockPos): Float {
        var speed = stack.getDestroySpeed(mc.world.getBlockState(pos))

        if (speed > 1.0f) {
            speed += EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack)
        }

        return speed
    }
}