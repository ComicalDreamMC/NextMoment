package cn.yingyya.next.moment.api.database.sql.executor;

import cn.yingyya.next.moment.api.database.sql.ColumnFormat;
import cn.yingyya.next.moment.api.database.DataConnector;
import cn.yingyya.next.moment.api.database.sql.SQLColumn;
import cn.yingyya.next.moment.api.database.sql.SQLExecute;
import cn.yingyya.next.moment.api.database.sql.SQLExecutor;
import cn.yingyya.next.moment.api.database.DataBaseType;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CreateTableExecutor extends SQLExecutor<Void> {

	private final DataBaseType type;
	private final List<SQLColumn> columns;

	protected CreateTableExecutor(@NotNull String table, @NotNull DataBaseType type) {
		super(table);
		this.type = type;
		this.columns = new ArrayList<>();
	}


	@NotNull
	public static CreateTableExecutor builder(@NotNull String table, @NotNull DataBaseType type) {
		return new CreateTableExecutor(table, type);
	}

	@NotNull
	public CreateTableExecutor columns(@NotNull SQLColumn... columns) {
		return this.columns(Arrays.asList(columns));
	}

	@NotNull
	public CreateTableExecutor columns(@NotNull List<SQLColumn> columns) {
		this.columns.clear();
		this.columns.addAll(columns);
		return this;
	}

	@Override
	public @NotNull Void execute(@NotNull DataConnector<?> connector) {
		if (this.columns.isEmpty()) return null;
		if (!(connector.dataSource() instanceof HikariDataSource dataSource)) return null;

		String id = "`id` " + ColumnFormat.INTEGER.format(this.type, 11);

		if (this.type == DataBaseType.SQLITE) {
			id += " PRIMARY KEY AUTOINCREMENT";
		} else {
			id += " PRIMARY KEY AUTO_INCREMENT";
		}

		String columns = id + "," + this.columns.stream()
				.map(column -> column.getNameEscaped() + " " + column.formatType(this.type))
				.collect(Collectors.joining(", "));
		String sql = "CREATE TABLE IF NOT EXISTS " + this.getTable() + "(" + columns + ");";

		SQLExecute.executeStatement(dataSource, sql);
		return null;
	}
}
