package org.progreso.api.common

open class ObservableSet<T> : LinkedHashSet<T>() {
    override fun add(element: T): Boolean {
        return super.add(element).also {
            elementAdded(element)
        }
    }

    override fun remove(element: T): Boolean {
        return super.remove(element).also {
            elementRemoved(element)
        }
    }

    open fun elementAdded(element: T) {}
    open fun elementRemoved(element: T) {}
}