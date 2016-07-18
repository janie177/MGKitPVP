package net.minegusta.heropvp.boosts;

public enum Boost {
	TICKETS30(0, "+30% Tickets", "Get 30% extra tickets.", 15, 250),
	HEALTHPOT(6, "Healthy", "Get a health splash potion on spawn.", 15, 100);

	private int slot;
	private String name;
	private String description;
	private int duration;
	private int cost;

	Boost(int slot, String name, String description, int duration, int cost)
	{
		this.slot = slot;
		this.name = name;
		this.description = description;
		this.duration = duration;
		this.cost = cost;
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
}
