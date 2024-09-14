package net.tylers1066.deadbolt;

import net.tylers1066.deadbolt.commands.DeadboltCommand;
import net.tylers1066.deadbolt.config.Config;
import net.tylers1066.deadbolt.listener.*;
import net.tylers1066.deadbolt.util.Util;
import org.bukkit.plugin.java.JavaPlugin;

public final class DeadboltReloaded extends JavaPlugin {
    private static DeadboltReloaded instance;

    public static DeadboltReloaded getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        Util.init();

        Config.protectIronDoors = getConfig().getBoolean("protectIronDoors", true);
        Config.protectIronTrapdoors = getConfig().getBoolean("protectIronTrapdoors", true);

        Config.denyExplosions = getConfig().getBoolean("denyExplosions", false);
        Config.denyPistons = getConfig().getBoolean("denyPistons", true);
        Config.denyRedstone = getConfig().getBoolean("denyRedstone", false);

        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBurnListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new EndermanListener(), this);
        getServer().getPluginManager().registerEvents(new EntityInteractListener(), this);
        if(Config.denyExplosions)
            getServer().getPluginManager().registerEvents(new ExplosionListener(), this);
        getServer().getPluginManager().registerEvents(new HopperMinecartListener(), this);
        if(Config.denyPistons)
            getServer().getPluginManager().registerEvents(new PistonListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        if(Config.denyRedstone)
            getServer().getPluginManager().registerEvents(new RedstoneListener(), this);
        getServer().getPluginManager().registerEvents(new SignChangeListener(), this);

        getCommand("deadbolt").setExecutor(new DeadboltCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
