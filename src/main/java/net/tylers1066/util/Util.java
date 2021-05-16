package net.tylers1066.util;

import net.tylers1066.config.Config;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.material.Attachable;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;

public class Util {
    private static final HashSet<BlockFace> CARDINAL_FACES = new HashSet<>();
    private static final HashSet<BlockFace> VERTICAL_FACES = new HashSet<>();

    public static void init() {
        CARDINAL_FACES.add(BlockFace.NORTH);
        CARDINAL_FACES.add(BlockFace.EAST);
        CARDINAL_FACES.add(BlockFace.SOUTH);
        CARDINAL_FACES.add(BlockFace.WEST);
        VERTICAL_FACES.add(BlockFace.UP);
        VERTICAL_FACES.add(BlockFace.DOWN);
    }


    public static boolean isWallSign(@NotNull Material m) {
        return m == Material.WALL_SIGN;
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
                return Config.protectIronDoors;
            default:
                return false;
        }
    }

    public static boolean isTrapdoor(@NotNull Material m) {
        switch(m) {
            case TRAP_DOOR:
                return true;
            case IRON_TRAPDOOR:
                return Config.protectIronTrapdoors;
            default:
                return false;
        }
    }

    public static boolean isGate(@NotNull Material m) {
        switch(m) {
            case FENCE_GATE:
            case BIRCH_FENCE_GATE:
            case SPRUCE_FENCE_GATE:
            case JUNGLE_FENCE_GATE:
            case ACACIA_FENCE_GATE:
            case DARK_OAK_FENCE_GATE:
                return true;
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

    public static boolean isFurnace(@NotNull Material m) {
        switch(m) {
            case FURNACE:
            case BURNING_FURNACE:
                return true;
            default:
                return false;
        }
    }

    public static boolean isDispenser(@NotNull Material m) {
        return m == Material.DISPENSER;
    }

    public static boolean isDropper(@NotNull Material m) {
        return m == Material.DROPPER;
    }

    public static boolean isOtherContainer(@NotNull Material m) {
        switch(m) {
            case BREWING_STAND:
            case CAULDRON:
            case ENCHANTMENT_TABLE:
            case BEACON:
            case ENDER_CHEST:
            case ANVIL:
                return true;
            default:
                return false;
        }
    }


    public static boolean isProtectableBlock(@NotNull Material m) {
        return isDoor(m) || isTrapdoor(m) || isChest(m) || isFurnace(m) || isDispenser(m) || isDropper(m) || isGate(m) || isOtherContainer(m);
    }


    public static boolean isValidHeader(@NotNull String line0) {
        return isValidPrivateSign(line0) || isValidMoreUsersSign(line0);
    }

    public static boolean isValidPrivateSign(@NotNull String line0) {
        return line0.equalsIgnoreCase("[Private]");
    }

    public static boolean isValidMoreUsersSign(@NotNull String line0) {
        return line0.equalsIgnoreCase("[More Users]");
    }

    public static boolean isValidHeader(@NotNull Sign sign) {
        String line0 = sign.getLine(0);
        return isValidHeader(line0);
    }

    public static boolean isValidPrivateSign(@NotNull Sign sign) {
        String line0 = sign.getLine(0);
        return isValidPrivateSign(line0);
    }

    public static boolean isValidMoreUsersSign(@NotNull Sign sign) {
        String line0 = sign.getLine(0);
        return isValidMoreUsersSign(line0);
    }



    @Nullable
    public static Block getAttached(@NotNull Block b) {
        MaterialData data = b.getState().getData();
        if(!(data instanceof Attachable))
            return null;

        Attachable a = (Attachable) data;
        return b.getRelative(a.getAttachedFace());
    }


    @NotNull
    public static HashSet<Block> getCardinalBlocks(Block base) {
        HashSet<Block> blocks = new HashSet<>();
        for(BlockFace bf : CARDINAL_FACES) {
            blocks.add(base.getRelative(bf));
        }
        return blocks;
    }

    @NotNull
    public static HashSet<Block> getSurroundingBlocks(Block base) {
        HashSet<Block> blocks = new HashSet<>();
        for(BlockFace bf : CARDINAL_FACES) {
            blocks.add(base.getRelative(bf));
        }
        for(BlockFace bf : VERTICAL_FACES) {
            blocks.add(base.getRelative(bf));
        }
        return blocks;
    }


    @NotNull
    public static String formatForSign(String s) {
        s = ChatColor.stripColor(s);
        s = s.substring(0, Math.min(s.length(), 15));
        return s;
    }
}
