package cn.yingyya.next.moment.api.manager;

import cn.yingyya.next.moment.NextPlugin;
import cn.yingyya.next.moment.api.command.NextCommand;
import cn.yingyya.next.moment.api.command.NextCommandMiddleware;
import cn.yingyya.next.moment.utils.ReflexUtils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.SimpleCommandMap;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CommandManager <T extends NextPlugin<T>> extends Manager<T> {

	private final List<NextCommand<T>> commands;

	public CommandManager(@NotNull T plugin) {
		super(plugin);
		this.commands = new ArrayList<>();

	}

	public SimpleCommandMap getCommandMap() {
		return (SimpleCommandMap) ReflexUtils.getFieldValue(Bukkit.getServer(), "commandMap");
	}

	public void syncServerCommand() {
		Server server = Bukkit.getServer();
		Method syncCommands = ReflexUtils.getMethod(server.getClass(), "syncCommands");
		if (syncCommands == null) {
			return;
		}
		ReflexUtils.invokeMethod(syncCommands, server);
		getPlugin().getLogger().info("Sync server command finished.");
	}

	public void registerCommand(NextCommand<T> command) {
		if (commands.contains(command)) {
			getPlugin().getLogger().warning(String.format("%s command already load", command.getCommandName()));
			return;
		}
		getPlugin().getLogger().info(String.format("%s command load success", command.getCommandName()));
		commands.add(command);
	}

	@Override
	public void onLoad() {
		getPlugin().getLogger().warning(String.format("Start of registration commands, totaling %d.", commands.size()));
		for (NextCommand<T> command : commands) {
			NextCommandMiddleware<T> nextCommandMiddleware = new NextCommandMiddleware<>(command);
			getCommandMap().register(command.getCommandName(), nextCommandMiddleware);
		}
		this.syncServerCommand();
	}

	@Override
	public void onUnload() {}
}
