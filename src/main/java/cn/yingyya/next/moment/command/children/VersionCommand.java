package cn.yingyya.next.moment.command.children;

import cn.yingyya.next.moment.NextMoment;
import cn.yingyya.next.moment.api.command.NextCommand;
import cn.yingyya.next.moment.utils.NextMomentUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class VersionCommand extends NextCommand<NextMoment> {

	public VersionCommand(NextMoment plugin) {
		super(plugin);
	}

	@Override
	public @NotNull String getCommandName() {
		return "version";
	}

	@Override
	public @NotNull String getUsage() {
		return "/nextmoment version";
	}

	@Override
	public @NotNull String getDescription() {
		return "show version of next moment plugin.";
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
			NextMomentUtils.printToCommandSender(sender, getPlugin());
		}
	}
}
