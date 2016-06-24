package net.minegusta.mgkitpvp.listeners;

import com.google.common.collect.Lists;
import net.minegusta.mgkitpvp.classes.Hero;
import net.minegusta.mgkitpvp.main.Main;
import net.minegusta.mgkitpvp.saving.MGPlayer;
import net.minegusta.mglib.utils.CooldownUtil;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.List;

public class HeroListener implements Listener {

	/**
	 * Listen for ultimate and passive abilities
	 */

	//Activate abilities using crouch.

	private static List<Hero> herosOnCrouch = Lists.newArrayList(Hero.SCOUT, Hero.DEFAULT);

	@EventHandler
	public void onCrouch(PlayerToggleSneakEvent e)
	{
		if(!e.isSneaking()) return;

		MGPlayer mgp = Main.getSaveManager().getMGPlayer(e.getPlayer());
		if(mgp.isUltimateReady() && herosOnCrouch.contains(mgp.getActiveHero()))
		{
			mgp.onUltimate(e.getPlayer());
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
		MGPlayer mgp = Main.getSaveManager().getMGPlayer(player);
		String uuid = player.getUniqueId().toString();
		if (mgp.getActiveHero() == Hero.SCOUT && CooldownUtil.isCooledDown("jumpscout", uuid))
		{
			e.setCancelled(true);
			player.setAllowFlight(false);
			player.setFlying(false);
			player.setVelocity(player.getLocation().getDirection().multiply(2.0).setY(1));
			CooldownUtil.newCoolDown("jumpscout",  uuid, 2);

		}
	}

}
