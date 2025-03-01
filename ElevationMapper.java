package org.dis.elevationMapper;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class ElevationMapper extends JavaPlugin {

    public static ElevationMapper plugin;

    @Override
    public void onEnable() {

        //makes this class easily accessible
        plugin = this;

        //create the directory for config and stat files
        File theDir = new File("plugins/ElevationMapper");
        if (!theDir.exists()){
            theDir.mkdirs();
        }

        getCommand("mapper").setExecutor(new MapperCommands());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
