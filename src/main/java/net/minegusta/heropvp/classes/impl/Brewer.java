package net.minegusta.heropvp.classes.impl;

import com.google.common.collect.Lists;
import net.minegusta.heropvp.classes.IHero;
import net.minegusta.heropvp.inventories.HeroInventory;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.List;

public class Brewer implements IHero {

	private static List<ItemStack> potions = Lists.newArrayList(
			new ItemStack(Material.SPLASH_POTION, 2)
			{
				{
					PotionMeta meta = (PotionMeta) getItemMeta();
					meta.setBasePotionData(new PotionData(PotionType.POISON, false, false));
					setItemMeta(meta);
				}
			},
			new ItemStack(Material.SPLASH_POTION, 1)
			{
				{
					PotionMeta meta = (PotionMeta) getItemMeta();
					meta.setBasePotionData(new PotionData(PotionType.INSTANT_DAMAGE, false, true));
					setItemMeta(meta);
				}
			},
			new ItemStack(Material.SPLASH_POTION, 2)
			{
				{
					PotionMeta meta = (PotionMeta) getItemMeta();
					meta.setBasePotionData(new PotionData(PotionType.INSTANT_DAMAGE, false, false));
					setItemMeta(meta);
				}
			},
			new ItemStack(Material.LINGERING_POTION, 1)
			{
				{
					PotionMeta meta = (PotionMeta) getItemMeta();
					meta.setBasePotionData(new PotionData(PotionType.SLOWNESS, false, false));
					setItemMeta(meta);
				}
			},
			new ItemStack(Material.SPLASH_POTION, 1)
			{
				{
					PotionMeta meta = (PotionMeta) getItemMeta();
					meta.setBasePotionData(new PotionData(PotionType.SPEED, true, false));
					setItemMeta(meta);
				}
			},
			new ItemStack(Material.POTION, 1)
			{
				{
					PotionMeta meta = (PotionMeta) getItemMeta();
					meta.setBasePotionData(new PotionData(PotionType.STRENGTH, false, false));
					setItemMeta(meta);
				}
			},
			new ItemStack(Material.POTION, 1)
			{
				{
					PotionMeta meta = (PotionMeta) getItemMeta();
					meta.setBasePotionData(new PotionData(PotionType.REGEN, false, true));
					setItemMeta(meta);
				}
			}
	);
	@Override
	public void doUltimate(Player player) {
		for(ItemStack i : potions)
		{
			if(player.getInventory().firstEmpty() != -1) player.getInventory().addItem(i);
		}
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
		return 10;
	}

	@Override
	public String getName() {
		return "Brewer";
	}

	private static String[] desc = new String[]{"Master of potions.", "Throw your potions on people", "to piss them off.", "Your potions are refilled", "when you use your ultimate."};

	@Override
	public String[] getDescription() {
		return desc;
	}

	@Override
	public String getTag() {
		return "[Brewer]";
	}

	private static HeroInventory inventory = new HeroInventory(){
		{
			//Helmet
			addItem(39, new ItemStack(Material.LEATHER_HELMET){
				{
					{
						LeatherArmorMeta meta = (LeatherArmorMeta) getItemMeta();
						meta.setColor(Color.PURPLE);
						meta.setDisplayName(ChatColor.WHITE + "Alchemist's Hood.");
						setItemMeta(meta);
					}
				}});
			//Chest
			addItem(38, new ItemStack(Material.LEATHER_CHESTPLATE){
				{
					{
						LeatherArmorMeta meta = (LeatherArmorMeta) getItemMeta();
						meta.setColor(Color.PURPLE);
						meta.setDisplayName(ChatColor.WHITE + "Alchemist's Robe.");
						setItemMeta(meta);
						addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
					}
				}});
			//Legs
			addItem(37, new ItemStack(Material.LEATHER_LEGGINGS){
				{
					{
						LeatherArmorMeta meta = (LeatherArmorMeta) getItemMeta();
						meta.setColor(Color.PURPLE);
						meta.setDisplayName(ChatColor.WHITE + "Alchemist's Robe.");
						setItemMeta(meta);
						addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
					}
				}});
			//Boots
			addItem(36, new ItemStack(Material.LEATHER_BOOTS){
				{
					{
						LeatherArmorMeta meta = (LeatherArmorMeta) getItemMeta();
						meta.setColor(Color.PURPLE);
						meta.setDisplayName(ChatColor.WHITE + "Alchemist's Boots.");
						setItemMeta(meta);
					}
				}});
			//hand1
			addItem(0, new ItemStack(Material.STONE_SWORD)
			{
				{
					addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
				}
			});
		}
	};

	@Override
	public void applyInventory(PlayerInventory inv) {
		inventory.fillInventory(inv);
		for(ItemStack i : potions)
		{
			inv.addItem(i);
		}
	}

	@Override
	public Material getMaterial() {
		return Material.SPLASH_POTION;
	}

	@Override
	public int getCost() {
		return 1200;
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
		return "You obtained new potions!";
	}
}
