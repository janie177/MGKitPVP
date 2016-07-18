package net.minegusta.heropvp.leaderboard.config;

import net.minegusta.heropvp.leaderboard.highscores.HighScore;
import net.minegusta.heropvp.leaderboard.highscores.HighScoreManager;
import net.minegusta.heropvp.leaderboard.highscores.SignManager;
import net.minegusta.mglib.configs.ConfigurationModel;
import net.minegusta.mglib.utils.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;

public class HighScoreConfig extends ConfigurationModel {

	@Override
	public void onLoad(FileConfiguration fileConfiguration) {


		for(int i = 0; i < 18; i++)
		{
			int kills = fileConfiguration.getInt("players.place" + i + ".kills", 0);
			int deaths = fileConfiguration.getInt("players.place" + i + ".deaths", 0);
			String uuid = fileConfiguration.getString("players.place" + i + ".uuid", "069a79f4-44e9-4726-a5be-fca90e38aaf5");
			String name = fileConfiguration.getString("players.place" + i + ".name",  "Notch");

			HighScore score = new HighScore(name, uuid, kills , deaths);
			HighScoreManager.fillHighScore(score, i);

			String stringLoc = fileConfiguration.getString("signs.location" + i, "");
			if(!stringLoc.isEmpty())
			{
				Location location = LocationUtil.stringToLocation(stringLoc);
				SignManager.loadSign(location, i);
			}
		}
	}

	@Override
	public void onSave(FileConfiguration fileConfiguration){
		int index = 0;
		for(Location l : SignManager.getSigns())
		{
			if(l != null)
			{
				fileConfiguration.set("signs.location" + index, LocationUtil.locationToString(l));
			}
			index++;
		}

		index = 0;
		for(HighScore score : HighScoreManager.getHighScores())
		{
			fileConfiguration.set("players.place" + index + ".kills", score.getKills());
			fileConfiguration.set("players.place" + index + ".deaths", score.getDeaths());
			try {
				OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(score.getUUID()));
				String name = player.getName();
				{
					if(name != null)
					{
						fileConfiguration.set("players.place" + index + ".name", player.getName());
					}
					else fileConfiguration.set("players.place" + index + ".name", score.getName());
				}

			} catch (Exception ignored)
			{
				fileConfiguration.set("players.place" + index + ".name", score.getName());
			}
			fileConfiguration.set("players.place" + index + ".uuid", score.getUUID());
			index++;
		}
	}
}
