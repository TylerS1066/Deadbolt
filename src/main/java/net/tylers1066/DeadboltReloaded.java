package net.tylers1066;

import net.tylers1066.util.Util;
import org.bukkit.plugin.java.JavaPlugin;

public final class DeadboltReloaded extends JavaPlugin {

    @Override
    public void onEnable() {
        Util.init();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
