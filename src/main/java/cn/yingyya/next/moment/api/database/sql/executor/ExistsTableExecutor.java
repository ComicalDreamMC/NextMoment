package cn.yingyya.next.moment.api.database.sql.executor;

import cn.yingyya.next.moment.api.database.DataConnector;
import cn.yingyya.next.moment.api.database.sql.SQLExecute;
import cn.yingyya.next.moment.api.database.sql.SQLExecutor;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;

public class ExistsTableExecutor extends SQLExecutor<Boolean> {

	protected ExistsTableExecutor(@NotNull String table) {
		super(table);
	}

	@NotNull
	public static ExistsTableExecutor builder(@NotNull String table) {
		return new ExistsTableExecutor(table);
	}

	@Override
	public @NotNull Boolean execute(@NotNull DataConnector<?> connector) {
		if (!(connector.dataSource() instanceof HikariDataSource dataSource)) return Boolean.FALSE;

		return SQLExecute.hasTable(dataSource, table);
	}
}
