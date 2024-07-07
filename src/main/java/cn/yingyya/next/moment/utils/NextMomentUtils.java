package cn.yingyya.next.moment.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

public class NextMomentUtils {

	public static void printToCommandSender(@NotNull CommandSender sender, @NotNull Plugin plugin) {
		PluginDescriptionFile description = plugin.getDescription();
		sender.sendMessage("Description: " + description.getDescription());
		sender.sendMessage("Author: " + description.getAuthors());
		sender.sendMessage("Version: " + description.getVersion());
		sender.sendMessage("Website: " + description.getWebsite());
		sender.sendMessage("Libraries: " + description.getLibraries());
		sender.sendMessage("APIVersion: " + description.getAPIVersion());
	}
}
