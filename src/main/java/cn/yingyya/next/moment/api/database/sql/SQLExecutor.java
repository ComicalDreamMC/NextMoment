package cn.yingyya.next.moment.api.database.sql;

import cn.yingyya.next.moment.api.database.DataConnector;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;

public abstract class SQLExecutor<T> {

	protected static final String SELECT      = "SELECT";
	protected static final String FROM        = "FROM";
	protected static final String UPDATE      = "UPDATE";
	protected static final String SET         = "SET";
	protected static final String WHERE       = "WHERE";
	protected static final String INSERT_INTO = "INSERT INTO";
	protected static final String VALUES      = "VALUES";

	protected final String table;

	protected SQLExecutor(@NotNull String table) {
		this.table = table;
	}

	@NotNull
	public String getTable() {
		return "`" + this.table + "`";
	}

	@NotNull
	public abstract T execute(@NotNull DataConnector<HikariDataSource> connector);
}