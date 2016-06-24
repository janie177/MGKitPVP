package net.minegusta.mgkitpvp.inventories;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

public class HeroInventory {

	List<ItemStack> stacks;
	List<Integer> indices;

	public void addItem(int slot, ItemStack item)
	{
		indices.add(slot);
		stacks.add(item);
	}

	public void fillInventory(PlayerInventory inv)
	{
		//Clear the inventory
		inv.clear();

		if(stacks.isEmpty()) return;

		//Add the items.
		for(int i = 0; i < stacks.size(); i++)
		{
			inv.setItem(indices.get(i), stacks.get(i));
		}
	}
}
