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

public class Witcher implements IHero {
	@Override
	public void doUltimate(Player player) {
		PotionUtil.updatePotion(player, PotionEffectType.SPEED, 0, ultimateDuration());
		PotionUtil.updatePotion(player, PotionEffectType.INCREASE_DAMAGE, 0, ultimateDuration());
		PotionUtil.updatePotion(player, PotionEffectType.NIGHT_VISION, 0, ultimateDuration());
		player.getInventory().addItem(new ItemStack(Material.ARROW, 3));
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
		return 15;
	}

	@Override
	public String getName() {
		return "Witcher";
	}

	private static String[] desc = new String[]{"Monster slayer turned mercenary.", "Wields two swords.", "Ultimate gives multiple", "potion effects.", "Ultimate also awards 3 arrows.", "Has a bow with few arrows."};

	@Override
	public String[] getDescription() {
		return desc;
	}

	@Override
	public String getTag() {
		return "[Witcher]";
	}

	private static HeroInventory inventory = new HeroInventory(){
		{
			//Helmet
			addItem(39, new ItemStack(Material.CHAINMAIL_HELMET));
			//Chest
			addItem(38, new ItemStack(Material.CHAINMAIL_CHESTPLATE));
			//Legs
			addItem(37, new ItemStack(Material.CHAINMAIL_LEGGINGS));
			//Boots
			addItem(36, new ItemStack(Material.CHAINMAIL_BOOTS));
			//hand1
			addItem(0, new ItemStack(Material.IRON_SWORD){
				{
					addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2);
				}
			});
			//hand2
			addItem(1, new ItemStack(Material.GOLD_SWORD){
				{
					addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
				}
			});
			addItem(2, new ItemStack(Material.BOW){
				{
					addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 4);
				}
			});
			addItem(3, new ItemStack(Material.ARROW, 3));
		}
	};

	@Override
	public void applyInventory(PlayerInventory inv) {
		inventory.fillInventory(inv);
	}

	@Override
	public Material getMaterial() {
		return Material.GOLD_SWORD;
	}

	@Override
	public int getCost() {
		return 250;
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.RED;
	}

	@Override
	public BarStyle getBarStyle() {
		return BarStyle.SEGMENTED_20;
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
		return "Your Witcher senses are active!";
	}
}
