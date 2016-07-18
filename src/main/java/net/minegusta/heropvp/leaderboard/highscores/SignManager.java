package net.minegusta.heropvp.leaderboard.highscores;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;

public class SignManager {

	private static Location[] signs = new Location[18];

	public static Location[] getSigns()
	{
		return signs;
	}

	public static void loadSign(Location l, int index)
	{
		signs[index] = l;
	}

	public static void addSign(int index, Location location)
	{
		signs[index] = location;
	}

	public static void updateSigns()
	{
		for(int i = 0; i < 18; i++)
		{
			Location l = signs[i];
			if(l != null)
			{
				HighScore score = HighScoreManager.getHighScoreEntry(i);

				if(score != null)
				{
					String name = score.getName();
					String kills = Integer.toString(score.getKills());
					String deaths = Integer.toString(score.getDeaths());
					String kd = Double.toString(score.getKDRatio()).substring(0, 3) + " K/D";

					Material m = l.getBlock().getType();
					if(m == Material.WALL_SIGN || m == Material.SIGN_POST || m == Material.SIGN)
					{
						Sign sign = (Sign) l.getBlock().getState();
						sign.setLine(0, name);
						sign.setLine(1, ChatColor.GREEN + kills + "K");
						sign.setLine(2, ChatColor.DARK_RED + deaths + "D");
						sign.setLine(3, ChatColor.YELLOW + kd);
						sign.update();
					}
				}
			}
		}
	}

}
