package me.skymc.banitem.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.entity.*;

import me.skymc.banitem.BanItems;

/**
 * @author sky
 * @since 2018��2��4�� ����11:18:03
 */
public class ListenerPickupItem implements Listener {
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void click(PlayerPickupItemEvent e) {
		if (e.isCancelled() && e.getPlayer().isOp()) {
			return;
		}
		if (BanItems.isBanned(e.getItem().getItemStack())) {
			e.getItem().remove();
			e.getPlayer().sendMessage(BanItems.getConf().getString("message"));
		}
	}

}
