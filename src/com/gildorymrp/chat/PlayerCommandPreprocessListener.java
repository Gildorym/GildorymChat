package com.gildorymrp.chat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.gildorymrp.api.plugin.chat.Channel;

public class PlayerCommandPreprocessListener implements Listener {
	
	private GildorymChat plugin;
	
	public PlayerCommandPreprocessListener(GildorymChat plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		for (Channel channel : plugin.getChannels()) {
			for (String alias : ((ChannelImpl) channel).getCommand().getAliases()) {
				if (event.getMessage().startsWith("/" + alias + " ")) {
					String[] args = new String[event.getMessage().split(" ").length - 1];
					for (int i = 1; i < event.getMessage().split(" ").length; i++) {
						args[i - 1] = event.getMessage().split(" ")[i];
					}
					((ChannelImpl) channel).getCommand().execute(event.getPlayer(), alias, args);
					event.setCancelled(true);
				}
				if (event.getMessage().startsWith("/" + alias) && event.getMessage().length() - 1 == alias.length()) {
					((ChannelImpl) channel).getCommand().execute(event.getPlayer(), alias, new String[] {});
					event.setCancelled(true);
				}
			}
		}
	}

}
