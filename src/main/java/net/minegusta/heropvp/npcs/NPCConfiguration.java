package net.minegusta.heropvp.npcs;

import com.google.common.collect.Maps;
import net.minegusta.mglib.configs.ConfigurationModel;
import net.minegusta.mglib.utils.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.concurrent.ConcurrentMap;

public class NPCConfiguration extends ConfigurationModel {

	private static ConcurrentMap<String, NPCSpawn> npcs = Maps.newConcurrentMap();

	@Override
	public void onLoad(FileConfiguration fileConfiguration) {
		npcs.clear();

		if(!fileConfiguration.isSet("npc")) return;

		for(String s : fileConfiguration.getConfigurationSection("npc").getKeys(false))
		{
			try {
				String locString = fileConfiguration.getString("npc." + s + ".location");
				NPCType type = NPCType.valueOf(fileConfiguration.getString("npc." + s + ".type"));
				String name = fileConfiguration.getString("npc." + s + ".name").toLowerCase();
				float yaw = (float) fileConfiguration.getDouble("npc." + s + ".yaw", 30);
				float pitch = (float) fileConfiguration.getDouble("npc." + s + ".pitch", 30);
				Location location = LocationUtil.stringToLocation(locString);
				location.setYaw(yaw);
				location.setPitch(pitch);

				npcs.put(name, new NPCSpawn(name, location, type, pitch, yaw));


			} catch (Exception ignored)
			{
				Bukkit.getLogger().info("Error while loading an NPC spawn Location.");
			}
		}
	}

	public static void spawnNPCS()
	{
		for(String s : npcs.keySet())
		{
			NPCSpawn spawn = npcs.get(s);
			NPCManager.spawnNPC(spawn.getType(), spawn.getLocation());
		}
	}

	public static Collection<String> getNPCNames()
	{
		return npcs.keySet();
	}

	public static void removeNPC(String name)
	{
		if(npcs.containsKey(name.toLowerCase()))
		{
			npcs.remove(name);
		}
		resetNPCS();
	}

	public static void addNPC(String name, Location location, NPCType type)
	{
		npcs.put(name, new NPCSpawn(name, location, type, location.getPitch(), location.getYaw()));
		resetNPCS();
	}

	public static void resetNPCS()
	{
		//Despawn all entities in the worlds
		Bukkit.getWorlds().stream().forEach(world -> world.getLivingEntities().stream().filter(ent -> !(ent instanceof Player)).forEach(LivingEntity::remove));

		spawnNPCS();
	}

	@Override
	public void onSave(FileConfiguration fileConfiguration) {

		if(fileConfiguration.isSet("npc")) fileConfiguration.set("npc", null);

		for(String s : npcs.keySet())
		{
			fileConfiguration.set("npc." + s + ".location", LocationUtil.locationToString(npcs.get(s).getLocation()));
			fileConfiguration.set("npc." + s + ".name", npcs.get(s).getName());
			fileConfiguration.set("npc." + s + ".type", npcs.get(s).getType().name());
			fileConfiguration.set("npc." + s + ".yaw", npcs.get(s).getYaw());
			fileConfiguration.set("npc." + s + ".pitch", npcs.get(s).getPitch());
		}
	}
}
