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

public class DeleteQueryExecutor extends SQLExecutor<Void> {

	private final List<SQLCondition> wheres;

	protected DeleteQueryExecutor(@NotNull String table) {
		super(table);
		this.wheres = new ArrayList<>();
	}


	@NotNull
	public static DeleteQueryExecutor builder(@NotNull String table) {
		return new DeleteQueryExecutor(table);
	}

	@NotNull
	public DeleteQueryExecutor where(@NotNull SQLCondition... wheres) {
		return this.where(Arrays.asList(wheres));
	}

	@NotNull
	public DeleteQueryExecutor where(@NotNull List<SQLCondition> wheres) {
		this.wheres.clear();
		this.wheres.addAll(wheres);
		return this;
	}

	@Override
	public @NotNull Void execute(@NotNull DataConnector<?> connector) {
		if (this.wheres.isEmpty()) return null;
		if (!(connector.dataSource() instanceof HikariDataSource dataSource)) return null;

		String whereCols = this.wheres.stream()
				.map(where -> where.value().column().getNameEscaped() + " " + where.type().getOperator() + " ?")
				.collect(Collectors.joining(" AND "));
		String sql = "DELETE FROM " + this.getTable() + (whereCols.isEmpty() ? "" : " WHERE " + whereCols);
		List<String> value = this.wheres.stream().map(SQLCondition::value).map(SQLValue::value).toList();
		SQLExecute.executeStatement(dataSource, sql, value);
		return null;
	}
}
