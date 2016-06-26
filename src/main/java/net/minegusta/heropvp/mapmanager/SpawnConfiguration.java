package net.minegusta.heropvp.mapmanager;

import com.google.common.collect.Lists;
import net.minegusta.mglib.configs.ConfigurationModel;
import net.minegusta.mglib.utils.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class SpawnConfiguration extends ConfigurationModel {

	@Override
	public void onLoad(FileConfiguration fileConfiguration) {
		SpawnManager.getArenas().clear();

		if(!fileConfiguration.isSet("arenas")) return;

		for(String s : fileConfiguration.getConfigurationSection("arenas").getKeys(false))
		{
			try {
				List<Location> locations = Lists.newArrayList();

				for(String l : fileConfiguration.getStringList("arenas." + s + ".spawns"))
				{
					Location location = LocationUtil.stringToLocation(l);
					location.add(location);
				}
				SpawnManager.addArena(s.toLowerCase(), locations);


			} catch (Exception ignored)
			{
				Bukkit.getLogger().info("Error while loading an arena.");
			}

		}
	}

	@Override
	public void onSave(FileConfiguration fileConfiguration) {

		if(fileConfiguration.isSet("arenas")) fileConfiguration.set("arenas", null);

		for(String s : SpawnManager.getArenas().keySet())
		{
			List<Location> locations = SpawnManager.listLocationsForArena(s);
			List<String> llist = Lists.newArrayList();
			for(Location l : locations)
			{
				llist.add(LocationUtil.locationToString(l));
			}
			fileConfiguration.set("arenas." + s + ".spawns", llist);
		}
	}
}
