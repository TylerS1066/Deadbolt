package net.tylers1066.db;

import net.tylers1066.util.Util;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.material.Attachable;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;

public class DeadboltDetectionTask {
    private final Block base;
    private final HashSet<Block> baseBlocks = new HashSet<>();
    private final HashSet<Block> signs = new HashSet<>();
    private final HashSet<Block> traversed = new HashSet<>();
    private Material baseType;

    public DeadboltDetectionTask(Block base) {
        this.base = base;
    }

    @Nullable
    public Material getBaseType() {
        return baseType;
    }

    public HashSet<Block> getBaseBlocks() {
        return baseBlocks;
    }

    public HashSet<Block> getSigns() {
        return signs;
    }

    public void run() {
        detect(base, false, false);
        pruneSigns();
    }

    @NotNull
    private HashSet<Block> getDoorSupportingBlocks(Block base) {
        HashSet<Block> blocks = new HashSet<>();

        // Add top of door if base is the bottom
        Block up = base.getRelative(BlockFace.UP);
        if(up.getType() == base.getType())
            blocks.add(up);

        // Always add the block below, it is either the bottom or the supporting block
        blocks.add(base.getRelative(BlockFace.DOWN));

        return blocks;
    }

    @NotNull
    private HashSet<Block> getSupportingBlocks(Block base) {
        Material type = base.getType();
        if(Util.isTrapdoor(type)) {
            Block b = Util.getAttached(base);
            if(b != null) {
                HashSet<Block> blocks = new HashSet<>();
                blocks.add(b);
                return blocks;
            }
        }
        else if(Util.isDoor(type)) {
            return getDoorSupportingBlocks(base);
        }
        return new HashSet<>();
    }

    /**
     * @param base Base block to search from
     * @param signOnly Search this block as a base block (false) or as a signOnly block (true)
     */
    private void detect(Block base, boolean signOnly, boolean sourceSign) {
        // This logic is 100% based on searching from a door/trapdoor/chest/furnace/etc
        // TODO: Improve it to have a secondary algorithm to search from a sign
        if(traversed.contains(base))
            return;

        traversed.add(base);

        if (Util.isSign(base.getType())) {
            signs.add(base);
        }
        if(signOnly)
            return;

        baseBlocks.add(base);

        // Search supporting blocks as base blocks
        for (Block b : getSupportingBlocks(base)) {
            detect(b, false, false);
        }

        // Search adjacent blocks
        for(Block b : Util.getCardinalBlocks(base)) {
            detect(b, b.getType() != this.base.getType(), false);
            // If not of same type, search for only signs
            // Else, search as a base block
        }
    }

    private void pruneSigns() {
        HashSet<Block> pruneSet = new HashSet<>();
        for(Block sign : signs) {
            Sign s = Util.blockToBlockSign(sign);
            if(s == null || !Util.isValidHeader(s)) {
                pruneSet.add(sign);
                continue;
            }

            Block b = Util.getAttached(sign);
            if(b == null || !baseBlocks.contains(b))
                pruneSet.add(sign);
        }
        for(Block b : pruneSet)
            signs.remove(b);
    }
}
