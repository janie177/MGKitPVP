package net.minegusta.heropvp.classes.impl;

import net.minegusta.heropvp.classes.IHero;
import net.minegusta.heropvp.inventories.HeroInventory;
import net.minegusta.mglib.particles.ParticleUtil;
import net.minegusta.mglib.utils.PotionUtil;
import net.minegusta.mglib.utils.Title;
import net.minegusta.mglib.utils.TitleUtil;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffectType;

public class Assassin implements IHero {
	@Override
	public void doUltimate(Player player) {
		Title title = TitleUtil.createTitle(ChatColor.DARK_RED + "Marked for death!", ChatColor.ITALIC + "You better run...", 5, ultimateDuration() * 20, 0, true);
		title.send(player);
		PotionUtil.updatePotion(player, PotionEffectType.WEAKNESS, 0, ultimateDuration());
		player.setHealth(player.getHealth() / 3);

		ParticleUtil.createNewTargetingParticle(ultimateDuration(), Effect.WITCH_MAGIC, player.getLocation(), 12.0, player, false, false, Effect.MAGIC_CRIT);
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
		return 6;
	}

	@Override
	public String getName() {
		return "Assassin";
	}

	private static String[] desc = new String[]{"Sneaky backstabber.", "Does more damage when", "stabbing in the back.", "Ultimate allows you to mark", "someone for death.", "You take no fall damage either."};

	@Override
	public String[] getDescription() {
		return desc;
	}

	@Override
	public String getTag() {
		return "[Assassin]";
	}

	private static HeroInventory inventory = new HeroInventory(){
		{
			//Helmet
			addItem(39, new ItemStack(Material.LEATHER_HELMET){
			{
				{
					LeatherArmorMeta meta = (LeatherArmorMeta) getItemMeta();
					meta.setColor(Color.WHITE);
					meta.setDisplayName(ChatColor.WHITE + "Assassin's Hood.");
					setItemMeta(meta);
					addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
				}
			}});
			//Chest
			addItem(38, new ItemStack(Material.LEATHER_CHESTPLATE){
				{
					{
						LeatherArmorMeta meta = (LeatherArmorMeta) getItemMeta();
						meta.setColor(Color.WHITE);
						meta.setDisplayName(ChatColor.WHITE + "Assassin's Robe.");
						setItemMeta(meta);
						addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
					}
				}});
			//Legs
			addItem(37, new ItemStack(Material.LEATHER_LEGGINGS){
				{
					{
						LeatherArmorMeta meta = (LeatherArmorMeta) getItemMeta();
						meta.setColor(Color.WHITE);
						meta.setDisplayName(ChatColor.WHITE + "Assassin's Robe.");
						setItemMeta(meta);
						addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
					}
				}});
			//Boots
			addItem(36, new ItemStack(Material.LEATHER_BOOTS){
				{
					{
						LeatherArmorMeta meta = (LeatherArmorMeta) getItemMeta();
						meta.setColor(Color.WHITE);
						meta.setDisplayName(ChatColor.WHITE + "Assassin's Boots.");
						setItemMeta(meta);
						addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
					}
				}});
			//hand1
			addItem(0, new ItemStack(Material.IRON_SWORD){
				{
					addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2);
				}
			});
			//hand 2
			addItem(1, new ItemStack(Material.BOW){
				{
					addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
				}
			});
			addItem(2, new ItemStack(Material.ENDER_PEARL, 3));
			addItem(3, new ItemStack(Material.ARROW, 10));
		}
	};

	@Override
	public void applyInventory(PlayerInventory inv) {
		inventory.fillInventory(inv);
	}

	@Override
	public Material getMaterial() {
		return Material.EYE_OF_ENDER;
	}

	@Override
	public int getCost() {
		return 250;
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.BLACK;
	}

	@Override
	public BarStyle getBarStyle() {
		return BarStyle.SEGMENTED_20;
	}

	@Override
	public BarColor getBarColor() {
		return BarColor.PURPLE;
	}

	@Override
	public String ultimateReadyMessage() {
		return ChatColor.GREEN + "Ultimate Ready!" + ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + " Right click someone to mark them for death.";
	}

	@Override
	public String ultimateBarMessage() {
		return ChatColor.RED + "You have marked your target. Strike now!";
	}
}
