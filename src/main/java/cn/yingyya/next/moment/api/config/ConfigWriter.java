package cn.yingyya.next.moment.api.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ConfigWriter<T> {
	void write(@NotNull String path, @Nullable T value, @NotNull YamlConfiguration configuration);
}
