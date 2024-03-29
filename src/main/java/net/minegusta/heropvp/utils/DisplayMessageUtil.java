package net.minegusta.heropvp.utils;

import com.google.common.collect.Lists;
import net.minegusta.heropvp.boosts.Boost;
import net.minegusta.heropvp.classes.Hero;
import net.minegusta.heropvp.main.Main;
import net.minegusta.heropvp.saving.MGPlayer;
import net.minegusta.mglib.bossbars.BossBarUtil;
import net.minegusta.mglib.bossbars.PermanentBossBarHolder;
import net.minegusta.mglib.bossbars.TimedBossBarHolder;
import net.minegusta.mglib.utils.EffectUtil;
import net.minegusta.mglib.utils.RandomUtil;
import net.minegusta.mglib.utils.Title;
import net.minegusta.mglib.utils.TitleUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

import java.util.List;

public class DisplayMessageUtil {

	private static PermanentBossBarHolder announcementBar;
	private static List<String> deathMessages = Lists.newArrayList("%victim% was killed by %killer%", "%killer% dominated %victim%", "%victim% learned not to mess with %killer%", "%killer% destroyed %victim% with brute force");

	public static void initBar()
	{
		announcementBar = BossBarUtil.createPermanentBar(ChatColor.LIGHT_PURPLE + "Welcome to " + ChatColor.RED + "HeroPvP", BarColor.WHITE, BarStyle.SOLID);
		announcementBar.setProgress(1);
	}

	public static void addPlayerToBar(Player player)
	{
		announcementBar.addPlayer(player);
	}

	public static void setDeathMessageAnnounced(Player player, Hero hero, String killedName)
	{
		String message = deathMessages.get(RandomUtil.getZeroIncludedMaxExcluded(deathMessages.size())).replace("%killer%",hero.getTag() + ChatColor.YELLOW + " " + player.getName() + ChatColor.DARK_GRAY).replace("%victim%", ChatColor.DARK_RED + killedName + ChatColor.DARK_GRAY);
		announcementBar.setTitle(message);
	}

	public static void onKill(Hero hero, Player player, int streak, String killedName)
	{
		String message = deathMessages.get(RandomUtil.getZeroIncludedMaxExcluded(deathMessages.size())).replace("%killer%",hero.getTag() + ChatColor.YELLOW + " " + player.getName() + ChatColor.DARK_GRAY).replace("%victim%", ChatColor.DARK_RED + killedName + ChatColor.DARK_GRAY);
		announcementBar.setTitle(message);
		EffectUtil.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);

		String title = "";
		String subtitle = ChatColor.RED + Integer.toString(streak) + " Kills!";
		int staytime = 20;
		boolean announce = false;

		switch (streak)
		{
			case 3: title = ChatColor.DARK_RED + "" + ChatColor.BOLD + "TRIPLE KILL!";
				announce = true;
				break;
			case 5: title = ChatColor.DARK_RED + "" + ChatColor.BOLD + "PENTA KILL!";
				announce = true;
				break;
			case 10: title = ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "ULTRA KILL!";
				announce = true;
				break;
			case 20: title = ChatColor.GOLD+ "" + ChatColor.BOLD + "MEGA KILL!";
				announce = true;
				break;
			case 25: title = ChatColor.YELLOW + "" + ChatColor.BOLD + "TOTAL ANNIHILATION!";
				announce = true;
				break;
		}

		if(announce)
		{
			staytime = 40;
			EffectUtil.playSound(player, Sound.ENTITY_ENDERDRAGON_GROWL);
			announcementBar.setTitle(ChatColor.YELLOW + player.getName() + " " + ChatColor.LIGHT_PURPLE + "just reached " + title + ChatColor.LIGHT_PURPLE + " by killing " + ChatColor.RED + killedName);
		}

		Title t = TitleUtil.createTitle(title, subtitle, 6, staytime, 6,true);
		TitleUtil.sendTitle(t, player);
	}

	public static void unlockHero(Player player, Hero hero)
	{
		Title title = TitleUtil.createTitle(hero.getColor() + "" + ChatColor.BOLD + hero.getName() + ChatColor.YELLOW + " " + ChatColor.BOLD + "Unlocked!", "", 6, 20, 6,true);
		TitleUtil.sendTitle(title, player);
		EffectUtil.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_TOUCH);
	}

	public static void unlockBoost(Player player, Boost boost)
	{
		Title title = TitleUtil.createTitle(ChatColor.RED + "" + ChatColor.BOLD + boost.getName() + ChatColor.YELLOW + " " + ChatColor.BOLD + "Unlocked!", ChatColor.GRAY + "Lasts for: " + ChatColor.LIGHT_PURPLE + boost.getDuration() + ChatColor.GRAY + " Minutes.", 6, 45, 6,true);
		TitleUtil.sendTitle(title, player);
		EffectUtil.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_TOUCH);
	}

	public static void onAssist(Player player, String assistName)
	{
		Title title = TitleUtil.createTitle("", ChatColor.RED + "Assist! " + ChatColor.GREEN + "+2 Tickets", 6, 14, 6,true);
		TitleUtil.sendTitle(title, player);
		EffectUtil.playSound(player, Sound.BLOCK_LEVER_CLICK);
	}

	public static void onDeath(Player player, int kills)
	{
		Title title = TitleUtil.createTitle(ChatColor.DARK_RED + "" + ChatColor.BOLD + "WASTED", ChatColor.GRAY + "Your kill streak was " + kills, 40, 60, 5,true);
		TitleUtil.sendTitle(title, player);
		EffectUtil.playSound(player, Sound.ENTITY_GENERIC_DEATH);
	}

	public static void giveTickets(Player player , int tickets, int messageDelay)
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), ()->
		{
			if(!player.isOnline()) return;
			if(tickets >= 10)
			{
				final int divide = tickets > 30 ? 30 : tickets > 20 ? 20 : 10;
				int step = tickets / divide;
				int total = 0;
				final int interval = 2;

				for(int i = 0; i <= divide; i++)
				{
					final int display = total;
					final int k = i;
					Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), ()->
					{
						if(!player.isOnline()) return;
						Title title = TitleUtil.createTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "+" + display + " " + ChatColor.GOLD + "" + ChatColor.BOLD + " Tickets", "", 0, k >= divide ? 80 : 6, 0,true);
						TitleUtil.sendTitle(title, player);
						EffectUtil.playSound(player, Sound.BLOCK_LEVER_CLICK);
					}, interval * i);
					total += step;
					if(i == divide-1)
					{
						total = tickets;
					}
				}
			}
			else
			{
				Title title = TitleUtil.createTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "+" + tickets + " " + ChatColor.GOLD + "" + ChatColor.BOLD + " Tickets", "", 0, 40, 0,true);
				TitleUtil.sendTitle(title, player);
				EffectUtil.playSound(player, Sound.BLOCK_LEVER_CLICK);
			}



		}, messageDelay);
	}

	public static void takeTickets(Player player, int tickets, int messageDelay)
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), ()->
		{
			Title title = TitleUtil.createTitle(ChatColor.DARK_RED + "" + ChatColor.BOLD + "-" + tickets + " " + ChatColor.GOLD + "" + ChatColor.BOLD + " Tickets", "", 10, 60, 0,true);
			TitleUtil.sendTitle(title, player);
			EffectUtil.playSound(player, Sound.BLOCK_FURNACE_FIRE_CRACKLE);
		}, messageDelay);
	}

	public static void selectHero(Player player, Hero hero)
	{
		Title title = TitleUtil.createTitle(ChatColor.YELLOW + "" + ChatColor.BOLD + "You selected " + hero.getColor() + "" + ChatColor.BOLD + hero.getName(), "", 10, 40, 10,true);
		TitleUtil.sendTitle(title, player);
		EffectUtil.playSound(player, Sound.BLOCK_STONE_PRESSUREPLATE_CLICK_ON);
	}

	public static void ultimateReady(Player player, Hero hero, MGPlayer mgp)
	{
		player.sendMessage(hero.getUltimateReadyMessage());
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), ()->
		{
			if(!player.isOnline() || !mgp.isPlaying()) return;
			Title title = TitleUtil.createTitle("", hero.getUltimateReadyMessage(), 10, 40, 10,true);
			TitleUtil.sendTitle(title, player);
			EffectUtil.playSound(player, Sound.ENTITY_CHICKEN_DEATH);
		}, 30);

	}

	public static void displayMapEndWinners(String name, int place, int tickets, int kills) {

		ChatColor color = ChatColor.BLUE;

		int o = place % 10;

		String ordinal;

		if(o == 1)
		{
			ordinal = place + "ST";
		}
		else if (o == 2)
		{
			ordinal = place + "ND";
		}
		else if(o == 3)
		{
			ordinal = place + "RD";
		}
		else ordinal = place + "TH";

		switch (place)
		{
			case 1: color = ChatColor.GOLD;
				break;
			case 2: color = ChatColor.GRAY;
				break;
			case 3: color = ChatColor.DARK_GRAY;
				break;
		}

		Title title = TitleUtil.createTitle(color + "" + ChatColor.BOLD + "You got " + ordinal + " place", ChatColor.YELLOW + "You earned " + tickets + " bonus tickets.", 20, 80, 20, true);
		try {
			title.send(Bukkit.getPlayer(name));
		} catch (Exception ignored){}

		Bukkit.broadcastMessage(ChatColor.YELLOW + name + color + "" + ChatColor.BOLD + " ended " + ordinal + " with " + kills + " kills.");

	}

	public static void activateUltimate(Player player, Hero hero)
	{
		TimedBossBarHolder holder = BossBarUtil.createSecondCountdown(hero.ultimateBarMessage(), hero.getBarColor(), hero.getBarStyle(), hero.ultimateDuration());
		holder.addPlayer(player);
		EffectUtil.playSound(player, Sound.BLOCK_ANVIL_FALL);
	}

	public static void onSpawn(Player player, Hero hero)
	{
		Title title = TitleUtil.createTitle("", ChatColor.RED + "" + ChatColor.BOLD + "Kill! Kill! Kill!", 10, 20, 10, true);
		TitleUtil.sendTitle(title, player);
		EffectUtil.playSound(player, Sound.AMBIENT_CAVE);
	}

	public static void announceMapChange()
	{
		TimedBossBarHolder holder = BossBarUtil.createSecondCountdown(ChatColor.YELLOW + "The map will change in 60 seconds.", BarColor.WHITE, BarStyle.SEGMENTED_20, 60);
		Bukkit.getOnlinePlayers().stream().forEach(holder::addPlayer);
	}
}
