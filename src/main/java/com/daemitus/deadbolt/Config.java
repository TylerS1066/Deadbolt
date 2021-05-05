package com.daemitus.deadbolt;

import com.daemitus.deadbolt.config.AnnotatedConfig;
import com.daemitus.deadbolt.config.ConfigComment;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class Config extends AnnotatedConfig {

    //------------------------------------------------------------------------//
    @ConfigComment("File to load for messages")
    public String language = "english.yml";
    @ConfigComment("Protections will auto-expire if the player is offline for X days. 0 means never expire.")
    public int auto_expire_days = 0;
    @ConfigComment("Allows trapdoors to link with each other vertically")
    public boolean vertical_trapdoors = true;
    @ConfigComment("Allows furnaces to act like chests, one sign for all connected blocks")
    public boolean group_furnaces = true;
    @ConfigComment("Allows dispensers to act like chests, one sign for all connected blocks")
    public boolean group_dispensers = true;
    @ConfigComment("Allows cauldrons to act like chests, one sign for all connected blocks")
    public boolean group_cauldrons = true;
    @ConfigComment("Allows enchantment tables to act like chests, one sign for all connected blocks")
    public boolean group_enchantment_tables = true;
    @ConfigComment("Allows brewing stands to act like chests, one sign for all connected blocks")
    public boolean group_brewing_stands = true;
    @ConfigComment("Allows right click placement of signs automatically on the target")
    public boolean deny_quick_signs = false;
    @ConfigComment("Clear sign selection after using /deadbolt <line> <text>")
    public boolean clear_sign_selection = false;
    @ConfigComment("Denies things such as snowmen opening doors")
    public boolean deny_entity_interact = true;
    @ConfigComment("Denies explosions from breaking protected blocks")
    public boolean deny_explosions = true;
    @ConfigComment("Denies endermen from breaking protected blocks")
    public boolean deny_endermen = true;
    @ConfigComment("Denies pistons from breaking protected blocks")
    public boolean deny_pistons = true;
    @ConfigComment("Denies redstone from toggling protected blocks")
    public boolean deny_redstone = true;
    @ConfigComment("Denies Hopper Minecart from interacting with protected blocks")
    public boolean deny_hoppercart = true;
    @ConfigComment("List of blockIDs protected by redstone unless overrode by [everyone]")
    public List<Material> redstone_protected_blockids = Arrays.asList(
            // Doors
            Material.WOODEN_DOOR,
            Material.IRON_DOOR_BLOCK,
            Material.SPRUCE_DOOR,
            Material.BIRCH_DOOR,
            Material.JUNGLE_DOOR,
            Material.ACACIA_DOOR,
            Material.DARK_OAK_DOOR,
            // trap doors
            Material.TRAP_DOOR,
            Material.IRON_TRAPDOOR
    );
    @ConfigComment("Denies function of the [timer: x] tag on signs")
    public boolean deny_timed_doors = false;
    @ConfigComment("Forces timed doors on every protected (trap)door")
    public boolean forced_timed_doors = false;
    @ConfigComment("Default delay used with forced timed doors")
    public int forced_timed_doors_delay = 3;
    @ConfigComment("Enables sound effects on timed doors")
    public boolean timed_door_sounds = true;
    @ConfigComment("Gives traditional wood door sounds to silent ones (iron doors)")
    public boolean silent_door_sounds = true;
    //------------------------------------------------------------------------//
    private final transient String TAG = "Deadbolt: ";
    //------------------------------------------------------------------------//
    public transient Set<Player> reminder = new HashSet<>();
    public transient Map<Player, Block> selectedSign = new HashMap<>();
    //------------------------------------------------------------------------//
    public final transient Set<BlockFace> CARDINAL_FACES = EnumSet.of(BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST);
    public final transient Set<BlockFace> VERTICAL_FACES = EnumSet.of(BlockFace.UP, BlockFace.DOWN);
    //------------------------------------------------------------------------//
    // TODO: This has nothing to do with configuration. Should be placed somewhere else

    public void sendMessage(CommandSender sender, ChatColor color, String message, String... args) {
        sender.sendMessage(color + TAG + String.format(message, (Object[]) args));
    }

    // TODO: This has nothing to do with configuration. Should be placed somewhere else
    public void sendBroadcast(String permission, ChatColor color, String message, String... args) {
        Bukkit.getServer().broadcast(color + TAG + String.format(message, (Object[]) args), permission);
    }
}
