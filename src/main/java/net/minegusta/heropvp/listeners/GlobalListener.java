package net.minegusta.heropvp.listeners;

import com.google.common.collect.Lists;
import net.minegusta.heropvp.main.Main;
import net.minegusta.heropvp.saving.MGPlayer;
import net.minegusta.heropvp.scoreboards.ScoreBoardManager;
import net.minegusta.heropvp.utils.DisplayMessageUtil;
import net.minegusta.mglib.scoreboards.MGScore;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.Optional;

public class GlobalListener implements Listener {

	//Block exp gaining, is used by the kill and ultimate ability system.

	@EventHandler
	public void onExpDrop(EntityDeathEvent e)
	{
		e.setDroppedExp(0);
	}

	@EventHandler
	public void onExpFurnace(FurnaceExtractEvent e)
	{
		e.setExpToDrop(0);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e)
	{
		e.setExpToDrop(0);
	}

	@EventHandler
	public void onExpBottle(ExpBottleEvent e)
	{
		e.setExperience(0);
	}

	//Listen for kills and apply damage to players
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDamage(EntityDamageByEntityEvent e)
	{
		if(e.isCancelled()) return;
		if(e.getEntity() instanceof Player)
		{
			Player target = (Player) e.getEntity();
			Player damager;
			if(e.getDamager() instanceof Player)
			{
				damager = (Player) e.getDamager();
				MGPlayer damagerMGP = Main.getSaveManager().getMGPlayer(damager);
				MGPlayer targetMGP = Main.getSaveManager().getMGPlayer(target);
				/**
				 * Player attacked directly by another player.
				 */

				//On death
				if(target.getHealth() - e.getFinalDamage() <= 0)
				{
					targetMGP.addDamage(damager, e.getFinalDamage());
					e.setCancelled(true);
					Optional<Player> p = targetMGP.getMostDamage();
					if(p.isPresent())
					{
						Main.getSaveManager().getMGPlayer(p.get()).onKillPlayer(target.getName());
						if(!p.get().getUniqueId().equals(damager.getUniqueId()))
						{
							DisplayMessageUtil.onAssist(damager, target.getName());
						}
					}
					else
					{
						damagerMGP.onKillPlayer(target.getName());
					}
				}
				//On normal damage that does not result in death
				else
				{
					targetMGP.addDamage(damager, e.getFinalDamage());
				}
			}
			else if(e.getDamager() instanceof Projectile && ((Projectile) e.getDamager()).getShooter() instanceof Player)
			{
				damager = (Player) ((Projectile) e.getDamager()).getShooter();
				MGPlayer damagerMGP = Main.getSaveManager().getMGPlayer(damager);
				MGPlayer targetMGP = Main.getSaveManager().getMGPlayer(target);
				/**
				 * Projectile damage
				 */

				//On death
				if(target.getHealth() - e.getFinalDamage() <= 0)
				{
					targetMGP.addDamage(damager, e.getFinalDamage());
					e.setCancelled(true);
					Optional<Player> p = targetMGP.getMostDamage();
					if(p.isPresent())
					{
						Main.getSaveManager().getMGPlayer(p.get()).onKillPlayer(target.getName());
						if(!p.get().getUniqueId().equals(damager.getUniqueId()))
						{
							DisplayMessageUtil.onAssist(damager, target.getName());
						}
					}
					else
					{
						damagerMGP.onKillPlayer(target.getName());
					}
				}
				//On normal damage that does not result in death
				else
				{
					targetMGP.addDamage(damager, e.getFinalDamage());
				}
			}
		}
	}

	//Make sure players wont die.
	@EventHandler
	public void onPassiveKill(EntityDamageEvent e)
	{
		if(e instanceof EntityDamageByEntityEvent) return;
		if(e.getEntity() instanceof Player && ((Player) e.getEntity()).getHealth() - e.getFinalDamage() <= 0)
		{
			e.setCancelled(true);
			MGPlayer mgp = Main.getSaveManager().getMGPlayer((Player) e.getEntity());
			Optional<Player> killer = mgp.getMostDamage();
			if(killer.isPresent())
			{
				Main.getSaveManager().getMGPlayer(killer.get()).onKillPlayer(e.getEntity().getName());
			}
			mgp.onDeath();
		}
	}

	//Stop players from dropping anything on death. Also call onDeath when a player somehow does die.
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e)
	{
		e.getDrops().clear();
		MGPlayer mgp = Main.getSaveManager().getMGPlayer(e.getEntity());
		if(mgp.isPlaying())
		{
			mgp.onDeath();
		}
	}

	//Allowed spawn reasons
	private static List<CreatureSpawnEvent.SpawnReason> allowedReasons = Lists.newArrayList(CreatureSpawnEvent.SpawnReason.SPAWNER, CreatureSpawnEvent.SpawnReason.SPAWNER_EGG, CreatureSpawnEvent.SpawnReason.CUSTOM);

	//Block natural mob spawning
	@EventHandler
	public void onMobSpawn(CreatureSpawnEvent e)
	{
		if(!allowedReasons.contains(e.getSpawnReason()))
		{
			e.setCancelled(true);
		}
	}

	//Teleport user to spawn area on login. Also add users to the announcement bar.
	//Also set players to be unable to collide.
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent e)
	{
		DisplayMessageUtil.addPlayerToBar(e.getPlayer());
		e.getPlayer().setCollidable(false);
		MGPlayer mgp = Main.getSaveManager().getMGPlayer(e.getPlayer());
		mgp.resetOnMapChange();
		ScoreBoardManager.getTicketBoard().addPlayer(e.getPlayer(), new MGScore(ChatColor.GREEN + "Tickets:", mgp.getTickets()));
	}

	//Prevent dropping items
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e)
	{
		if(!e.getPlayer().isOp())
		{
			e.getPlayer().sendMessage(ChatColor.RED + "You cannot drop items!");
			e.setCancelled(true);
		}
	}

	//Listen for interaction with the game NPC's.
	@EventHandler
	public void onEntityDamage(PlayerInteractEntityEvent e)
	{
		if(e.getRightClicked() instanceof Villager || e.getRightClicked() instanceof Zombie || e.getRightClicked() instanceof Skeleton)
		{
			LivingEntity rightClicked = (LivingEntity) e.getRightClicked();
			if(rightClicked.getCustomName() != null)
			{

				if(e.getPlayer().isOp() && e.getPlayer().getInventory().getItemInMainHand().getType() == Material.BLAZE_ROD)
				{
					rightClicked.remove();
					e.getPlayer().sendMessage(ChatColor.GREEN + "You removed an entity.");
					return;
				}

				//Spawn
				if(rightClicked.getCustomName().equalsIgnoreCase(ChatColor.GREEN + "" + ChatColor.BOLD + "" + "Play Now!"))
				{
					Main.getSaveManager().getMGPlayer(e.getPlayer()).onSpawn();
					e.setCancelled(true);
					return;
				}
				//Open shop
				if(rightClicked.getCustomName().equalsIgnoreCase(ChatColor.GOLD + "" + ChatColor.BOLD + "" + "Hero Shop"))
				{
					Main.getTicketShop().openInventory(e.getPlayer());
					e.setCancelled(true);
					return;
				}
				//Open selection interface
				if(rightClicked.getCustomName().equalsIgnoreCase(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "" + "Select Hero"))
				{
					Main.getHeroSelectionMenu().openInventory(e.getPlayer());
					e.setCancelled(true);
					return;
				}
			}
		}
	}

	//Also check for hitting the NPC's to do the custom actions.
	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent e)
	{
		if(e.getDamager() instanceof Player && (e.getEntity() instanceof Villager || e.getEntity() instanceof Zombie || e.getEntity() instanceof Skeleton))
		{
			LivingEntity rightClicked = (LivingEntity) e.getEntity();
			Player player = (Player) e.getDamager();
			if(rightClicked.getCustomName() != null)
			{

				if(player.isOp() && player.getInventory().getItemInMainHand().getType() == Material.BLAZE_ROD)
				{
					rightClicked.remove();
					player.sendMessage(ChatColor.GREEN + "You removed an entity.");
					return;
				}

				//Spawn
				if(rightClicked.getCustomName().equalsIgnoreCase(ChatColor.GREEN + "" + ChatColor.BOLD + "" + "Play Now!"))
				{
					Main.getSaveManager().getMGPlayer(player).onSpawn();
					e.setCancelled(true);
					return;
				}
				//Open shop
				if(rightClicked.getCustomName().equalsIgnoreCase(ChatColor.GOLD + "" + ChatColor.BOLD + "" + "Hero Shop"))
				{
					Main.getTicketShop().openInventory(player);
					e.setCancelled(true);
					return;
				}
				//Open selection interface
				if(rightClicked.getCustomName().equalsIgnoreCase(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "" + "Select Hero"))
				{
					Main.getHeroSelectionMenu().openInventory(player);
					e.setCancelled(true);
					return;
				}
			}
		}
	}

	//Stop players from using projectiles and stuff.
	@EventHandler
	public void onUseItem(ProjectileLaunchEvent e)
	{
		if(e.getEntity().getShooter() instanceof Player)
		{
			MGPlayer mgp = Main.getSaveManager().getMGPlayer((Player) e.getEntity().getShooter());
			if(!mgp.isPlaying())
			{
				e.setCancelled(true);
			}
		}
	}

	//Stop NPC's from burning
	@EventHandler
	public void onCombust(EntityCombustEvent e)
	{
		if(e.getEntity().hasMetadata("heropvpnpc"))
		{
			e.setCancelled(true);
		}
	}
}