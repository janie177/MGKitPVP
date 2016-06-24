package net.minegusta.heropvp.classes;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.PlayerInventory;

public interface IHero {

	void doUltimate(Player player);

	void doUltimate(Event event);

	void doPassive(Event event);

	void doPassive(Player player);

	void applyPermanentPassives(Player player);

	void onKill(Player player);

	void onSelect(Player player);

	int powerPerKill();

	int ultimateDuration();

	String getName();

	String[] getDescription();

	String getTag();

	void applyInventory(PlayerInventory inv);

	Material getMaterial();

	int getCost();

	ChatColor getColor();

	BarStyle getBarStyle();

	BarColor getBarColor();

	String ultimateReadyMessage();

	String ultimateBarMessage();

}
