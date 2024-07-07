package cn.yingyya.next.moment.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 反射工具
 */
public class ReflexUtils {

	public static Method getMethod(@NotNull Class<?> clazz, @NotNull String fieldName, @NotNull Class<?>... o) {
		try {
			return clazz.getDeclaredMethod(fieldName, o);
		} catch (NoSuchMethodException e) {
			Class<?> superClass = clazz.getSuperclass();
			return superClass == null ? null : getMethod(superClass, fieldName);
		}
	}

	public static Object invokeMethod(@NotNull Method method, @Nullable Object by, @Nullable Object... param) {
		method.setAccessible(true);
		try {
			return method.invoke(by, param);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<Field> getFields(@NotNull Class<?> type) {
		List<Field> list = new ArrayList<>();
		Class<?> clazz = type;
		while (clazz != null && clazz != Object.class) {
			if (!list.isEmpty()) {
				list.addAll(0, Arrays.asList(clazz.getDeclaredFields()));
			} else {
				Collections.addAll(list, clazz.getDeclaredFields());
			}
			clazz = clazz.getSuperclass();
		}
		return list;
	}

	@Nullable
	public static Field getField(@NotNull Class<?> type, @NotNull String fieldName) {
		try {
			return type.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Nullable
	public static Object getFieldValue(@NotNull Object object, @NotNull String fieldName) {
		try {
			Class<?> clazz = object.getClass();
			Field field = getField(clazz, fieldName);
			if (field == null) {
				return null;
			}
			field.setAccessible(true);
			return field.get(object);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}
