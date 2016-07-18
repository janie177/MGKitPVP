package net.minegusta.heropvp.classes.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minegusta.heropvp.classes.IHero;
import net.minegusta.heropvp.inventories.HeroInventory;
import net.minegusta.heropvp.main.Main;
import net.minegusta.heropvp.saving.MGPlayer;
import net.minegusta.mglib.utils.CooldownUtil;
import net.minegusta.mglib.utils.EffectUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class Explodo implements IHero {

	@Override
	public void doUltimate(Player player) {

	}

	@Override
	public void doUltimate(Event event) {

	}

	@Override
	public void doPassive(Event event) {

	}

	@Override
	public void doPassive(Player player) {

		if(player.getInventory().getItemInMainHand().getType() != Material.STICK) return;

		if(!CooldownUtil.isCooledDown("tntlaunch", player.getUniqueId().toString()))
		{
			EffectUtil.playSound(player, Sound.BLOCK_DISPENSER_FAIL);
			return;
		}

		MGPlayer mgp = Main.getSaveManager().getMGPlayer(player);

		Block target = player.getTargetBlock(Sets.newHashSet(Material.AIR), 20);
		Vector v = target.getLocation().toVector().subtract(player.getLocation().toVector());

		//Nuke
		if(mgp.isUltimateReady())
		{
			mgp.activateUltimate();
			TNTPrimed tnt = (TNTPrimed) player.getWorld().spawnEntity(player.getLocation().add(player.getLocation().getDirection().normalize()).add(0,1.3F,0), EntityType.PRIMED_TNT);
			tnt.setVelocity(v.normalize().multiply(1.8));
			tnt.setFuseTicks(28);
			tnt.setGlowing(true);
			tnt.setMetadata("nuke", new FixedMetadataValue(Main.getPlugin(), true));
			tnt.setMetadata("mgplayer", new FixedMetadataValue(Main.getPlugin(), player.getUniqueId().toString()));
		}
		//Normal tnt
		else
		{
			TNTPrimed tnt = (TNTPrimed) player.getWorld().spawnEntity(player.getLocation().add(player.getLocation().getDirection().normalize()).add(0,1.3F,0), EntityType.PRIMED_TNT);
			tnt.setVelocity(v.normalize().multiply(1.8));
			tnt.setGlowing(true);
			tnt.setFuseTicks(28);
			tnt.setMetadata("explodo", new FixedMetadataValue(Main.getPlugin(), true));
			tnt.setMetadata("mgplayer", new FixedMetadataValue(Main.getPlugin(), player.getUniqueId().toString()));
		}

		CooldownUtil.newCoolDown("tntlaunch", player.getUniqueId().toString(), 2);

	}

	@Override
	public void applyPermanentPassives(Player player) {

	}

	@Override
	public void onKill(Player player) {

	}

	@Override
	public void onSelect(Player player) {

	}

	@Override
	public int powerPerKill() {
		return 34;
	}

	@Override
	public int ultimateDuration() {
		return 5;
	}

	@Override
	public String getName() {
		return "Explodo";
	}

	private static String[] desc = new String[]{"Who doesn't like explosions?", "Fires TNT using a stick.", "Ultimate launches a nuke."};

	@Override
	public String[] getDescription() {
		return desc;
	}

	@Override
	public String getTag() {
		return "[Explodo]";
	}

	private static HeroInventory inventory = new HeroInventory(){
		{
			//Helmet
			addItem(39, new ItemStack(Material.CHAINMAIL_HELMET));
			//Chest
			addItem(38, new ItemStack(Material.LEATHER_CHESTPLATE));
			//Legs
			addItem(37, new ItemStack(Material.LEATHER_LEGGINGS));
			//Boots
			addItem(36, new ItemStack(Material.LEATHER_BOOTS));
			//hand1
			addItem(0, new ItemStack(Material.STICK)
			{
				{
					ItemMeta meta = getItemMeta();
					meta.setDisplayName(ChatColor.DARK_RED + "Explosion Stick");
					meta.setLore(Lists.newArrayList(ChatColor.RED + "Right click to fire TNT."));
					addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2);
					addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
					setItemMeta(meta);
				}
			});
		}
	};

	@Override
	public void applyInventory(PlayerInventory inv) {
		inventory.fillInventory(inv);
	}

	@Override
	public Material getMaterial() {
		return Material.TNT;
	}

	@Override
	public int getCost() {
		return 2500;
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.RED;
	}

	@Override
	public BarStyle getBarStyle() {
		return BarStyle.SEGMENTED_10;
	}

	@Override
	public BarColor getBarColor() {
		return BarColor.RED;
	}

	@Override
	public String ultimateReadyMessage() {
		return ChatColor.DARK_PURPLE + "Ultimate Ready!" + ChatColor.YELLOW + "" + ChatColor.BOLD + " Your next explosion is a nuke!";
	}

	@Override
	public String ultimateBarMessage() {
		return "You fired a nuke!";
	}
}
