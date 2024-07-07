package cn.yingyya.next.moment.api.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

public interface ConfigReader<T> {
	T read(@NotNull String path, @NotNull T value, @NotNull YamlConfiguration configuration);
}
