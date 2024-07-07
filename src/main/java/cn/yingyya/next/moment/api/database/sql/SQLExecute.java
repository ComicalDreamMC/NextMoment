package cn.yingyya.next.moment.api.database.sql;

import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class SQLExecute {

	public static <T> List<@NotNull T> executeQuery(@NotNull HikariDataSource dataSource, @NotNull String sql, @NotNull Collection<String> value, @NotNull Function<ResultSet, T> function, int amount) {
		List<T> list = new ArrayList<>();
		try (Connection connection = dataSource.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(sql);

			int count = 1;
			for (String wValue : value) {
				statement.setString(count++, wValue);
			}
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next() && (amount < 0 || list.size() < amount)) {
				list.add(function.apply(resultSet));
			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static void executeStatement(@NotNull HikariDataSource dataSource, @NotNull String sql, @NotNull Collection<String> value, @NotNull Collection<String> value2) {
		try (Connection connection = dataSource.getConnection();
		     PreparedStatement statement = connection.prepareStatement(sql)) {
			int count = 1;
			for (String columnName : value) {
				statement.setString(count++, columnName);
			}
			for (String columnValue : value2) {
				statement.setString(count++, columnValue);
			}
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void executeStatement(@NotNull HikariDataSource dataSource, @NotNull String sql, @NotNull Collection<String> value) {
		executeStatement(dataSource, sql, value, Collections.emptySet());
	}

	public static void executeStatement(@NotNull HikariDataSource dataSource, @NotNull String sql) {
		executeStatement(dataSource, sql, Collections.emptySet());
	}

	public static boolean hasTable(@NotNull HikariDataSource dataSource, @NotNull String table) {
		try (Connection connection = dataSource.getConnection()) {

			boolean has;
			DatabaseMetaData metaData = connection.getMetaData();
			ResultSet tables = metaData.getTables(null, null, table, null);
			has = tables.next();
			tables.close();
			return has;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean hasColumn(@NotNull HikariDataSource dataSource, @NotNull String table, @NotNull SQLColumn column) {
		String sql = "SELECT * FROM " + table;
		String columnName = column.name();
		try (Connection connection = dataSource.getConnection();
		     Statement statement = connection.createStatement()) {

			ResultSet resultSet = statement.executeQuery(sql);
			ResultSetMetaData metaData = resultSet.getMetaData();
			int columns = metaData.getColumnCount();
			for (int index = 1; index <= columns; index++) {
				if (columnName.equals(metaData.getColumnName(index))) {
					return true;
				}
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
