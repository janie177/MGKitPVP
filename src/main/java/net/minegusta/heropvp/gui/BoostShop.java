package net.minegusta.heropvp.gui;

import com.google.common.collect.Lists;
import net.minegusta.heropvp.boosts.Boost;
import net.minegusta.heropvp.main.Main;
import net.minegusta.heropvp.saving.MGPlayer;
import net.minegusta.heropvp.utils.DisplayMessageUtil;
import net.minegusta.mglib.gui.InventoryGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Optional;

public class BoostShop extends InventoryGUI {

	public BoostShop(String name, int rows, String key) {
		super(name, rows, key);
	}

	@Override
	public Inventory buildInventory(Player player, int i, InventoryHolder inventoryHolder, String s) {

		MGPlayer mgp = Main.getSaveManager().getMGPlayer(player);
		Inventory inv = Bukkit.createInventory(inventoryHolder, i, ChatColor.LIGHT_PURPLE + "Tickets: " + ChatColor.YELLOW + mgp.getTickets());

		for(Boost boost : Boost.values())
		{
			if(mgp.hasBoost(boost))
			{
				long left = mgp.getMinutesLeft(boost);
				inv.setItem(boost.getSlot(), new ItemStack(Material.WATCH, (int) (left > 64 ? 64 : left), boost.getData())
				{
					{
						ItemMeta meta = getItemMeta();
						meta.setDisplayName(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + boost.getName());
						List<String> lore = Lists.newArrayList(ChatColor.YELLOW + "" + ChatColor.BOLD + "ACTIVE", ChatColor.GREEN + boost.getDescription(), ChatColor.GRAY + "Time Left: " + ChatColor.RED + left + ChatColor.GRAY + " Minutes");
						meta.setLore(lore);
						setItemMeta(meta);
					}
				});
			}
			else
			{
				inv.setItem(boost.getSlot(), new ItemStack(boost.getMaterial(), 1, boost.getData())
				{
					{
						ItemMeta meta = getItemMeta();
						meta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + boost.getName());
						List<String> lore = Lists.newArrayList(ChatColor.LIGHT_PURPLE + boost.getDescription(), ChatColor.GRAY + "Duration: " + ChatColor.RED + boost.getDuration() + ChatColor.GRAY + " Minutes", ChatColor.YELLOW + "" + ChatColor.BOLD + "Cost: " + (boost.getCost() > mgp.getTickets() ? ChatColor.RED : ChatColor.GREEN) + boost.getCost() + ChatColor.YELLOW + " Tickets.");
						meta.setLore(lore);
						setItemMeta(meta);
					}
				});
			}
		}
		return inv;
	}

	@Override
	public void processClick(Player player, int i, InventoryClickEvent inventoryClickEvent) {
		Optional<Boost> oBoost = Boost.getForSlot(i);
		if (oBoost.isPresent()) {
			Boost boost = oBoost.get();
			MGPlayer mgp = Main.getSaveManager().getMGPlayer(player);

			if(mgp.hasBoost(boost))
			{
				return;
			}
			else
			{
				int cost = boost.getCost();
				int tickets = mgp.getTickets();

				if(cost > tickets)
				{
					player.sendMessage(ChatColor.RED + "You do not have enough tickets.");
				}
				else
				{
					mgp.removeTickets(cost, 60);
					DisplayMessageUtil.unlockBoost(player, boost);
					mgp.addboostSeconds(boost, boost.getDuration() * 60);
					player.closeInventory();
				}

			}
		}


	}

	@Override
	public void animate(Inventory inventory) {

	}

	@Override
	public int getAnimationInterval() {
		return 0;
	}
}
