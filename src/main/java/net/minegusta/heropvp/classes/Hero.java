package net.minegusta.heropvp.classes;

import net.minegusta.heropvp.classes.impl.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.PlayerInventory;

public enum Hero {
	DEFAULT(new Default()),
	ARTEMIZ(new Artemiz()),
	WITCHER(new Witcher()),
	ASSASSIN(new Assassin()),
	ELVENLORD(new ElvenLord()),
	ICEMAGE(new IceMage()),
	FIREMAGE(new FireMage()),
	BLOODMAGE(new BloodMage()),
	SCOUT(new Scout());

	private IHero hero;

	Hero(IHero hero)
	{
		this.hero = hero;
	}

	public IHero getHero() {
		return hero;
	}

	public void doPassive(Event event)
	{
		hero.doPassive(event);
	}

	public void doPassive(Player player)
	{
		hero.doPassive(player);
	}

	public String getName()
	{
		return hero.getName();
	}

	public String getTag()
	{
		return hero.getColor() + hero.getTag();
	}

	public void doUltimate(Event event)
	{
		hero.doUltimate(event);
	}

	public ChatColor getColor()
	{
		return hero.getColor();
	}

	public BarStyle getBarStyle()
	{
		return hero.getBarStyle();
	}

	public BarColor getBarColor()
	{
		return hero.getBarColor();
	}

	public String getUltimateReadyMessage()
	{
		return hero.getColor() + hero.ultimateReadyMessage();
	}

	public Material getMaterial()
	{
		return hero.getMaterial();
	}

	public int getCost()
	{
		return hero.getCost();
	}

	public int getPowerPerKill()
	{
		return hero.powerPerKill();
	}

	public String[] getDescription()
	{
		return hero.getDescription();
	}

	public void onSelect(Player player)
	{
		hero.onSelect(player);
	}

	public void onKill(Player player)
	{
		hero.onKill(player);
	}

	public void applyInventory(PlayerInventory inv)
	{
		hero.applyInventory(inv);
	}

	public int ultimateDuration()
	{
		return hero.ultimateDuration();
	}

	public void doUltimate(Player player)
	{
		hero.doUltimate(player);
	}

	public void applyPermanentPassives(Player player)
	{
		hero.applyPermanentPassives(player);
	}

	public String ultimateBarMessage()
	{
		return hero.getColor() + hero.ultimateBarMessage();
	}
}
