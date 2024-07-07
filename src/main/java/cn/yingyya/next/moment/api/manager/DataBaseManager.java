package cn.yingyya.next.moment.api.manager;

import cn.yingyya.next.moment.NextPlugin;
import cn.yingyya.next.moment.api.database.DataConnector;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class DataBaseManager<T extends NextPlugin<T>> extends Manager<T> {

	private final HashMap<String, DataConnector<?>> dataSources = new HashMap<>();
	private String defaultDataSourceName;

	public DataBaseManager(T plugin) {
		super(plugin);
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
