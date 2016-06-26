package net.minegusta.heropvp.mapmanager;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minegusta.heropvp.main.Main;
import net.minegusta.heropvp.saving.MGPlayer;
import net.minegusta.heropvp.utils.DisplayMessageUtil;
import net.minegusta.mglib.configs.ConfigurationFileManager;
import net.minegusta.mglib.tasks.Task;
import net.minegusta.mglib.utils.Title;
import net.minegusta.mglib.utils.TitleUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

public class SpawnManager {
	private static Task mapChangeTask = new Task();
	private static ConfigurationFileManager<SpawnConfiguration> configurationFileManager;
	private static ConcurrentMap<String, Arena> locations = Maps.newConcurrentMap();
	private static ConcurrentMap<Integer, String> indexes = Maps.newConcurrentMap();
	private static int current = 0;
	private static Arena currentArena = new Arena("spawn", Lists.newArrayList(Bukkit.getWorlds().get(0).getSpawnLocation()));


	public static void init()
	{
		mapChangeTask.start(Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), ()-> {

			//Warn one minute ahead
			DisplayMessageUtil.announceMapChange();

			//Run the task a minute after.
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), ()->
			{
				if(current >= locations.size())
				{
					current = 0;
				}

				if(!locations.isEmpty())
				{
					String currentName = indexes.get(current);
					currentArena = locations.get(currentName);
					Main.getSaveManager().getAllPlayers().stream().forEach(MGPlayer::resetOnMapChange);

					Bukkit.broadcastMessage(ChatColor.GOLD +   "----------------------");
					Bukkit.broadcastMessage(ChatColor.YELLOW + " The map is changing!");
					Bukkit.broadcastMessage(ChatColor.YELLOW + " The map is changing!");
					Bukkit.broadcastMessage(ChatColor.YELLOW + " The map is changing!");
					Bukkit.broadcastMessage(ChatColor.GOLD +   "----------------------");
				}

				current++;
			}, 20 * 60);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), ()->
			{
				Title t = TitleUtil.createTitle(ChatColor.YELLOW + "The map is changing in 10 seconds.", ChatColor.GRAY + "You will be teleported to the spawn.", 10, 100, 0, true);
				Bukkit.getOnlinePlayers().stream().forEach(t::send);

				Bukkit.broadcastMessage(ChatColor.GOLD +   "------------------------------------");
				Bukkit.broadcastMessage(ChatColor.YELLOW + " The map is changing in 10 seconds!");
				Bukkit.broadcastMessage(ChatColor.YELLOW + " The map is changing in 10 seconds!");
				Bukkit.broadcastMessage(ChatColor.YELLOW + " The map is changing in 10 seconds!");
				Bukkit.broadcastMessage(ChatColor.GOLD +   "------------------------------------");

			}, 20 * 50);

		}, 20 * 300, 20 * 300));

		//Load the spawn configuration
		configurationFileManager = new ConfigurationFileManager<>(Main.getPlugin(), SpawnConfiguration.class, 300, "spawns");
		Optional<Arena> a = locations.values().stream().findAny();
		if(a.isPresent()) currentArena = a.get();
	}

	public static void addArena(String name, Location l)
	{
		Arena a = new Arena(name.toLowerCase(), Lists.newArrayList(l));
		locations.put(name.toLowerCase(), a);
		int index = 0;
		indexes.clear();
		for(String s : locations.keySet())
		{
			indexes.put(index, s);
			index++;
		}
	}

	public static void addArena(String name, List<Location> l)
	{
		Arena a = new Arena(name.toLowerCase(), l);
		locations.put(name.toLowerCase(), a);
		int index = 0;
		indexes.clear();
		for(String s : locations.keySet())
		{
			indexes.put(index, s);
			index++;
		}
	}

	public static boolean addLocationToArena(String arena, Location l)
	{
		if(locations.containsKey(arena.toLowerCase()))
		{
			Arena a = locations.get(arena.toLowerCase());
			a.addLocation(l);
			return true;
		}
		return false;
	}

	public static ConcurrentMap<String, Arena> getArenas()
	{
		return locations;
	}

	public static boolean removeLocationFromArena(String name, Location l)
	{
		if(locations.containsKey(name.toLowerCase()))
		{
			Arena a = locations.get(name.toLowerCase());
			return a.removeLocation(l);
		}
		return false;
	}

	public static List<Location> listLocationsForArena(String name)
	{
		if(locations.containsKey(name.toLowerCase()))
		{
			return locations.get(name.toLowerCase()).getSpawnLocations();
		}
		return Lists.newArrayList();
	}

	public static Arena getCurrentArena()
	{
		return currentArena;
	}

	public static boolean isArena(String s)
	{
		return locations.containsKey(s.toLowerCase());
	}

	public static void removeArena(String name)
	{
		if(locations.containsKey(name.toLowerCase()))
		{
			locations.remove(name.toLowerCase());
		}

		int index = 0;
		indexes.clear();
		for(String s : locations.keySet())
		{
			indexes.put(index, s);
			index++;
		}

	}

	public static ConfigurationFileManager getSpawnConfigManager()
	{
		return configurationFileManager;
	}




}
