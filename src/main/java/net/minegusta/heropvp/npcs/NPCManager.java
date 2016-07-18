package net.minegusta.heropvp.npcs;

import net.minegusta.heropvp.main.Main;
import net.minegusta.mglib.configs.ConfigurationFileManager;
import net.minegusta.mglib.tasks.Task;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.metadata.FixedMetadataValue;

public class NPCManager {

	private static ConfigurationFileManager<NPCConfiguration> npcConfig;
	private static Task respawnNPCs = new Task();

	public static void initConfig()
	{
		npcConfig = new ConfigurationFileManager<>(Main.getPlugin(), NPCConfiguration.class, 180, "npcs");
		respawnNPCs.start(Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), NPCConfiguration::resetNPCS, 20 * 600, 20 * 600));
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
			case BOOST:
				spawnBoostNPC(location);
				break;
		}
	}

	public static void spawnShopNPC(Location location)
	{
		Villager shopNPC = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
		shopNPC.teleport(location);
		shopNPC.setCustomNameVisible(true);
		shopNPC.setRemoveWhenFarAway(false);
		shopNPC.setCustomName(NPCType.SHOP.getDisplayName());
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
		selectClassNPC.setCustomName(NPCType.SELECTOR.getDisplayName());
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

	public static void spawnBoostNPC(Location location)
	{
		Witch boostNPC = (Witch) location.getWorld().spawnEntity(location, EntityType.WITCH);
		boostNPC.setCustomNameVisible(true);
		boostNPC.setRemoveWhenFarAway(false);
		boostNPC.setCustomName(NPCType.BOOST.getDisplayName());
		boostNPC.getEquipment().setItemInMainHand(new ItemStack(Material.WATCH));
		boostNPC.getEquipment().setItemInOffHand(new ItemStack(Material.DIAMOND));
		boostNPC.setCollidable(false);
		boostNPC.setGravity(false);
		boostNPC.setInvulnerable(true);
		boostNPC.setAI(false);
		boostNPC.setMetadata("heropvpnpc", new FixedMetadataValue(Main.getPlugin(), true));
		boostNPC.setSilent(true);
	}

	public static void spawnGameStartNPC(Location location)
	{
		Skeleton startGameNPC = (Skeleton) location.getWorld().spawnEntity(location, EntityType.SKELETON);
		startGameNPC.setRemoveWhenFarAway(false);
		startGameNPC.setCustomNameVisible(true);
		startGameNPC.setCustomName(NPCType.SPAWN.getDisplayName());
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
