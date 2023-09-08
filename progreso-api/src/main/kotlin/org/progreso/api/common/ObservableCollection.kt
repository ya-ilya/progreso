package org.progreso.api.common

interface ObservableCollection<E> {
    open class List<E> : ArrayList<E>(), ObservableCollection<E> {
        override fun add(element: E): Boolean {
            return super.add(element).also {
                elementAdded(element)
            }
        }

        override fun add(index: Int, element: E) {
            super.add(index, element).also {
                elementAdded(element)
            }
        }

        override fun addAll(elements: Collection<E>): Boolean {
            return super.addAll(elements).also {
                for (element in elements) elementAdded(element)
            }
        }

        override fun addAll(index: Int, elements: Collection<E>): Boolean {
            return super.addAll(index, elements).also {
                for (element in elements) elementAdded(element)
            }
        }

        override fun remove(element: E): Boolean {
            return super.remove(element).also {
                elementRemoved(element)
            }
        }
    }

    open class Set<E> : LinkedHashSet<E>(), ObservableCollection<E> {
        override fun add(element: E): Boolean {
            return super.add(element).also {
                elementAdded(element)
            }
        }

        override fun addAll(elements: Collection<E>): Boolean {
            return super.addAll(elements).also {
                for (element in elements) elementAdded(element)
            }
        }

        override fun remove(element: E): Boolean {
            return super.remove(element).also {
                elementRemoved(element)
            }
        }
    }

    fun elementAdded(element: E) {}
    fun elementRemoved(element: E) {}
}