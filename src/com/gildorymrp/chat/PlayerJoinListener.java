package com.gildorymrp.chat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.gildorymrp.api.plugin.chat.Channel;

public class PlayerJoinListener implements Listener {
	
	private GildorymChat plugin;
	
	public PlayerJoinListener(GildorymChat plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerJoin(PlayerJoinEvent event) {
		for (Channel channel : plugin.getChannels()) {
			if (event.getPlayer().hasPermission("gildorym.chat.ch.listen." + channel.getName().toLowerCase())) {
				channel.addListener(event.getPlayer());
			}
		}
		plugin.setPlayerChannel(event.getPlayer(), plugin.getChannel(plugin.getConfig().getString("default-channel").toLowerCase()));
	}

}
