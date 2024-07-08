package cn.yingyya.next.moment.api.database.sql.executor;

import cn.yingyya.next.moment.api.database.DataConnector;
import cn.yingyya.next.moment.api.database.sql.SQLCondition;
import cn.yingyya.next.moment.api.database.sql.SQLExecute;
import cn.yingyya.next.moment.api.database.sql.SQLExecutor;
import cn.yingyya.next.moment.api.database.sql.SQLValue;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateQueryExecutor extends SQLExecutor<Void> {

	private final List<SQLValue> values;
	private final List<SQLCondition> wheres;

	protected UpdateQueryExecutor(@NotNull String table) {
		super(table);
		this.values = new ArrayList<>();
		this.wheres = new ArrayList<>();
	}

	@Override
	public @NotNull Void execute(@NotNull DataConnector<?> connector) {
		if (this.values.isEmpty()) return null;
		if (!(connector.dataSource() instanceof HikariDataSource dataSource)) return null;

		String values = this.values.stream().map(value -> value.column().getNameEscaped() + " = ?")
				.collect(Collectors.joining(","));
		String wheres = this.wheres.stream().map(where -> where.value().column().getNameEscaped() + " " + where.type().getOperator() + " ?")
				.collect(Collectors.joining(" AND "));
		String sql = "UPDATE " + this.getTable() + " SET " + values + (wheres.isEmpty() ? "" : " WHERE " + wheres);

		List<String> value = this.values.stream().map(SQLValue::value).toList();
		List<String> where = this.wheres.stream().map(SQLCondition::value).map(SQLValue::value).toList();
		SQLExecute.executeStatement(dataSource, sql, value, where);
		return null;
	}

	@NotNull
	public static UpdateQueryExecutor builder(@NotNull String table) {
		return new UpdateQueryExecutor(table);
	}

	@NotNull
	public UpdateQueryExecutor values(@NotNull SQLValue... values) {
		return this.values(Arrays.asList(values));
	}

	@NotNull
	public UpdateQueryExecutor values(@NotNull List<SQLValue> values) {
		this.values.clear();
		this.values.addAll(values);
		return this;
	}

	@NotNull
	public UpdateQueryExecutor where(@NotNull SQLCondition... wheres) {
		return this.where(Arrays.asList(wheres));
	}

	@NotNull
	public UpdateQueryExecutor where(@NotNull List<SQLCondition> wheres) {
		this.wheres.clear();
		this.wheres.addAll(wheres);
		return this;
	}
}
