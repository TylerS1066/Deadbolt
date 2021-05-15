package net.tylers1066.selection;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class SelectionManager {
    private static final HashMap<Player, Selection> map = new HashMap<>();

    public static void add(Player p, Selection s) {
        map.put(p, s);
    }

    @Nullable
    public static Selection get(Player p) {
        return map.get(p);
    }

    public static void clear(Player p) {
        map.remove(p);
    }
}
