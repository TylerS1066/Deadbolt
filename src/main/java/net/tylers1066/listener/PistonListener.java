package net.tylers1066.listener;

import net.tylers1066.config.Config;
import net.tylers1066.db.Deadbolt;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

public class PistonListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onBlockPistonExtendEvent(BlockPistonExtendEvent e) {
        if (!Config.denyPistons)
            return;

        for (Block b : e.getBlocks()) {
            Deadbolt db = new Deadbolt(b);
            if (db.isProtected()) {
                e.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPistonRetract(BlockPistonRetractEvent e) {
        if (!Config.denyPistons)
            return;

        Block block = e.getBlock().getRelative(e.getDirection()).getRelative(e.getDirection());
        Deadbolt db = new Deadbolt(block);
        if (db.isProtected()) {
            // TODO: this probably will break AP's abusive breaking system, OH WELL
            e.setCancelled(true);
        }
    }
}
