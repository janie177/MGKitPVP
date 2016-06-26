package net.minegusta.heropvp.classes.impl;

import net.minegusta.heropvp.classes.IHero;
import net.minegusta.heropvp.inventories.HeroInventory;
import net.minegusta.mglib.utils.PotionUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;

public class Tank implements IHero {
	@Override
	public void doUltimate(Player player) {
		PotionUtil.updatePotion(player, PotionEffectType.SPEED, 0, ultimateDuration());
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
		PotionUtil.updatePotion(player, PotionEffectType.SLOW, 0, 5);
	}

	@Override
	public void onKill(Player player) {

	}

	@Override
	public void onSelect(Player player) {

	}

	@Override
	public int powerPerKill() {
		return 70;
	}

	@Override
	public int ultimateDuration() {
		return 12;
	}

	@Override
	public String getName() {
		return "Tank";
	}

	private static String[] desc = new String[]{"The ultimate defence.", "Has strong armour.", "Is also very slow.", "The ultimate makes", "you move faster."};

	@Override
	public String[] getDescription() {
		return desc;
	}

	@Override
	public String getTag() {
		return "[Tank]";
	}

	private static HeroInventory inventory = new HeroInventory(){
		{
			//Helmet
			addItem(39, new ItemStack(Material.IRON_HELMET));
			//Chest
			addItem(38, new ItemStack(Material.DIAMOND_CHESTPLATE){
				{
					addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 2);
				}
			});
			//Legs
			addItem(37, new ItemStack(Material.DIAMOND_LEGGINGS));
			//Boots
			addItem(36, new ItemStack(Material.IRON_BOOTS));
			//hand1
			addItem(0, new ItemStack(Material.IRON_SWORD));
			//shield
			addItem(1, new ItemStack(Material.SHIELD)
			{
				{
					setDurability((short) (8 * (Material.SHIELD.getMaxDurability() / 9)));
				}
			});
		}
	};

	@Override
	public void applyInventory(PlayerInventory inv) {
		inventory.fillInventory(inv);
	}

	@Override
	public Material getMaterial() {
		return Material.DIAMOND_CHESTPLATE;
	}

	@Override
	public int getCost() {
		return 2000;
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.BLUE;
	}

	@Override
	public BarStyle getBarStyle() {
		return BarStyle.SOLID;
	}

	@Override
	public BarColor getBarColor() {
		return BarColor.BLUE;
	}

	@Override
	public String ultimateReadyMessage() {
		return ChatColor.DARK_PURPLE + "Ultimate Ready!" + ChatColor.YELLOW + "" + ChatColor.BOLD + " Crouch to activate!";
	}

	@Override
	public String ultimateBarMessage() {
		return "You are faster now!";
	}
}
