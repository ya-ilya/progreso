package org.progreso.client.module.modules.misc

import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.Enchantments
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import org.progreso.client.events.block.DamageBlockEvent
import org.progreso.client.events.safeEventListener
import org.progreso.client.module.Category
import org.progreso.client.module.Module
import org.progreso.client.util.player.InventoryUtil

object AutoTool : Module("AutoTool", Category.Misc) {
    init {
        safeEventListener<DamageBlockEvent> { event ->
            val best = InventoryUtil.hotbar
                .associateWith { getDestroySpeedForBlock(it.stack, event.pos) }
                .maxByOrNull { it.value }

            if (best != null && best.value > getDestroySpeedForBlock(mc.player.mainHandStack, event.pos)) {
                InventoryUtil.updateSelectedSlot(best.key.index)
            }
        }
    }

    private fun getDestroySpeedForBlock(stack: ItemStack, pos: BlockPos): Float {
        var speed = stack.getMiningSpeedMultiplier(mc.world.getBlockState(pos))

        if (speed > 1.0f) {
            speed += EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, stack)
        }

        return speed
    }
}