package cn.yingyya.next.moment.api.database.sql;

import cn.yingyya.next.moment.api.database.sql.enums.SQLConditionType;
import org.jetbrains.annotations.NotNull;

public record SQLCondition(SQLValue value, SQLConditionType type) {

	public SQLCondition(@NotNull SQLValue value, @NotNull SQLConditionType type) {
		this.value = value;
		this.type = type;
	}

	@NotNull
	public static SQLCondition of(@NotNull SQLValue value, @NotNull SQLConditionType type) {
		return new SQLCondition(value, type);
	}

	@NotNull
	public static SQLCondition equal(@NotNull SQLValue value) {
		return new SQLCondition(value, SQLConditionType.EQUAL);
	}

	@NotNull
	public static SQLCondition notEqual(@NotNull SQLValue value) {
		return new SQLCondition(value, SQLConditionType.NOT_EQUAL);
	}

	@NotNull
	public static SQLCondition smaller(@NotNull SQLValue value) {
		return new SQLCondition(value, SQLConditionType.SMALLER);
	}

	@NotNull
	public static SQLCondition greater(@NotNull SQLValue value) {
		return new SQLCondition(value, SQLConditionType.GREATER);
	}

	@Override
	@NotNull
	public SQLValue value() {
		return value;
	}

	@Override
	@NotNull
	public SQLConditionType type() {
		return type;
	}
}
