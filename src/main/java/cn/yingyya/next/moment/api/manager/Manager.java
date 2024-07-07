package cn.yingyya.next.moment.api.manager;

import cn.yingyya.next.moment.NextPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class Manager<T extends NextPlugin<T>> {

	public abstract void onLoad();
	public abstract void onUnload();

	private final T plugin;

	public Manager(@NotNull T plugin) {
		this.plugin = plugin;
	}

	public T getPlugin() {
		return plugin;
	}
}
