package net.tylers1066.deadbolt.listener;

import net.tylers1066.deadbolt.db.Deadbolt;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;

public class EntityInteractListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onEntityInteraceBlock(EntityInteractEvent e) {
        Block b = e.getBlock();
        Deadbolt db = new Deadbolt(b);
        if (!db.isProtected())
            return;

        e.setCancelled(true);
    }
}
