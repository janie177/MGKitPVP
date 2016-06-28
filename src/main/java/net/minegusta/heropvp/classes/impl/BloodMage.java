package net.minegusta.heropvp.classes.impl;

import com.google.common.collect.Sets;
import net.minegusta.heropvp.classes.IHero;
import net.minegusta.heropvp.inventories.HeroInventory;
import net.minegusta.heropvp.main.Main;
import net.minegusta.heropvp.saving.MGPlayer;
import net.minegusta.heropvp.spells.SpellUtil;
import net.minegusta.mglib.bossbars.BossBarUtil;
import net.minegusta.mglib.utils.CooldownUtil;
import net.minegusta.mglib.utils.EffectUtil;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.Optional;

public class BloodMage implements IHero {

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
		MGPlayer mgp = Main.getSaveManager().getMGPlayer(player);

		if(player.getInventory().getItemInMainHand().getType() == Material.BONE && CooldownUtil.isCooledDown("spellcast", player.getUniqueId().toString()) && CooldownUtil.isCooledDown("spelltargetsearch", player.getUniqueId().toString()))
		{
			Optional<Player> oTarget = SpellUtil.getTarget(player);
			if(oTarget.isPresent())
			{
				CooldownUtil.newCoolDown("spelltargetsearch", player.getUniqueId().toString(), 1);
				CooldownUtil.newCoolDown("spellcast", mgp.getUuid().toString(), 8);
				BossBarUtil.createTimedBar(ChatColor.DARK_PURPLE + "Homing spell cast in:", BarColor.PURPLE, BarStyle.SOLID, 160, 2, 1.0/160.0 , true).addPlayer(player);
				SpellUtil.castBloodSpell(player, oTarget.get(), mgp.isUltimateActive());
			}
			else
			{
				EffectUtil.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_FAIL);
			}

		}
		else if(player.getInventory().getItemInMainHand().getType() == Material.STICK && CooldownUtil.isCooledDown("normalspellcast", player.getUniqueId().toString()) && CooldownUtil.isCooledDown("spelltargetsearch", player.getUniqueId().toString()))
		{
			CooldownUtil.newCoolDown("spelltargetsearch", player.getUniqueId().toString(), 1);
			CooldownUtil.newCoolDown("normalspellcast", mgp.getUuid().toString(), 2);
			SpellUtil.castNormalBloodSpell(player, player.getTargetBlock(Sets.newHashSet(Material.AIR), 15).getLocation(), mgp.isUltimateActive());
			BossBarUtil.createTimedBar(ChatColor.RED + "Normal spell cast in:", BarColor.RED, BarStyle.SOLID, 40, 2, 1.0/20.0 , true).addPlayer(player);
		}


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
		return 50;
	}

	@Override
	public int ultimateDuration() {
		return 15;
	}

	@Override
	public String getName() {
		return "BloodMage";
	}

	private static String[] desc = new String[]{"A mage specialized in Blood.", "Drains your opponents health.", "Ultimate makes spells go faster.", "Spells have a 4 second cooldown."};

	@Override
	public String[] getDescription() {
		return desc;
	}

	@Override
	public String getTag() {
		return "[BloodMage]";
	}

	private static HeroInventory inventory = new HeroInventory(){
		{
			//Helmet
			addItem(39, new ItemStack(Material.LEATHER_HELMET){
				{
					{
						LeatherArmorMeta meta = (LeatherArmorMeta) getItemMeta();
						meta.setColor(Color.RED);
						meta.setDisplayName(ChatColor.WHITE + "Mage's Hood.");
						setItemMeta(meta);
						addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
					}
				}});
			//Chest
			addItem(38, new ItemStack(Material.LEATHER_CHESTPLATE){
				{
					{
						LeatherArmorMeta meta = (LeatherArmorMeta) getItemMeta();
						meta.setColor(Color.RED);
						meta.setDisplayName(ChatColor.WHITE + "Mage's Robe.");
						setItemMeta(meta);
						addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
					}
				}});
			//Legs
			addItem(37, new ItemStack(Material.LEATHER_LEGGINGS){
				{
					{
						LeatherArmorMeta meta = (LeatherArmorMeta) getItemMeta();
						meta.setColor(Color.RED);
						meta.setDisplayName(ChatColor.WHITE + "Mage's Robe.");
						setItemMeta(meta);
						addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
					}
				}});
			//Boots
			addItem(36, new ItemStack(Material.LEATHER_BOOTS){
				{
					{
						LeatherArmorMeta meta = (LeatherArmorMeta) getItemMeta();
						meta.setColor(Color.RED);
						meta.setDisplayName(ChatColor.WHITE + "Mage's Boots.");
						setItemMeta(meta);
						addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
					}
				}});
			//hand1
			addItem(0, new ItemStack(Material.BONE){
				{
					ItemMeta meta = getItemMeta();
					meta.setDisplayName(ChatColor.DARK_RED + "Homing Blood Wand");
					setItemMeta(meta);
					addUnsafeEnchantment(Enchantment.DURABILITY, 1);
				}
			});
			//hand1
			addItem(1, new ItemStack(Material.STICK){
				{
					ItemMeta meta = getItemMeta();
					meta.setDisplayName(ChatColor.DARK_RED + "Blood Wand");
					setItemMeta(meta);
					addUnsafeEnchantment(Enchantment.DURABILITY, 1);
				}
			});
			//hand2
			addItem(2, new ItemStack(Material.WOOD_SWORD){
				{
					addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
					addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
				}
			});
			addItem(3, new ItemStack(Material.SPLASH_POTION, 1)
			{
				{
					PotionMeta meta = (PotionMeta) getItemMeta();
					meta.setBasePotionData(new PotionData(PotionType.INSTANT_DAMAGE, false, true));
					setItemMeta(meta);
				}
			});
			addItem(4, new ItemStack(Material.POTION, 1)
			{
				{
					PotionMeta meta = (PotionMeta) getItemMeta();
					meta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL, false, false));
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
		return Material.BONE;
	}

	@Override
	public int getCost() {
		return 750;
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.DARK_RED;
	}

	@Override
	public BarStyle getBarStyle() {
		return BarStyle.SOLID;
	}

	@Override
	public BarColor getBarColor() {
		return BarColor.RED;
	}

	@Override
	public String ultimateReadyMessage() {
		return ChatColor.GREEN + "Ultimate Ready!" + ChatColor.DARK_RED + "" + ChatColor.BOLD + " Crouch to activate!";
	}

	@Override
	public String ultimateBarMessage() {
		return "Your spells travel twice as fast!";
	}
}
