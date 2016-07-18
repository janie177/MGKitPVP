package net.minegusta.heropvp.commands;

import com.google.common.collect.Sets;
import net.minegusta.heropvp.leaderboard.highscores.SignManager;
import net.minegusta.heropvp.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HighscoreBoardCommand implements CommandExecutor{
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!sender.isOp() || !(sender instanceof Player)) return true;

		Player p = (Player) sender;
		if(args.length == 0)
		{
			p.sendMessage(ChatColor.RED + "/hsb <index>");
			return true;
		}
		String arg1 = args[0];
		try
		{
			int index  = Integer.parseInt(arg1);
			if(index > 18 || index < 0)
			{
				p.sendMessage(ChatColor.RED + "Index has to be between 1 and 18.");
				return true;
			}

			Block b = p.getTargetBlock(Sets.newHashSet(Material.AIR), 20);
			if(b == null || (b.getType() != Material.WALL_SIGN && b.getType() != Material.SIGN_POST && b.getType() != Material.SIGN))
			{
				p.sendMessage(ChatColor.RED + "You have to be looking at a sign.");
				return true;
			}

			SignManager.addSign(index, b.getLocation());
			p.sendMessage(ChatColor.GREEN + "You added a sign with index " + index + ".");
			Main.getScoreManager().saveConfig();
			return true;


		} catch (Exception ignored)
		{
			p.sendMessage(ChatColor.RED + "/hsb <index between 1-18>");
			return true;
		}
	}
}
