package org.progreso.client.modules.misc

import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.Enchantments
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

            if (best != null && best.value > getDestroySpeedForBlock(mc.player.mainHandItem, event.pos)) {
                mc.player.inventory.updateSelectedSlot(best.key.index)
            }
        }
    }

    private fun getDestroySpeedForBlock(stack: ItemStack, pos: BlockPos): Float {
        var speed = stack.getDestroySpeed(mc.level.getBlockState(pos))

        if (speed > 1.0f) {
            val enchantmentRegistry = mc.level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
            val efficiencyHolder = enchantmentRegistry.getOrThrow(Enchantments.EFFICIENCY)

            val effLevel = EnchantmentHelper.getItemEnchantmentLevel(efficiencyHolder, stack)

            if (effLevel > 0) {
                speed += (effLevel * effLevel + 1).toFloat()
            }
        }

        return speed
    }
}