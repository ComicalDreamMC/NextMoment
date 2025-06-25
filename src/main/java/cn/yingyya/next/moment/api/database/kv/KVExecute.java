package cn.yingyya.next.moment.api.database.kv;

import cn.yingyya.next.moment.api.database.DataBaseType;
import cn.yingyya.next.moment.api.database.DataConnector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.rocksdb.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KVExecute {
	public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	private final RocksDB db;

	public KVExecute(@NotNull DataConnector<?> dataConnector) {
		if (dataConnector.dataSource() instanceof RocksDB && dataConnector.type() == DataBaseType.KV) {
			this.db = (RocksDB) dataConnector.dataSource();
		} else
			throw new IllegalStateException("DataConnector is not a LevelDB instance");
	}

	public static KVExecute of(@NotNull DataConnector<?> dataConnector) {
		return new KVExecute(dataConnector);
	}

	public boolean set(String key, String value) {
		return set(key.getBytes(DEFAULT_CHARSET), value.getBytes(DEFAULT_CHARSET));
	}

	public boolean set(String key, String value, boolean flush) {
		boolean result = set(key, value);
		if (flush)
			flush();
		return result;
	}

	public String get(String key, String defaultValue) {
		return new String(get(key.getBytes(DEFAULT_CHARSET), defaultValue.getBytes(DEFAULT_CHARSET)), DEFAULT_CHARSET);
	}

	@Nullable
	public String get(String key) {
		var val = get(key.getBytes(DEFAULT_CHARSET), null);
		if (val == null) {
			return null;
		}
		return new String(val, DEFAULT_CHARSET);
	}

	public boolean set(byte[] key, byte[] value) {
		try {
			db.put(key, value);
			return true;
		} catch (RocksDBException e) {
			e.printStackTrace();
			return false;
		}
	}

	public byte[] get(byte[] key, byte[] defaultValue) {
		try {
			byte[] bytes = db.get(key);
			if (bytes == null) {
				return defaultValue;
			}
			return bytes;
		} catch (RocksDBException e) {
			e.printStackTrace();
			return defaultValue;
		}
	}

	public boolean delete(String key) {
		return delete(key.getBytes(DEFAULT_CHARSET));
	}

	public boolean delete(byte[] key) {
		try {
			db.delete(key);
			return true;
		} catch (RocksDBException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteRange(byte[] start, byte[] end) {
		try {
			db.deleteRange(start, end);
			return true;
		} catch (RocksDBException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean flush() {
		try {
			db.flush(new FlushOptions());
			return true;
		} catch (RocksDBException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<byte[]> multiGetAsList(List<byte[]> keys) {
		try {
			List<byte[]> bytes = db.multiGetAsList(keys);
			if (bytes == null) {
				return Collections.emptyList();
			}
			return bytes;
		} catch (RocksDBException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public Map<byte[], byte[]> multiGetAsMap(List<byte[]> keys) {
		try {
			return db.multiGet(keys);
		} catch (RocksDBException e) {
			e.printStackTrace();
			return Collections.emptyMap();
		}
	}

	public boolean keyMayExist(byte[] key) {
		try {
			Holder<byte[]> holder = new Holder<>();
			return db.keyMayExist(key, holder);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public Map<byte[], byte[]> getAll() {
		Map<byte[], byte[]> map = new HashMap<>();
		try (final RocksIterator iterator = db.newIterator()) {
			for (iterator.seekToFirst(); iterator.isValid(); iterator.next()) {
				map.put(iterator.key(), iterator.value());
			}
		}
		return map;
	}
}
