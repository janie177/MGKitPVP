package net.minegusta.heropvp.leaderboard.highscores;

public class HighScore {

	private String name;
	private String uuid;
	private int kills;
	private int deaths;

	public HighScore(String name, String uuid, int kills, int deaths)
	{
		this.name = name;
		this.deaths = deaths;
		this.kills = kills;
		this.uuid = uuid;
	}

	public int getKills()
	{
		return kills;
	}

	public int getDeaths()
	{
		return deaths;
	}

	public double getKDRatio()
	{
		if(deaths == 0) return kills;
		return (double) kills / (double) deaths;
	}

	public String getUUID()
	{
		return uuid;
	}

	public String getName()
	{
		return name;
	}


}
