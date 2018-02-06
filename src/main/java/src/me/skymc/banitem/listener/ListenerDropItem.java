package me.skymc.banitem.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.entity.*;

import me.skymc.banitem.BanItems;

/**
 * @author sky
 * @since 2018年2月4日 上午11:18:03
 */
public class ListenerDropItem implements Listener {
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void click(PlayerDropItemEvent e) {
		if (e.isCancelled() && e.getPlayer().isOp()) {
			return;
		}
		if (BanItems.isBanned(e.getItemDrop().getItemStack())) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(BanItems.getConf().getString("message"));
		}
	}

}
