package cn.wode490390.debug;

import cn.nukkit.Server;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NukkitDebuggingHashMap20200118<K,V> implements Map<K,V> {

    private final Map<K,V> map = new HashMap<>();

    @Override
    public int size() {
    if (!Server.getInstance().isPrimaryThread()) {
        Throwable t = new IllegalStateException("检测到实体操作不在主线程");
        Server.getInstance().getLogger().emergency("请将日志上报负责人", t);
        //throw t;
    }
        return map.size();
    }

    @Override
    public boolean isEmpty() {
    if (!Server.getInstance().isPrimaryThread()) {
        Throwable t = new IllegalStateException("检测到实体操作不在主线程");
        Server.getInstance().getLogger().emergency("请将日志上报负责人", t);
        //throw t;
    }
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
    if (!Server.getInstance().isPrimaryThread()) {
        Throwable t = new IllegalStateException("检测到实体操作不在主线程");
        Server.getInstance().getLogger().emergency("请将日志上报负责人", t);
        //throw t;
    }
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
    if (!Server.getInstance().isPrimaryThread()) {
        Throwable t = new IllegalStateException("检测到实体操作不在主线程");
        Server.getInstance().getLogger().emergency("请将日志上报负责人", t);
        //throw t;
    }
        return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
    if (!Server.getInstance().isPrimaryThread()) {
        Throwable t = new IllegalStateException("检测到实体操作不在主线程");
        Server.getInstance().getLogger().emergency("请将日志上报负责人", t);
        //throw t;
    }
        return map.get(key);
    }

    @Override
    public V put(K key, V value) {
    if (!Server.getInstance().isPrimaryThread()) {
        Throwable t = new IllegalStateException("检测到实体操作不在主线程");
        Server.getInstance().getLogger().emergency("请将日志上报负责人", t);
        //throw t;
    }
        return map.put(key, value);
    }

    @Override
    public V remove(Object key) {
if (!Server.getInstance().isPrimaryThread()) {
    Throwable t = new IllegalStateException("检测到实体操作不在主线程");
    Server.getInstance().getLogger().emergency("请将日志上报负责人", t);
    //throw t;
}
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
    if (!Server.getInstance().isPrimaryThread()) {
        Throwable t = new IllegalStateException("检测到实体操作不在主线程");
        Server.getInstance().getLogger().emergency("请将日志上报负责人", t);
        //throw t;
    }
        map.putAll(m);
    }

    @Override
    public void clear() {
if (!Server.getInstance().isPrimaryThread()) {
    Throwable t = new IllegalStateException("检测到实体操作不在主线程");
    Server.getInstance().getLogger().emergency("请将日志上报负责人", t);
    //throw t;
}
        map.clear();
    }

    @Override
    public Set<K> keySet() {
if (!Server.getInstance().isPrimaryThread()) {
    Throwable t = new IllegalStateException("检测到实体操作不在主线程");
    Server.getInstance().getLogger().emergency("请将日志上报负责人", t);
    //throw t;
}
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
if (!Server.getInstance().isPrimaryThread()) {
    Throwable t = new IllegalStateException("检测到实体操作不在主线程");
    Server.getInstance().getLogger().emergency("请将日志上报负责人", t);
    //throw t;
}
        return map.values();
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
if (!Server.getInstance().isPrimaryThread()) {
    Throwable t = new IllegalStateException("检测到实体操作不在主线程");
    Server.getInstance().getLogger().emergency("请将日志上报负责人", t);
    //throw t;
}
        return map.entrySet();
    }
}
