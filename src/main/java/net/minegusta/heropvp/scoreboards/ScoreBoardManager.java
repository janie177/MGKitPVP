package net.minegusta.heropvp.scoreboards;

import net.minegusta.heropvp.classes.Hero;
import net.minegusta.heropvp.saving.MGPlayer;
import net.minegusta.mglib.scoreboards.HealthBoard;
import net.minegusta.mglib.scoreboards.MGScore;
import net.minegusta.mglib.scoreboards.ScoreBoardUtil;
import net.minegusta.mglib.scoreboards.SideBarBoard;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ScoreBoardManager {

	private static HealthBoard heroTags = ScoreBoardUtil.createNewHealthboard(ChatColor.RED + "Health");
	private static SideBarBoard ticketBoard = ScoreBoardUtil.createNewSideBarBoard(ChatColor.YELLOW + "Info");

	public static void init()
	{
		for(Hero hero : Hero.values())
		{
			heroTags.addTeam(hero.name(), hero.getTag(), "", false, true);
		}
	}

	public static void setToTicketBoard(MGPlayer mgPlayer)
	{
		ScoreBoardManager.getTicketBoard().updatePlayer(mgPlayer.getPlayer(),
				new MGScore(ChatColor.LIGHT_PURPLE + "Tickets:", mgPlayer.getTickets()),
				new MGScore(ChatColor.GREEN + "Kills:", mgPlayer.getKills()),
				new MGScore(ChatColor.DARK_RED + "Deaths:", mgPlayer.getDeaths())
		);
	}

	public static HealthBoard getHeroTagsBoard()
	{
		return heroTags;
	}

	public static SideBarBoard getTicketBoard()
	{
		return ticketBoard;
	}
}
