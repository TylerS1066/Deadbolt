package net.tylers1066.listener;

import net.tylers1066.db.Deadbolt;
import net.tylers1066.util.EnhancedSign;
import net.tylers1066.util.Util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChangeListener implements Listener {
    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        Sign sign = (new EnhancedSign(e.getBlock())).getSign();

        if (sign == null)
            return;

        if (!Util.isWallSign(e.getBlock().getType()))
            return;

        if (Util.isValidHeader(sign)) {
            // player hacking, bad on dem
            e.setCancelled(true);
            return;
        }

        String line0 = e.getLine(0);
        if(!Util.isValidHeader(line0))
            return;

        Deadbolt db = new Deadbolt(e.getBlock());
        Bukkit.getLogger().info("" + db);

        if (!validatePlacement(db, e.getPlayer(), line0) || !validatePermissions(db.getType(), e.getPlayer())) {
            // Not valid change, either not allowed or something
            e.setCancelled(true);
        }
        else {
            Bukkit.getLogger().info("\t- Not cancelled");
            if (!Util.isValidPrivateSign(line0)) {
                Bukkit.getLogger().info("\t- Not valid private sign");
                return;
            }

            // New private sign
            e.getLines()[0] = ChatColor.stripColor(e.getLine(0));

            if (!e.getPlayer().hasPermission("deadbolt.admin.create") || e.getLine(1).isEmpty())
                e.getLines()[1] = Util.formatForSign(e.getPlayer().getName()); // Not admin or empty, fill out with owner's name
            else
                e.getLines()[1] = ChatColor.stripColor(e.getLine(1));

            if (!e.getPlayer().hasPermission("deadbolt.user.color")) {
                e.getLines()[2] = ChatColor.stripColor(e.getLine(2));
                e.getLines()[3] = ChatColor.stripColor(e.getLine(3));
            }
            Bukkit.getLogger().info("\t- Done: '" + e.getLines()[0] + "' '" + e.getLines()[1] + "' '" + e.getLines()[2] + "' '" + e.getLines()[3] + "'");
        }
    }

    private boolean validatePlacement(Deadbolt db, Player p, String line0) {
        if (db.isProtected()) {
            // Only allow the owner to place a [More Users] sign
            if (!Util.isValidMoreUsersSign(line0)) {
                p.sendMessage("This block is already protected");
                return false;
            }

            if (!db.isOwner(p)) {
                if(p.hasPermission("deadbolt.admin.create"))
                    return true;

                p.sendMessage("You don't own the adjacent block(s)");
                return false;
            }

            return true;
        }

        // Unprotected deadbolt, reject if a [More Users] or invalid header
        if (Util.isValidMoreUsersSign(line0)) {
            p.sendMessage("No sign with [Private] nearby");
            return false;
        }
        if (!Util.isValidPrivateSign(line0))
            return false;

        // Found some good blocks
        if (db.getBlockCount() > 0)
            return true;

        p.sendMessage("Nothing nearby to protect");
        return false;
    }

    private boolean validatePermissions(Material type, Player p) {
        if (Util.isChest(type) && p.hasPermission("deadbolt.user.create.chest"))
            return true;
        if (Util.isDispenser(type) && p.hasPermission("deadbolt.user.create.dispenser"))
            return true;
        if (Util.isDoor(type) && p.hasPermission("deadbolt.user.create.door"))
            return true;
        if (Util.isFurnace(type) && p.hasPermission("deadbolt.user.create.furnace"))
            return true;
        if (Util.isTrapdoor(type) && p.hasPermission("deadbolt.user.create.trapdoor"))
            return true;
        if (Util.isGate(type) && p.hasPermission("deadbolt.user.create.fencegate"))
            return true;
        if (Util.isDropper(type) && p.hasPermission("deadbolt.user.create.dropper"))
            return true;
        return Util.isOtherContainer(type) && p.hasPermission("deadbolt.user.create.other");
    }
}
