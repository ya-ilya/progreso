package org.progreso.client.mixin;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.progreso.api.managers.PluginManager;
import org.progreso.api.plugin.AbstractPlugin;
import org.progreso.api.plugin.loader.PluginLoader;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MixinLoader implements IFMLLoadingPlugin {
    public MixinLoader() throws IOException {
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.progreso.json");
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");

        Path pluginsPath = Paths.get("progreso" + File.separator + "plugins");

        if (!Files.exists(pluginsPath)) {
            Files.createDirectories(pluginsPath);
        }

        try (Stream<Path> stream = Files.walk(pluginsPath)) {
            List<Path> plugins = stream
                .filter(it -> !Files.isDirectory(it))
                .filter(it -> it.toFile().getName().endsWith("jar"))
                .collect(Collectors.toList());

            for (Path pluginPath : plugins) {
                AbstractPlugin plugin = PluginLoader.INSTANCE.loadPlugin(pluginPath.toAbsolutePath().toString());

                for (String mixinConfig : plugin.getMixinConfigs()) {
                    Mixins.addConfiguration(mixinConfig);
                }

                PluginManager.INSTANCE.add(plugin);
            }
        }
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}