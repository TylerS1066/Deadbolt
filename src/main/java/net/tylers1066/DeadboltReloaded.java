package net.tylers1066;

import net.tylers1066.commands.DeadboltCommand;
import net.tylers1066.config.Config;
import net.tylers1066.listener.*;
import net.tylers1066.util.Util;
import org.bukkit.plugin.java.JavaPlugin;

public final class DeadboltReloaded extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();

        Util.init();

        Config.protectIronDoors = getConfig().getBoolean("protectIronDoors", true);
        Config.protectIronTrapdoors = getConfig().getBoolean("protectIronTrapdoors", true);

        Config.denyExplosions = getConfig().getBoolean("denyExplosions", false);
        Config.denyPistons = getConfig().getBoolean("denyPistons", true);

        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBurnListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new EndermanListener(), this);
        getServer().getPluginManager().registerEvents(new EntityInteractListener(), this);
        getServer().getPluginManager().registerEvents(new ExplosionListener(), this);
        getServer().getPluginManager().registerEvents(new HopperMinecartListener(), this);
        getServer().getPluginManager().registerEvents(new PistonListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new RedstoneListener(), this);
        getServer().getPluginManager().registerEvents(new SignChangeListener(), this);

        getCommand("deadbolt").setExecutor(new DeadboltCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
