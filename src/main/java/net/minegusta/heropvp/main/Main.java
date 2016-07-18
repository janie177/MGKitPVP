package net.minegusta.heropvp.main;

import net.minegusta.heropvp.commands.AdminCommand;
import net.minegusta.heropvp.commands.BreakCommand;
import net.minegusta.heropvp.commands.HighscoreBoardCommand;
import net.minegusta.heropvp.gui.BoostShop;
import net.minegusta.heropvp.gui.HeroSelectionMenu;
import net.minegusta.heropvp.gui.TicketShop;
import net.minegusta.heropvp.leaderboard.config.HighScoreConfig;
import net.minegusta.heropvp.listeners.GlobalListener;
import net.minegusta.heropvp.listeners.HeroListener;
import net.minegusta.heropvp.mapmanager.SpawnConfiguration;
import net.minegusta.heropvp.mapmanager.SpawnManager;
import net.minegusta.heropvp.npcs.NPCManager;
import net.minegusta.heropvp.saving.MGPlayer;
import net.minegusta.heropvp.scoreboards.ScoreBoardManager;
import net.minegusta.heropvp.utils.DisplayMessageUtil;
import net.minegusta.mglib.configs.ConfigurationFileManager;
import net.minegusta.mglib.gui.InventoryGUI;
import net.minegusta.mglib.saving.mgplayer.PlayerSaveManager;
import net.minegusta.mglib.tasks.Task;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	private static PlayerSaveManager<MGPlayer> saveManager;
	private static ConfigurationFileManager<HighScoreConfig> scoreConfigManager;
	private static InventoryGUI ticketShop;
	private static InventoryGUI heroSelectionMenu;
	private static InventoryGUI boostShop;
	private static Task heroPassiveTask = new Task();

	private static Plugin plugin;

	@Override
	public void onEnable()
	{
		//Init the plugin variable.
		plugin = this;

		//Despawn all entities on load
		Bukkit.getWorlds().stream().forEach(world -> world.getLivingEntities().stream().filter(ent -> !(ent instanceof Player)).forEach(LivingEntity::remove));

		//Init the spawns configuration
		SpawnManager.init();

		//Initialize the save manager.
		saveManager = new PlayerSaveManager<>(this, MGPlayer.class, 120);
		//In case of reload, add all players already online to the save manager.
		Bukkit.getOnlinePlayers().stream().forEach(pl -> saveManager.loadMGPlayer(pl));

		//Initialize the config for highscore boards and per-map winners.
		scoreConfigManager = new ConfigurationFileManager<>(this, HighScoreConfig.class, 180, "scores");

		//Register listeners
		Bukkit.getPluginManager().registerEvents(new GlobalListener(), this);
		Bukkit.getPluginManager().registerEvents(new HeroListener(), this);

		//Register the commands
		getCommand("heropvp").setExecutor(new AdminCommand());
		getCommand("break").setExecutor(new BreakCommand());
		getCommand("hsb").setExecutor(new HighscoreBoardCommand());

		//Load the inventory GUI menu.
		ticketShop = new TicketShop(ChatColor.LIGHT_PURPLE + "Ticket Shop!", 5, "kitpvpticketshop");
		boostShop = new BoostShop(ChatColor.DARK_AQUA + "Boost Shop!", 5, "kitpvpboostshop");
		heroSelectionMenu = new HeroSelectionMenu(ChatColor.LIGHT_PURPLE + "Pick a Hero!", 5, "heroselectiongui");

		//Load the scoreboards
		ScoreBoardManager.init();

		//Hero passive tasks
		heroPassiveTask.start(Bukkit.getScheduler().scheduleSyncRepeatingTask(this, ()-> {
			getSaveManager().getAllPlayers().stream().filter(MGPlayer::isPlaying).forEach(MGPlayer::applyPermanentPassives);
		}, 20, 20));

		//Load the bar for announcements in the display util.
		DisplayMessageUtil.initBar();

		//Init NPC config
		NPCManager.initConfig();

		//Load all the NPCs
		NPCManager.spawnNPCS();
	}

	@Override
	public void onDisable()
	{
		//Despawn all entities on shutdown
		Bukkit.getWorlds().stream().forEach(world -> world.getLivingEntities().stream().filter(ent -> !(ent instanceof Player)).forEach(LivingEntity::remove));

		//Save everything
		getSaveManager().saveAllMGPlayers();
		SpawnManager.getSpawnConfigManager().saveConfig();
		scoreConfigManager.saveConfig();
	}

	public static ConfigurationFileManager getScoreManager()
	{
		return scoreConfigManager;
	}

	public static Plugin getPlugin()
	{
		return plugin;
	}

	public static PlayerSaveManager<MGPlayer> getSaveManager()
	{
		return saveManager;
	}

	public static InventoryGUI getTicketShop()
	{
		return ticketShop;
	}

	public static InventoryGUI getHeroSelectionMenu()
	{
		return heroSelectionMenu;
	}

}
