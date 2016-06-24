package net.minegusta.mgkitpvp.classes.impl;

import net.minegusta.mgkitpvp.classes.IHero;
import net.minegusta.mgkitpvp.inventories.HeroInventory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Artemiz implements IHero {

	@Override
	public void doUltimate(Player player) {

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
		return 50;
	}

	@Override
	public int ultimateDuration() {
		return 18;
	}

	@Override
	public String getName() {
		return "Artemiz";
	}

	private static String[] desc = new String[]{"A skilled archer.", "Shoot poisonous arrows", "as ultimate ability."};

	@Override
	public String[] getDescription() {
		return desc;
	}

	@Override
	public String getTag() {
		return "[Artemiz]";
	}

	private static HeroInventory inventory = new HeroInventory(){
		{
			//Helmet
			addItem(39, new ItemStack(Material.LEATHER_HELMET));
			//Chest
			addItem(38, new ItemStack(Material.CHAINMAIL_CHESTPLATE));
			//Legs
			addItem(37, new ItemStack(Material.LEATHER_LEGGINGS));
			//Boots
			addItem(36, new ItemStack(Material.LEATHER_BOOTS));
			//hand1
			addItem(0, new ItemStack(Material.BOW){
				{
					addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 2);
					addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
					addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
				}
			});
			//hand2
			addItem(1, new ItemStack(Material.WOOD_SWORD){
				{
					addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
					addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
				}
			});
			addItem(35, new ItemStack(Material.ARROW, 64));
		}
	};

	@Override
	public void applyInventory(PlayerInventory inv) {
		inventory.fillInventory(inv);
	}

	@Override
	public Material getMaterial() {
		return Material.BOW;
	}

	@Override
	public int getCost() {
		return 250;
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.GREEN;
	}

	@Override
	public BarStyle getBarStyle() {
		return BarStyle.SEGMENTED_10;
	}

	@Override
	public BarColor getBarColor() {
		return BarColor.GREEN;
	}

	@Override
	public String ultimateReadyMessage() {
		return ChatColor.GREEN + "Ultimate Ready!" + ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + " Crouch to activate!";
	}

	@Override
	public String ultimateBarMessage() {
		return "Your arrows are now poisonous.";
	}
}
