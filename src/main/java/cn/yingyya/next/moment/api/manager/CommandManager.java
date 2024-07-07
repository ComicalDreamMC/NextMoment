package cn.yingyya.next.moment.api.manager;

import cn.yingyya.next.moment.NextPlugin;
import cn.yingyya.next.moment.api.command.NextCommand;
import cn.yingyya.next.moment.api.command.NextCommandMiddleware;
import cn.yingyya.next.moment.utils.ReflexUtils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.SimpleCommandMap;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CommandManager <T extends NextPlugin<T>> extends Manager {

	private final T plugin;
	private final List<NextCommand<T>> commands;

	public CommandManager(T plugin) {
		this.plugin = plugin;
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
		plugin.getNextLogger().info("Sync server command finished.");
	}

	public void registerCommand(NextCommand<T> command) {
		if (commands.contains(command)) {
			plugin.getNextLogger().warn(String.format("%s command already load", command.getCommandName()));
			return;
		}
		plugin.getNextLogger().info(String.format("%s command load success", command.getCommandName()));
		commands.add(command);
	}

	@Override
	public void onLoad() {
		plugin.getNextLogger().warn(String.format("Start of registration commands, totaling %d.", commands.size()));
		for (NextCommand<T> command : commands) {
			NextCommandMiddleware<T> nextCommandMiddleware = new NextCommandMiddleware<>(command);
			getCommandMap().register(command.getCommandName(), nextCommandMiddleware);
		}
		this.syncServerCommand();
	}

	@Override
	public void onUnload() {}
}
