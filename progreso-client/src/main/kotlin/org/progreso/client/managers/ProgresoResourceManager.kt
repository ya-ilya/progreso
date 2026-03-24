package org.progreso.client.managers

import net.minecraft.resources.Identifier
import net.minecraft.server.packs.PackResources
import net.minecraft.server.packs.resources.Resource
import net.minecraft.server.packs.resources.ResourceManager
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

    override fun getResource(id: Identifier): Optional<Resource> {
        return when {
            id.namespace == "progreso-resources" -> {
                val resourcePath = Paths.get(path.toString(), id.path)
                resourcePath.createParentDirectories()

                if (!resourcePath.exists()) {
                    throw FileNotFoundException("Resource not found")
                }

                Optional.of(Resource(mc.client.vanillaPackResources) { resourcePath.inputStream() })
            }

            else -> mc.resourceManager.getResource(id)
        }
    }

    override fun getNamespaces(): Set<String> {
        return mc.resourceManager.namespaces
    }

    override fun getResourceStack(location: Identifier): List<Resource> {
        return mc.resourceManager.getResourceStack(location)
    }

    override fun listResources(
        directory: String,
        filter: Predicate<Identifier>
    ): Map<Identifier, Resource> {
        return mc.resourceManager.listResources(directory, filter)
    }

    override fun listResourceStacks(
        directory: String,
        filter: Predicate<Identifier>
    ): Map<Identifier, List<Resource>> {
        return mc.resourceManager.listResourceStacks(directory, filter)
    }

    override fun listPacks(): Stream<PackResources> {
        return mc.resourceManager.listPacks()
    }
}