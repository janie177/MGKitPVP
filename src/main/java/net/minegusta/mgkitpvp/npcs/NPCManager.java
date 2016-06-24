package net.minegusta.mgkitpvp.npcs;

import net.minegusta.mgkitpvp.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.metadata.FixedMetadataValue;

public class NPCManager {

	public static void spawnShopNPC(Player player)
	{
		Villager shopNPC = (Villager) player.getWorld().spawnEntity(player.getLocation(), EntityType.VILLAGER);
		shopNPC.setCustomNameVisible(true);
		shopNPC.setRemoveWhenFarAway(false);
		shopNPC.setCustomName(ChatColor.GOLD + "" + ChatColor.BOLD + "" + "Hero Shop");
		shopNPC.setAdult();
		shopNPC.getEquipment().setItemInMainHand(new ItemStack(Material.BOOK));
		shopNPC.getEquipment().setItemInOffHand(new ItemStack(Material.FEATHER));
		shopNPC.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
		shopNPC.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
		shopNPC.setCollidable(false);
		shopNPC.setInvulnerable(true);
		shopNPC.setGravity(false);
		shopNPC.setAI(false);
		shopNPC.getLocation().setPitch(player.getLocation().getPitch());
		shopNPC.getLocation().setYaw(player.getLocation().getYaw());
		shopNPC.getLocation().setDirection(player.getLocation().getDirection());
		shopNPC.setMetadata("heropvpnpc", new FixedMetadataValue(Main.getPlugin(), true));
	}

	public static void spawnClassSelectorNPC(Player player)
	{
		Zombie selectClassNPC = (Zombie) player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);
		selectClassNPC.setCustomNameVisible(true);
		selectClassNPC.setRemoveWhenFarAway(false);
		selectClassNPC.setCustomName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "" + "Select Hero");
		selectClassNPC.setBaby(false);
		selectClassNPC.setVillagerProfession(null);
		selectClassNPC.getEquipment().setItemInMainHand(new ItemStack(Material.BOOK));
		selectClassNPC.getEquipment().setItemInOffHand(new ItemStack(Material.FEATHER));
		selectClassNPC.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
		selectClassNPC.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
		selectClassNPC.setCollidable(false);
		selectClassNPC.setGravity(false);
		selectClassNPC.setInvulnerable(true);
		selectClassNPC.setAI(false);
		selectClassNPC.getLocation().setPitch(player.getLocation().getPitch());
		selectClassNPC.getLocation().setYaw(player.getLocation().getYaw());
		selectClassNPC.getLocation().setDirection(player.getLocation().getDirection());
		selectClassNPC.setMetadata("heropvpnpc", new FixedMetadataValue(Main.getPlugin(), true));
	}

	public static void spawnGameStartNPC(Player player)
	{
		Skeleton startGameNPC = (Skeleton) player.getWorld().spawnEntity(player.getLocation(), EntityType.SKELETON);
		startGameNPC.setRemoveWhenFarAway(false);
		startGameNPC.setCustomNameVisible(true);
		startGameNPC.setCustomName(ChatColor.GREEN + "" + ChatColor.BOLD + "" + "Play Now!");
		startGameNPC.setSkeletonType(Skeleton.SkeletonType.NORMAL);
		startGameNPC.getEquipment().setItemInMainHand(new ItemStack(Material.IRON_SWORD));
		startGameNPC.getEquipment().setItemInOffHand(new ItemStack(Material.IRON_SWORD));
		startGameNPC.getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET)
		{
			{
				LeatherArmorMeta meta = (LeatherArmorMeta) getItemMeta();
				meta.setColor(Color.LIME);
			}
		});
		startGameNPC.setCollidable(false);
		startGameNPC.setInvulnerable(true);
		startGameNPC.setGravity(false);
		startGameNPC.setAI(false);
		startGameNPC.getLocation().setPitch(player.getLocation().getPitch());
		startGameNPC.getLocation().setYaw(player.getLocation().getYaw());
		startGameNPC.getLocation().setDirection(player.getLocation().getDirection());
		startGameNPC.setMetadata("heropvpnpc", new FixedMetadataValue(Main.getPlugin(), true));
	}
}
