package net.minegusta.heropvp.leaderboard.game;

import com.google.common.collect.Maps;
import org.bukkit.entity.Player;

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

	/**
	 * Give awards to all eligible players in the last game
	 */
	public static void awardWinner()
	{
		//TODO award players from the kills map.
	}

	/**
	 * Reset the winners of the last game.
	 */
	public static void reset()
	{
		kills.clear();
	}
}
