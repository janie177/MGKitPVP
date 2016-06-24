package net.minegusta.mgkitpvp.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minegusta.mgkitpvp.classes.Hero;
import net.minegusta.mgkitpvp.main.Main;
import net.minegusta.mgkitpvp.saving.MGPlayer;
import net.minegusta.mglib.gui.InventoryGUI;
import net.minegusta.mglib.tasks.Task;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

public class TicketShop extends InventoryGUI {

	private static Task cleanTask;
	private static ConcurrentMap<String, Map<Integer, Hero>> shopLayout = Maps.newConcurrentMap();

	public TicketShop(String name, int rows, String key) {
		super(name, rows, key);
		cleanTask = new Task();
		cleanTask.start(Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), ()->
		{
			for(String s : shopLayout.keySet())
			{
				try
				{
					if(!Bukkit.getPlayer(UUID.fromString(s)).isOnline())
					{
						shopLayout.remove(s);
					}
				} catch (Exception ignored){
					shopLayout.remove(s);
				}
			}
		}, 20 * 60, 20 * 60));
	}



	@Override
	public Inventory buildInventory(Player player, int i, InventoryHolder inventoryHolder, String s) {

		MGPlayer mgp = Main.getSaveManager().getMGPlayer(player);
		Inventory inv = Bukkit.createInventory(inventoryHolder, i , ChatColor.YELLOW+ "Tickets: " + ChatColor.BOLD + "" + ChatColor.LIGHT_PURPLE + mgp.getTickets());
		Map<Integer, Hero> indexes = Maps.newHashMap();
		int index = 0;

		for(Hero h : Hero.values())
		{
			if(!mgp.hasHeroUnlocked(h))
			{
				inv.addItem(new ItemStack(h.getMaterial())
				{
					{
						ItemMeta meta = getItemMeta();
						meta.setDisplayName(h.getColor() + "" + ChatColor.BOLD + h.getName());
						List<String> lore = Lists.newArrayList();
						for(String s : h.getDescription())
						{
							lore.add(ChatColor.GRAY + s);
						}
						lore.add(ChatColor.BOLD + "" + ChatColor.YELLOW + "Cost: " + (mgp.getTickets() >= h.getCost() ? ChatColor.GREEN : ChatColor.RED) + h.getCost() + " Tickets.");
						meta.setLore(lore);
						setItemMeta(meta);
					}
				});
				indexes.put(index, h);
				index++;
			}
			shopLayout.put(player.getUniqueId().toString(), indexes);
		}

		return inv;
	}

	@Override
	public void processClick(Player player, int i, InventoryClickEvent inventoryClickEvent) {
		Map<Integer, Hero> indexes = shopLayout.get(player.getUniqueId().toString());

		if(indexes.containsKey(i))
		{
			Hero h = indexes.get(i);
			MGPlayer mgp = Main.getSaveManager().getMGPlayer(player);
			if(mgp.getTickets() >= h.getCost()){

				mgp.addHero(h);
				mgp.removeTickets(h.getCost(), 40);

				player.closeInventory();
				shopLayout.remove(player.getUniqueId().toString());
			}
			else
			{
				player.sendMessage(ChatColor.RED + "You do not have enough tickets to unlock this hero.");
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
