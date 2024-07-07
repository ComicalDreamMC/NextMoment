package cn.yingyya.next.moment.api.manager;

import cn.yingyya.next.moment.NextPlugin;
import cn.yingyya.next.moment.api.database.DataConnector;
import cn.yingyya.next.moment.api.database.DataBaseType;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;

public class DataBaseManager<T extends NextPlugin<T>> extends Manager {

	private final T plugin;
	private final HashMap<String, DataConnector<?>> dataSources = new HashMap<>();
	private String defaultDataSourceName;

	public DataBaseManager(T plugin) {
		this.plugin = plugin;
	}

	public boolean setDefaultData(@NotNull String key) {
		if (dataSources.containsKey(key)) {
			defaultDataSourceName = key;
			return true;
		}
		return false;
	}

	@Nullable
	public DataConnector<?> getDefaultDataSource() {
		if (defaultDataSourceName == null) {
			return null;
		}
		if (dataSources.containsKey(defaultDataSourceName)) {
			return dataSources.get(defaultDataSourceName);
		}
		return null;
	}

	public void addDataBase(@Nullable DataConnector<?> dataConnector) throws IllegalAccessException {
		if (dataConnector == null) {
			throw new IllegalArgumentException("dataConnector is null");
		}
		String name = dataConnector.name();
		if (dataSources.containsKey(name)) {
			throw new IllegalAccessException("Database duplication initiated.");
		}
		dataSources.put(name, dataConnector);
		if (defaultDataSourceName == null) defaultDataSourceName = name;
	}

	@Nullable
	public DataConnector<?> getDataConnector(@NotNull String name) {
		return dataSources.get(name);
	}

//	public boolean initDataBase(@NotNull String file) {
//		HikariConfig config = getHikariConfig(file);
//		try {
//			HikariDataSource dataSource = new HikariDataSource(config);
//			DataConnector dataConnector = new DataConnector(DataBaseType.SQLITE, dataSource);
//			dataSources.put(file, dataConnector);
//			// 第一个数据连接信息为默认数据源
//			if (defaultDataSourceName == null) {
//				defaultDataSourceName = file;
//			}
//			return true;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return false;
//	}
//
//	private @NotNull HikariConfig getHikariConfig(@NotNull String file) {
//		File folder = plugin.getDataFolder();
//		File datafile = new File(folder, file);
//		if (!datafile.exists()) {
//			datafile.getParentFile().mkdirs();
//		}
//		HikariConfig config = new HikariConfig();
//		config.setDriverClassName("org.sqlite.JDBC");
//		config.setJdbcUrl("jdbc:sqlite:" + datafile.getPath());
//		config.addDataSourceProperty("cachePrepStmts", "true");
//		config.addDataSourceProperty("prepStmtCacheSize", "250");
//		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
//		config.setMaximumPoolSize(1); // sqlite 需要为 1
//		return config;
//	}

	@Override
	public void onLoad() {}

	@Override
	public void onUnload() {
		// 关闭数据库连接
		dataSources.forEach((key, value) -> {
			if (value.dataSource() instanceof HikariDataSource hikariDataSource) {
				if (hikariDataSource.isRunning()) hikariDataSource.close();
			}
		});
	}
}
