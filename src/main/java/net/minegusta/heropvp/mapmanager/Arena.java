package net.minegusta.heropvp.mapmanager;

import com.google.common.collect.Lists;
import net.minegusta.mglib.utils.RandomUtil;
import org.bukkit.Location;

import java.util.List;

public class Arena {

	private List<Location> spawnLocations = Lists.newArrayList();
	private String name;

	public Arena(String name, List<Location> locations)
	{
		this.spawnLocations = locations;
		this.name = name;
	}

	public List<Location> getSpawnLocations() {
		return spawnLocations;
	}

	public String getName() {
		return name;
	}

	public void addLocation(Location l)
	{
		spawnLocations.add(l);
	}

	public boolean removeLocation(Location l)
	{
		if(spawnLocations.contains(l))
		{
			spawnLocations.remove(l);
			return true;
		}
		return false;
	}

	public Location getRandomSpawn()
	{
		return spawnLocations.get(RandomUtil.getZeroIncludedMaxExcluded(spawnLocations.size()));
	}
}
