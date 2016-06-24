package net.minegusta.mgkitpvp.mapmanager;

import net.minegusta.mglib.configs.ConfigurationModel;
import net.minegusta.mglib.utils.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class SpawnConfiguration extends ConfigurationModel {

	@Override
	public void onLoad(FileConfiguration fileConfiguration) {
		SpawnManager.getLocations().clear();

		if(!fileConfiguration.isSet("spawn")) return;

		for(String s : fileConfiguration.getConfigurationSection("spawn").getKeys(false))
		{
			try {
				String locString = fileConfiguration.getString("spawn." + s);
				Location location = LocationUtil.stringToLocation(locString);
				SpawnManager.addSpawnLocation(s, location);

			} catch (Exception ignored)
			{
				Bukkit.getLogger().info("Error while loading a spawn location.");
			}


		}
	}

	@Override
	public void onSave(FileConfiguration fileConfiguration) {

		if(fileConfiguration.isSet("spawn")) fileConfiguration.set("spawn", null);

		for(String s : SpawnManager.getLocations().keySet())
		{
			fileConfiguration.set("spawn." + s, LocationUtil.locationToString(SpawnManager.getLocations().get(s)));
		}
	}
}
