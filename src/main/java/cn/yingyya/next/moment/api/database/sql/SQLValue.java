package cn.yingyya.next.moment.api.database.sql;

import org.jetbrains.annotations.NotNull;

public record SQLValue(SQLColumn column, String value) {

	public SQLValue(@NotNull SQLColumn column, @NotNull String value) {
		this.column = column;
		this.value = value;
	}

	public static SQLValue of(@NotNull SQLColumn column, @NotNull String value) {
		return new SQLValue(column, value);
	}

	@Override
	@NotNull
	public SQLColumn column() {
		return column;
	}

	@Override
	@NotNull
	public String value() {
		return value;
	}
}
