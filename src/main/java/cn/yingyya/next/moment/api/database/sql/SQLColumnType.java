package cn.yingyya.next.moment.api.database.sql;

import org.jetbrains.annotations.NotNull;

public record SQLColumnType(ColumnFormat former) {
	public static final SQLColumnType INTEGER = new SQLColumnType(ColumnFormat.INTEGER);
	public static final SQLColumnType DOUBLE = new SQLColumnType(ColumnFormat.DOUBLE);
	public static final SQLColumnType LONG = new SQLColumnType(ColumnFormat.LONG);
	public static final SQLColumnType BOOLEAN = new SQLColumnType(ColumnFormat.BOOLEAN);
	public static final SQLColumnType STRING = new SQLColumnType(ColumnFormat.STRING);

	public SQLColumnType(@NotNull ColumnFormat former) {
		this.former = former;
	}

	@Override
	@NotNull
	public ColumnFormat former() {
		return former;
	}
}
