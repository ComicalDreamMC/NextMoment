package cn.yingyya.next.moment.api.database.sql.executor;

import cn.yingyya.next.moment.api.database.DataConnector;
import cn.yingyya.next.moment.api.database.sql.SQLColumn;
import cn.yingyya.next.moment.api.database.sql.SQLExecute;
import cn.yingyya.next.moment.api.database.sql.SQLExecutor;
import cn.yingyya.next.moment.api.database.sql.SQLValue;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InsertQueryExecutor extends SQLExecutor<Void> {

	private final List<SQLValue> values;

	protected InsertQueryExecutor(@NotNull String table) {
		super(table);
		this.values = new ArrayList<>();
	}


	@NotNull
	public static InsertQueryExecutor builder(@NotNull String table) {
		return new InsertQueryExecutor(table);
	}

	@NotNull
	public InsertQueryExecutor values(@NotNull SQLValue... values) {
		return this.values(Arrays.asList(values));
	}

	@NotNull
	public InsertQueryExecutor values(@NotNull List<SQLValue> values) {
		this.values.clear();
		this.values.addAll(values);
		return this;
	}


	@Override
	public @NotNull Void execute(@NotNull DataConnector<?> connector) {
		if (this.values.isEmpty()) return null;
		if (!(connector.dataSource() instanceof HikariDataSource dataSource)) return null;

		String columns = this.values.stream().map(SQLValue::column).map(SQLColumn::getNameEscaped).collect(Collectors.joining(","));
		String values = this.values.stream().map(value -> "?").collect(Collectors.joining(","));
		String sql = INSERT_INTO + " " + this.getTable() + "(" + columns + ") " + VALUES + "(" + values + ")";
		List<String> value = this.values.stream().map(SQLValue::value).toList();
		SQLExecute.executeStatement(dataSource, sql, value);
		return null;
	}
}
