package net.tylers1066.listener;

import net.tylers1066.db.Deadbolt;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.Iterator;

public class ExplosionListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent e) {
        Iterator<Block> iter = e.blockList().iterator();
        while(iter.hasNext()) {
            Block b = iter.next();
            Deadbolt db = new Deadbolt(b);
            if(db.isProtected()) {
                iter.remove();
            }
        }
    }
}
