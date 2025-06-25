package cn.yingyya.next.moment.utils;

import java.util.function.Supplier;

public class Lazy<T> {
	private final Supplier<T> supplier;
	private volatile T result;

	public Lazy(Supplier<T> supplier) {
		this.supplier = supplier;
	}

	public static <T> Lazy<T> of(Supplier<T> supplier) {
		return new Lazy<>(supplier);
	}

	public T get() {
		var value = result;
		if (value == null) {
			synchronized (this) {
				value = result;
				if (value == null) {
					result = value = supplier.get();
				}
			}
		}
		return value;
	}
}
