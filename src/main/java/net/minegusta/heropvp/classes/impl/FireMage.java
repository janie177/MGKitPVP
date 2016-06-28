package net.minegusta.heropvp.classes.impl;

import com.google.common.collect.Sets;
import net.minegusta.heropvp.classes.IHero;
import net.minegusta.heropvp.inventories.HeroInventory;
import net.minegusta.heropvp.main.Main;
import net.minegusta.heropvp.saving.MGPlayer;
import net.minegusta.heropvp.spells.SpellUtil;
import net.minegusta.mglib.bossbars.BossBarUtil;
import net.minegusta.mglib.particles.ParticleEffect;
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

public class FireMage implements IHero {

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

		if(player.getInventory().getItemInMainHand().getType() == Material.BLAZE_ROD && CooldownUtil.isCooledDown("spellcast", player.getUniqueId().toString()) && CooldownUtil.isCooledDown("spelltargetsearch", player.getUniqueId().toString()))
		{
			Optional<Player> oTarget = SpellUtil.getTarget(player);

			if(oTarget.isPresent())
			{
				CooldownUtil.newCoolDown("spelltargetsearch", player.getUniqueId().toString(), 1);
				CooldownUtil.newCoolDown("spellcast", mgp.getUuid().toString(), 8);
				BossBarUtil.createSecondCountdown(ChatColor.RED + "Homing spell cast in:", BarColor.RED, BarStyle.SOLID, 8);
				SpellUtil.castFireSpell(player, oTarget.get(), mgp.isUltimateActive());
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
			SpellUtil.castNormalFireSpell(player, player.getTargetBlock(Sets.newHashSet(Material.AIR), 15).getLocation(), mgp.isUltimateActive());
			BossBarUtil.createSecondCountdown(ChatColor.DARK_RED + "Normal spell cast in:", BarColor.RED, BarStyle.SOLID, 2);
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
		return "FireMage";
	}

	private static String[] desc = new String[]{"A mage specialized in Fire.", "Shoots spells that burn you.", "Ultimate makes spells go faster.", "Spells have a 2 second cooldown."};

	@Override
	public String[] getDescription() {
		return desc;
	}

	@Override
	public String getTag() {
		return "[FireMage]";
	}

	private static HeroInventory inventory = new HeroInventory(){
		{
			//Helmet
			addItem(39, new ItemStack(Material.LEATHER_HELMET){
				{
					{
						LeatherArmorMeta meta = (LeatherArmorMeta) getItemMeta();
						meta.setColor(Color.ORANGE);
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
						meta.setColor(Color.ORANGE);
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
						meta.setColor(Color.ORANGE);
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
						meta.setColor(Color.ORANGE);
						meta.setDisplayName(ChatColor.WHITE + "Mage's Boots.");
						setItemMeta(meta);
						addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
					}
				}});
			//hand1
			addItem(0, new ItemStack(Material.BLAZE_ROD){
				{
					ItemMeta meta = getItemMeta();
					meta.setDisplayName(ChatColor.DARK_RED + "Homing Fire Wand");
					setItemMeta(meta);
					addUnsafeEnchantment(Enchantment.DURABILITY, 1);
				}
			});
			addItem(1, new ItemStack(Material.STICK){
				{
					ItemMeta meta = getItemMeta();
					meta.setDisplayName(ChatColor.DARK_RED + "Fire Wand");
					setItemMeta(meta);
					addUnsafeEnchantment(Enchantment.DURABILITY, 1);
				}
			});
			//hand2
			addItem(2, new ItemStack(Material.WOOD_SWORD){
				{
					addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
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
		return Material.BLAZE_ROD;
	}

	@Override
	public int getCost() {
		return 600;
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
