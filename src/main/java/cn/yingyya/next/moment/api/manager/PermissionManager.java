package cn.yingyya.next.moment.api.manager;

import cn.yingyya.next.moment.NextPlugin;
import cn.yingyya.next.moment.api.permission.annotations.NPermission;
import cn.yingyya.next.moment.utils.ReflexUtils;
import org.bukkit.permissions.Permission;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class PermissionManager<T extends NextPlugin<T>> extends Manager{

	private final T plugin;
	private final List<Permission> permissions;

	public PermissionManager(T plugin) {
		this.plugin = plugin;
		this.permissions = new ArrayList<>();
	}

	public void register(Class<?> clazz) {
		List<Field> fields = ReflexUtils.getFields(clazz).stream().filter(v -> v.getAnnotation(NPermission.class) != null && v.getType() == Permission.class).toList();
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				permissions.add((Permission) field.get(null));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onLoad() {
		for (Permission permission : permissions) {
			if (plugin.getPluginManager().getPermission(permission.getName()) == null) {
				plugin.getPluginManager().addPermission(permission);
				plugin.getNextLogger().info("Added permission " + permission.getName() + " to plugin " + plugin.getName());
			}
		}
	}

	@Override
	public void onUnload() {

	}
}
