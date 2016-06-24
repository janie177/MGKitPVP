package net.minegusta.heropvp.npcs;

import org.bukkit.Location;

public class NPCSpawn {
	private Location location;
	private NPCType type;
	private String name;
	private float pitch;
	private float yaw;

	public NPCSpawn(String name, Location location, NPCType npcType, float pitch, float yaw)
	{
		this.name = name;
		this.location = location;
		this.type = npcType;
		this.pitch = pitch;
		this.yaw = yaw;
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

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}
}
