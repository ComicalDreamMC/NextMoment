package cn.yingyya.next.moment;

import cn.yingyya.next.moment.api.config.ConfigOption;
import cn.yingyya.next.moment.api.manager.CommandManager;
import cn.yingyya.next.moment.api.manager.DataBaseManager;
import cn.yingyya.next.moment.api.manager.EventManager;
import cn.yingyya.next.moment.api.manager.PermissionManager;
import cn.yingyya.next.moment.utils.ReflexUtils;
import cn.yingyya.next.moment.utils.collections.Pair;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public abstract class NextPlugin<T extends NextPlugin<T>> extends JavaPlugin {

	public abstract void onNextLoad();

	public abstract void onNextUnload();

	@NotNull
	protected abstract T getPluginInstance();

	private EventManager<T> eventManager;
	private CommandManager<T> commandManager;
	private PermissionManager<T> permissionManager;
	private DataBaseManager<T> dataBaseManager;
	private final LinkedHashMap<Class<?>, Pair<File, YamlConfiguration>> configMap = new LinkedHashMap<>();
	private static final HashMap<Class<?>, NextPlugin<?>> pluginMap = new HashMap<>();

	@Override
	public void onEnable() {
		dataBaseManager = new DataBaseManager<>(getPluginInstance());
		permissionManager = new PermissionManager<>(getPluginInstance());
		eventManager = new EventManager<>(getPluginInstance());
		commandManager = new CommandManager<>(getPluginInstance());

		onNextLoad();
		pluginMap.put(getPluginInstance().getClass(), getPluginInstance());

		// manager load
		this.dataBaseManager.onLoad();
		this.permissionManager.onLoad();
		this.eventManager.onLoad();
		this.commandManager.onLoad();
	}

	@Override
	public void onDisable() {
		onNextUnload();
		pluginMap.remove(getPluginInstance().getClass());

		// manager unload
		this.eventManager.onUnload();
		this.commandManager.onUnload();
		this.permissionManager.onUnload();

		this.dataBaseManager.onUnload();
	}

	@Nullable
	public static <T extends NextPlugin<T>> NextPlugin<T> getNextPlugin(Class<T> clazz) {
		if (clazz == null) {
			return null;
		}
		if (!pluginMap.containsKey(clazz)) {
			return null;
		}
		return (NextPlugin<T>) pluginMap.get(clazz);
	}

	public void loadNextConfig(@NotNull Class<?> clazz) {
		loadNextConfig(clazz, "config.yml");
	}

	public void loadNextConfig(@NotNull Class<?> clazz, @NotNull String configPath) {
		File path = new File(getDataFolder(), configPath);
		if (configPath.equals("config.yml")) {
			super.saveDefaultConfig();
			super.reloadConfig();
		} else {
			if (!path.exists()) {
				path.getParentFile().mkdirs();
				try {
					path.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			YamlConfiguration configuration = YamlConfiguration.loadConfiguration(path);
			configuration.load(path);

			// 添加配置文件列表
			configMap.put(clazz, Pair.create(path, configuration));

			injectionConfigOfObject(clazz, configuration);

		} catch (IOException | InvalidConfigurationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private void injectionConfigOfObject(@NotNull Class<?> clazz, @NotNull YamlConfiguration configuration) throws IllegalAccessException {
		List<Field> fields = ReflexUtils.getFields(clazz).stream().filter(v -> v.canAccess(null) && v.getType() == ConfigOption.class).toList();
		for (Field field : fields) {
			Object o1 = field.get(null);
			if (o1 instanceof ConfigOption<?> option) {
				option.setConfiguration(configuration);
			}
		}
	}

	public void reloadNextConfig(Class<?> clazz) {
		Pair<File, YamlConfiguration> pair = getConfig(clazz);
		File file = pair.getLeft();
		YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

		configMap.put(clazz, Pair.create(file, configuration));
		try {
			injectionConfigOfObject(clazz, configuration);
			getLogger().info("reloaded configuration for " + clazz.getName());
		} catch (IllegalAccessException e) {
			getLogger().info("failed to reload configuration for " + clazz.getName());
			e.printStackTrace();
		}
	}

	public void saveNextConfig(Class<?> clazz) {
		Pair<File, YamlConfiguration> config = getConfig(clazz);
		File file = config.getLeft();
		YamlConfiguration configuration = config.getRight();

		try {
			configuration.save(file);
		} catch (IOException e) {
			getLogger().info("Save config failed in file " + file.getAbsolutePath());
			e.printStackTrace();
		}
	}

	private Pair<File, YamlConfiguration> getConfig(Class<?> clazz) {
		for (Class<?> key : configMap.keySet()) {
			if (key == clazz) {
				return configMap.get(key);
			}
		}
		throw new IllegalStateException("No config found for " + clazz.getName());
	}

	public EventManager<T> getEventManager() {
		return eventManager;
	}

	public PluginManager getPluginManager() {
		return this.getServer().getPluginManager();
	}

	public CommandManager<T> getCommandManager() {
		return commandManager;
	}

	public PermissionManager<T> getPermissionManager() {
		return permissionManager;
	}

	public DataBaseManager<T> getDataBaseManager() {
		return dataBaseManager;
	}

	public BukkitScheduler getScheduler() {
		return getServer().getScheduler();
	}
}
