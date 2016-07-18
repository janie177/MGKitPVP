package net.minegusta.heropvp.boosts;

import com.google.common.collect.Maps;
import org.bukkit.Material;

import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

public enum Boost {

	TICKETS30(0, "+30% Tickets", "Get 30% extra tickets.", 15, 100, Material.PAPER, 0),
	POWER35(4, "Starting Power", "Start with 35 power.", 6, 200, Material.EXP_BOTTLE, 0),
	MARTYRDOME(6, "MartyrDome", "Drop TNT when you die.", 5, 180, Material.TNT, 0),
	SWORDMASTER(8, "Sword Master", "Gain a diamond sword for 5 minutes.", 5, 500, Material.DIAMOND_SWORD, 0),
	HEALTHPOT(2, "Healthy", "Get a health potion on spawn.", 12, 120, Material.INK_SACK, 1);

	private int slot;
	private String name;
	private String description;
	private byte data;
	private int duration;
	private int cost;
	private Material material;

	Boost(int slot, String name, String description, int duration, int cost, Material material, int data)
	{
		this.slot = slot;
		this.name = name;
		this.material = material;
		this.data = (byte) data;
		this.description = description;
		this.duration = duration;
		this.cost = cost;
	}

	private static ConcurrentMap<Integer, Boost> indexes = Maps.newConcurrentMap();

	static {
		for (Boost boost : Boost.values()) {
			indexes.put(boost.slot, boost);
		}
	}

	public static Optional<Boost> getForSlot(int slot)
	{
		if(indexes.containsKey(slot)) return Optional.of(indexes.get(slot));
		return Optional.empty();
	}

	public int getSlot() {
		return slot;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public int getDuration() {
		return duration;
	}

	public int getCost() {
		return cost;
	}

	public Material getMaterial() {
		return material;
	}

	public byte getData() {
		return data;
	}
}
