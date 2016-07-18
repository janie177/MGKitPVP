package net.minegusta.heropvp.classes.impl;

import net.minegusta.heropvp.classes.IHero;
import net.minegusta.heropvp.inventories.HeroInventory;
import net.minegusta.mglib.utils.PotionUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;

public class Default implements IHero {

	@Override
	public void doUltimate(Player player) {
		PotionUtil.updatePotion(player, PotionEffectType.REGENERATION, 1, ultimateDuration());
		PotionUtil.updatePotion(player, PotionEffectType.DAMAGE_RESISTANCE, 0, ultimateDuration());
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
	public void onKill(Player player) {

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
		return "Noobly";
	}

	private static String[] desc = new String[]{"The default hero!", "Gain tickets to unlock more.", "Ultimate heals you and", "reduces damage taken."};

	@Override
	public String[] getDescription() {
		return desc;
	}

	@Override
	public String getTag() {
		return "[Noobly]";
	}

	private static HeroInventory inventory = new HeroInventory(){
		{
			//Helmet
			addItem(39, new ItemStack(Material.CHAINMAIL_HELMET));
			//Chest
			addItem(38, new ItemStack(Material.CHAINMAIL_CHESTPLATE));
			//Legs
			addItem(37, new ItemStack(Material.IRON_LEGGINGS));
			//Boots
			addItem(36, new ItemStack(Material.LEATHER_BOOTS));
			//hand1
			addItem(0, new ItemStack(Material.IRON_SWORD));
		}
	};

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
