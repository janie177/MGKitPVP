package net.minegusta.heropvp.gui;

import net.minegusta.heropvp.main.Main;
import net.minegusta.heropvp.saving.MGPlayer;
import net.minegusta.mglib.gui.InventoryGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
public class BoostShop extends InventoryGUI {

	public BoostShop(String name, int rows, String key) {
		super(name, rows, key);
	}

	@Override
	public Inventory buildInventory(Player player, int i, InventoryHolder inventoryHolder, String s) {
		Inventory inv = Bukkit.createInventory(inventoryHolder, i, ChatColor.DARK_PURPLE + "Boost!");
		MGPlayer mgp = Main.getSaveManager().getMGPlayer(player);

		//TODO build the inventory, fill will all items in the Boost enum.
		//TODO Then check if the player has the boost active or not. Display all info on the item.
		//TODO Add item material in the boost enum.

		//Maybe lock the boost when already active?

		return inv;
	}

	@Override
	public void processClick(Player player, int i, InventoryClickEvent inventoryClickEvent) {

		//TODO Get boost for slot. Then apply boost with cost if the player has tickets.


	}

	@Override
	public void animate(Inventory inventory) {

	}

	@Override
	public int getAnimationInterval() {
		return 0;
	}
}
