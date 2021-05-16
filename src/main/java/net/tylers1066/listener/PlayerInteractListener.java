package net.tylers1066.listener;

import net.tylers1066.db.Deadbolt;
import net.tylers1066.selection.Selection;
import net.tylers1066.selection.SelectionManager;
import net.tylers1066.util.EnhancedSign;
import net.tylers1066.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent e) {
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if(!handle(e.getPlayer(), e.getClickedBlock()))
            return;

        e.setUseInteractedBlock(Event.Result.DENY);
        e.setUseItemInHand(Event.Result.DENY);
    }

    private boolean handle(Player p, Block b) {
        Material type = b.getType();
        if(Util.isWallSign(type)) {
            return handleSign(p, b);
        }
        else if(Util.isDoor(type) || Util.isTrapdoor(type) || Util.isGate(type)) {
            return handleOpenable(p, b);
        }
        else if(Util.isChest(type) || Util.isFurnace(type) || Util.isDispenser(type)
                || Util.isDropper(type) || Util.isOtherContainer(type)) {
            return handleContainer(p, b);
        }
        return false;
    }

    private boolean handleSign(Player p, Block b) {
        Deadbolt db = new Deadbolt(b);

        Bukkit.broadcastMessage("Sign: " + db);

        if(!db.isProtected())
            return false;

        if(!db.isOwner(p))
            return false;

        SelectionManager.add(p, new Selection(new EnhancedSign(b), db));
        return true;
    }

    private boolean handleOpenable(Player p, Block b) {
        Deadbolt db = new Deadbolt(b);

        Bukkit.broadcastMessage("Openable: " + db);

        if(!db.isProtected())
            return true;

        if(!db.isMember(p))
            return true;

        db.toggleDoors();
        return false;
    }

    private boolean handleContainer(Player p, Block b) {
        Deadbolt db = new Deadbolt(b);

        Bukkit.broadcastMessage("Container: " + db);

        if(!db.isProtected())
            return false;

        // Deny if not member
        return !db.isMember(p);
    }
}
