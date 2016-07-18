package net.minegusta.heropvp.npcs;

import org.bukkit.ChatColor;

public enum NPCType {
	SHOP(ChatColor.GOLD + "" + ChatColor.BOLD + "" + "Hero Shop"),
	SELECTOR(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "" + "Select Hero"),
	SPAWN(ChatColor.GREEN + "" + ChatColor.BOLD + "" + "Play Now!"),
	BOOST(ChatColor.AQUA + "" + ChatColor.BOLD + "" + "Boost!");

	private String displayName;

	NPCType(String displayName)
	{
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}
