package net.tylers1066.db;

import net.tylers1066.util.EnhancedSign;
import net.tylers1066.util.Util;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class DeadboltParseTask {
    private final Set<Block> signBlocks;

    private String owner;
    private final Set<String> members = new HashSet<>();
    private final Set<EnhancedSign> signs = new HashSet<>();
    private boolean isEveryone = false;

    private int timer = -1;

    public DeadboltParseTask(@NotNull Set<Block> signBlocks) {
        this.signBlocks = signBlocks;
    }

    public void run() {
        parse();
    }

    public Set<EnhancedSign> getSigns() {
        return signs;
    }

    @Nullable
    public String getOwner() {
        return owner;
    }

    @NotNull
    public Set<String> getMembers() {
        return members;
    }

    public boolean isEveryone() {
        return isEveryone;
    }

    public int getTimer() {
        return timer;
    }

    private void add(String member) {
        if(!member.startsWith("[") || !member.endsWith("]")) {
            members.add(member);
            return;
        }

        if(member.equalsIgnoreCase("[Everyone]")) {
            isEveryone = true;
            return;
        }

        String time;
        try {
            time = member.substring(1, 6);
        }
        catch (IndexOutOfBoundsException ignored) {
            return;
        }

        if (!time.equalsIgnoreCase("Timer"))
            return;

        time = member.substring(7, member.length() - 1);
        try {
            timer = Integer.parseInt(time);
        }
        catch (NumberFormatException ignored) { }
    }

    private void parse() {
        for (Block b : signBlocks) {
            EnhancedSign sign = new EnhancedSign(b);

            Sign s = sign.getSign();
            if (s == null)
                continue;

            if (Util.isValidPrivateSign(s)) {
                if (owner == null) {
                    owner = s.getLine(1);
                    add(s.getLine(2));
                    add(s.getLine(3));
                    signs.add(sign);
                }
                else if (!owner.equals(s.getLine(1))) {
                    // Note: this *will* cause problems, but yo we just ignore them!
                    owner = "Double private sign!";
                }
            }
            else if (Util.isValidMoreUsersSign(s)) {
                add(s.getLine(1));
                add(s.getLine(2));
                add(s.getLine(3));
                signs.add(sign);
            }
        }
    }
}
