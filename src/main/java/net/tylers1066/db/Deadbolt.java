package net.tylers1066.db;

import net.tylers1066.util.EnhancedBlock;
import net.tylers1066.util.EnhancedSign;
import net.tylers1066.util.Util;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;

public class Deadbolt {
    private final Material type;
    private final HashSet<EnhancedBlock> blocks;
    private final HashSet<EnhancedSign> signs;
    private final String owner;
    private final HashSet<String> members;
    private final boolean isEveryone;

    private static HashSet<EnhancedBlock> convert(HashSet<Block> blocks) {
        HashSet<EnhancedBlock> eb = new HashSet<>();
        for(Block b : blocks) {
            eb.add(new EnhancedBlock(b));
        }
        return eb;
    }

    public Deadbolt(Block base) {
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
    }

    public boolean isProtected() {
        return owner != null;
    }

    public boolean isOwner(Player p) {
        return Util.formatForSign(p.getName()).equalsIgnoreCase(owner);
    }

    public boolean isEveryone() {
        return isEveryone;
    }

    public boolean isMember(Player p) {
        if(isEveryone)
            return true;

        if(isOwner(p))
            return true;

        String name = Util.formatForSign(p.getName());
        for(String s : members) {
            if(s.equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    public boolean verify() {
        // Verify blocks
        for(EnhancedSign sign : signs) {
            if(!Util.isWallSign(sign.getLocation().getBlock().getType()))
                return false;
        }
        for(EnhancedBlock b : blocks) {
            if(b.getLocation().getBlock().getType() != type)
                return false;
        }
        return true;
    }

    public void toggleDoors() {
        boolean isOpen = false;
        boolean first = true;
        if(!verify()) {
            return;
        }

        for(EnhancedBlock b : blocks) {
            Material type = b.getBlock().getType();
            if(type != this.type)
                continue;

            if(Util.isDoor(type) || Util.isTrapdoor(type) || Util.isGate(type)) {
                BlockState s = b.getBlock().getState();
                MaterialData data = s.getData();
                if(!(data instanceof Openable))
                    return;

                Openable o = (Openable) data;
                if(first) {
                    isOpen = !o.isOpen();
                    first = false;
                }
                o.setOpen(isOpen);
                s.setData(data);
                s.update();
            }
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
