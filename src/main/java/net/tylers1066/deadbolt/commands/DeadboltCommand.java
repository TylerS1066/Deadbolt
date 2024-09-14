package net.tylers1066.deadbolt.commands;

import net.tylers1066.deadbolt.DeadboltReloaded;
import net.tylers1066.deadbolt.selection.Selection;
import net.tylers1066.deadbolt.selection.SelectionManager;
import net.tylers1066.deadbolt.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeadboltCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player))
            return false;

        if (args.length < 1) {
            sender.sendMessage(ChatColor.GOLD + "Deadbolt-Reloaded" + ChatColor.WHITE + " v" + DeadboltReloaded.getInstance().getDescription().getVersion());
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
            sender.sendMessage("Bad format, your line number should be 1, 2, 3, 4");
            // Invalid line number
            return false;
        }

        Player p = (Player) sender;
        Selection sel = SelectionManager.get(p);
        if (sel == null) {
            sender.sendMessage("Nothing selected, right click a valid sign first");
            // No selection
            return false;
        }
        if (!sel.getDeadbolt().verify()) {
            sender.sendMessage("Selected sign has an error. Right click it again");
            // Failed verification
            return false;
        }
        if (!sel.getDeadbolt().isOwner(p) && !p.hasPermission("deadbolt.admin.commands")) {
            // Not owner and not admin
            return false;
        }

        Sign s = sel.getSign().getSign();
        if (s == null || !Util.isValidHeader(s)) {
            sender.sendMessage("Selected sign has an error. Right click it again");
            // Invalid sign?
            return false;
        }

        if (lineNum == 0) {
            sender.sendMessage("The identifier on line 1 is not changeable");
            // Can't edit first line
            return false;
        }

        if (lineNum == 1 && Util.isValidPrivateSign(s)) {
            sender.sendMessage("The owner on line 2 is not changeable");
            // Can't edit owner
            return false;
        }


        StringBuilder text = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            text.append(args[i]).append(i + 1 < args.length ? " " : "");
        }

        // Format for sign
        String line = text.toString();
        if (!p.hasPermission("deadbolt.user.color"))
            line = ChatColor.stripColor(line);
        line = Util.formatForSign(line);

        // Update sign
        s.setLine(lineNum, line);
        s.update();

        sender.sendMessage("Sign updated");
        return true;
    }
}
