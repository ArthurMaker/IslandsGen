package com.dumptruckman.bukkit.islandsgen;

import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public class IslandsGenPlugin extends JavaPlugin {

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new IslandsGenerator();
    }
}

