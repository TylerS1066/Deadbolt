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

        if(!Util.isValidHeader(sign))
            return;

        if(Util.isWallSign(e.getBlock().getType())) {
            // player hacking, bad on dem
            e.setCancelled(true);
            return;
        }

        Deadbolt db = new Deadbolt(e.getBlock());

        if(!validatePlacement(db, e.getPlayer(), sign)) {
            e.setCancelled(true);
        }
    }

    private boolean validatePlacement(Deadbolt db, Player p, Sign sign) {
        if(db.isProtected()) {
            // Only allow the owner to place a [More Users] sign
            return db.isOwner(p) && Util.isValidMoreUsersSign(sign);
        }

        // Unprotected deadbolt, reject if a [More Users] or invalid header
        if(!Util.isValidPrivateSign(sign) || Util.isValidMoreUsersSign(sign))
            return false;

        // TODO: check perms

        // Found some good blocks
        return db.getBlockCount() > 0;
    }
}
