package net.minegusta.heropvp.saving;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minegusta.heropvp.boosts.Boost;
import net.minegusta.heropvp.classes.Hero;
import net.minegusta.heropvp.leaderboard.game.WinnerManager;
import net.minegusta.heropvp.mapmanager.SpawnManager;
import net.minegusta.heropvp.scoreboards.ScoreBoardManager;
import net.minegusta.heropvp.utils.DisplayMessageUtil;
import net.minegusta.mglib.saving.mgplayer.MGPlayerModel;
import net.minegusta.mglib.utils.EffectUtil;
import net.minegusta.mglib.utils.PotionUtil;
import net.minegusta.mglib.utils.RandomUtil;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class MGPlayer extends MGPlayerModel {

	private int kills = 0;
	private int assists = 0;
	private int killstreak = 0;
	private int deaths = 0;
	private int power = 0;
	private boolean isPlaying = false;
	private long ultimateActive = 0;
	private Hero hero = Hero.DEFAULT;
	private int tickets = 0;
	private ConcurrentMap<String, Double> damagers = Maps.newConcurrentMap();
	private List<Hero> unlocked = Lists.newArrayList(Hero.DEFAULT);

	@Override
	public void onLoad(FileConfiguration fileConfiguration) {
		killstreak = fileConfiguration.getInt("killstreak", 0);
		assists = fileConfiguration.getInt("assists", 0);
		kills = fileConfiguration.getInt("kills", 0);
		deaths = fileConfiguration.getInt("deaths", 0);
		power = fileConfiguration.getInt("power", 0);
		try {
			hero = Hero.valueOf(fileConfiguration.getString("hero"));
		} catch (Exception ignored){}
		tickets = fileConfiguration.getInt("tickets", 0);
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
		ScoreBoardManager.setToTicketBoard(this);
		setActiveHero(hero);

		if(fileConfiguration.isSet("boost"))
		{
			for(String s : fileConfiguration.getConfigurationSection("boost").getKeys(false))
			{
				try
				{
					Boost boost = Boost.valueOf(s);
					long time = fileConfiguration.getLong("boost." + s);
					if(time > System.currentTimeMillis())
					{
						boosts.put(boost, time);
					}
				} catch (Exception ignored){}
			}
		}

		getPlayer().setCollidable(false);
	}

	@Override
	public void updateConf(FileConfiguration fileConfiguration) {
		fileConfiguration.set("killstreak", killstreak);
		fileConfiguration.set("kills", kills);
		fileConfiguration.set("power", power);
		fileConfiguration.set("deaths", deaths);
		fileConfiguration.set("hero", hero.name());
		fileConfiguration.set("tickets", tickets);
		fileConfiguration.set("assists", assists);

		//Clear all old saved boosts
		if(fileConfiguration.isSet("boost")) fileConfiguration.set("boost", null);

		//Save all active boosts.
		for(Boost boost : boosts.keySet())
		{
			long value  = boosts.get(boost);
			if(value > System.currentTimeMillis())
			{
				fileConfiguration.set("boost." + boost.name(), boosts.get(boost));
			}
		}

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
		hero.applyInventory(getPlayer().getInventory());
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

		if(this.power >= 100)
		{
			DisplayMessageUtil.ultimateReady(getPlayer(), hero, this);
		}

		double progressBar = (double) power / 100.0;
		getPlayer().setExp((float) progressBar);
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

	public int getDeaths()
	{
		return deaths;
	}

	public void addAssist()
	{
		assists++;
	}

	public void setAssists(int assists)
	{
		this.assists = assists;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public void addKill()
	{
		setKills(getKills() + 1);
	}

	public void addDeath()
	{
		deaths++;
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
		setKillstreak(getKillstreak() + 1);
	}

	public int getTickets() {
		return tickets;
	}

	public void setTickets(int tickets) {
		this.tickets = tickets;
		ScoreBoardManager.setToTicketBoard(this);
	}

	public void addTickets(int ticketsToAdd, int messageDelay)
	{
		if(hasBoost(Boost.TICKETS30))
		{
			int third = ticketsToAdd / 3;
			ticketsToAdd = ticketsToAdd + third;
		}
		setTickets(getTickets() + ticketsToAdd);
		if(messageDelay > -1) DisplayMessageUtil.giveTickets(getPlayer(), ticketsToAdd, messageDelay);
	}

	public void removeTickets(int ticketsToRemove, int messageDelay)
	{
		setTickets(getTickets() - ticketsToRemove);
		DisplayMessageUtil.takeTickets(getPlayer(), ticketsToRemove, messageDelay);
	}

	public void onDeath()
	{
		//Remove wolves.
		for(Wolf w : wolves.keySet())
		{
			if(w.isValid()) w.remove();
		}
		wolves.clear();

		Player player = getPlayer();
		if(isPlaying() && hasBoost(Boost.MARTYRDOME))
		{
			TNTPrimed tnt = (TNTPrimed) player.getWorld().spawnEntity(player.getLocation(), EntityType.PRIMED_TNT);
			tnt.setFuseTicks(60);
		}
		
		ScoreBoardManager.getHeroTagsBoard().removePlayer(player);
		ScoreBoardManager.setToTicketBoard(this);
		setPlaying(false);
		EffectUtil.playParticle(player.getLocation(), Effect.CLOUD, 1, 1, 1, 0.1F, 40, 40);
		setPower(0);
		DisplayMessageUtil.onDeath(player, killstreak);
		player.setHealth(player.getMaxHealth());
		player.setFoodLevel(20);
		player.teleport(player.getWorld().getSpawnLocation());
		int ticketsToAdd = killstreak * 10 + (killstreak * (killstreak / 2));
		if(ticketsToAdd > 0)
		{
			addTickets(ticketsToAdd, 80);
		}
		damagers.clear();
		setKillstreak(0);
		setAssists(0);
		addDeath();
		
		for(PotionEffect effect : getPlayer().getActivePotionEffects())
		{
			player.removePotionEffect(effect.getType());
		}


		getPlayer().getInventory().clear();
		getPlayer().setCollidable(false);
	}

	//--// BOOSTS //--//

	private Map<Boost, Long> boosts = Maps.newHashMap();

	public boolean hasBoost(Boost boost)
	{
		if(boosts.containsKey(boost))
		{
			if(boosts.get(boost) > System.currentTimeMillis())
			{
				return true;
			}
			boosts.remove(boost);
			return false;
		}
		return false;
	}

	public long getMinutesLeft(Boost boost)
	{
		if(hasBoost(boost))
		{
			long millis = boosts.get(boost) - System.currentTimeMillis();
			if(millis < 60000) return 1;
			return TimeUnit.MILLISECONDS.toMinutes(millis);
		}
		return 0;
	}

	public void addboostSeconds(Boost boost, int seconds)
	{
		boosts.put(boost, (long) seconds * 1000 + System.currentTimeMillis());
	}

	//--//========//--//



	public void breakPlaying()
	{
		ScoreBoardManager.getHeroTagsBoard().removePlayer(getPlayer());
		ScoreBoardManager.setToTicketBoard(this);
		setPlaying(false);
		EffectUtil.playParticle(getPlayer().getLocation(), Effect.CLOUD, 1, 1, 1, 0.1F, 40, 40);
		setPower(0);
		getPlayer().setHealth(getPlayer().getMaxHealth());
		getPlayer().setFoodLevel(20);
		getPlayer().teleport(getPlayer().getWorld().getSpawnLocation());
		int ticketsToAdd = (killstreak * 10 + (killstreak * (killstreak / 2)) + assists * 2);
		if(ticketsToAdd > 0)
		{
			addTickets(ticketsToAdd, 5);
		}
		damagers.clear();
		setKillstreak(0);
		setAssists(0);

		Player player = getPlayer();
		for(PotionEffect effect : getPlayer().getActivePotionEffects())
		{
			player.removePotionEffect(effect.getType());
		}

		getPlayer().getInventory().clear();
		getPlayer().setCollidable(false);
		ScoreBoardManager.setToTicketBoard(this);
	}
	
	private ConcurrentMap<Wolf, Boolean> wolves = Maps.newConcurrentMap();

	public void onSpawn()
	{
		//Remove old wolves.
		for(Wolf w : wolves.keySet())
		{
			if(w.isValid())
			{
				w.remove();
			}
		}
		wolves.clear();

		Player player = getPlayer();
		setPlaying(true);
		player.teleport(SpawnManager.getCurrentArena().getRandomSpawn());
		player.setHealth(player.getMaxHealth());
		player.setFoodLevel(20);
		player.getInventory().clear();
		applyInventory();
		player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 64));
		DisplayMessageUtil.onSpawn(player, hero);
		ScoreBoardManager.getTicketBoard().removePlayer(player);
		ScoreBoardManager.getHeroTagsBoard().addPlayer(player, hero.name());
		player.setCollidable(true);

		if(hasBoost(Boost.POWER35))
		{
			setPower(35);
		}


		if(hasBoost(Boost.SWORDMASTER))
		{
			player.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD));
		}


		if(hasBoost(Boost.HEALTHPOT))
		{
			player.getInventory().addItem(new ItemStack(Material.POTION, 1)
			{
				{
					PotionMeta meta = (PotionMeta) getItemMeta();
					meta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL, false, true));
					setItemMeta(meta);
				}
			});
		}
		
		if(hasBoost(Boost.BATTLEPET))
		{
			Wolf wolf = (Wolf) player.getWorld().spawnEntity(player.getLocation(), EntityType.WOLF);
			wolf.setTamed(true);
			wolf.setOwner(player);
			wolf.setCollarColor(DyeColor.MAGENTA);
			wolf.setSitting(false);
			wolf.setAdult();
			wolf.setBreed(false);
			wolf.setCustomNameVisible(true);
			wolf.setCustomName(ChatColor.RED + player.getPlayerListName() + "'s Wolf");
			if(RandomUtil.chance(10)) wolf.setCustomName(ChatColor.LIGHT_PURPLE + "Lassie");

			wolves.put(wolf, true);
		}
		
	}

	public void onKillPlayer(String killedName)
	{
		addPower(hero.getPowerPerKill());
		//Heal player on killing someone.
		PotionUtil.updatePotion(getPlayer(), PotionEffectType.REGENERATION, 0, 3);
		addKill();
		addKillStreakKill();
		DisplayMessageUtil.onKill(hero, getPlayer(), killstreak, killedName);
		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "addcredits " + getPlayer().getName() + " 2");
		getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "You earned " + ChatColor.YELLOW + "2" + ChatColor.LIGHT_PURPLE + " credits.");
		hero.onKill(getPlayer());
		WinnerManager.addKill(getPlayer());
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

	public void onUltimate()
	{
		onUltimate(null, null);
	}

	public void onUltimate(Player player)
	{
		onUltimate(player, null);
	}

	public void onUltimate(Event event)
	{
		onUltimate(null, event);
	}

	public void activateUltimate()
	{
		if(!isPlaying()) return;
		setPower(0);
		if(hero.ultimateDuration() > 0)
		{
			setUltimateActive();
			DisplayMessageUtil.activateUltimate(getPlayer(), hero);
		}
	}

	public void onUltimate(Player player, Event event)
	{
		if(!isPlaying()) return;
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
