package net.minegusta.heropvp.mapmanager;

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

import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

public class SpawnManager {
	private static Task mapChangeTask = new Task();
	private static ConfigurationFileManager<SpawnConfiguration> configurationFileManager;
	private static ConcurrentMap<String, Location> locations = Maps.newConcurrentMap();
	private static ConcurrentMap<Integer, String> indexes = Maps.newConcurrentMap();
	private static int current = 0;
	private static Location currentSpawn = Bukkit.getWorlds().get(0).getSpawnLocation();


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
					currentSpawn = locations.get(currentName);
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

		}, 20 * 600, 20 * 600));

		//Load the spawn configuration
		configurationFileManager = new ConfigurationFileManager<>(Main.getPlugin(), SpawnConfiguration.class, 300, "spawns");
		Optional<Location> loc = locations.values().stream().findAny();
		if(loc.isPresent()) currentSpawn = loc.get();
	}

	public static void addSpawnLocation(String name, Location l)
	{
		locations.put(name.toLowerCase(), l);
		int index = 0;
		indexes.clear();
		for(String s : locations.keySet())
		{
			indexes.put(index, s);
			index++;
		}
	}

	public static ConcurrentMap<String, Location> getLocations()
	{
		return locations;
	}

	public static Location getCurrentSpawn()
	{
		return currentSpawn;
	}

	public static boolean isSpawnLocation(String s)
	{
		return locations.containsKey(s.toLowerCase());
	}

	public static void removeSpawnLocation(String name)
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
