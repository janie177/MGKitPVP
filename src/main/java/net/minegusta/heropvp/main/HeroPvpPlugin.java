package net.minegusta.heropvp.main;

import org.bukkit.entity.Player;

public class HeroPvpPlugin {

	public static String getTag(Player player)
	{
		return Main.getSaveManager().getMGPlayer(player).getActiveHero().getTag();
	}
}
