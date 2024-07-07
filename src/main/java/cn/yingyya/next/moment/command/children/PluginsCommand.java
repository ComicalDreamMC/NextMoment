package cn.yingyya.next.moment.command.children;

import cn.yingyya.next.moment.NextMoment;
import cn.yingyya.next.moment.api.command.NextCommand;
import cn.yingyya.next.moment.utils.NextMomentUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class PluginsCommand extends NextCommand<NextMoment> {

	public PluginsCommand(NextMoment plugin) {
		super(plugin);
	}

	@Override
	public @NotNull String getCommandName() {
		return "plugins";
	}

	@Override
	public @NotNull String getUsage() {
		return "/nextmoment plugins";
	}

	@Override
	public @NotNull String getDescription() {
		return "list all next moment plugins";
	}

	@Override
	public @Nullable Set<NextCommand<NextMoment>> getChildren() {
		return Set.of();
	}

	@Override
	public @Nullable Permission getPermission(String[] args) {
		return null;
	}

	@Override
	public @NotNull List<String> getAliases() {
		return List.of();
	}

	@Override
	public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String[] args) {
		return List.of();
	}

	@Override
	public void onExecute(@NotNull CommandSender sender, @NotNull String[] args) {
		if (args.length == 1) {
			List<Plugin> plugins = Arrays.stream(getPlugin().getPluginManager().getPlugins()).
					filter(plugin -> plugin.isEnabled() && plugin.getDescription().getDepend().contains(getPlugin().getName()))
					.toList();
			if (plugins.isEmpty()) {
				sender.sendMessage("No plugins found.");
				return;
			}
			for (Plugin plugin : plugins) {
				sender.sendMessage("=================");
				NextMomentUtils.printToCommandSender(sender, plugin);
			}
		}
	}
}
