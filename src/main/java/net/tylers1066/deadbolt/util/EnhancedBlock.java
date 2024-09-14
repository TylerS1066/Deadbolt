package net.tylers1066.deadbolt.util;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public class EnhancedBlock {
    private final Location location;
    private final Block block;

    public EnhancedBlock(@NotNull Block b) {
        location = b.getLocation();
        block = b;
    }

    @NotNull
    public Location getLocation() {
        return location;
    }

    @NotNull
    public Block getBlock() {
        return block;
    }
}
