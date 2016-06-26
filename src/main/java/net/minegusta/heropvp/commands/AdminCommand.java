package net.minegusta.heropvp.commands;

import com.google.common.collect.Lists;
import net.minegusta.heropvp.main.Main;
import net.minegusta.heropvp.mapmanager.SpawnManager;
import net.minegusta.heropvp.npcs.NPCConfiguration;
import net.minegusta.heropvp.npcs.NPCManager;
import net.minegusta.heropvp.npcs.NPCType;
import net.minegusta.mglib.utils.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class AdminCommand implements CommandExecutor {

	private static List<String> help = Lists.newArrayList("/heropvp help - Show this menu.", "/heropvp addarena <name> - Add arena with current location as spawn.", "/heropvp removearena <name> - Remove arena.", "/heropvp addspawn <arena> - Add current location as a spawn to given arena.", "/heropvp removespawn <arena> <index> - Remove given index from arena spawns.", "/heropvp list - List all arenas.", "/heropvp listspawns <arena> - List all spawn locations for arena.", "/heropvp addNPC <name> <shop/spawn/selector> - Spawn an NPC with the given role.", "/heropvp npclist - List all NPC's.", "/heropvp removeNPC <name> - Remove the given NPC.", "/heropvp resetNPCS - Reset all NPC's.", "/heropvp addtickets <name> <amount> - Add tickets to someone.");

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if(!sender.hasPermission("heropvp.admin")) return true;

		if(args.length > 2 && args[0].equalsIgnoreCase("addtickets"))
		{
			try {
				Player player = Bukkit.getPlayer(args[1]);
				int amount = Integer.valueOf(args[2]);
				Main.getSaveManager().getMGPlayer(player).addTickets(amount, 20);
			} catch (Exception ignored) {}
			return true;
		}

		if(!(sender instanceof Player)) return true;

		Player p = (Player) sender;


		if(args.length < 1 || args[0].equalsIgnoreCase("help"))
		{
			sendHelp(p);
			return true;
		}

		if(args[0].equalsIgnoreCase("list"))
		{
			p.sendMessage(ChatColor.DARK_GRAY + "Current Arenas:");
			for(String s : SpawnManager.getArenas().keySet())
			{
				p.sendMessage(ChatColor.YELLOW + " - " + s);
			}
			return true;
		}

		if(args[0].equalsIgnoreCase("npclist"))
		{
			p.sendMessage(ChatColor.DARK_GRAY + "Current NPC's");
			for(String s : NPCConfiguration.getNPCNames())
			{
				p.sendMessage(ChatColor.YELLOW + " - " + s);
			}
			return true;
		}

		if(args[0].equalsIgnoreCase("resetnpcs"))
		{
			NPCConfiguration.resetNPCS();
			return true;
		}


		if(args.length < 2)
		{
			sendHelp(p);
			return true;
		}

		if(args[0].equalsIgnoreCase("addNPC") && args.length > 2)
		{
			String name = args[1].toLowerCase();
			String type = args[2].toLowerCase();
			switch (type)
			{
				case "spawn":
				{
					p.sendMessage(ChatColor.GREEN + "You spawned a game start NPC.");
					NPCConfiguration.addNPC(name, p.getLocation(), NPCType.SPAWN);
				}
				break;
				case "selector":
				{
					p.sendMessage(ChatColor.GREEN + "You spawned a hero selector NPC.");
					NPCConfiguration.addNPC(name, p.getLocation(), NPCType.SELECTOR);
				}
				break;
				case "shop":
				{
					p.sendMessage(ChatColor.GREEN + "You spawned a shop NPC.");
					NPCConfiguration.addNPC(name, p.getLocation(), NPCType.SHOP);
				}
				break;
			}
			NPCManager.getNpcConfig().saveConfig();
			return true;
		}


		if(args[0].equalsIgnoreCase("removeNPC"))
		{
			String name = args[1].toLowerCase();
			NPCConfiguration.removeNPC(name);
			NPCManager.getNpcConfig().saveConfig();
			return true;
		}


		/**
		 * ARENAS ------------------------------------------------------------|
		 */



		if(args[0].equalsIgnoreCase("addarena"))
		{
			String spawnName = args[1].toLowerCase();
			if(SpawnManager.isArena(spawnName))
			{
				p.sendMessage(ChatColor.RED + "Arena already exists.");
				return true;
			}
			SpawnManager.addArena(spawnName, p.getLocation());
			p.sendMessage(ChatColor.GREEN + "You added an arena.");

			return true;
		}

		else if(args[0].equalsIgnoreCase("removearena"))
		{
			String spawnName = args[1].toLowerCase();
			if(SpawnManager.isArena(spawnName))
			{
				SpawnManager.removeArena(spawnName);
				p.sendMessage(ChatColor.GREEN + "You removed an arena.");
			}
			else
			{
				p.sendMessage(ChatColor.RED + "That arena could not be found.");
			}
			return true;
		}

		else if(args[0].equalsIgnoreCase("addspawn"))
		{
			String arenaName = args[1].toLowerCase();
			Location l = p.getLocation();

			if(SpawnManager.isArena(arenaName))
			{
				SpawnManager.addLocationToArena(arenaName, l);
				p.sendMessage(ChatColor.GREEN + "You added a spawn to " + arenaName + ".");
			}
			else
			{
				p.sendMessage(ChatColor.RED + "That arena could not be found.");
			}
			return true;
		}

		else if(args[0].equalsIgnoreCase("listspawns"))
		{
			String arenaName = args[1].toLowerCase();

			if(SpawnManager.isArena(arenaName))
			{
				p.sendMessage(ChatColor.GOLD + "Spawns for arena " + arenaName + ":");
				int index = 0;
				for(Location l : SpawnManager.listLocationsForArena(arenaName))
				{
					p.sendMessage(index + " - " + LocationUtil.locationToString(l));
					index++;
				}
			}
			else
			{
				p.sendMessage(ChatColor.RED + "That arena could not be found.");
			}
			return true;
		}

		else if(args[0].equalsIgnoreCase("removespawn") && args.length == 3)
		{
			String arenaName = args[1].toLowerCase();
			if(!SpawnManager.isArena(arenaName))
			{
				p.sendMessage(ChatColor.RED + "That arena could not be found.");
				return true;
			}
			try
			{
				int index = Integer.valueOf(args[2]);
				if(SpawnManager.listLocationsForArena(arenaName).size() < index)
				{
					p.sendMessage(ChatColor.RED + "Index of spawn could not be found.");
					return true;
				}
				else if(SpawnManager.listLocationsForArena(arenaName).size() == 1)
				{
					p.sendMessage(ChatColor.RED + "There has to be at least one location per arena.");
					return true;
				}
				else
				{
					Location toRemove = SpawnManager.listLocationsForArena(arenaName).get(index);
					SpawnManager.removeLocationFromArena(arenaName, toRemove);
					p.sendMessage(ChatColor.GREEN + "Arena removed.");
				}


			} catch (Exception ignored){
				p.sendMessage(ChatColor.RED + "Index of spawn could not be found.");
			}
			return true;
		}


		/**
		 * ARENAS ------------------------------------------------------------|
		 */


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
			String[] split = s.split("-");
			p.sendMessage(ChatColor.YELLOW + split[0] + ChatColor.GRAY + split[1]);
		}
	}
}
