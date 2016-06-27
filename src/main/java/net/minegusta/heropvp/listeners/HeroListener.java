package net.minegusta.heropvp.listeners;

import com.google.common.collect.Lists;
import net.minegusta.heropvp.classes.Hero;
import net.minegusta.heropvp.main.Main;
import net.minegusta.heropvp.saving.MGPlayer;
import net.minegusta.mglib.permissionsex.PEXUtil;
import net.minegusta.mglib.utils.CooldownUtil;
import net.minegusta.mglib.utils.EffectUtil;
import net.minegusta.mglib.utils.Title;
import net.minegusta.mglib.utils.TitleUtil;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.TippedArrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.List;
import java.util.UUID;

public class HeroListener implements Listener {

	/**
	 * Listen for ultimate and passive abilities
	 */

	private static List<Hero> activateOnCrouch = Lists.newArrayList(Hero.BREWER, Hero.MIMIC, Hero.DWARF, Hero.TANK, Hero.KNIGHT, Hero.SLOWMOBIUS, Hero.SCOUT, Hero.WITCHER, Hero.DEFAULT, Hero.ELVENLORD, Hero.BLOODMAGE, Hero.ICEMAGE, Hero.FIREMAGE);

	//Activate abilities using crouch.
	@EventHandler
	public void onCrouch(PlayerToggleSneakEvent e)
	{
		if(!e.isSneaking()) return;

		MGPlayer mgp = Main.getSaveManager().getMGPlayer(e.getPlayer());
		if(mgp.isUltimateReady())
		{
			if(activateOnCrouch.contains(mgp.getActiveHero()) && mgp.isPlaying())
			{
				mgp.activateUltimate();
				mgp.onUltimate(e.getPlayer());
			}
		}
	}

	//On interact
	@EventHandler
	public void onInteract(PlayerInteractEntityEvent e)
	{
		if(e.getHand() != EquipmentSlot.HAND) return;

		MGPlayer mgp = Main.getSaveManager().getMGPlayer(e.getPlayer());
		if(e.getRightClicked() instanceof Player) {
			Player target = (Player) e.getRightClicked();

			//Assassin
			if (mgp.isUltimateReady() && mgp.getActiveHero() == Hero.ASSASSIN) {
				mgp.activateUltimate();
				mgp.onUltimate(target);
				Title t = TitleUtil.createTitle("", ChatColor.RED + "You marked " + target.getName() + " for death.", 5, 30, 5, true);
				t.send(e.getPlayer());
				return;
			}

			if (mgp.getActiveHero() == Hero.MIMIC)
			{
				if(CooldownUtil.isCooledDown("mimic", e.getPlayer().getUniqueId().toString()))
				{
					CooldownUtil.newCoolDown("mimic", e.getPlayer().getUniqueId().toString(), 5);
					TitleUtil.createTitle("", ChatColor.YELLOW + "" + ChatColor.ITALIC + "Armour copied!", 5, 20, 5, true).send(e.getPlayer());
					TitleUtil.createTitle("", ChatColor.RED + "" + ChatColor.ITALIC + "A mimic copied you!", 5, 20, 5, true).send(target);
					e.getPlayer().getInventory().setArmorContents(target.getInventory().getArmorContents());
				}
				else
				{
					e.getPlayer().sendMessage(ChatColor.RED + "You have to wait " + CooldownUtil.getRemainingSeconds("mimic", e.getPlayer().getUniqueId().toString()) + " seconds before stealing armour.");
				}
			}
		}

	}

	//On data
	@EventHandler(priority = EventPriority.HIGH)
	public void onPvpDamage(EntityDamageByEntityEvent e)
	{
		if(e.getEntity() instanceof  Player && e.getDamager() instanceof Player && !e.isCancelled())
		{
			Player attacker = (Player) e.getDamager();
			Player victim = (Player) e.getEntity();

			MGPlayer mgp = Main.getSaveManager().getMGPlayer(attacker);
			//Assassin extra damage on backstab
			if(mgp.getActiveHero() == Hero.ASSASSIN)
			{
				if(Math.abs(attacker.getLocation().getYaw() - victim.getLocation().getYaw()) < 50)
				{
					TitleUtil.createTitle("", ChatColor.RED + "" + ChatColor.ITALIC + "Backstab! " + ChatColor.DARK_PURPLE + "1.5x damage dealt", 5, 15, 5, true).send(attacker);
					TitleUtil.createTitle("", ChatColor.RED + "" + ChatColor.ITALIC + "You got backstabbed!", 5, 10, 5, true).send(victim);
					e.setDamage(e.getDamage() * 1.5);
				}
			}
		}
	}

	//Jumping for the scout
	@EventHandler(priority = EventPriority.LOWEST)
	public void onDoubleJump(PlayerToggleFlightEvent e)
	{
		Player player = e.getPlayer();
		if (player.getGameMode() != GameMode.SURVIVAL && player.getGameMode() != GameMode.ADVENTURE) {
			return;
		}

		e.setCancelled(true);
		player.setAllowFlight(false);
		player.setFlying(false);

		MGPlayer mgp = Main.getSaveManager().getMGPlayer(player);
		String uuid = player.getUniqueId().toString();
		if(mgp.getActiveHero() == Hero.SCOUT)
		{
			PEXUtil.addPermission(e.getPlayer(),"nocheatplus.checks.moving.survivalfly");
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), ()-> {
				player.setAllowFlight(true);
				PEXUtil.removePermission(e.getPlayer(),"nocheatplus.checks.moving.survivalfly");
			}, 30);
			if (CooldownUtil.isCooledDown("jumpscout", uuid))
			{
				player.setVelocity(player.getLocation().getDirection().multiply(0.5).setY(1));
				CooldownUtil.newCoolDown("jumpscout",  uuid, 1);
				EffectUtil.playSound(player.getLocation(), Sound.BLOCK_SLIME_FALL);
				EffectUtil.playParticle(player, Effect.TILE_DUST);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onFallDamage(EntityDamageEvent e)
	{
		if(e.getEntity() instanceof Player && e.getCause() == EntityDamageEvent.DamageCause.FALL)
		{
			MGPlayer mgp = Main.getSaveManager().getMGPlayer((Player) e.getEntity());
			if(mgp.getActiveHero() == Hero.SCOUT || mgp.getActiveHero() == Hero.ASSASSIN)
			{
				e.setCancelled(true);
			}

		}
	}

	//Bow shoot event
	@EventHandler
	public void onShootBow(EntityShootBowEvent e)
	{
		if(e.getEntity() instanceof Player)
		{
			MGPlayer mgp = Main.getSaveManager().getMGPlayer((Player) e.getEntity());
			if(!mgp.isPlaying())
			{
				e.setCancelled(true);
				return;
			}

			//Artemiz ultimate ability to shoot poison arrows.
			if(mgp.getActiveHero() == Hero.ARTEMIZ && mgp.isUltimateActive())
			{
				TippedArrow arrow = e.getEntity().getWorld().spawnArrow(e.getEntity().getLocation(), e.getProjectile().getVelocity(), 0, 0, TippedArrow.class);
				arrow.setBasePotionData(new PotionData(PotionType.POISON, false, false));
				e.setProjectile(arrow);
			}
		}
	}

	private static List<Hero> mages = Lists.newArrayList(Hero.BLOODMAGE, Hero.ICEMAGE, Hero.FIREMAGE);

	//Elven lord arrow spam
	@EventHandler
	public void interact(PlayerInteractEvent e)
	{
		if(e.getHand() == EquipmentSlot.HAND && e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			MGPlayer mgp = Main.getSaveManager().getMGPlayer(e.getPlayer());

			//Mage abilities
			if(mages.contains(mgp.getActiveHero()) && mgp.isPlaying())
			{
				mgp.onPassive(e.getPlayer());
				return;
			}

			//Explodo BOOM
			if(mgp.getActiveHero() == Hero.EXPLODO && mgp.isPlaying())
			{
				mgp.onPassive(e.getPlayer());
				return;
			}


			//Elven lord ultimate ability
			if(mgp.isUltimateActive() && mgp.getActiveHero() == Hero.ELVENLORD && e.getPlayer().getInventory().getItemInMainHand().getType() == Material.BOW)
			{
				final Player player = e.getPlayer();
				e.setCancelled(true);
				for(int i = 1; i <= 3; i+=2)
				{
					Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), ()->
					{
						if(!player.isOnline()) return;
						player.getWorld().spawnArrow(player.getLocation().add(player.getLocation().getDirection().normalize()).add(0, 1.4F, 0), player.getLocation().getDirection(), 2.2F, 0.1F);

					}, i);
				}
			}
		}
	}

	//On explodo tnt explode
	@EventHandler
	public void onTntExplode(ExplosionPrimeEvent e)
	{
		if(e.getEntity() instanceof TNTPrimed)
		{
			TNTPrimed tnt = (TNTPrimed) e.getEntity();
			if(tnt.hasMetadata("nuke"))
			{
				e.setCancelled(true);
				try
				{
					String uuid = tnt.getMetadata("mgplayer").get(0).asString();
					Player player = Bukkit.getPlayer(UUID.fromString(uuid));
					if(player.isOnline()) {
						final Location target = tnt.getLocation();
						for (int i = 0; i < 60; i++) {
							final int k = i;
							Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
								@Override
								public void run() {
									nukeEffect(target, 110 + k, 30 * k, k / 4);
								}
							}, i);
						}

						e.getEntity().getWorld().getLivingEntities().stream().filter(ent -> ent.getLocation().distance(e.getEntity().getLocation()) < 8).forEach(ent ->
						{
							EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(player, ent, EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, 8 + (15 - ent.getLocation().distance(tnt.getLocation())));
							Bukkit.getPluginManager().callEvent(event);
							if (!event.isCancelled()) {
								ent.damage(event.getFinalDamage());
							}
						});
					}
				} catch (Exception ignored){
				}
			}
			else if(tnt.hasMetadata("explodo"))
			{
				e.setCancelled(true);
				try
				{
					String uuid = tnt.getMetadata("mgplayer").get(0).asString();
					Player player = Bukkit.getPlayer(UUID.fromString(uuid));
					if(player.isOnline()) {
						e.getEntity().getWorld().playEffect(e.getEntity().getLocation(), Effect.EXPLOSION_HUGE, 0, 60);
						EffectUtil.playSound(e.getEntity().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 4, 1);
						e.getEntity().getWorld().getLivingEntities().stream().filter(ent -> ent.getLocation().distance(e.getEntity().getLocation()) < 4).forEach(ent ->
						{
							EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(player, ent, EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, 6 + (7 - ent.getLocation().distance(tnt.getLocation())));
							Bukkit.getPluginManager().callEvent(event);
							if (!event.isCancelled()) {
								ent.damage(event.getFinalDamage());
							}
						});
					}
				} catch (Exception ignored){
				}

			}
		}
	}

	private static void nukeEffect(Location target, int range, int particles, int offSetY) {
		target.getWorld().playEffect(target.clone().add(0, offSetY, 0), Effect.EXPLOSION_HUGE, 1, 60);
		EffectUtil.playSound(target, Sound.ENTITY_GENERIC_EXPLODE, 4, 1);
		target.getWorld().playSound(target, Sound.AMBIENT_CAVE, 1F, 1F);
		target.getWorld().spigot().playEffect(target, Effect.CLOUD, 1, 1, 3F, 0F, 3F, 1F, particles, range);
		target.getWorld().spigot().playEffect(target, Effect.LAVA_POP, 1, 1, 0.4F, 10F, 0.4F, 1F, particles, range);
		target.getWorld().spigot().playEffect(target, Effect.SMOKE, 1, 1, 0.4F, 10F, 0.4F, 1F, particles, range);
		target.getWorld().spigot().playEffect(target, Effect.FLAME, 1, 1, 0.4F, 10F, 0.4F, 1F, particles, range);
	}
}
