package net.minegusta.heropvp.classes.impl;

import net.minegusta.heropvp.classes.IHero;
import net.minegusta.heropvp.inventories.HeroInventory;
import net.minegusta.heropvp.main.Main;
import net.minegusta.heropvp.saving.MGPlayer;
import net.minegusta.heropvp.utils.DisplayMessageUtil;
import net.minegusta.mglib.utils.EffectUtil;
import net.minegusta.mglib.utils.PotionUtil;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;

import java.util.Optional;

public class Mimic implements IHero {
	@Override
	public void doUltimate(Player player) {
		MGPlayer mgp = Main.getSaveManager().getMGPlayer(player);
		Optional<Player> killer = mgp.getMostDamage();
		EffectUtil.playParticle(player.getLocation(), Effect.CLOUD, 1, 1, 1, 0.1F, 40, 40);
		if(killer.isPresent()) DisplayMessageUtil.setDeathMessageAnnounced(killer.get(), Main.getSaveManager().getMGPlayer(killer.get()).getActiveHero(), player.getName());
		EffectUtil.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
		PotionUtil.updatePotion(player, PotionEffectType.INVISIBILITY, 0, ultimateDuration());
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
		return 100;
	}

	@Override
	public int ultimateDuration() {
		return 6;
	}

	@Override
	public String getName() {
		return "Mimic";
	}

	private static String[] desc = new String[]{"The copycat hero.", "Right click someone to", "copy their armour.", "The ultimate ability", "lets you fake your death."};

	@Override
	public String[] getDescription() {
		return desc;
	}

	@Override
	public String getTag() {
		return "[Mimic]";
	}

	private static HeroInventory inventory = new HeroInventory(){
		{
			//hand1
			addItem(0, new ItemStack(Material.IRON_SWORD)
			{
				{
					addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2);
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
		return Material.SLIME_BLOCK;
	}

	@Override
	public int getCost() {
		return 1300;
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.YELLOW;
	}

	@Override
	public BarStyle getBarStyle() {
		return BarStyle.SEGMENTED_6;
	}

	@Override
	public BarColor getBarColor() {
		return BarColor.RED;
	}

	@Override
	public String ultimateReadyMessage() {
		return ChatColor.DARK_PURPLE + "Ultimate Ready!" + ChatColor.YELLOW + "" + ChatColor.BOLD + " Crouch to fake death!";
	}

	@Override
	public String ultimateBarMessage() {
		return "You faked your death! Reappearing in...";
	}
}
