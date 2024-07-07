package cn.yingyya.next.moment.api.database.sql;

import cn.yingyya.next.moment.api.database.DataBaseType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record SQLColumn(String name, SQLColumnType type, int length) {

	public SQLColumn(@NotNull String name, @NotNull SQLColumnType type, int length) {
		this.name = name.replaceAll(" ", "_");
		this.length = length;
		this.type = type;
	}

	@NotNull
	public static SQLColumn of(@NotNull String name, @NotNull SQLColumnType type, int length) {
		return new SQLColumn(name, type, length);
	}

	@NotNull
	public static SQLColumn of(@NotNull String name, @NotNull SQLColumnType type) {
		return new SQLColumn(name, type, 1);
	}

	@Override
	@NotNull
	public String name() {
		return name;
	}

	@Override
	@NotNull
	public SQLColumnType type() {
		return type;
	}


	@Nullable
	public SQLValue asValue(Object o) {
		return SQLValue.of(this, String.valueOf(o));
	}

	@NotNull
	public String getNameEscaped() {
		return this.name().equalsIgnoreCase("*") ? this.name() : "`" + this.name() + "`";
	}

	@NotNull
	public String formatType(@NotNull DataBaseType type) {
		return this.type().former().format(type, this.length());
	}
}
