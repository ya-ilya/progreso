package org.progreso.client.managers

import net.minecraft.resource.Resource
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourcePack
import net.minecraft.util.Identifier
import org.progreso.client.Client.Companion.mc
import java.io.FileNotFoundException
import java.nio.file.Paths
import java.util.*
import java.util.function.Predicate
import java.util.stream.Stream
import kotlin.io.path.*

object ProgresoResourceManager : ResourceManager {
    private val path = Paths.get("progreso", "resources")

    init {
        path.createDirectories()
    }

    val fonts
        get() = Paths.get(path.toString(), "font")
            .createDirectories()
            .listDirectoryEntries()
            .filter { !it.isDirectory() && it.extension == "ttf" }
            .map { it.nameWithoutExtension }

    override fun getResource(id: Identifier?): Optional<Resource> {
        return when {
            id == null -> Optional.empty()

            id.namespace == "progreso-resources" -> {
                val resourcePath = Paths.get(path.toString(), id.path)
                resourcePath.createParentDirectories()

                if (!resourcePath.exists()) {
                    throw FileNotFoundException("Resource not found")
                }

                Optional.of(Resource(mc.client.defaultResourcePack) { resourcePath.inputStream() })
            }

            else -> mc.resourceManager.getResource(id)
        }
    }

    override fun getAllNamespaces(): MutableSet<String> {
        return mc.resourceManager.allNamespaces
    }

    override fun getAllResources(id: Identifier?): MutableList<Resource> {
        return mc.resourceManager.getAllResources(id)
    }

    override fun findResources(
        startingPath: String?,
        allowedPathPredicate: Predicate<Identifier>?
    ): MutableMap<Identifier, Resource> {
        return mc.resourceManager.findResources(startingPath, allowedPathPredicate)
    }

    override fun findAllResources(
        startingPath: String?,
        allowedPathPredicate: Predicate<Identifier>?
    ): MutableMap<Identifier, MutableList<Resource>> {
        return mc.resourceManager.findAllResources(startingPath, allowedPathPredicate)
    }

    override fun streamResourcePacks(): Stream<ResourcePack> {
        return mc.resourceManager.streamResourcePacks()
    }
}