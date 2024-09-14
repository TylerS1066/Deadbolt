package net.tylers1066.deadbolt.util;

import net.tylers1066.deadbolt.config.Config;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Door;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class Util {
    private static final Set<BlockFace> CARDINAL_FACES = new HashSet<>();
    private static final Set<BlockFace> VERTICAL_FACES = new HashSet<>();

    public static void init() {
        CARDINAL_FACES.add(BlockFace.NORTH);
        CARDINAL_FACES.add(BlockFace.EAST);
        CARDINAL_FACES.add(BlockFace.SOUTH);
        CARDINAL_FACES.add(BlockFace.WEST);
        VERTICAL_FACES.add(BlockFace.UP);
        VERTICAL_FACES.add(BlockFace.DOWN);
    }


    public static boolean isWallSign(@NotNull Material m) {
        return Tag.WALL_SIGNS.getValues().contains(m);
    }


    public static boolean isDoor(@NotNull Material m) {
        if(isTrapdoor(m))
            return false;

        if(!m.name().contains("DOOR"))
            return false;

        if(m.name().contains("IRON"))
            return Config.protectIronDoors;

        return true;
    }

    public static boolean isLowerDoor(@NotNull Block b) {
        BlockData data = b.getBlockData();
        if(!(data instanceof Door))
            return false;

        Door d = (Door) data;
        return d.getHalf() == Half.BOTTOM;
    }

    public static boolean isTrapdoor(@NotNull Material m) {
        return Tag.TRAPDOORS.getValues().contains(m);
    }

    public static boolean isGate(@NotNull Material m) {
        return m.name().contains("FENCE_GATE");
    }


    public static boolean isChest(@NotNull Material m) {
        switch (m) {
            case CHEST:
            case TRAPPED_CHEST:
            case BARREL:
                return true;
            default:
                return false;
        }
    }

    public static boolean isFurnace(@NotNull Material m) {
        switch (m) {
            case FURNACE:
            case SMOKER:
            case BLAST_FURNACE:
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
            case CARTOGRAPHY_TABLE:
            case SMITHING_TABLE:
            case FLETCHING_TABLE:
            case STONECUTTER:
            case GRINDSTONE:
            case LECTERN:
            case LOOM:
            case BREWING_STAND:
            case CAULDRON:
            case ENCHANTING_TABLE:
            case BEACON:
            case ENDER_CHEST:
            case ANVIL:
                return true;
            default:
                return false;
        }
    }


    public static boolean isProtectableBlock(@NotNull Material m) {
        return isDoor(m) || isTrapdoor(m)
                || isChest(m) || isFurnace(m) || isDispenser(m) || isDropper(m)
                || isGate(m) || isOtherContainer(m);
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
        BlockData data = b.getBlockData();
        if (!(data instanceof Directional))
            return null;

        Directional directional = (Directional) data;
        return b.getRelative(directional.getFacing().getOppositeFace());
    }


    @NotNull
    public static Set<Block> getCardinalBlocks(Block base) {
        HashSet<Block> blocks = new HashSet<>();
        for (BlockFace bf : CARDINAL_FACES) {
            blocks.add(base.getRelative(bf));
        }
        return blocks;
    }

    @NotNull
    public static Set<Block> getSurroundingBlocks(Block base) {
        HashSet<Block> blocks = new HashSet<>();
        for (BlockFace bf : CARDINAL_FACES) {
            blocks.add(base.getRelative(bf));
        }
        for (BlockFace bf : VERTICAL_FACES) {
            blocks.add(base.getRelative(bf));
        }
        return blocks;
    }


    @NotNull
    public static String formatForSign(String s) {
        s = s.substring(0, Math.min(s.length(), 15));
        return s;
    }
}
