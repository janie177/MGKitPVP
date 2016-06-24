package net.minegusta.mgkitpvp.listeners;

import com.google.common.collect.Lists;
import net.minegusta.mgkitpvp.classes.Hero;
import net.minegusta.mgkitpvp.main.Main;
import net.minegusta.mgkitpvp.saving.MGPlayer;
import net.minegusta.mglib.utils.CooldownUtil;
import net.minegusta.mglib.utils.EffectUtil;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.TippedArrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class HeroListener implements Listener {

	/**
	 * Listen for ultimate and passive abilities
	 */

	//Activate abilities using crouch.
	@EventHandler
	public void onCrouch(PlayerToggleSneakEvent e)
	{
		if(!e.isSneaking()) return;

		MGPlayer mgp = Main.getSaveManager().getMGPlayer(e.getPlayer());
		if(mgp.isUltimateReady())
		{
			mgp.activateUltimate();
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
				player.setVelocity(player.getLocation().getDirection().multiply(0.8).setY(1));
				Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), ()-> player.setFallDistance(0), 15);
				CooldownUtil.newCoolDown("jumpscout",  uuid, 1);
				EffectUtil.playSound(player.getLocation(), Sound.BLOCK_SLIME_FALL);
				EffectUtil.playParticle(player, Effect.TILE_DUST);
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


}
