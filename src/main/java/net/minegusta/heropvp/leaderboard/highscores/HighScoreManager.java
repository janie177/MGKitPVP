package net.minegusta.heropvp.leaderboard.highscores;

import net.minegusta.heropvp.leaderboard.game.WinnerManager;
import net.minegusta.heropvp.main.Main;
import net.minegusta.heropvp.saving.MGPlayer;
import net.minegusta.mglib.particles.ParticleEffect;
import org.apache.commons.lang.ArrayUtils;

import java.util.Arrays;

public class HighScoreManager {



	private static HighScore[] highScores = new HighScore[18];

	public static HighScore[] getHighScores()
	{
		return highScores;
	}

	public static void fillHighScore(HighScore score, int place)
	{
		highScores[place] = score;
	}

	public static HighScore getHighScoreEntry(int index)
	{
		return highScores[index];
	}

	private static void insertScore(HighScore score, int index) {
		HighScore temp;
		HighScore insert = score;

		for(int i = index; i < 18; i++)
		{
			temp = highScores[i];
			highScores[i] = insert;
			insert = temp;
		}
	}

	private static void sortScores()
	{
		HighScore temp;
		int n = highScores.length;

		for(int i=0; i < n; i++){
			for(int j=1; j < (n-i); j++){

				if(highScores[j-1].getKills() > highScores[j].getKills()){
					temp = highScores[j-1];
					highScores[j-1] = highScores[j];
					highScores[j] = temp;
				}
			}
		}

		ArrayUtils.reverse(highScores);
	}


	public static void update()
	{
		//Update the highscore data. Compare it to player files.
		for(String s : WinnerManager.getUUIDS())
		{
			try {
				MGPlayer mgp = Main.getSaveManager().getMGPlayer(s);

				HighScore pScore = new HighScore(mgp.getPlayer().getName(), mgp.getUuidString(), mgp.getKills(), mgp.getDeaths());
				boolean run = true;


				for(int i = 17; i >= 0; i--)
				{
					HighScore score = highScores[i];

					if(score.getUUID().equalsIgnoreCase(pScore.getUUID()))
					{
						highScores[i] = pScore;
						sortScores();
						run = false;
						break;
					}
				}
				if(run)
				{
					for(int i = 0; i < 18; i++)
					{
						HighScore score = highScores[i];

						if(pScore.getKills() > score.getKills())
						{
							insertScore(pScore, i);
							break;
						}
					}
				}

			} catch (Exception ignored){}
		}

		sortScores();

		//Save the config
		Main.getScoreManager().saveConfig();

		//Update the signs with the data.
		SignManager.updateSigns();
	}

}
