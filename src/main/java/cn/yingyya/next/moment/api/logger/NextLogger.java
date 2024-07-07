package cn.yingyya.next.moment.api.logger;

import cn.yingyya.next.moment.NextPlugin;

import java.util.logging.Level;

public class NextLogger<T extends NextPlugin<T>> {

	private final T plugin;

	public NextLogger(T plugin) {
		this.plugin = plugin;
	}

	public String getLogPrefix() {
		return "服务器 > ";
	}

	public void info(String msg) {
		plugin.getLogger().info(getLogPrefix() + msg);
	}

	public void warn(String msg) {
		plugin.getLogger().warning(getLogPrefix() + msg);
	}

	public void error(String msg) {
		plugin.getLogger().severe(getLogPrefix() + msg);
	}

	public void error(String msg, Throwable t) {
		plugin.getLogger().log(Level.SEVERE, getLogPrefix() + msg, t);
	}
}
