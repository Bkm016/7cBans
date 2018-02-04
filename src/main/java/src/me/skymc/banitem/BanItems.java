package me.skymc.banitem;

import java.io.File;

import javax.swing.text.html.HTML.Tag;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.skymc.banitem.listener.ListenerInteract;
import me.skymc.banitem.listener.ListenerInventoryClick;
import me.skymc.banitem.listener.ListenerPickupItem;
import me.skymc.taboolib.fileutils.ConfigUtils;
import me.skymc.taboolib.message.MsgUtils;
import net.minecraft.server.v1_7_R4.NBTTagCompound;

/**
 * 主类
 * 
 * @author sky
 * @since 2018年2月4日 上午10:55:59
 */
public class BanItems extends JavaPlugin implements Listener {
	
	@Getter
	private static Plugin inst;
	
	@Getter
	private static FileConfiguration conf;
	
	@Override
	public FileConfiguration getConfig() {
		return conf;
	}
	
	@Override
	public void reloadConfig() {
		File file = new File(getDataFolder(), "config.yml");
		if (!file.exists()) {
			saveResource("config.yml", true);
		}
		conf = ConfigUtils.load(this, file);
	}
	
	@Override
	public void onLoad() {
		inst = this;
		reloadConfig();
	}
	
	@Override
	public void onEnable() {
		if (conf.getBoolean("events.InventoryClick")) {
			Bukkit.getPluginManager().registerEvents(new ListenerInventoryClick(), this);
			MsgUtils.send("启用背包点击监听", this);
		}
		if (conf.getBoolean("events.PlayerInteract")) {
			Bukkit.getPluginManager().registerEvents(new ListenerInteract(), this);
			MsgUtils.send("启用玩家交互监听", this);
		}
		if (conf.getBoolean("events.PlayerPickupItem")) {
			Bukkit.getPluginManager().registerEvents(new ListenerPickupItem(), this);
			MsgUtils.send("启用物品捡拾监听", this);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.bukkit.plugin.java.JavaPlugin#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		reloadConfig();
		sender.sendMessage("reload ok");
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public static boolean isBanned(ItemStack item) {
		if (item == null || item.getType().equals(Material.AIR)) {
			return false;
		}
		for (String condition : conf.getStringList("condition")) {
			try {
				if (condition.startsWith("type")) {
					if (condition.split("=")[1].equals(item.getTypeId() + ":" + item.getDurability()) 
							|| condition.split("=")[1].equals(item.getTypeId() + ":*")) {
						return true;
					}
				}
				else {
					// 获取 NBT
					NBTTagCompound nbt = CraftItemStack.asNMSCopy(item).getTag();
					// 获取名称
					String key = condition.split("=")[1].split(":")[0];
					// 判断类型
					if (nbt.hasKey(key) && condition.startsWith("nbt[int]")) {
						// 获取 NBT 值
						Integer value = Integer.valueOf(condition.split("=")[1].split(":")[1]);
						// 判断是否为
						if (value.equals(nbt.getInt(key))) {
							return true;
						}
					}
					else if (nbt.hasKey(key) && condition.startsWith("nbt[text]")) {
						// 获取 NBT 值
						String value = condition.split("=")[1].split(":")[1];
						// 判断是否为
						if (value.equals(nbt.getString(key))) {
							return true;
						}
					}
				}
			}
			catch (Exception e) {
				//
			}
		}
		return false;
	}
}
