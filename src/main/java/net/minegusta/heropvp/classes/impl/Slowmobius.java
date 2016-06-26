package net.minegusta.heropvp.classes.impl;

import net.minegusta.heropvp.classes.IHero;
import net.minegusta.heropvp.inventories.HeroInventory;
import net.minegusta.heropvp.main.Main;
import net.minegusta.mglib.particles.ParticleUtil;
import net.minegusta.mglib.utils.LocationUtil;
import net.minegusta.mglib.utils.MathUtil;
import net.minegusta.mglib.utils.PotionUtil;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.UUID;

public class Slowmobius implements IHero {
	@Override
	public void doUltimate(Player player) {

		final Location center = player.getLocation();
		final int radius = 12;
		final List<Location> circleSpots = LocationUtil.getPointsOnCircle(center, 15, radius);
		final UUID uuid = player.getUniqueId();


		for(int i = 0; i < ultimateDuration() * 4 * 20; i++)
		{
			//The effect task
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), ()->
			{
				circleSpots.stream().forEach(l ->
				{
					ParticleUtil.createNewMovingParticle(30, Effect.WITCH_MAGIC, l, 2.5, l.clone().add(0, 5, 0));
				});
			}, i * 4);

			//The slowing task
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), ()->
			{
				center.getWorld().getEntitiesByClasses(LivingEntity.class, Projectile.class).stream().filter(ent -> ent.getLocation().distance(center) < radius).forEach(ent ->
				{
					if(ent instanceof LivingEntity && !ent.getUniqueId().equals(uuid))
					{
						PotionUtil.updatePotion((LivingEntity) ent, PotionEffectType.SLOW, 2, 3);
					}
					else
					{
						ent.setVelocity(ent.getVelocity().multiply(0.5));
					}
				});
			}, i * 4);
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
		return 100;
	}

	@Override
	public int ultimateDuration() {
		return 12;
	}

	@Override
	public String getName() {
		return "Slowmobius";
	}

	private static String[] desc = new String[]{"A time controller.", "Can summon a slow-time circle", "when killing someone.", "Activate by crouching."};

	@Override
	public String[] getDescription() {
		return desc;
	}

	@Override
	public String getTag() {
		return "[Slowmobius]";
	}

	private static HeroInventory inventory = new HeroInventory(){
		{
			//Helmet
			addItem(39, new ItemStack(Material.STAINED_GLASS, 1, (short) 2));
			//Chest
			addItem(38, new ItemStack(Material.GOLD_CHESTPLATE));
			//Legs
			addItem(37, new ItemStack(Material.GOLD_LEGGINGS));
			//Boots
			addItem(36, new ItemStack(Material.GOLD_BOOTS));
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
		return Material.WATCH;
	}

	@Override
	public int getCost() {
		return 900;
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.DARK_PURPLE;
	}

	@Override
	public BarStyle getBarStyle() {
		return BarStyle.SEGMENTED_10;
	}

	@Override
	public BarColor getBarColor() {
		return BarColor.PURPLE;
	}

	@Override
	public String ultimateReadyMessage() {
		return ChatColor.DARK_PURPLE + "Ultimate Ready!" + ChatColor.YELLOW + "" + ChatColor.BOLD + " Crouch to activate!";
	}

	@Override
	public String ultimateBarMessage() {
		return "Time is slowed around you!";
	}
}
