package cn.yingyya.next.moment.api.config;

//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
//@Target(ElementType.FIELD)
//@Retention(RetentionPolicy.RUNTIME)
//public @interface ConfigOption {
//
//	String value() default "";
//
//	ConfigType type() default ConfigType.Object;
//}


import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class ConfigOption<T> {

	public final static ConfigReader<String> StringReader = (path, value, configuration) -> configuration.getString(path, value);
	public final static ConfigReader<Integer> IntegerReader = (path, value, configuration) -> configuration.getInt(path, value);
	public final static ConfigReader<Long> LongReader = (path, value, configuration) -> configuration.getLong(path, value);
	public final static ConfigReader<Boolean> BooleanReader = (path, value, configuration) -> configuration.getBoolean(path, value);
	public final static ConfigReader<Double> DoubleReader = (path, value, configuration) -> configuration.getDouble(path, value);
	public final static ConfigReader<Color> ColorReader = (path, value, configuration) -> configuration.getColor(path, value);
	public final static ConfigReader<Object> ObjectReader = (path, value, configuration) -> configuration.get(path, value);
	public final static ConfigReader<ItemStack> ItemStackReader = (path, value, configuration) -> configuration.getItemStack(path, value);
	public final static ConfigReader<Location> LocationReader = (path, value, configuration) -> configuration.getLocation(path, value);
	public final static ConfigReader<List<?>> ListReader = (path, value, configuration) -> configuration.getList(path, value);
	public final static ConfigReader<List<Long>> LongListReader = (path, value, configuration) -> configuration.getLongList(path);
	public final static ConfigReader<List<String>> StringListReader = (path, value, configuration) -> configuration.getStringList(path);
	public final static ConfigReader<List<Integer>> IntegerListReader = (path, value, configuration) -> configuration.getIntegerList(path);
	public final static ConfigReader<List<Boolean>> BooleanListReader = (path, value, configuration) -> configuration.getBooleanList(path);
	public final static ConfigReader<List<Double>> DoubleListReader = (path, value, configuration) -> configuration.getDoubleList(path);
	public final static ConfigReader<List<Map<?, ?>>> MapListReader = (path, value, configuration) -> configuration.getMapList(path);

	private final String path;
	private final T defaultValue;
	private final ConfigReader<T> reader;
	private final ConfigWriter<T> writer;
	private YamlConfiguration configuration;

	private ConfigOption(String path, T defaultValue, ConfigReader<T> reader, ConfigWriter<T> writer) {
		this.path = path;
		this.defaultValue = defaultValue;
		this.reader = reader;
		this.writer = writer;
	}

	private ConfigOption(String path, T defaultValue, ConfigReader<T> reader) {
		this(path, defaultValue, reader, (p, v, c) -> c.set(p, v));
	}

	public void setConfiguration(YamlConfiguration configuration) {
		this.configuration = configuration;
	}

	public T get() {
		if (configuration == null) {
			return defaultValue;
		}
		T read = reader.read(path, defaultValue, configuration);
		if (read == null) {
			return defaultValue;
		}
		return read;
	}

	public void set(T value) {
		if (configuration != null) {
			writer.write(path, value, configuration);
		}
	}

	public static <T> ConfigOption<T> of(String path, T defaultValue, ConfigReader<T> getter, ConfigWriter<T> setter) {
		return new ConfigOption<T>(path, defaultValue, getter, setter);
	}

	public static <T> ConfigOption<T> of(String path, T defaultValue, ConfigReader<T> getter) {
		return new ConfigOption<T>(path, defaultValue, getter);
	}
}