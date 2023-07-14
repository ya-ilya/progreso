package org.progreso.api.event

import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.util.concurrent.ConcurrentHashMap

class EventBus {
    companion object {
        private val CACHE = ConcurrentHashMap<Class<*>, MutableList<ListenerInvoker>>()
    }

    private val instances = ConcurrentHashMap<Class<*>, MutableSet<Any>>()

    fun register(instance: Any) {
        val instanceClass = instance.javaClass

        if (!CACHE.containsKey(instanceClass)) {
            val listeners = mutableListOf<ListenerInvoker>()

            for (method in instanceClass.declaredMethods) {
                val annotation = method.getAnnotation(EventHandler::class.java)

                if (annotation != null) {
                    val parameterType = method.parameterTypes[0]

                    if (!Event::class.java.isAssignableFrom(parameterType)) {
                        throw RuntimeException("Method: $method")
                    }

                    listeners.add(ListenerInvoker(annotation.priority, parameterType, method))
                }
            }

            for (field in instanceClass.declaredFields) {
                field.isAccessible = true

                val annotation = field.getAnnotation(EventHandler::class.java)

                if (annotation != null) {
                    val type = field.genericType as ParameterizedType

                    if (!EventListener::class.java.isAssignableFrom(type.rawType as Class<*>)) {
                        throw RuntimeException("Field: $field")
                    }

                    listeners.add(
                        ListenerInvoker(
                            annotation.priority,
                            type.actualTypeArguments[0] as Class<*>,
                            EventListener.INVOKE_METHOD,
                            field.get(instance)
                        )
                    )
                }

                field.isAccessible = false
            }

            sortListeners(listeners)

            CACHE[instanceClass] = listeners
        }

        if (instances.containsKey(instanceClass)) {
            instances[instanceClass]!!.add(instanceClass)
        } else {
            instances[instanceClass] = mutableSetOf(instance)
        }
    }

    fun registerListener(
        instanceClass: Class<*>,
        eventClass: Class<*>,
        priority: EventPriority,
        listener: EventListener<*>
    ) {
        val listenerInvoker = ListenerInvoker(
            priority,
            eventClass,
            EventListener.INVOKE_METHOD,
            listener
        )

        if (CACHE.containsKey(instanceClass)) {
            val listeners = CACHE[instanceClass]!!
            listeners.add(listenerInvoker)
            sortListeners(listeners)
            CACHE[instanceClass] = listeners
        } else {
            CACHE[instanceClass] = mutableListOf(listenerInvoker)
        }
    }

    fun unregister(instance: Any) {
        instances[instance.javaClass]?.remove(instance)
    }

    fun post(event: Event): Boolean {
        for ((instanceClass, instances) in instances) {
            for (listener in CACHE[instanceClass]!!) {
                if (listener.eventClass.isAssignableFrom(event.javaClass)) {
                    for (instance in instances) {
                        listener.invoke(instance, event)

                        if (event.isCancelled) {
                            return true
                        }
                    }
                }
            }
        }

        return false
    }

    private fun sortListeners(listeners: MutableList<ListenerInvoker>) {
        listeners.sortWith { listener1, listener2 ->
            if (listener1.priority > listener2.priority) {
                -1
            } else if (listener1.priority == listener2.priority) {
                0
            } else {
                1
            }
        }

        listeners.sortWith { listener1, listener2 ->
            if (listener1.eventClass == listener2.eventClass) {
                0
            } else if (listener1.eventClass.isAssignableFrom(listener2.eventClass)) {
                -1
            } else {
                1
            }
        }
    }

    private data class ListenerInvoker(
        val priority: EventPriority,
        val eventClass: Class<*>,
        val method: Method,
        val methodInstance: Any? = null
    ) {
        init {
            method.isAccessible = true
        }

        operator fun invoke(instance: Any, event: Event) {
            method.invoke(methodInstance ?: instance, event)
        }
    }
}