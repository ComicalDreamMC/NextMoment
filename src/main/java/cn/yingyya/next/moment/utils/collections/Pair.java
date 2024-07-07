package cn.yingyya.next.moment.utils.collections;

import java.util.Objects;

public final class Pair<L, R> {

	private static final Pair<Object, Object> EMPTY = new Pair<>(null, null);

	private final L left;
	private final R right;

	@SuppressWarnings("unchecked")
	public static <L, R> Pair<L, R> empty() {
		return (Pair<L, R>) EMPTY;
	}

	public static <L, R> Pair<L, R> createLeft(L left) {
		if (left == null) {
			return empty();
		} else {
			return new Pair<>(left, null);
		}
	}

	public static <L, R> Pair<L, R> createRight(R right) {
		if (right == null) {
			return empty();
		} else {
			return new Pair<>(null, right);
		}
	}

	public static <L, R> Pair<L, R> create(L left, R right) {
		if (right == null && left == null) {
			return empty();
		} else {
			return new Pair<>(left, right);
		}
	}

	private Pair(L left, R right) {
		this.left = left;
		this.right = right;
	}

	public L getLeft() {
		return left;
	}

	public R getRight() {
		return right;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(left) + 31 * Objects.hashCode(right);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (obj instanceof Pair) {
			Pair<L, R> pair = (Pair<L, R>) obj;
			return Objects.equals(left, pair.left) && Objects.equals(right, pair.right);
		}

		return false;
	}

	@Override
	public String toString() {
		return "(" + left + ", " + right + ")";
	}
}

