package cn.yingyya.next.moment.api.database.sql.executor;

import cn.yingyya.next.moment.api.database.DataConnector;
import cn.yingyya.next.moment.api.database.sql.*;
import cn.yingyya.next.moment.api.database.DataBaseType;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlterTableExecutor extends SQLExecutor<Void> {

	private final DataBaseType dataBaseType;
	private final List<SQLValue> columns;
	private Type type;

	enum Type {
		ADD_COLUMN, RENAME_COLUMN, DROP_COLUMN
	}

	protected AlterTableExecutor(@NotNull String table, @NotNull DataBaseType dataBaseType) {
		super(table);
		this.dataBaseType = dataBaseType;
		this.columns = new ArrayList<>();
	}

	@NotNull
	public static AlterTableExecutor builder(@NotNull String table, @NotNull DataBaseType type) {
		return new AlterTableExecutor(table, type);
	}

	@NotNull
	public AlterTableExecutor addColumn(@NotNull SQLValue... columns) {
		return this.addColumn(Arrays.asList(columns));
	}

	@NotNull
	public AlterTableExecutor addColumn(@NotNull List<SQLValue> columns) {
		return this.columns(columns, Type.ADD_COLUMN);
	}

	@NotNull
	public AlterTableExecutor renameColumn(@NotNull SQLValue... columns) {
		return this.addColumn(Arrays.asList(columns));
	}

	@NotNull
	public AlterTableExecutor renameColumn(@NotNull List<SQLValue> columns) {
		return this.columns(columns, Type.RENAME_COLUMN);
	}

	@NotNull
	public AlterTableExecutor dropColumn(@NotNull SQLColumn... columns) {
		return this.dropColumn(Arrays.asList(columns));
	}

	@NotNull
	public AlterTableExecutor dropColumn(@NotNull List<SQLColumn> columns) {
		return this.columns(columns.stream().map(column -> column.asValue("dummy")).toList(), Type.DROP_COLUMN);
	}

	private AlterTableExecutor columns(@NotNull List<SQLValue> values, @NotNull Type type) {
		this.columns.clear();
		this.columns.addAll(values);
		this.type = type;
		return this;
	}

	@Override
	public @NotNull Void execute(@NotNull DataConnector<HikariDataSource> connector) {
		if (this.columns.isEmpty()) return null;

		if (this.type == Type.ADD_COLUMN) {
			this.columns.forEach(value -> {
				if (SQLExecute.hasColumn(connector.dataSource(), this.getTable(), value.column())) return;

				String sql = "ALTER TABLE " + this.getTable() + " ADD "
						+ value.column().name() + " " + value.column().formatType(this.dataBaseType);
				if (connector.type() == DataBaseType.SQLITE || value.column().type() != SQLColumnType.STRING) {
					sql = sql + " DEFAULT '" + value.value() + "'";
				}

				SQLExecute.executeStatement(connector.dataSource(), sql);
			});
		} else if (this.type == Type.RENAME_COLUMN) {
			this.columns.forEach(value -> {
				if (!SQLExecute.hasColumn(connector.dataSource(), this.getTable(), value.column())) return;

				String sql = "ALTER TABLE " + this.getTable() + " RENAME COLUMN " + value.column().name() + " TO " + value.value();

				SQLExecute.executeStatement(connector.dataSource(), sql);
			});
		} else if (this.type == Type.DROP_COLUMN) {
			this.columns.forEach(value -> {
				if (!SQLExecute.hasColumn(connector.dataSource(), this.getTable(), value.column())) return;

				String sql = "ALTER TABLE " + this.getTable() + " DROP COLUMN " + value.column().name();

				SQLExecute.executeStatement(connector.dataSource(), sql);
			});
		}
		return null;
	}
}
