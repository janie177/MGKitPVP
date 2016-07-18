package net.minegusta.heropvp.boosts;

import com.google.common.collect.Maps;
import org.bukkit.Material;

import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

public enum Boost {
	TICKETS30(0, "+30% Tickets", "Get 30% extra tickets.", 15, 250, Material.PAPER, 1),
	HEALTHPOT(6, "Healthy", "Get a health splash potion on spawn.", 15, 100, Material.INK_SACK, 1);

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
