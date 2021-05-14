package net.tylers1066.db;

import net.tylers1066.util.EnhancedSign;
import net.tylers1066.util.Util;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;

public class DeadboltDetectionTask {
    private final Block root;
    private final HashSet<Block> blocks = new HashSet<>();
    private final HashSet<Block> signs = new HashSet<>();
    private final HashSet<Block> traversed = new HashSet<>();
    private Material type;

    public DeadboltDetectionTask(Block root) {
        this.root = root;
    }

    @Nullable
    public Material getType() {
        return type;
    }

    public HashSet<Block> getBlocks() {
        return blocks;
    }

    public HashSet<Block> getSigns() {
        return signs;
    }

    public void run() {
        detect(root, DetectionType.ROOT);
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

    private enum DetectionType {
        ROOT,
        ROOT_ATTACHED,
        NEW_TYPE,
        SAME_TYPE,
        SUPPORTING_BLOCK,
        SIGN_ONLY
    }

    private void detectSurrounding(@NotNull Block block, @NotNull DetectionType dt) {
        for(Block b : Util.getSurroundingBlocks(block)) {
            detect(b, dt);
        }
    }

    /**
     * @param block Base block to detect from
     * @param dt Detection type to detect
     */
    private void detect(@NotNull Block block, DetectionType dt) {
        if(traversed.contains(block))
            return;

        traversed.add(block);

        Material type = block.getType();
        switch(dt) {
            case ROOT:
                if(Util.isProtectableBlock(type)) {
                    // This is a valid block to protect, start search
                    this.type = type;
                    detectSurrounding(block, DetectionType.SAME_TYPE);
                }
                else if(Util.isWallSign(type)) {
                    // This is a sign, start searching from the attached block
                    Block other = Util.getAttached(block);
                    if(other == null)
                        return;

                    detect(other, DetectionType.ROOT_ATTACHED);
                }
                // This is not a valid block to protect!
                break;

            case ROOT_ATTACHED:
                for(Block b : Util.getSurroundingBlocks(block)) {
                    if(Util.isDoor(b.getType()) && getDoorSupportingBlocks(b).contains(block)) {
                        // Is door that is supported by this block
                        detect(b, DetectionType.NEW_TYPE);
                    }
                    else if(Util.isTrapdoor(b.getType()) && Util.getAttached(b) == block) {
                        // Is trapdoor that is supported by this block
                        detect(b, DetectionType.NEW_TYPE);
                    }

                    // Is not an attached block, search only for a sign
                    detect(b, DetectionType.SIGN_ONLY);
                }
                break;

            case NEW_TYPE:
                if(this.type != null) {
                    // New type already detected, try again as SAME_TYPE
                    detect(block, DetectionType.SAME_TYPE);
                    return;
                }

                // This is a new (possible) base block
                if(!Util.isProtectableBlock(type))
                    return;

                // This is a valid block to protect, start search
                this.type = type;
                detectSurrounding(block, DetectionType.SAME_TYPE);
                break;

            case SAME_TYPE:
                if(Util.isWallSign(type)) {
                    signs.add(block);
                    return;
                }

                if(type != this.type)
                    return;

                blocks.add(block);

                detectSurrounding(block, DetectionType.SAME_TYPE);

                for(Block b : getSupportingBlocks(block)) {
                    detect(b, DetectionType.SUPPORTING_BLOCK);
                }
                break;

            case SIGN_ONLY:
                if(!Util.isWallSign(type))
                    return;

                signs.add(block);
                break;

            case SUPPORTING_BLOCK:
                detectSurrounding(block, DetectionType.SIGN_ONLY);

            default:
                break;
        }
    }

    private void pruneSigns() {
        HashSet<Block> pruneSet = new HashSet<>();
        for(Block sign : signs) {
            Sign s = (new EnhancedSign(sign)).getSign();
            if(s == null || !Util.isValidHeader(s)) {
                pruneSet.add(sign);
                continue;
            }

            Block b = Util.getAttached(sign);
            if(b == null || !blocks.contains(b))
                pruneSet.add(sign);
        }
        for(Block b : pruneSet)
            signs.remove(b);
    }
}
