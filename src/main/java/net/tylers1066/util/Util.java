package net.tylers1066.util;

import net.tylers1066.config.Config;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.material.Openable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;

public class Util {
    private static final HashSet<BlockFace> CARDINAL_FACES = new HashSet<>();

    public static void init() {
        CARDINAL_FACES.add(BlockFace.NORTH);
        CARDINAL_FACES.add(BlockFace.EAST);
        CARDINAL_FACES.add(BlockFace.SOUTH);
        CARDINAL_FACES.add(BlockFace.WEST);
    }

    public static boolean isDoor(@NotNull Material m) {
        switch(m) {
            case DARK_OAK_DOOR:
            case ACACIA_DOOR:
            case BIRCH_DOOR:
            case JUNGLE_DOOR:
            case SPRUCE_DOOR:
            case WOOD_DOOR:
            case WOODEN_DOOR:
                return true;
            case IRON_DOOR:
            case IRON_DOOR_BLOCK:
                return Config.PROTECT_IRON_DOORS;
            default:
                return false;
        }
    }

    public static boolean isTrapdoor(@NotNull Material m) {
        switch(m) {
            case TRAP_DOOR:
                return true;
            case IRON_TRAPDOOR:
                return Config.PROTECT_IRON_TRAPDOORS;
            default:
                return false;
        }
    }

    public static boolean isChest(@NotNull Material m) {
        switch (m) {
            case CHEST:
            case TRAPPED_CHEST:
                return true;
            default:
                return false;
        }
    }

    public static boolean isSign(@NotNull Material m) {
        switch(m) {
            case WALL_SIGN:
            case SIGN:
            case SIGN_POST:
                return true;
            default:
                return false;
        }
    }

    public static boolean isWallSign(@NotNull Material m) {
        return m == Material.WALL_SIGN;
    }

    @Nullable
    public static Sign blockToBlockSign(@NotNull Block b) {
        if(!isSign(b.getType()))
            return null;

        BlockState state = b.getState();
        if(!(state instanceof Sign))
            return null;

        return (Sign) state;
    }

    public static boolean isValidPrivateSign(@NotNull Sign sign) {
        String line0 = sign.getLine(0);
        return line0.equalsIgnoreCase("[Private]");
    }

    public static boolean isValidMoreUsersSign(@NotNull Sign sign) {
        String line0 = sign.getLine(0);
        return line0.equalsIgnoreCase("[More Users]");
    }

    public static boolean isValidHeader(@NotNull Sign sign) {
        return isValidPrivateSign(sign) || isValidMoreUsersSign(sign);
    }

    public static void toggleOpenable(@NotNull Block b) {
        BlockState state = b.getState();
        if(!(state instanceof Openable))
            return;

        Openable o = (Openable) state;
        o.setOpen(!o.isOpen());
    }

    @NotNull
    public static HashSet<Block> getCardinalBlocks(Block base) {
        HashSet<Block> blocks = new HashSet<>();
        for(BlockFace bf : CARDINAL_FACES) {
            blocks.add(base.getRelative(bf));
        }
        return blocks;
    }
}
