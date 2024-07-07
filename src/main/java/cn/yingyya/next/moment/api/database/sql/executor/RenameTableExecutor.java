package cn.yingyya.next.moment.api.database.sql.executor;

import cn.yingyya.next.moment.api.database.DataConnector;
import cn.yingyya.next.moment.api.database.sql.SQLExecute;
import cn.yingyya.next.moment.api.database.sql.SQLExecutor;
import cn.yingyya.next.moment.api.database.DataBaseType;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;

public class RenameTableExecutor extends SQLExecutor<Void> {

	private String renameTo;
	private final DataBaseType type;

	protected RenameTableExecutor(@NotNull String table, @NotNull DataBaseType type) {
		super(table);
		this.type = type;
	}


	@NotNull
	public static RenameTableExecutor builder(@NotNull String table, @NotNull DataBaseType type) {
		return new RenameTableExecutor(table, type);
	}

	@NotNull
	public RenameTableExecutor renameTo(@NotNull String renameTo) {
		this.renameTo = renameTo;
		return this;
	}


	@Override
	public @NotNull Void execute(@NotNull DataConnector<HikariDataSource> connector) {
		if (this.renameTo == null || this.renameTo.isEmpty()) return null;
		if (!SQLExecute.hasTable(connector.dataSource(), this.getTable())) return null;

		StringBuilder sql = new StringBuilder();
		if (this.type == DataBaseType.MYSQL) {
			sql.append("RENAME TABLE ").append(this.getTable()).append(" TO ").append(this.renameTo).append(";");
		} else {
			sql.append("ALTER TABLE ").append(this.getTable()).append(" RENAME TO ").append(this.renameTo);
		}
		SQLExecute.executeStatement(connector.dataSource(), sql.toString());
		return null;
	}
}
