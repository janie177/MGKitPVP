package net.minegusta.mgkitpvp.npcs;

import net.minegusta.mgkitpvp.main.Main;
import net.minegusta.mglib.configs.ConfigurationFileManager;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.metadata.FixedMetadataValue;

public class NPCManager {

	private static ConfigurationFileManager<NPCConfiguration> npcConfig;

	public static void initConfig()
	{
		npcConfig = new ConfigurationFileManager<>(Main.getPlugin(), NPCConfiguration.class, 180, "npcs");
	}

	public static void spawnNPCS()
	{
		NPCConfiguration.spawnNPCS();
	}

	public static void spawnNPC(NPCType type, Location location)
	{
		switch (type)
		{
			case SELECTOR:
				spawnClassSelectorNPC(location);
				break;
			case SPAWN:
				spawnGameStartNPC(location);
				break;
			case SHOP:
				spawnShopNPC(location);
				break;
		}
	}

	public static void spawnShopNPC(Location location)
	{
		Villager shopNPC = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
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
		shopNPC.setMetadata("heropvpnpc", new FixedMetadataValue(Main.getPlugin(), true));
		shopNPC.setSilent(true);
	}

	public static void spawnClassSelectorNPC(Location location)
	{
		Zombie selectClassNPC = (Zombie) location.getWorld().spawnEntity(location, EntityType.ZOMBIE);
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
		selectClassNPC.setMetadata("heropvpnpc", new FixedMetadataValue(Main.getPlugin(), true));
		selectClassNPC.setSilent(true);
	}

	public static void spawnGameStartNPC(Location location)
	{
		Skeleton startGameNPC = (Skeleton) location.getWorld().spawnEntity(location, EntityType.SKELETON);
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
		startGameNPC.setMetadata("heropvpnpc", new FixedMetadataValue(Main.getPlugin(), true));
		startGameNPC.setSilent(true);
	}

	public static ConfigurationFileManager<NPCConfiguration> getNpcConfig() {
		return npcConfig;
	}
}
