package net.tylers1066.db;

import net.tylers1066.DeadboltReloaded;
import net.tylers1066.util.EnhancedBlock;
import net.tylers1066.util.EnhancedSign;
import net.tylers1066.util.Util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Openable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class Deadbolt {
    private final Material type;
    private final Set<EnhancedBlock> blocks;
    private final Set<EnhancedSign> signs;
    private final String owner;
    private final Set<String> members;
    private final boolean isEveryone;
    private final int timer;

    private static Set<EnhancedBlock> convert(Set<Block> blocks) {
        Set<EnhancedBlock> eb = new HashSet<>();
        for (Block b : blocks) {
            eb.add(new EnhancedBlock(b));
        }
        return eb;
    }

    public Deadbolt(@NotNull Block base) {
        DeadboltDetectionTask detection = new DeadboltDetectionTask(base);
        detection.run();

        type = detection.getType();
        blocks = convert(detection.getBlocks());

        DeadboltParseTask parse = new DeadboltParseTask(detection.getSigns());
        parse.run();

        signs = parse.getSigns();
        owner = parse.getOwner();
        members = parse.getMembers();
        isEveryone = parse.isEveryone();
        timer = parse.getTimer();
    }

    public boolean isProtected() {
        return owner != null && type != null;
    }

    public String getOwner() {
        return owner;
    }

    public boolean isOwner(Player p) {
        return Util.formatForSign(p.getName()).equalsIgnoreCase(owner);
    }

    public boolean isEveryone() {
        return isEveryone;
    }

    public boolean isMember(Player p) {
        if (isEveryone)
            return true;

        if (isOwner(p))
            return true;

        String name = Util.formatForSign(p.getName());
        for (String s : members) {
            if (s.equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    public boolean verify() {
        // Verify blocks
        for (EnhancedSign sign : signs) {
            if (!Util.isWallSign(sign.getLocation().getBlock().getType()))
                return false;
        }
        for (EnhancedBlock b : blocks) {
            if (b.getLocation().getBlock().getType() != type)
                return false;
        }
        return true;
    }

    public void toggle() {
        if (!verify())
            return;

        toggleAll();
        if (timer != -1) {
            Bukkit.getScheduler().runTaskLater(DeadboltReloaded.getInstance(), this::toggleAll, timer * 20L);
        }
    }

    private void toggleAll() {
        boolean isOpen = false;
        boolean first = true;

        for (EnhancedBlock block : blocks) {
            Material type = block.getBlock().getType();
            if (type != this.type)
                continue;

            if (!Util.isTrapdoor(type) && !Util.isGate(type)
                    && !(Util.isDoor(type) && Util.isLowerDoor(block.getBlock())))
                continue;

            BlockData data = block.getBlock().getBlockData();
            if (!(data instanceof Openable))
                return;

            Openable openable = (Openable) data;
            if (first) {
                isOpen = !openable.isOpen();
                first = false;
            }
            openable.setOpen(isOpen);
            block.getBlock().setBlockData(openable);
        }
    }

    @Nullable
    public Material getType() {
        return type;
    }

    public int getBlockCount() {
        return blocks.size();
    }

    public String toString() {
        return "Deadbolt of type " + (type == null ? "null" : type) + " with " + blocks.size() + " blocks and "
                + signs.size() + " signs with owner " + (owner == null ? "null" : owner);
    }
}
