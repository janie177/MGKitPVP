package net.minegusta.heropvp.classes.impl;

import com.google.common.collect.Lists;
import net.minegusta.heropvp.classes.IHero;
import net.minegusta.heropvp.inventories.HeroInventory;
import net.minegusta.mglib.utils.RandomUtil;
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

import java.util.List;

public class Knight implements IHero {

	private static List<ItemStack> ultimateItems = Lists.newArrayList(
			new ItemStack(Material.SHIELD)
			{
				{
					setDurability((short) (Material.SHIELD.getMaxDurability() / 2));
				}
			},
			new ItemStack(Material.SHIELD)
			{
				{
					setDurability((short) (Material.SHIELD.getMaxDurability() / 2));
				}
			},
			new ItemStack(Material.SHIELD)
			{
				{
					setDurability((short) (Material.SHIELD.getMaxDurability() / 2));
				}
			},
			new ItemStack(Material.IRON_AXE)
			{
				{
					addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
					addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
					setDurability((short) (Material.IRON_AXE.getMaxDurability() / 20));
				}
			},
			new ItemStack(Material.IRON_AXE)
			{
				{
					addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
					addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
					setDurability((short) (Material.IRON_AXE.getMaxDurability() / 20));
				}
			},
			new ItemStack(Material.IRON_SWORD)
			{
				{
					addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 3);
					addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
					addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);
					setDurability((short) (Material.IRON_AXE.getMaxDurability() / 20));
				}
			},
			new ItemStack(Material.POTION)
			{
				{
					PotionMeta meta = (PotionMeta) getItemMeta();
					meta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL, false, true));
					setItemMeta(meta);
				}
			},
			new ItemStack(Material.POTION)
			{
				{
					PotionMeta meta = (PotionMeta) getItemMeta();
					meta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL, false, true));
					setItemMeta(meta);
				}
			}
	);

	@Override
	public void doUltimate(Player player) {
		player.getInventory().addItem(ultimateItems.get(RandomUtil.getZeroIncludedMaxExcluded(ultimateItems.size())));
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
		return 50;
	}

	@Override
	public int ultimateDuration() {
		return 5;
	}

	@Override
	public String getName() {
		return "Knight";
	}

	private static String[] desc = new String[]{"A royal knight.", "Has strong armor and weapons.", "Gains more equipment when", "the ultimate is used."};

	@Override
	public String[] getDescription() {
		return desc;
	}

	@Override
	public String getTag() {
		return "[Knight]";
	}

	private static HeroInventory inventory = new HeroInventory(){
		{
			//Helmet
			addItem(39, new ItemStack(Material.IRON_HELMET));
			//Chest
			addItem(38, new ItemStack(Material.IRON_CHESTPLATE));
			//Legs
			addItem(37, new ItemStack(Material.IRON_LEGGINGS));
			//Boots
			addItem(36, new ItemStack(Material.IRON_BOOTS));
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
		return Material.SHIELD;
	}

	@Override
	public int getCost() {
		return 1000;
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.WHITE;
	}

	@Override
	public BarStyle getBarStyle() {
		return BarStyle.SOLID;
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
		return "You obtained more equipment!";
	}
}
