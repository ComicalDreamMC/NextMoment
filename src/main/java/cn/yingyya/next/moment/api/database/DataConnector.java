package cn.yingyya.next.moment.api.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.rocksdb.*;
import org.rocksdb.util.SizeUnit;

import java.io.File;

public record DataConnector<T>(String name, DataBaseType type, T dataSource) {

	public static final String SQLITE_DRIVER = "org.sqlite.JDBC";
	public static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";

	public DataConnector(@NotNull String name, @NotNull DataBaseType type, @NotNull T dataSource) {
		this.dataSource = dataSource;
		this.type = type;
		this.name = name;
	}

	public static DataConnector<?> ofSQL(@NotNull String name, @NotNull DataBaseType type, @NotNull String driver, @NotNull String url, @Nullable String username, @Nullable String password) {
		if (type == DataBaseType.LEVLEDB) {
			throw new IllegalArgumentException("LevelDB database type not supported of HikariDataSource.");
		}
		HikariConfig config = new HikariConfig();

		config.setDriverClassName(driver);
		config.setJdbcUrl(url);
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		config.setMaximumPoolSize(type == DataBaseType.SQLITE ? 1 : 10);
		if (username != null) config.setUsername(username);
		if (password != null) config.setPassword(password);

		return new DataConnector<>(name, type, new HikariDataSource(config));
	}

	@Nullable
	public static DataConnector<?> ofLevelDB(@NotNull String name, @NotNull File file) {
		if (file.isFile()) throw new IllegalArgumentException("LevelDB database type not supported of file, require directory.");
		if (!file.exists()) {
			file.getParentFile().mkdirs();
		}
		Options options = new Options().setCreateIfMissing(true)
				.setWriteBufferSize(10 * SizeUnit.MB)
				.setMaxWriteBufferNumber(3)
				.setMaxBackgroundJobs(10)
				.setCompressionType(CompressionType.SNAPPY_COMPRESSION)
				.setCompactionStyle(CompactionStyle.UNIVERSAL);
		try {
			RocksDB rocksDB = RocksDB.open(options, file.getAbsolutePath());
			return new DataConnector<>(name, DataBaseType.SQLITE, rocksDB);
		} catch (RocksDBException e) {
			e.printStackTrace();
			return null;
		}
	}
}
