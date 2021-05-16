package net.tylers1066.commands;

import net.tylers1066.DeadboltReloaded;
import net.tylers1066.selection.Selection;
import net.tylers1066.selection.SelectionManager;
import net.tylers1066.util.Util;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeadboltCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player))
            return false;

        if(args.length < 1) {
            sender.sendMessage("Deadbolt v" + DeadboltReloaded.getInstance().getDescription().getVersion());
            return true;
        }

        int lineNum;
        try {
            lineNum = Integer.parseInt(args[0]);
            if(lineNum < 1 || lineNum > 4)
                throw new NumberFormatException();

            --lineNum;
        }
        catch (NumberFormatException e) {
            // Invalid line number
            return false;
        }

        Player p = (Player) sender;
        Selection sel = SelectionManager.get(p);
        if(sel == null) {
            // No selection
            return false;
        }
        if(!sel.getDeadbolt().verify()) {
            // Failed verification
            return false;
        }
        if(!sel.getDeadbolt().isOwner(p)) {
            // Not owner
            return false;
        }

        Sign s = sel.getSign().getSign();
        if(s == null || !Util.isValidHeader(s)) {
            // Invalid sign?
            return false;
        }

        if(lineNum == 0) {
            // Can't edit first line
            return false;
        }

        if(lineNum == 1 && Util.isValidPrivateSign(s)) {
            // Can't edit owner
            return false;
        }


        StringBuilder text = new StringBuilder();
        for(int i = 1; i < args.length; i++) {
            text.append(args[i]).append(i + 1 < args.length ? " " : "");
        }

        // Format for sign
        String line = Util.formatForSign(text.toString());

        // Update sign
        s.setLine(lineNum, line);
        s.update();
        return true;
    }
}
