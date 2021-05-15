package net.tylers1066.listener;

import net.tylers1066.db.Deadbolt;
import net.tylers1066.util.EnhancedSign;
import net.tylers1066.util.Util;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChangeListener implements Listener {
    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        Sign sign = (new EnhancedSign(e.getBlock())).getSign();

        if(sign == null)
            return;

        if(!Util.isWallSign(e.getBlock().getType()))
            return;

        if(Util.isValidHeader(sign)) {
            // player hacking, bad on dem
            e.setCancelled(true);
            return;
        }

        String line0 = e.getLine(0);
        if(!Util.isValidHeader(line0))
            return;

        Deadbolt db = new Deadbolt(e.getBlock());

        if(!validatePlacement(db, e.getPlayer(), line0)) {
            // Not valid change, either not allowed or something
            e.setCancelled(true);
        }
        else {
            if(Util.isValidPrivateSign(line0)) {
                // New private sign, allow it and fill out line 1
                e.getLines()[1] = Util.formatForSign(e.getPlayer().getName());
            }
        }
    }

    private boolean validatePlacement(Deadbolt db, Player p, String line0) {
        if(db.isProtected()) {
            // Only allow the owner to place a [More Users] sign
            return db.isOwner(p) && Util.isValidMoreUsersSign(line0);
        }

        // Unprotected deadbolt, reject if a [More Users] or invalid header
        if(!Util.isValidPrivateSign(line0) || Util.isValidMoreUsersSign(line0))
            return false;

        // TODO: check perms

        // Found some good blocks
        return db.getBlockCount() > 0;
    }
}
