package net.tylers1066.listener;

import net.tylers1066.db.Deadbolt;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

public class RedstoneListener implements Listener {
    @EventHandler
    public void onBlockRedstone(BlockRedstoneEvent e) {
        Block b = e.getBlock();

        // if protected redstone block...
        if(false) {
            return;
        }

        Deadbolt db = new Deadbolt(b);

        if(!db.isProtected() || db.isEveryone())
            return;

        e.setNewCurrent(e.getOldCurrent());
    }
}
