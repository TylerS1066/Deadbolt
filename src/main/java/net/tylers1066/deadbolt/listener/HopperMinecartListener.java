package net.tylers1066.deadbolt.listener;

import net.tylers1066.deadbolt.db.Deadbolt;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;

public class HopperMinecartListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInventoryMoveItemEvent(InventoryMoveItemEvent e) {
        Inventory initiator = e.getInitiator();

        if (!(initiator.getHolder() instanceof HopperMinecart))
            return;

        Inventory other = e.getSource();
        if (other == initiator)
            other = e.getDestination();

        Deadbolt db = new Deadbolt(other.getLocation().getBlock());
        if (db.isProtected())
            e.setCancelled(true);
    }
}
