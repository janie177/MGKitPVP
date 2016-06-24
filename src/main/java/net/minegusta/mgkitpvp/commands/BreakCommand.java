package net.minegusta.mgkitpvp.commands;

import net.minegusta.mgkitpvp.main.Main;
import net.minegusta.mgkitpvp.saving.MGPlayer;
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
			}
		}
		return true;
	}
}
