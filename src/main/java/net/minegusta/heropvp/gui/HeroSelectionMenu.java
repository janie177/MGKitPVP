package net.minegusta.heropvp.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minegusta.heropvp.classes.Hero;
import net.minegusta.heropvp.main.Main;
import net.minegusta.heropvp.saving.MGPlayer;
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

public class HeroSelectionMenu extends InventoryGUI {

	private static Task cleanTask;
	private static ConcurrentMap<String, Map<Integer, Hero>> selectionLayout = Maps.newConcurrentMap();

	public HeroSelectionMenu(String name, int rows, String key) {
		super(name, rows, key);
		cleanTask = new Task();
		cleanTask.start(Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), ()->
		{
			for(String s : selectionLayout.keySet())
			{
				try
				{
					if(!Bukkit.getPlayer(UUID.fromString(s)).isOnline())
					{
						selectionLayout.remove(s);
					}
				} catch (Exception ignored){
					selectionLayout.remove(s);
				}
			}
		}, 20 * 60, 20 * 60));
	}

	@Override
	public Inventory buildInventory(Player player, int i, InventoryHolder inventoryHolder, String s) {
		MGPlayer mgp = Main.getSaveManager().getMGPlayer(player);
		Inventory inv = Bukkit.createInventory(inventoryHolder, i , ChatColor.GOLD + "Choose your " + ChatColor.BOLD + "" + ChatColor.LIGHT_PURPLE + "Hero");
		Map<Integer, Hero> indexes = Maps.newHashMap();
		int index = 0;

		for(Hero h : mgp.getUnlockedHeroes())
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
					meta.setLore(lore);
					setItemMeta(meta);
				}
			});
			indexes.put(index, h);
			index++;
		}
		selectionLayout.put(player.getUniqueId().toString(), indexes);


		return inv;
	}

	@Override
	public void processClick(Player player, int i, InventoryClickEvent inventoryClickEvent) {
		Map<Integer, Hero> indexes = selectionLayout.get(player.getUniqueId().toString());

		if(indexes.containsKey(i))
		{
			Hero h = indexes.get(i);
			MGPlayer mgp = Main.getSaveManager().getMGPlayer(player);
			mgp.setActiveHero(h);
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
