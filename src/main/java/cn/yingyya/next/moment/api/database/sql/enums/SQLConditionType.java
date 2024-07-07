package cn.yingyya.next.moment.api.database.sql.enums;

import org.jetbrains.annotations.NotNull;

public enum SQLConditionType {

	GREATER(">"),
	SMALLER("<"),
	EQUAL("="),
	NOT_EQUAL("!=");

	private final String operator;

	@NotNull
	public String getOperator() {
		return operator;
	}
	
	SQLConditionType(@NotNull String operator) {
		this.operator = operator;
	}
}
