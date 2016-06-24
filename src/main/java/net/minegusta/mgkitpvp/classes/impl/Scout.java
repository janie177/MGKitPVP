package net.minegusta.mgkitpvp.classes.impl;

import net.minegusta.mgkitpvp.classes.IHero;
import net.minegusta.mgkitpvp.inventories.HeroInventory;
import net.minegusta.mglib.utils.PotionUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;

public class Scout implements IHero {

	@Override
	public void doUltimate(Player player) {
		PotionUtil.updatePotion(player, PotionEffectType.SPEED, 1, 10);
	}

	@Override
	public void doUltimate(Event event) {

	}

	@Override
	public void doPassive(Event event) {

	}

	@Override
	public void doPassive(Player player) {

	}

	@Override
	public void applyPermanentPassives(Player player) {
		PotionUtil.updatePotion(player, PotionEffectType.SPEED, 0, 5);
	}

	@Override
	public void onSelect(Player player) {
		player.setAllowFlight(true);
	}

	@Override
	public int powerPerKill() {
		return 100;
	}

	@Override
	public int ultimateDuration() {
		return 10;
	}

	@Override
	public String getName() {
		return "Scout";
	}

	private static String[] desc = new String[]{"The scout is fast.", "They can double jump too!", "Ultimate gives extra speed."};

	@Override
	public String[] getDescription() {
		return desc;
	}

	@Override
	public String getTag() {
		return "[Scout]";
	}

	private static HeroInventory inventory = new HeroInventory();

	@Override
	public void applyInventory(PlayerInventory inv) {
		inventory.fillInventory(inv);
	}

	@Override
	public Material getMaterial() {
		return Material.FEATHER;
	}

	@Override
	public int getCost() {
		return 260;
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.YELLOW;
	}

	@Override
	public BarStyle getBarStyle() {
		return BarStyle.SEGMENTED_20;
	}

	@Override
	public BarColor getBarColor() {
		return BarColor.YELLOW;
	}

	@Override
	public String ultimateReadyMessage() {
		return ChatColor.DARK_PURPLE + "Ultimate Ready!" + ChatColor.YELLOW + "" + ChatColor.BOLD + " Crouch to activate!";
	}

	@Override
	public String ultimateBarMessage() {
		return "Gotta go fast(er)!";
	}
}
