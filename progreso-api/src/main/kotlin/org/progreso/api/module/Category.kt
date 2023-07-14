package org.progreso.api.module

enum class Category {
    Combat,
    Movement,
    Render,
    Misc,
    Client,
    Hud;

    companion object {
        fun byPackage(packageName: String): Category {
            return entries.first {
                it.name.equals(packageName.split(".").last(), true)
            }
        }
    }
}