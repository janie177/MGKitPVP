package net.minegusta.heropvp.npcs;

import org.bukkit.Location;

public class NPCSpawn {
	private Location location;
	private NPCType type;
	private String name;

	public NPCSpawn(String name, Location location, NPCType npcType)
	{
		this.name = name;
		this.location = location;
		this.type = npcType;
	}

	public Location getLocation() {
		return location;
	}

	public NPCType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

}
