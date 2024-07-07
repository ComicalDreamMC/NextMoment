package cn.yingyya.next.moment.api.database.sql.executor;

import cn.yingyya.next.moment.api.database.*;
import cn.yingyya.next.moment.api.database.sql.*;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SelectQueryExecutor<T> extends SQLExecutor<List<T>> {

	private final List<SQLColumn> columns;
	private final List<SQLCondition> wheres;
	private final Function<ResultSet, T> dataFunction;
	private int amount;

	protected SelectQueryExecutor(@NotNull String table, @NotNull Function<ResultSet, T> dataFunction) {
		super(table);
		this.columns = new ArrayList<>();
		this.wheres = new ArrayList<>();
		this.dataFunction = dataFunction;
		this.amount = -1;
	}

	@Override
	public @NotNull List<T> execute(@NotNull DataConnector<HikariDataSource> connector) {
		if (this.columns.isEmpty()) return Collections.emptyList();

		String columns = this.columns.stream().map(SQLColumn::getNameEscaped).collect(Collectors.joining(","));
		String wheres = this.wheres.stream().map(where -> where.value().column().getNameEscaped() + " " + where.type().getOperator() + " ?")
				.collect(Collectors.joining(" AND "));
		String sql = "SELECT " + columns + " FROM " + this.getTable() + (wheres.isEmpty() ? "" : " WHERE " + wheres);
		List<String> where = this.wheres.stream().map(SQLCondition::value).map(SQLValue::value).toList();

		return SQLExecute.executeQuery(connector.dataSource(), sql, where, dataFunction, amount);
	}

	@NotNull
	public static <T> SelectQueryExecutor<T> builder(@NotNull String table, @NotNull Function<ResultSet, T> dataFunction) {
		return new SelectQueryExecutor<>(table, dataFunction);
	}

	@NotNull
	public SelectQueryExecutor<T> all() {
		return this.columns(new SQLColumn("*", SQLColumnType.STRING, -1));
	}

	@NotNull
	public SelectQueryExecutor<T> columns(@NotNull SQLColumn... columns) {
		return this.columns(Arrays.asList(columns));
	}

	@NotNull
	public SelectQueryExecutor<T> columns(@NotNull List<SQLColumn> columns) {
		if (columns.isEmpty()) return this.all();

		this.columns.clear();
		this.columns.addAll(columns);
		return this;
	}

	@NotNull
	public SelectQueryExecutor<T> where(@NotNull SQLCondition... wheres) {
		return this.where(Arrays.asList(wheres));
	}

	@NotNull
	public SelectQueryExecutor<T> where(@NotNull List<SQLCondition> wheres) {
		this.wheres.clear();
		this.wheres.addAll(wheres);
		return this;
	}

	@NotNull
	public SelectQueryExecutor<T> amount(int amount) {
		this.amount = amount;
		return this;
	}
}
