package org.progreso.client.modules.misc

import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.Enchantments
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.math.BlockPos
import org.progreso.api.module.AbstractModule
import org.progreso.client.Client.Companion.mc
import org.progreso.client.events.block.DamageBlockEvent
import org.progreso.client.events.safeEventListener
import org.progreso.client.util.player.hotbar
import org.progreso.client.util.player.updateSelectedSlot

@AbstractModule.AutoRegister
object AutoTool : AbstractModule() {
    init {
        safeEventListener<DamageBlockEvent> { event ->
            val best = mc.player.inventory.hotbar
                .associateWith { getDestroySpeedForBlock(it.stack, event.pos) }
                .maxByOrNull { it.value }

            if (best != null && best.value > getDestroySpeedForBlock(mc.player.mainHandStack, event.pos)) {
                mc.player.inventory.updateSelectedSlot(best.key.index)
            }
        }
    }

    private fun getDestroySpeedForBlock(stack: ItemStack, pos: BlockPos): Float {
        var speed = stack.getMiningSpeedMultiplier(mc.world.getBlockState(pos))

        if (speed > 1.0f) {
            speed += EnchantmentHelper.getLevel(
                mc.world.registryManager.get(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.EFFICIENCY).get(),
                stack
            )
        }

        return speed
    }
}