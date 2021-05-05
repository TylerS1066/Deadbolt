package com.daemitus.deadbolt;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class Config {

    public String language = "english.yml";
    public int auto_expire_days = 0;
    public boolean vertical_trapdoors = true;
    public boolean group_furnaces = true;
    public boolean group_dispensers = true;
    public boolean group_cauldrons = true;
    public boolean group_enchantment_tables = true;
    public boolean group_brewing_stands = true;
    public boolean deny_quick_signs = false;
    public boolean clear_sign_selection = false;
    public boolean deny_entity_interact = true;
    public boolean deny_explosions = true;
    public boolean deny_endermen = true;
    public boolean deny_pistons = true;
    public boolean deny_redstone = true;
    public boolean deny_hoppercart = true;
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
    public boolean deny_timed_doors = false;
    public boolean forced_timed_doors = false;
    public int forced_timed_doors_delay = 3;
    public boolean timed_door_sounds = true;
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
