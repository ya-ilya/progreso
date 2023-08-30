package org.progreso.client.util.client

object TimerUtil {
    fun createTimer(unit: TimeUnit): Timer {
        return Timer(unit)
    }

    class Timer(val unit: TimeUnit) {
        private val currentTime get() = System.currentTimeMillis()

        var time = currentTime; private set

        fun tick(number: Long): Boolean {
            return (currentTime - time > number * unit.milliseconds).also {
                if (it) reset()
            }
        }

        fun skip(number: Long) {
            time = currentTime - number * unit.milliseconds
        }

        fun reset() {
            time = currentTime
        }
    }

    enum class TimeUnit(val milliseconds: Long) {
        Millisecond(1),
        Second(1000),
        Tick(50)
    }
}