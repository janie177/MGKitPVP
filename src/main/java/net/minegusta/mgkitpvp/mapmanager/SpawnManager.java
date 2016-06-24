package net.minegusta.mgkitpvp.mapmanager;

import com.google.common.collect.Maps;
import net.minegusta.mgkitpvp.main.Main;
import net.minegusta.mglib.configs.ConfigurationFileManager;
import net.minegusta.mglib.tasks.Task;
import org.bukkit.Bukkit;
import org.bukkit.Location;

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

			if(current >= locations.size())
			{
				current = 0;
			}

			if(!locations.isEmpty())
			{
				String currentName = indexes.get(current);
				currentSpawn = locations.get(currentName);
			}


			current++;

		}, 60, 20 * 600));

		//Load the spawn configuration
		configurationFileManager = new ConfigurationFileManager<>(Main.getPlugin(), SpawnConfiguration.class, 300, "spawns");
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
