package net.minegusta.heropvp.commands;

import net.minegusta.heropvp.main.Main;
import net.minegusta.heropvp.saving.MGPlayer;
import net.minegusta.mglib.utils.TitleUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BreakCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player)
		{
			Player player = (Player) sender;
			MGPlayer mgp = Main.getSaveManager().getMGPlayer(player);
			if(mgp.isPlaying())
			{
				mgp.resetOnMapChange();
				TitleUtil.createTitle(ChatColor.YELLOW + "You have been returned to the spawn.", "", 10, 40, 10, true).send(player);

			}
		}
		return true;
	}
}
