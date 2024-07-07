package cn.yingyya.next.moment.api.manager;

import cn.yingyya.next.moment.NextPlugin;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class EventManager<T extends NextPlugin<T>> extends Manager<T> {
	private final Set<Listener> listeners = new HashSet<>();

	public EventManager(@NotNull T plugin) {
		super(plugin);
	}

	public void registerEventListener(@NotNull Listener listener) {
		listeners.add(listener);
	}

	@Override
	public void onLoad() {
		listeners.forEach(listener -> getPlugin().getPluginManager().registerEvents(listener, getPlugin()));
	}

	@Override
	public void onUnload() {
		listeners.forEach(HandlerList::unregisterAll);
	}
}
