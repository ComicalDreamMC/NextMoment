package cn.yingyya.next.moment.command;

import cn.yingyya.next.moment.NextMoment;
import cn.yingyya.next.moment.api.command.NextCommand;
import cn.yingyya.next.moment.command.children.PluginsCommand;
import cn.yingyya.next.moment.command.children.VersionCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class NextMomentCommand extends NextCommand<NextMoment> {

	public NextMomentCommand(NextMoment plugin) {
		super(plugin);
	}

	@Override
	public @NotNull String getCommandName() {
		return "nextmoment";
	}

	@Override
	public @NotNull String getUsage() {
		return """
				/nextmoment plugins
				/nextmoment version
				""";
	}

	@Override
	public @NotNull String getDescription() {
		return "next moment command";
	}

	@Override
	public @Nullable Set<NextCommand<NextMoment>> getChildren() {
		return Set.of(new PluginsCommand(getPlugin()), new VersionCommand(getPlugin()));
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
		if (args.length == 1) {
			return List.of("plugins", "version");
		}
		return List.of();
	}

	@Override
	public void onExecute(@NotNull CommandSender sender, @NotNull String[] args) {
		sender.sendMessage(getUsage());
	}
}
