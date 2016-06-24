package net.minegusta.mgkitpvp.classes.impl;

import net.minegusta.mgkitpvp.classes.IHero;
import net.minegusta.mgkitpvp.inventories.HeroInventory;
import net.minegusta.mglib.utils.PotionUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;

public class Default implements IHero {

	@Override
	public void doUltimate(Player player) {
		PotionUtil.updatePotion(player, PotionEffectType.HEAL, 1, 6);
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

	}

	@Override
	public void onSelect(Player player) {

	}

	@Override
	public int powerPerKill() {
		return 25;
	}

	@Override
	public int ultimateDuration() {
		return 10;
	}

	@Override
	public String getName() {
		return "Noobington";
	}

	private static String[] desc = new String[]{"The default hero!", "Gain tickets to unlock more."};

	@Override
	public String[] getDescription() {
		return desc;
	}

	@Override
	public String getTag() {
		return "[Noobington]";
	}

	private static HeroInventory inventory = new HeroInventory();

	@Override
	public void applyInventory(PlayerInventory inv) {
		inventory.fillInventory(inv);
	}

	@Override
	public Material getMaterial() {
		return Material.WOOD_SWORD;
	}

	@Override
	public int getCost() {
		return 0;
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.GRAY;
	}

	@Override
	public BarStyle getBarStyle() {
		return BarStyle.SEGMENTED_10;
	}

	@Override
	public BarColor getBarColor() {
		return BarColor.WHITE;
	}

	@Override
	public String ultimateReadyMessage() {
		return ChatColor.DARK_PURPLE + "Ultimate Ready!" + ChatColor.YELLOW + "" + ChatColor.BOLD + " Crouch to activate!";
	}

	@Override
	public String ultimateBarMessage() {
		return "You are being healed!";
	}
}
