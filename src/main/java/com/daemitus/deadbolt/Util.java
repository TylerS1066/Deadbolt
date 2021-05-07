package com.daemitus.deadbolt;

import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.material.Openable;

public final class Util {

    protected static final String patternBracketTooLong = "\\[.{14,}\\]";
    private static final Pattern PSEUDO_COLOR = Pattern.compile("\\&([0-9a-f])");

    public static int blockFaceToNotch(BlockFace face) {
        switch (face) {
            case DOWN:
                return 0;
            case UP:
                return 1;
            case NORTH:
                return 2;
            case SOUTH:
                return 3;
            case WEST:
                return 4;
            case EAST:
                return 5;
            default:
                return 7; // Good as anything here, but technically invalid
        }
    }

    public static String formatForSign(String line, int maxlen) {
        line = removeColor(line);
        line = line.substring(0, Math.min(line.length(), maxlen));
        return line;
    }

    public static String formatForSign(String line) {
        return formatForSign(line, 15);
    }

    public static boolean signNameEqualsPlayerName(String signName, String playerName) {
        String playerName15 = formatForSign(playerName);

        return signName.equalsIgnoreCase(playerName15);
    }

    public static Block getSignAttached(Sign signState) {
        return signState.getBlock().getRelative(((org.bukkit.material.Sign) signState.getData()).getAttachedFace());
    }

    public static String removeColor(String text) {
        if (text == null) {
            return null;
        }
        return ChatColor.stripColor(text);
    }

    public static String createColor(String text) {
        return text == null ? null : PSEUDO_COLOR.matcher(text).replaceAll("\u00A7$1");
    }

    public static String getLine(Sign signBlock, int line) {
        return removeColor(signBlock.getLine(line));
    }

    public static String truncate(String text) {
        if (text.matches(patternBracketTooLong)) {
            return "[" + text.substring(1, 14) + "]";
        }
        return text;
    }

    public static void toggleOpenable(Block block) {
        if(!((block.getState()) instanceof Openable))
            return;

        Openable openable = (Openable) block.getState().getData();
        openable.setOpen(!openable.isOpen());
    }
}
