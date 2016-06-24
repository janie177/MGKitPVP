package net.minegusta.mgkitpvp.main;

import net.minegusta.mgkitpvp.commands.AdminCommand;
import net.minegusta.mgkitpvp.commands.BreakCommand;
import net.minegusta.mgkitpvp.gui.HeroSelectionMenu;
import net.minegusta.mgkitpvp.gui.TicketShop;
import net.minegusta.mgkitpvp.listeners.GlobalListener;
import net.minegusta.mgkitpvp.listeners.HeroListener;
import net.minegusta.mgkitpvp.mapmanager.SpawnManager;
import net.minegusta.mgkitpvp.saving.MGPlayer;
import net.minegusta.mgkitpvp.scoreboards.ScoreBoardManager;
import net.minegusta.mgkitpvp.utils.DisplayMessageUtil;
import net.minegusta.mglib.gui.InventoryGUI;
import net.minegusta.mglib.saving.mgplayer.PlayerSaveManager;
import net.minegusta.mglib.tasks.Task;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	private static PlayerSaveManager<MGPlayer> saveManager;
	private static InventoryGUI ticketShop;
	private static InventoryGUI heroSelectionMenu;
	private static Task heroPassiveTask = new Task();

	private static Plugin plugin;

	@Override
	public void onEnable()
	{
		//Init the plugin variable.
		plugin = this;

		//Init the spawns configuration
		SpawnManager.init();

		//Initialize the save manager.
		saveManager = new PlayerSaveManager<>(this, MGPlayer.class, 120);
		//In case of reload, add all players already online to the save manager.
		Bukkit.getOnlinePlayers().stream().forEach(pl -> saveManager.loadMGPlayer(pl));

		//Register listeners
		Bukkit.getPluginManager().registerEvents(new GlobalListener(), this);
		Bukkit.getPluginManager().registerEvents(new HeroListener(), this);

		//Register the commands
		getCommand("heropvp").setExecutor(new AdminCommand());
		getCommand("break").setExecutor(new BreakCommand());

		//Load the inventory GUI menu.
		ticketShop = new TicketShop(ChatColor.LIGHT_PURPLE + "Ticket Shop!", 5, "kitpvpticketshop");
		heroSelectionMenu = new HeroSelectionMenu(ChatColor.LIGHT_PURPLE + "Pick a Hero!", 5, "heroselectiongui");

		//Load the scoreboards
		ScoreBoardManager.init();

		//Hero passive tasks
		heroPassiveTask.start(Bukkit.getScheduler().scheduleSyncRepeatingTask(this, ()-> {
			getSaveManager().getAllPlayers().stream().filter(MGPlayer::isPlaying).forEach(MGPlayer::applyPermanentPassives);
		}, 20, 20));

		//Load the bar for announcements in the display util.
		DisplayMessageUtil.initBar();
	}

	@Override
	public void onDisable()
	{

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
