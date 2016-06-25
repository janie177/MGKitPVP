package net.minegusta.heropvp.listeners;

import com.google.common.collect.Lists;
import net.minegusta.heropvp.classes.Hero;
import net.minegusta.heropvp.main.Main;
import net.minegusta.heropvp.saving.MGPlayer;
import net.minegusta.mglib.utils.CooldownUtil;
import net.minegusta.mglib.utils.EffectUtil;
import net.minegusta.mglib.utils.Title;
import net.minegusta.mglib.utils.TitleUtil;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TippedArrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.List;

public class HeroListener implements Listener {

	/**
	 * Listen for ultimate and passive abilities
	 */

	private static List<Hero> activateOnCrouch = Lists.newArrayList(Hero.SCOUT, Hero.WITCHER, Hero.DEFAULT, Hero.ELVENLORD, Hero.BLOODMAGE, Hero.ICEMAGE, Hero.FIREMAGE);

	//Activate abilities using crouch.
	@EventHandler
	public void onCrouch(PlayerToggleSneakEvent e)
	{
		if(!e.isSneaking()) return;

		MGPlayer mgp = Main.getSaveManager().getMGPlayer(e.getPlayer());
		if(mgp.isUltimateReady())
		{
			if(activateOnCrouch.contains(mgp.getActiveHero()))
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
		//Activate assassin
		MGPlayer mgp = Main.getSaveManager().getMGPlayer(e.getPlayer());
		if(e.getRightClicked() instanceof Player)
		{
			Player target = (Player) e.getRightClicked();
			if(mgp.isUltimateReady() && mgp.getActiveHero() == Hero.ASSASSIN)
			{
				mgp.activateUltimate();
				mgp.onUltimate(target);
				Title t = TitleUtil.createTitle("", ChatColor.RED + "You marked " + target.getName() + " for death.", 5, 30, 5, true);
				t.send(e.getPlayer());
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
				if(Math.abs(attacker.getLocation().getYaw() - victim.getLocation().getYaw()) < 40)
				{
					TitleUtil.createTitle("", ChatColor.RED + "" + ChatColor.ITALIC + "Backstab! " + ChatColor.DARK_PURPLE + "1.5x damage dealt", 5, 15, 5, true).send(attacker);
					TitleUtil.createTitle("", ChatColor.RED + "" + ChatColor.ITALIC + "You got backstabbed!", 5, 10, 5, true).send(victim);
					e.setDamage(e.getDamage() * 1.5);
				}
			}
		}
	}

	//Jumping for the scout
	@EventHandler
	public void onDoubleJump(PlayerToggleFlightEvent e)
	{
		Player player = e.getPlayer();
		if (player.getGameMode() != GameMode.SURVIVAL) {
			return;
		}

		e.setCancelled(true);
		player.setAllowFlight(false);
		player.setFlying(false);

		MGPlayer mgp = Main.getSaveManager().getMGPlayer(player);
		String uuid = player.getUniqueId().toString();
		if(mgp.getActiveHero() == Hero.SCOUT)
		{
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), ()-> player.setAllowFlight(true), 30);
			if (CooldownUtil.isCooledDown("jumpscout", uuid))
			{
				player.setVelocity(player.getLocation().getDirection().multiply(0.5).setY(1));
				CooldownUtil.newCoolDown("jumpscout",  uuid, 1);
				EffectUtil.playSound(player.getLocation(), Sound.BLOCK_SLIME_FALL);
				EffectUtil.playParticle(player, Effect.TILE_DUST);
			}
		}
	}

	@EventHandler
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
			if(mages.contains(mgp.getActiveHero()))
			{
				mgp.onPassive(e.getPlayer());
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
}
