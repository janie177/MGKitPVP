package net.minegusta.heropvp.leaderboard.game;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minegusta.heropvp.utils.DisplayMessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

public class WinnerManager {
	private static ConcurrentMap<String, Integer> kills = Maps.newConcurrentMap();

	/**
	 * Add kills for a given player
	 * @param player The player that got the kill.
	 */
	public static void addKill(Player player)
	{
		String uuid = player.getUniqueId().toString();
		if(kills.containsKey(uuid))
		{
			kills.put(uuid, kills.get(uuid) + 1);
		}
		else kills.put(uuid, 1);
	}

	public static Collection<String> getUUIDS()
	{
		return kills.keySet();
	}

	/**
	 * Give awards to all eligible players in the last game
	 */
	public static void awardWinner()
	{
		String[] winners = new String[10];
		int[] pkills = new int[10];

		List<String> added = Lists.newArrayList();

		int largestKills = 0;
		String killer = "";


		//Sorting the top 10 players.
		for(int i = 0; i<10; i++)
		{
			for(String s : kills.keySet())
			{
				int pKills = kills.get(s);
				if(pKills > largestKills && !added.contains(s))
				{
					largestKills = pKills;
					killer = s;
				}
			}
			if(!killer.isEmpty())
			{
				winners[i] = killer;
				pkills[i] = largestKills;
			}
		}

		//Awarding players in the arrays.
		for(int i = 0; i < 10; i++)
		{
			if(winners[i] != null)
			{
				String pName = winners[i];
				int kills = pkills[i];
				try {
					Player p = Bukkit.getPlayer(UUID.fromString(pName));
					int tickets = 110 - i * 10;
					DisplayMessageUtil.displayMapEndWinners(p, i, tickets, kills);
				} catch (Exception ignored){}
			}
		}

	}

	/**
	 * Reset the winners of the last game.
	 */
	public static void reset()
	{
		kills.clear();
	}
}
