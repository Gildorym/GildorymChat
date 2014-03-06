package com.gildorymrp.chat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.gildorymrp.api.plugin.chat.Channel;

public class PlayerQuitListener implements Listener {
	
	private GildorymChat plugin;
	
	public PlayerQuitListener(GildorymChat plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		for (Channel channel : plugin.getChannels()) {
			if (channel.getListeners().contains(event.getPlayer())) {
				channel.removeListener(event.getPlayer());
			}
			if (channel.getSpeakers().contains(event.getPlayer())) {
				channel.removeSpeaker(event.getPlayer());
			}
		}
	}

}
