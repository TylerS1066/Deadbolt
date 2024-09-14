package net.tylers1066.deadbolt.util;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnhancedSign extends EnhancedBlock {
    private final Sign sign;

    private static boolean isSign(@NotNull Material m) {
        return Tag.SIGNS.getValues().contains(m);
    }

    @Nullable
    private static Sign blockToSign(@NotNull Block b) {
        if (!isSign(b.getType()))
            return null;

        BlockState state = b.getState();
        if (!(state instanceof Sign))
            return null;

        return (Sign) state;
    }

    public EnhancedSign(@NotNull Block b) {
        super(b);
        sign = blockToSign(b);
    }

    @Nullable
    public Sign getSign() {
        return sign;
    }
}
