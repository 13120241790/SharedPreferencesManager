# SharedPreferencesManager


> 一款简单易用的支持内存映射的 SharedPreferences 工具类



- 基于 SharedPreferences 键值对的特性，在 SharedPreferences  xml 文件缓存(磁盘缓存)前置了一层并发安全内存容器模型 ConcurrentHashMap 
- 主要提高了读取数据的效率
- 和原生的 SharedPreferences 一样不支持夸进程
- 目前支持的数据类型有  String 、int 、boolean、float、long ，过大的数据建议采用数据库方式存储





### 使用

Init

```java
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesManager.getInstance().init(this);
    }
}
```

Put

```java
SharedPreferencesManager.getInstance().put("keyString", "value");
```

Get

```java
String s = (String) SharedPreferencesManager.getInstance().get("keyString", "x");
```



### API

| 方法名                                    | 参数                                          | 描述                                               |
| ----------------------------------------- | --------------------------------------------- | -------------------------------------------------- |
| init                                      | Context context                               | 初始化                                             |
| init                                      | Context context, String sharedPreferencesName | 初始化，自定义 sp 文件命名                         |
| put                                       | String key, Object value                      | 存储数据，异步提交                                 |
| putHasResult                              | String key, Object value                      | 存储数据，同步提交且返回是否存储成功 Boolean       |
| get                                       | String key, Object defaultValue               | 取出数据，优先命中缓存，如果磁盘也没有则返回默认值 |
| clearAll                                  |                                               | 清除所有缓存，包含内存和磁盘                       |
| remove                                    | String key                                    | 根据键移除某个值                                   |
| registerSharedPreferencesChangeListener   | OnSharedPreferenceChangeListener              | 注册 SharedPreferences 变化监听的包装接口          |
| unRegisterSharedPreferencesChangeListener | OnSharedPreferenceChangeListener              | 反注册 SharedPreferences 变化监听的包装接口        |



### TODO

根据需求考虑支持更多的数据类型，例如对象序列化

