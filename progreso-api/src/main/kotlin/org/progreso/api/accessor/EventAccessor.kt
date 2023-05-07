package org.progreso.api.accessor

/**
 * Provides access to the event system
 * ```java
 * import org.progreso.api.accessor.EventAccessor;
 *
 * public class EventAccessorImpl implements EventAccessor {
 *     @Override
 *     public void register(Object instance) {
 *         MinecraftForge.EVENT_BUS.register(instance);
 *     }
 *
 *     @Override
 *     public void unregister(Object instance) {
 *         MinecraftForge.EVENT_BUS.unregister(instance);
 *     }
 * }
 * ```
 */
interface EventAccessor {
    /**
     * Register an object in event system
     *
     * @param instance Object
     */
    fun register(instance: Any)

    /**
     * Unregister an object in event system
     *
     * @param instance Object
     */
    fun unregister(instance: Any)
}