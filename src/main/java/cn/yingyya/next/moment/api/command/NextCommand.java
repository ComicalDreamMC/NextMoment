package cn.yingyya.next.moment.api.command;

import cn.yingyya.next.moment.NextPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public abstract class NextCommand<T extends NextPlugin<T>> {

	private final T plugin;

	@NotNull
	public abstract String getCommandName();

	@NotNull
	public abstract String getUsage();

	@NotNull
	public abstract String getDescription();

	@Nullable
	public abstract Set<NextCommand<T>> getChildren();

	@Nullable
	public abstract Permission getPermission(String[] args);

	@NotNull
	public abstract List<String> getAliases();

	@NotNull
	public abstract List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String[] args);

	public abstract void onExecute(@NotNull CommandSender sender, @NotNull String[] args);

	public boolean isRootCommand() {
		Set<NextCommand<T>> children = getChildren();
		return children == null || children.isEmpty();
	}

	public NextCommand(T plugin) {
		this.plugin = plugin;
	}

	public T getPlugin() {
		return plugin;
	}
}
