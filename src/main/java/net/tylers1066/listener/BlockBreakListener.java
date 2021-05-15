package net.tylers1066.listener;

import net.tylers1066.db.Deadbolt;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e) {
        Block b = e.getBlock();
        Player p = e.getPlayer();
        Deadbolt db = new Deadbolt(b);

        if(!db.isProtected() || db.isOwner(p))
            return;

        e.setCancelled(true);
    }
}
