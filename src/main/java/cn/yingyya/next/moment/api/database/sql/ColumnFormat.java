package cn.yingyya.next.moment.api.database.sql;

import cn.yingyya.next.moment.api.database.DataBaseType;
import org.jetbrains.annotations.NotNull;

public interface ColumnFormat {

	ColumnFormat STRING = (type, length) -> {
		if (length < 1 || type == DataBaseType.SQLITE) {
			return type == DataBaseType.SQLITE ? "text not null" : "mediumtext not null";
		}
		return "varchar(" + length + ") character SET utf8 not null";
	};

	ColumnFormat INTEGER = (type, length) -> {
		if (length < 1 || type == DataBaseType.SQLITE) {
			return "integer not null";
		}
		return "int(" + length + ") not null";
	};

	ColumnFormat DOUBLE = (type, length) -> type == DataBaseType.SQLITE ? "real not null" : "double not null";

	ColumnFormat LONG = (type, length) -> length < 1 || type == DataBaseType.SQLITE ? "bigint not null" : "bigint(" + length + ") not null";

	ColumnFormat BOOLEAN = (type, length) -> type == DataBaseType.SQLITE ? "integer not null" : "tinyint(1) not null";

	@NotNull String format(@NotNull DataBaseType type, int length);
}
