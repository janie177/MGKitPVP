package net.minegusta.heropvp.leaderboard.highscores;

import net.minegusta.heropvp.leaderboard.game.WinnerManager;
import net.minegusta.heropvp.main.Main;
import net.minegusta.heropvp.saving.MGPlayer;

public class HighScoreManager {

	private static HighScore[] highScores = new HighScore[18];

	public static void fillHighScores(HighScore score, int place)
	{
		highScores[place] = score;
	}

	public static void update()
	{
		//Update the highscore data. Compare it to player files.
		for(String s : WinnerManager.getUUIDS())
		{
			try {
				MGPlayer mgp = Main.getSaveManager().getMGPlayer(s);

			} catch (Exception ignored){}

		}



		//Save the config
		Main.getScoreManager().saveConfig();

		//Update the signs with the data.
		SignManager.updateSigns();
	}

}
