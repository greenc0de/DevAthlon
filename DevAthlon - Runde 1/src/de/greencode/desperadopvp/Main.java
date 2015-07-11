package de.greencode.desperadopvp;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Main extends JavaPlugin{
	
	public static int time = 0;
	
	public static HashMap<Player, Integer> mouseDown = new HashMap<>();
	
	public static ArrayList<FallingBlock> fbs = new ArrayList<>();
	
	@Override
	public void onEnable() {
		
		
		
		Bukkit.getPluginManager().registerEvents(new Event(), this);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			
			@Override
			public void run() {
				for (Player all:Bukkit.getOnlinePlayers()) {
					if (all.getLocation().subtract(0, 0.5, 0).getBlock().getType() == Material.getMaterial(44))
						if (all.isOnGround())
							all.damage(3);
				}
				
			}
		}, 20, 20);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			
			@Override
			public void run() {
				time++;
				for (Player all:Bukkit.getOnlinePlayers()) {
					if (all.getLocation().getBlock().getType() == Material.DEAD_BUSH) {
						all.damage(1);
					}
					/*Block block = all.getLocation().subtract(0, 1, 0).getBlock();
					if (block.getType() == Material.SAND && block.getData() == 1) {
						all.teleport(all.getLocation().subtract(0, 0.1, 0));
					}*/
				}
				
			}
		}, 5, 5);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			
			@Override
			public void run() {
				for (Player all:Bukkit.getOnlinePlayers()) {
					for (int i=0;i<9;i++) {
						if (all.getInventory().getItem(i) != null) {
							if (Event.gun_reload.contains(all.getInventory().getItem(i).getType())) {
								if (all.getInventory().getHeldItemSlot() == i) {
									if (isMouseDown(all)){
										if (all.getItemInHand().getType() == Material.getMaterial(Event.ids[Event.ids.length-1]))
											all.setItemInHand(getItemstack(Material.BOW, "§r§fMuskete"));
										else {
											all.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10, 4));
											all.setItemInHand(getItemstack(Material.getMaterial(Event.ids[get(all)+1]), "§r§fMuskete"));
										}
										
									}else
										all.setItemInHand(getItemstack(Material.getMaterial(Event.ids[0]), "§r§fMuskete"));
								}else
									all.getInventory().setItem(i,getItemstack(Material.getMaterial(Event.ids[0]), "§r§fMuskete"));
								
							}
							
						}
					}
					
				}
				
			}

			
		}, 5, 5);
	}
	
	private boolean isMouseDown(Player p) {
		if (mouseDown.containsKey(p))
			if (mouseDown.get(p) >= time-1)
				return true;
		return false;
	}
	private int get(Player all) {
		for (int i=0;i<Event.ids.length;i++) {
			if (Event.ids[i] == all.getItemInHand().getTypeId())
				return i;
		}
		return 0;
	}
	
	public static ItemStack getItemstack(Material mat, String name) {
		ItemStack item = new ItemStack(mat);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}
	
}
