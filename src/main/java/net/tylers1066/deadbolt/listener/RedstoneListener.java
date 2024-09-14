package net.tylers1066.deadbolt.listener;

import net.tylers1066.deadbolt.config.Config;
import net.tylers1066.deadbolt.db.Deadbolt;
import net.tylers1066.deadbolt.util.Util;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

public class RedstoneListener implements Listener {
    @EventHandler
    public void onBlockRedstone(BlockRedstoneEvent e) {
        if (!Config.denyRedstone)
            return;

        Block b = e.getBlock();

        Deadbolt db = new Deadbolt(b);

        Material type = db.getType();
        if (type == null || !Util.isDoor(type) && !Util.isTrapdoor(type) || !Util.isGate(type))
            return;

        if (!db.isProtected() || db.isEveryone())
            return;

        e.setNewCurrent(e.getOldCurrent());
    }
}
