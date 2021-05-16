package net.tylers1066.db;

import net.tylers1066.util.EnhancedSign;
import net.tylers1066.util.Util;
import org.bukkit.Bukkit;
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
    private final HashSet<Block> supporting = new HashSet<>();
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
        Bukkit.broadcastMessage("Found " + blocks.size() + " of type " + type + " and " + signs.size() + " signs");
        pruneSigns();
        Bukkit.broadcastMessage("Now has " + signs.size() + " signs");
    }

    @Nullable
    private Block getDoorSupportingBlock(Block base) {
        // Return empty if this is the top of the door
        if(!Util.isLowerDoor(base))
            return null;

        // Add the supporting block and return
        return base.getRelative(BlockFace.DOWN);
    }

    @Nullable
    private Block getSupportingBlock(Block base) {
        Material type = base.getType();
        if(Util.isTrapdoor(type)) {
            return Util.getAttached(base);
        }
        else if(Util.isDoor(type)) {
            return getDoorSupportingBlock(base);
        }
        return null;
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

    private void detectSupporting(@NotNull Block block) {
        Block b = getSupportingBlock(block);
        if(b != null)
            detect(b, DetectionType.SUPPORTING_BLOCK);
    }

    /**
     * @param block Base block to detect from
     * @param dt Detection type to detect
     */
    private void detect(@NotNull Block block, DetectionType dt) {
        Bukkit.broadcastMessage("Detect " + block + "," + dt);
        if(traversed.contains(block))
            return;

        traversed.add(block);

        Material type = block.getType();
        switch(dt) {

            case ROOT:
                if(Util.isProtectableBlock(type)) {
                    // This is a valid block to protect, start search
                    this.type = type;
                    blocks.add(block);
                    detectSupporting(block);
                    detectSurrounding(block, DetectionType.SAME_TYPE);
                }
                else if(Util.isWallSign(type)) {
                    // This is a sign, start searching from the attached block
                    Block other = Util.getAttached(block);
                    if(other == null)
                        return;

                    detect(block, DetectionType.SIGN_ONLY);
                    supporting.add(other);
                    detect(other, DetectionType.ROOT_ATTACHED);
                }
                // This is not a valid block to protect!
                break;


            case ROOT_ATTACHED:
                for(Block b : Util.getSurroundingBlocks(block)) {
                    Block support = getSupportingBlock(b);
                    if(block.equals(support)) {
                        // Other block is supported by this
                        detect(b, DetectionType.NEW_TYPE);
                    }
                    else {
                        // Is not an attached block, search only for a sign
                        detect(b, DetectionType.SIGN_ONLY);
                    }
                }
                detect(block, DetectionType.NEW_TYPE);
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

                blocks.add(block);
                detectSupporting(block);
                detectSurrounding(block, DetectionType.SAME_TYPE);
                break;


            case SAME_TYPE:
                if(Util.isWallSign(type)) {
                    signs.add(block);
                    return;
                }

                if(type != this.type) {
                    // Fix for chests being different types but still wanted to be merged
                    if(!(Util.isChest(type) && Util.isChest(this.type)))
                        return;
                }

                blocks.add(block);
                detectSupporting(block);
                detectSurrounding(block, DetectionType.SAME_TYPE);
                break;


            case SIGN_ONLY:
                if(!Util.isWallSign(type))
                    return;

                signs.add(block);
                break;


            case SUPPORTING_BLOCK:
                supporting.add(block);
                detectSurrounding(block, DetectionType.SIGN_ONLY);
                break;


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
            if(b == null || !(blocks.contains(b) || supporting.contains(b)))
                pruneSet.add(sign);
        }
        for(Block b : pruneSet)
            signs.remove(b);
    }
}
