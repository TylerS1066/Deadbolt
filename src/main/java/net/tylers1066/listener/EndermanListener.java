package net.tylers1066.listener;

import net.tylers1066.db.Deadbolt;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class EndermanListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onEntityChangeBlock(EntityChangeBlockEvent e) {
        Block b = e.getBlock();
        Deadbolt db = new Deadbolt(b);
        if(!db.isProtected())
            return;

        e.setCancelled(true);
    }
}
