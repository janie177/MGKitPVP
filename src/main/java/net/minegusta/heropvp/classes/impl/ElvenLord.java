package net.minegusta.heropvp.classes.impl;

import net.minegusta.heropvp.classes.IHero;
import net.minegusta.heropvp.inventories.HeroInventory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class ElvenLord implements IHero {

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
	public void onKill(Player player) {

	}

	@Override
	public void onSelect(Player player) {

	}

	@Override
	public int powerPerKill() {
		return 40;
	}

	@Override
	public int ultimateDuration() {
		return 8;
	}

	@Override
	public String getName() {
		return "ElfLord";
	}

	private static String[] desc = new String[]{"A master of the bow.", "Has a good bow.", "Spams arrows as ultimate."};

	@Override
	public String[] getDescription() {
		return desc;
	}

	@Override
	public String getTag() {
		return "[ElfLord]";
	}

	private static HeroInventory inventory = new HeroInventory(){
		{
			//Helmet
			addItem(39, new ItemStack(Material.LEATHER_HELMET));
			//Chest
			addItem(38, new ItemStack(Material.CHAINMAIL_CHESTPLATE));
			//Legs
			addItem(37, new ItemStack(Material.CHAINMAIL_LEGGINGS));
			//Boots
			addItem(36, new ItemStack(Material.CHAINMAIL_BOOTS));
			//hand1
			addItem(0, new ItemStack(Material.BOW){
				{
					addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 2);
					addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
					addUnsafeEnchantment(Enchantment.DURABILITY, 3);
				}
			});
			//hand2
			addItem(1, new ItemStack(Material.WOOD_SWORD){
				{
					addUnsafeEnchantment(Enchantment.KNOCKBACK, 3);
				}
			});
			addItem(2, new ItemStack(Material.SPLASH_POTION, 1)
			{
				{
					PotionMeta meta = (PotionMeta) getItemMeta();
					meta.setBasePotionData(new PotionData(PotionType.REGEN, false, false));
					setItemMeta(meta);
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
		return 400;
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.GREEN;
	}

	@Override
	public BarStyle getBarStyle() {
		return BarStyle.SOLID;
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
		return "Right click your bow to fire countless arrows!";
	}
}
