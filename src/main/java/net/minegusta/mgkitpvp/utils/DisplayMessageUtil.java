package net.minegusta.mgkitpvp.utils;

import net.minegusta.mgkitpvp.classes.Hero;
import net.minegusta.mglib.bossbars.BossBarUtil;
import net.minegusta.mglib.bossbars.PermanentBossBarHolder;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

public class DisplayMessageUtil {

	private static PermanentBossBarHolder announcementBar;

	public static void initBar()
	{
		announcementBar = BossBarUtil.createPermanentBar(ChatColor.LIGHT_PURPLE + "Welcome to " + ChatColor.RED + "HeroPvP", BarColor.WHITE, BarStyle.SOLID);
		announcementBar.setProgress(1);
	}

	public static void addPlayerToBar(Player player)
	{
		announcementBar.addPlayer(player);
	}

	public static void onKill(Player player, int streak, String killedName)
	{

	}

	public static void unlockHero(Player player, Hero hero)
	{

	}

	public static void onAssist(Player player, String assistName)
	{

	}

	public static void onDeath(Player player, int kills)
	{

	}

	public static void giveTickets(Player player , int tickets, int messageDelay)
	{

	}

	public static void takeTickets(Player player, int tickets, int messageDelay)
	{

	}

	public static void selectHero(Player player, Hero hero)
	{

	}

	public static void ultimateReady(Player player, Hero hero)
	{

	}

	public static void activateUltimate(Player player, Hero hero)
	{

	}

	public static void onSpawn(Player player, Hero hero)
	{

	}

	public static void announceMapChange()
	{

	}
}
