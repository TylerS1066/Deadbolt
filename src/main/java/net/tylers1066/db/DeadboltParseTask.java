package net.tylers1066.db;

import net.tylers1066.util.EnhancedSign;
import net.tylers1066.util.Util;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;

public class DeadboltParseTask {
    private final HashSet<Block> signBlocks;

    private String owner;
    private final HashSet<String> members = new HashSet<>();
    private final HashSet<EnhancedSign> signs = new HashSet<>();
    private boolean isEveryone = false;

    private int timer = -1;

    public DeadboltParseTask(HashSet<Block> signBlocks) {
        this.signBlocks = signBlocks;
    }

    public void run() {
        parse();
    }

    public HashSet<EnhancedSign> getSigns() {
        return signs;
    }

    @Nullable
    public String getOwner() {
        return owner;
    }

    @NotNull
    public HashSet<String> getMembers() {
        return members;
    }

    public boolean isEveryone() {
        return isEveryone;
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
        catch (NumberFormatException ignored) {
            return;
        }
    }

    private void parse() {
        for(Block b : signBlocks) {
            EnhancedSign es = new EnhancedSign(b);

            Sign s = es.getSign();
            if(s == null)
                continue;

            if(Util.isValidPrivateSign(s)) {
                if(owner != null)
                    throw new RuntimeException("Double private sign!");

                owner = s.getLine(1);
                add(s.getLine(2));
                add(s.getLine(3));
                signs.add(es);
            }
            else if(Util.isValidMoreUsersSign(s)) {
                add(s.getLine(1));
                add(s.getLine(2));
                add(s.getLine(3));
                signs.add(es);
            }
        }
    }
}
