package net.tylers1066.db;

import net.tylers1066.util.Util;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;

public class Deadbolt {
    private final Material baseType;
    private final HashSet<Block> baseBlocks;
    private final String owner;
    private final HashSet<String> members;
    private final boolean isEveryone;

    public Deadbolt(Block base) {
        DeadboltDetectionTask detection = new DeadboltDetectionTask(base);
        detection.run();

        baseType = detection.getBaseType();
        baseBlocks = detection.getBaseBlocks();

        DeadboltParseTask parse = new DeadboltParseTask(detection.getSigns());
        parse.run();

        owner = parse.getOwner();
        members = parse.getMembers();
        isEveryone = parse.isEveryone();
    }

    public boolean isProtected() {
        return owner != null;
    }

    public boolean isOwner(Player p) {
        return p.getName().equalsIgnoreCase(owner);
    }

    public boolean isEveryone() {
        return isEveryone;
    }

    public boolean isMember(Player p) {
        if(isEveryone)
            return true;

        String name = p.getName();
        for(String s : members) {
            if(s.equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    public void toggleDoors() {
        for(Block b : baseBlocks) {
            Material type = b.getType();
            if(type != baseType)
                continue;

            if(Util.isDoor(type) || Util.isTrapdoor(type)) {
                Util.toggleOpenable(b);
            }
        }
    }

    @Nullable
    public Material getBaseType() {
        return baseType;
    }

    public int getBlockCount() {
        return baseBlocks.size();
    }
}
