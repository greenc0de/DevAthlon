package de.greencode.desperadopvp;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Event implements Listener {
	
	public static int[] ids = {2259,2258,2256,2260,2257,2265,2267};
	public static ArrayList<Material> gun_reload = new ArrayList<>();
	public Event() {
		for (int id:ids) {
			gun_reload.add(Material.getMaterial(id));
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		p.setTexturePack("http://greencodesite.tk/WesternPVP.zip");
		
		p.teleport(Bukkit.getWorld("World").getSpawnLocation());
		p.getInventory().clear();
		p.getInventory().setItem(0, Main.getItemstack(Material.BOW, "§rMuskete"));
		p.getInventory().setItem(8, Main.getItemstack(Material.ARROW, "§rMusketenkugel"));
		p.getInventory().setItem(7, Main.getItemstack(Material.COOKED_BEEF, "§rSteak"));
	}
	
	
	
	@EventHandler
	public void onTreibsand(ItemSpawnEvent e) {
		if (e.getEntity().getItemStack().getType() == Material.SAND) {
			e.setCancelled(true);
			
			FallingBlock fb = e.getEntity().getWorld().spawnFallingBlock(e.getEntity().getLocation(), Material.SAND, (byte) 0);
			fb.teleport(e.getEntity().getLocation().add(0, 0.05, 0));
		}
	}
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		
		if (e.getTo().clone().subtract(0, 0.5, 0).getBlock().getType() == Material.getMaterial(44))
			p.setVelocity(new Vector(0, 0.1, 0));
		
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getItem() != null) {
				if (gun_reload.contains(e.getItem().getType()))
				Main.mouseDown.put(e.getPlayer(), Main.time);
			}
		}
	}
	@EventHandler
	public void onShoooot(ProjectileLaunchEvent e) {
		if (e.getEntity() instanceof Arrow) {
			if (e.getEntity().getShooter() instanceof Player) {
				Player p = (Player)e.getEntity().getShooter();
				if (p.getGameMode() != GameMode.CREATIVE)
					p.setItemInHand(new ItemStack(ids[0]));
				
				Location looc = p.getLocation();
				looc.setPitch(looc.getPitch()-10);
				p.teleport(looc);
				
				e.getEntity().getWorld().playEffect(e.getEntity().getLocation(), Effect.SMOKE, 3);
				e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.EXPLODE, 3, 3);
				
				Vector vec = e.getEntity().getVelocity();
				vec.setX(vec.getX()/2);
				vec.setY(vec.getY()/2);
				vec.setZ(vec.getZ()/2);
				
				Location loc = e.getEntity().getLocation();
				Location loc1 = e.getEntity().getLocation();
				while (loc.distance(loc1) < 100) {
					loc.add(vec);
					if (loc.getBlock().getType() != Material.AIR)
						break;
					loc.getWorld().playEffect(loc, Effect.SMOKE, 3);
					loc.getWorld().playSound(loc, Sound.EXPLODE, 3, 3);
					for (Entity ent:p.getNearbyEntities(50, 50, 50)) {
						if (ent.getLocation().distance(loc) <= 1 || ent.getLocation().add(0, 1, 0).distance(loc) <= 1) {
							p.playSound(loc1, Sound.SUCCESSFUL_HIT, 30, 30);
							if (ent instanceof Player)
								((Player)ent).damage(7);
							else if (ent instanceof Creature)
								((Creature)ent).damage(7);
							
						}
					}
				}
				
				e.getEntity().remove();
			}
			
		}
	}
	
}
