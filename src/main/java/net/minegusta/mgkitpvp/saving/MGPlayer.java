package net.minegusta.mgkitpvp.saving;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minegusta.mgkitpvp.classes.Hero;
import net.minegusta.mgkitpvp.mapmanager.SpawnManager;
import net.minegusta.mgkitpvp.scoreboards.ScoreBoardManager;
import net.minegusta.mgkitpvp.utils.DisplayMessageUtil;
import net.minegusta.mglib.saving.mgplayer.MGPlayerModel;
import net.minegusta.mglib.scoreboards.MGScore;
import net.minegusta.mglib.utils.EffectUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

public class MGPlayer extends MGPlayerModel {

	private int kills = 0;
	private int killstreak = 0;
	private int power = 0;
	private boolean isPlaying = false;
	private long ultimateActive = 0;
	private Hero hero = Hero.DEFAULT;
	private int tickets = 0;
	private ConcurrentMap<String, Double> damagers = Maps.newConcurrentMap();
	private List<Hero> unlocked = Lists.newArrayList(Hero.DEFAULT);

	@Override
	public void onLoad(FileConfiguration fileConfiguration) {
		killstreak = fileConfiguration.getInt("killstreak");
		kills = fileConfiguration.getInt("kills");
		power = fileConfiguration.getInt("power");
		try {
			hero = Hero.valueOf(fileConfiguration.getString("hero"));
		} catch (Exception ignored){}
		tickets = fileConfiguration.getInt("tickets");
		if(fileConfiguration.isSet("unlocked-heroes"))
		{
			unlocked.clear();
			for(String s : fileConfiguration.getStringList("unlocked-heroes"))
			{
				try
				{
					Hero h = Hero.valueOf(s);
					unlocked.add(h);
				} catch (Exception ignored){}
			}
		}
		ScoreBoardManager.getTicketBoard().updatePlayer(getPlayer(), new MGScore(ChatColor.GREEN + "Tickets:", tickets));
		setActiveHero(hero);
	}

	@Override
	public void updateConf(FileConfiguration fileConfiguration) {
		fileConfiguration.set("killstreak", killstreak);
		fileConfiguration.set("kills", kills);
		fileConfiguration.set("power", power);
		fileConfiguration.set("hero", hero.name());
		fileConfiguration.set("tickets", tickets);

		List<String> heroes = Lists.newArrayList();
		for(Hero h : unlocked)
		{
			if(!heroes.contains(h.name())) heroes.add(h.name());
		}
		fileConfiguration.set("unlocked-heroes", heroes);
	}

	public Player getPlayer()
	{
		return Bukkit.getPlayer(getUuid());
	}

	public boolean hasHeroUnlocked(Hero hero)
	{
		return unlocked.contains(hero);
	}

	public void addHero(Hero hero)
	{
		DisplayMessageUtil.unlockHero(getPlayer(), hero);
		unlocked.add(hero);
	}

	public List<Hero> getUnlockedHeroes()
	{
		return unlocked;
	}

	public void setActiveHero(Hero hero)
	{
		DisplayMessageUtil.selectHero(getPlayer(), hero);
		setPower(0);
		setKillstreak(0);
		this.hero = hero;
		hero.onSelect(getPlayer());
	}

	public Hero getActiveHero()
	{
		return hero;
	}

	public int getPower()
	{
		return power;
	}

	public void setPower(int power)
	{
		this.power = power;
		if(this.power > 100)
		{
			this.power = 100;
		}
		else if(this.power < 0)
		{
			this.power = 0;
		}

		if(power == 100)
		{
			DisplayMessageUtil.ultimateReady(getPlayer(), hero);
		}

		float progressBar = power / 100;
		getPlayer().setExp(progressBar);
	}

	public void addPower(int power)
	{
		setPower(getPower() + power);
	}

	public void removePower(int power)
	{
		addPower(-power);
	}

	public int getKills() {
		return kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public void addKill()
	{
		setKills(getKills() + 1);
	}

	public int getKillstreak() {
		return killstreak;
	}

	public void setKillstreak(int killstreak) {
		this.killstreak = killstreak;
		getPlayer().setLevel(killstreak);
	}

	public void addKillStreakKill()
	{
		setKills(getKillstreak() + 1);
	}

	public int getTickets() {
		return tickets;
	}

	public void setTickets(int tickets) {
		this.tickets = tickets;
		ScoreBoardManager.getTicketBoard().updatePlayer(getPlayer(), new MGScore(ChatColor.GREEN + "Tickets:", tickets));
	}

	public void addTickets(int ticketsToAdd, int messageDelay)
	{
		setTickets(getTickets() + ticketsToAdd);
		DisplayMessageUtil.giveTickets(getPlayer(), ticketsToAdd, messageDelay);
	}

	public void removeTickets(int ticketsToRemove, int messageDelay)
	{
		setTickets(getTickets() - ticketsToRemove);
		DisplayMessageUtil.takeTickets(getPlayer(), ticketsToRemove, messageDelay);
	}

	public void onDeath()
	{
		ScoreBoardManager.getHeroTagsBoard().removePlayer(getPlayer());
		ScoreBoardManager.getTicketBoard().updatePlayer(getPlayer(), new MGScore(ChatColor.GREEN + "Tickets:", tickets));
		setPlaying(false);
		EffectUtil.playParticle(getPlayer().getLocation(), Effect.CLOUD, 1, 1, 1, 0.1F, 40, 40);
		setPower(0);
		DisplayMessageUtil.onDeath(getPlayer(), killstreak);
		getPlayer().setHealth(getPlayer().getMaxHealth());
		getPlayer().setFoodLevel(20);
		getPlayer().teleport(getPlayer().getWorld().getSpawnLocation());
		int ticketsToAdd = killstreak * 10 + (killstreak * (killstreak / 2));
		if(ticketsToAdd > 0)
		{
			addTickets(ticketsToAdd, 80);
		}
		damagers.clear();
		setKillstreak(0);
		getPlayer().getActivePotionEffects().clear();
		getPlayer().getInventory().clear();
	}

	public void resetOnMapChange()
	{
		ScoreBoardManager.getHeroTagsBoard().removePlayer(getPlayer());
		ScoreBoardManager.getTicketBoard().updatePlayer(getPlayer(), new MGScore(ChatColor.GREEN + "Tickets:", tickets));
		setPlaying(false);
		EffectUtil.playParticle(getPlayer().getLocation(), Effect.CLOUD, 1, 1, 1, 0.1F, 40, 40);
		setPower(0);
		getPlayer().setHealth(getPlayer().getMaxHealth());
		getPlayer().setFoodLevel(20);
		getPlayer().teleport(getPlayer().getWorld().getSpawnLocation());
		int ticketsToAdd = killstreak * 10 + (killstreak * (killstreak / 2));
		if(ticketsToAdd > 0)
		{
			addTickets(ticketsToAdd, 5);
		}
		damagers.clear();
		setKillstreak(0);
		getPlayer().getActivePotionEffects().clear();

		getPlayer().getInventory().clear();
	}

	public void onSpawn()
	{
		setPlaying(true);
		getPlayer().teleport(SpawnManager.getCurrentSpawn());
		getPlayer().setHealth(getPlayer().getMaxHealth());
		getPlayer().setFoodLevel(20);
		getPlayer().getInventory().clear();
		applyInventory();
		getPlayer().getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 64));
		DisplayMessageUtil.onSpawn(getPlayer(), hero);
		ScoreBoardManager.getTicketBoard().removePlayer(getPlayer());
		ScoreBoardManager.getHeroTagsBoard().addPlayer(getPlayer(), hero.name());
	}

	public void onKillPlayer(String killedName)
	{
		addPower(hero.getPowerPerKill());
		addKill();
		addKillStreakKill();
		DisplayMessageUtil.onKill(hero, getPlayer(), killstreak, killedName);
	}

	public void addDamage(Player damager, double damage)
	{
		if(damagers.containsKey(damager.getUniqueId().toString()))
		{
			double newDamage = damage + damagers.get(damager.getUniqueId().toString());
			damagers.put(damager.getUniqueId().toString(), newDamage);
		}
		else
		{
			damagers.put(damager.getUniqueId().toString(), damage);
		}
	}

	public Optional<Player> getMostDamage()
	{
		String winner = "";
		double mostDamage = 0;
		for(String s : damagers.keySet())
		{
			double d = damagers.get(s);
			if(d > mostDamage)
			{
				mostDamage = d;
				winner = s;
			}
		}

		if(winner.isEmpty())
		{
			return Optional.empty();
		}
		try
		{
			Player player = Bukkit.getPlayer(UUID.fromString(winner));
			if(player != null) return Optional.of(player);
		} catch (Exception ignored) {}
		return Optional.empty();
	}

	public void applyPermanentPassives()
	{
		hero.applyPermanentPassives(getPlayer());
	}

	public void onUltimate(Player player)
	{
		onUltimate(player, null);
	}

	public void onUltimate(Event event)
	{
		onUltimate(null, event);
	}

	public void onUltimate(Player player, Event event)
	{
		if(isPlaying()) return;
		setPower(0);
		if(hero.ultimateDuration() > 0)
		{
			setUltimateActive();
			DisplayMessageUtil.activateUltimate(player, hero);
		}
		if(player != null)
		{
			hero.doUltimate(player);
		}
		if(event != null)
		{
			hero.doUltimate(event);
		}
	}

	public void onPassive(Player player)
	{
		onPassive(player, null);
	}

	public void onPassive(Event event)
	{
		onPassive(null, event);
	}

	public void onPassive(Player player, Event event)
	{
		if(player != null)
		{
			hero.doPassive(player);
		}
		if(event != null)
		{
			hero.doPassive(event);
		}
	}

	public boolean isUltimateReady()
	{
		return power == 100;
	}

	public boolean isUltimateActive()
	{
		return System.currentTimeMillis() < ultimateActive;
	}

	public void setUltimateActive()
	{
		ultimateActive = System.currentTimeMillis() + hero.ultimateDuration() * 1000;
	}

	public void applyInventory()
	{
		hero.applyInventory(getPlayer().getInventory());
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public void setPlaying(boolean playing) {
		isPlaying = playing;
	}
}
