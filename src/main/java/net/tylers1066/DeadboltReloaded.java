package net.tylers1066;

import net.tylers1066.commands.DeadboltCommand;
import net.tylers1066.listener.*;
import net.tylers1066.util.Util;
import org.bukkit.plugin.java.JavaPlugin;

public final class DeadboltReloaded extends JavaPlugin {

    @Override
    public void onEnable() {
        Util.init();

        getServer().getPluginManager().registerEvents(new EndermanListener(), this);
        getServer().getPluginManager().registerEvents(new EntityInteractListener(), this);
        getServer().getPluginManager().registerEvents(new ExplosionListener(), this);
        getServer().getPluginManager().registerEvents(new HopperMinecartListener(), this);
        getServer().getPluginManager().registerEvents(new PistonListener(), this);
        getServer().getPluginManager().registerEvents(new RedstoneListener(), this);
        getServer().getPluginManager().registerEvents(new SignChangeListener(), this);

        getCommand("deadbolt").setExecutor(new DeadboltCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
