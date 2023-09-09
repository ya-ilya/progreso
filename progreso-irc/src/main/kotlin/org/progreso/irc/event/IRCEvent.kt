package org.progreso.irc.event

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.progreso.irc.event.c2s.MessageC2SEvent
import org.progreso.irc.event.c2s.RegisterC2SEvent
import org.progreso.irc.event.s2c.CloseS2CEvent
import org.progreso.irc.event.s2c.MessageS2CEvent
import org.progreso.irc.gson.RuntimeTypeAdapterFactory
import kotlin.reflect.KClass

sealed interface IRCEvent {
    interface C2S : IRCEvent {
        companion object : IRCEventClass<C2S>(
            C2S::class,
            MessageC2SEvent::class,
            RegisterC2SEvent::class
        )
    }

    interface S2C : IRCEvent {
        companion object : IRCEventClass<S2C>(
            S2C::class,
            CloseS2CEvent::class,
            MessageS2CEvent::class
        )
    }

    abstract class IRCEventClass<E : IRCEvent>(
        private val clazz: KClass<E>,
        vararg subtypes: KClass<out E>
    ) {
        private val factory by lazy {
            RuntimeTypeAdapterFactory.of(clazz.java)
                .apply { for (subtype in subtypes) registerSubtype(subtype.java) }
        }

        private val gson: Gson by lazy {
            GsonBuilder()
                .registerTypeAdapterFactory(factory)
                .create()
        }

        fun fromJson(json: String): E {
            return gson.fromJson(json, clazz.java)
        }

        fun toJson(event: E): String {
            return gson.toJson(event, clazz.java)
        }
    }
}