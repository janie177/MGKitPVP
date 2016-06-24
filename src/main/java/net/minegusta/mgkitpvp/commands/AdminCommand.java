package net.minegusta.mgkitpvp.commands;

import com.google.common.collect.Lists;
import net.minegusta.mgkitpvp.mapmanager.SpawnManager;
import net.minegusta.mgkitpvp.npcs.NPCManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class AdminCommand implements CommandExecutor {

	private static List<String> help = Lists.newArrayList("/heropvp help - Show this menu", "/heropvp addspawn <name> - Add current location as a spawn.", "/heropvp deletespawn <name> - Remove given location as spawn.", "/heropvp list - List all spawns.", "/heropvp spawn <shop/start/selector> - Spawn an NPC with the given role.");

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if(!(sender instanceof Player)) return true;

		Player p = (Player) sender;

		if(!p.hasPermission("heropvp.admin")) return true;


		if(args.length < 1 || args[0].equalsIgnoreCase("help"))
		{
			sendHelp(p);
			return true;
		}

		if(args[0].equalsIgnoreCase("list"))
		{
			p.sendMessage(ChatColor.DARK_GRAY + "Current Spawns:");
			for(String s : SpawnManager.getLocations().keySet())
			{
				p.sendMessage(ChatColor.YELLOW + " - " + s);
			}
			return true;
		}

		if(args.length < 2)
		{
			sendHelp(p);
			return true;
		}

		if(args[0].equalsIgnoreCase("spawn"))
		{
			String type = args[1].toLowerCase();
			switch (type)
			{
				case "spawn":
				{
					p.sendMessage(ChatColor.GREEN + "You spawned a game start NPC.");
					NPCManager.spawnGameStartNPC(p);
				}
				break;
				case "selector":
				{
					p.sendMessage(ChatColor.GREEN + "You spawned a hero selector NPC.");
					NPCManager.spawnClassSelectorNPC(p);
				}
				break;
				case "shop":
				{
					p.sendMessage(ChatColor.GREEN + "You spawned a shop NPC.");
					NPCManager.spawnShopNPC(p);
				}
				break;
			}
			return true;
		}

		if(args[0].equalsIgnoreCase("addspawn"))
		{
			String spawnName = args[1].toLowerCase();
			SpawnManager.addSpawnLocation(spawnName, p.getLocation());
			p.sendMessage(ChatColor.GREEN + "You added a spawn.");

			return true;
		}
		else if(args[0].equalsIgnoreCase("removespawn"))
		{
			String spawnName = args[1].toLowerCase();
			if(SpawnManager.isSpawnLocation(spawnName))
			{
				SpawnManager.removeSpawnLocation(spawnName);
				p.sendMessage(ChatColor.GREEN + "You removed a spawn.");
			}
			else
			{
				p.sendMessage(ChatColor.RED + "That spawn could not be found.");
			}
			return true;
		}
		else
		{
			sendHelp(p);
			return true;
		}
	}

	private void sendHelp(Player p)
	{
		for(String s : help)
		{
			p.sendMessage(ChatColor.DARK_GRAY + s);
		}
	}
}
