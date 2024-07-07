package cn.yingyya.next.moment.api.command;

import cn.yingyya.next.moment.NextPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class NextCommandMiddleware<T extends NextPlugin<T>> extends Command {

	private final NextCommand<T> command;

	public NextCommandMiddleware(@NotNull NextCommand<T> command) {
		super(command.getCommandName(), command.getDescription(), command.getUsage(), command.getAliases());
		this.command = command;
	}

	public NextCommand<T> getMatchesCommand(NextCommand<T> command, String[] args) {
		NextCommand<T> cmd = command;
		boolean retNull = false;
		for (String arg : args) {
			if (cmd.isRootCommand() || cmd.getChildren() == null) {
				return cmd;
			}
			Optional<NextCommand<T>> optional = cmd.getChildren().stream().filter(v -> arg.equals(v.getCommandName())).findFirst();
			if (optional.isPresent()) {
				// 有匹配的子命令
				cmd = optional.get();
				retNull = false;
			} else retNull = true;
		}
		return retNull ? null : cmd;
	}

	@Override
	public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] strings) {
		String[] newStrings = new String[strings.length + 1];
		newStrings[0] = s;
		System.arraycopy(strings, 0, newStrings, 1, strings.length);
		NextCommand<T> executeCommand = command.isRootCommand() ? command : getMatchesCommand(command, newStrings);
		if (executeCommand == null) {
			sender.sendMessage(command.getUsage());
			return true;
		}
		Permission permission = executeCommand.getPermission(strings);
		if (permission != null && !sender.hasPermission(permission)) {
			sender.sendMessage("You do not have permission to execute this command.");
			return true;
		}
		executeCommand.onExecute(sender, strings);
		return true;
	}

	@NotNull
	@Override
	public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
		if (command.isRootCommand()) {
			return command.onTabComplete(sender, args);
		}
		NextCommand<T> matchesCommand = getMatchesCommand(command, args);
		if (matchesCommand == null) {
			List<String> strings = command.onTabComplete(sender, args);
			if (strings.isEmpty()) {
				return Collections.emptyList();
			}
			return strings;
		}
		Set<NextCommand<T>> children = matchesCommand.getChildren();
		List<String> strings = matchesCommand.onTabComplete(sender, args);
		if (!strings.isEmpty()) {
			return strings;
		}
		if (children != null) {
			// 自动补全
			// 判断是否有该命令的权限 然后在返回tab
			return children.stream().filter(v -> {
				// 权限验证
				Permission permission = v.getPermission(args);
				return permission == null || sender.hasPermission(permission);
			}).map(NextCommand::getCommandName).toList();
		}
		return Collections.emptyList();
	}
}
