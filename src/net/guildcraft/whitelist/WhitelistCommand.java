package net.guildcraft.whitelist;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class WhitelistCommand implements CommandExecutor {
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

		CommandSender p = commandSender;
		if (!p.hasPermission("gcwhitelist.whitelist")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&',
					Main.getInstance().getConfig().getString("no-permission")));
			return true;
		}
		if (strings.length == 0) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
			p.sendMessage(
					ChatColor.translateAlternateColorCodes('&', "&f[&bGCWhitelist&f] &8&m--- &7Help &7(1/1) &8&m---"));
			p.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&8&l ┌ &breload &8- &7Reloads the config & whitelist file"));
			p.sendMessage(
					ChatColor.translateAlternateColorCodes('&', "&8&l ├ &blist &8- &7Lists all whitelisted users"));
			p.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&8&l ├ &badd/remove <player> &8- &7Manage users for the whitelist"));
			p.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&8&l ├ &bon/off &8- &7Enable or Disable the whitelist"));
			p.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&8&l └ &bclear &8- &7Clears all players from the whitelist."));
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));

		}
		if (strings.length == 1) {
			String type = strings[0];

			switch (type.toLowerCase()) {
			case ("reload"):
				Main.getInstance().reloadWhitelist();
				Main.getInstance().reloadConfig();
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',
						Main.getInstance().getConfig().getString("whitelist-reloaded")));
				return true;

			case ("add"):
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /whitelist add <player>"));
				return true;

			case ("remove"):
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /whitelist remove <player>"));
				return true;
				
			case ("on"):
				if (Lists.getLists().whitelist == true) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("already-enabled")));
					return true;
				}
				if (Lists.getLists().whitelist == false) {
					Main.getInstance().getConfig().set("whitelist-status", true);
					Main.getInstance().saveConfig();
					Lists.getLists().whitelist = true;
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("whitelist-enabled")));
					
					Bukkit.getOnlinePlayers().forEach(entry -> {
						if (!entry.getName().equals(p.getName())) {
						if (entry.hasPermission("gcwhitelist.whitelist")) {
							entry.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("enabled-by")).replace("%player%", p.getName()));
							}
						}
					});
					return true;
				}
				
			case ("off"):
				if (Lists.getLists().whitelist == false) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("already-disabled")));
					return true;
				}
				if (Lists.getLists().whitelist == true) {
					Main.getInstance().getConfig().set("whitelist-status", false);
					Main.getInstance().saveConfig();
					Lists.getLists().whitelist = false;
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("whitelist-disabled")));
					
					Bukkit.getOnlinePlayers().forEach(entry -> {
						if (!entry.getName().equals(p.getName())) {
						if (entry.hasPermission("gcwhitelist.whitelist")) {
							entry.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("disabled-by")).replace("%player%", p.getName()));
							}
						}
					});
					return true;
				}
				
			case ("clear"):
			case ("reset"):
			case ("empty"):
					Main.getInstance().clearWhitelist();
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("whitelist-cleared")));
					return true;
					
			case ("list"):
				String playerNames = Lists.getLists().players.toString().replace("[", "").replace("]", "");
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("whitelist-amount")
						.replace("%whitelisted%", Lists.getLists().players.size()+"").replace("%size%", Bukkit.getOfflinePlayers().length+"")));
				if (Lists.getLists().players.size() == 0) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("whitelist-list-none").replace("%players%", playerNames)));
				}
				if (Lists.getLists().players.size() > 0) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("whitelist-list").replace("%players%", playerNames)));
				}
				return true;

			default:
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&f[&bGCWhitelist&f] &8&m--- &7Help &7(1/1) &8&m---"));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&8&l ┌ &breload &8- &7Reloads the config & whitelist file"));
				p.sendMessage(
						ChatColor.translateAlternateColorCodes('&', "&8&l ├ &blist &8- &7Lists all whitelisted users"));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&8&l ├ &badd/remove <player> &8- &7Manage users for the whitelist"));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&8&l ├ &bon/off &8- &7Enable or Disable the whitelist"));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&8&l └ &bclear &8- &7Clears all players from the whitelist."));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
				return true;
			}
		}

		if (strings.length > 1) {
			String type = strings[0];
			String target = strings[1];

			switch (type.toLowerCase()) {
			case ("add"):

				if (Lists.getLists().players.contains(target)) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig()
							.getString("already-whitelisted").replace("%player%", target + "")));
					return true;

				}
				if (!Lists.getLists().players.contains(target)) {
					Lists.getLists().players.add(target);
					Main.getInstance().getWhitelist().set(target + ".is-whitelisted", true);
					Main.getInstance().saveWhitelist();
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig()
							.getString("player-whitelisted").replace("%player%", target + "")));
					return true;
				}
					
				case ("remove"):

					if (!Lists.getLists().players.contains(target)) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig()
								.getString("not-whitelisted").replace("%player%", target + "")));
						return true;
					}
					if (Lists.getLists().players.contains(target)) {
						Lists.getLists().players.remove(target);
						Main.getInstance().getWhitelist().set(target, null);
						Main.getInstance().saveWhitelist();
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig()
								.getString("player-unwhitelisted").replace("%player%", target + "")));
						return true;
					
		 

				}
				return true;

			}
		}
		return true;
	}
}
