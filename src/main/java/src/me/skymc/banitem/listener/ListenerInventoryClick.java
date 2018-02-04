package me.skymc.banitem.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.entity.*;

import me.skymc.banitem.BanItems;

/**
 * @author sky
 * @since 2018年2月4日 上午11:18:03
 */
public class ListenerInventoryClick implements Listener {
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void click(InventoryClickEvent e) {
		if (e.isCancelled() && e.getWhoClicked().isOp()) {
			return;
		}
		if (BanItems.isBanned(e.getCurrentItem())) {
			e.setCurrentItem(null);
			((Player) e.getWhoClicked()).sendMessage(BanItems.getConf().getString("message"));
		}
	}

}
