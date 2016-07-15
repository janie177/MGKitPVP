package net.minegusta.heropvp.leaderboard.config;

import net.minegusta.mglib.configs.ConfigurationModel;
import org.bukkit.configuration.file.FileConfiguration;

public class HighScoreConfig extends ConfigurationModel {

	@Override
	public void onLoad(FileConfiguration fileConfiguration) {
		//TODO Load all data from file into the highscoremanager.
	}

	@Override
	public void onSave(FileConfiguration fileConfiguration) {
		//TODO save all highscoremanager data to file.
	}
}
