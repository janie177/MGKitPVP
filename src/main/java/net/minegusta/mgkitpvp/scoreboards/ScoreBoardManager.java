package net.minegusta.mgkitpvp.scoreboards;

import net.minegusta.mgkitpvp.classes.Hero;
import net.minegusta.mglib.scoreboards.HealthBoard;
import net.minegusta.mglib.scoreboards.ScoreBoardUtil;
import net.minegusta.mglib.scoreboards.SideBarBoard;
import org.bukkit.ChatColor;

public class ScoreBoardManager {

	private static HealthBoard heroTags = ScoreBoardUtil.createNewHealthboard(ChatColor.RED + "Health");
	private static SideBarBoard ticketBoard = ScoreBoardUtil.createNewSideBarBoard(ChatColor.LIGHT_PURPLE + "Tickets");

	public static void init()
	{
		for(Hero hero : Hero.values())
		{
			heroTags.addTeam(hero.name(), hero.getColor() + hero.getTag(), "", false, true);
		}
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
