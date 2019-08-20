# SharedPreferencesManager


> 一款简单易用的支持内存映射的 SharedPreferences 工具库



- 基于 SharedPreferences 键值对的特性，在 SharedPreferences  xml 文件缓存(磁盘缓存)前置了一层并发安全内存容器模型 ConcurrentHashMap 

- 优先从内存缓存中取数据，大幅提高了读取数据的效率

- 支持对象的序列化和反序列化

- 和原生的 SharedPreferences 一样不支持夸进程

  



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
| setJsonParserStrategy                     | JsonParserStrategy                            | 暴露给调用者去实现的对象序列化和反序列化接口       |



### 高阶用法

ShaerdPreferencesManager 提供了基于 Json 对非基本数据类型对象的序列化和反序列化的能力，设计思想为

- 要保持类库的无依赖性，低耦合性。ShaerdPreferencesManager 只提供解析接口，对对象的序列化和反序列化不做具体实现，这一部分交由调用者去实现。

- 每个使用者所采用的 Json 解析方式不一样 (Gson、FastJson、Jackson等) ShaerdPreferencesManager 也不可能做全面覆盖。

  

  ```java
  public interface JsonParserStrategy {
      String encode(Object o);
  
      Object decode(String jsonString, Class<?> c);
  }
  ```

  

  

  建议用法:

  

  ```Java
  public class App extends Application {
      @Override
      public void onCreate() {
          super.onCreate();
          SharedPreferencesManager.getInstance().init(this);
          SharedPreferencesManager.getInstance().setJsonParserStrategy(new JsonParserStrategy() {
              @Override
              public String encode(Object o) {
                  String jsonString;
                  //采用 gson 的实现解析方式
                  Gson gson = new Gson();
                  jsonString = gson.toJson(o);
  
                  //采用 fastjson 的实现解析方式
                  //jsonString = JSON.toJSONString(o);
                  return jsonString;
              }
  
              @Override
              public Object decode(String jsonString, Class<?> c) {
                  if (jsonString == null) {
                      return null;
                  }
  
                  Object o;
                  //采用 gson 的实现解析方式
                  Gson gson = new Gson();
                  o = gson.fromJson(jsonString, c);
  
                  //采用 fastjson 的实现解析方式
                  // o = JSON.parseObject(jsonString, c);
                  return o;
              }
          });
      }
  }
  ```

   

Sample:



```java
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        User user = new User();
        user.name = "张三";
        user.age = 30;
        user.address = "北京市东城区银河SOHO";
        user.sex = "男";
        user.phone = "110";
        user.isVip = true;

        SharedPreferencesManager.getInstance().put("currentUser", user);

        User cacheUser = (User) SharedPreferencesManager.getInstance().get("currentUser", new User());
        Log.e(SharedPreferencesManager.class.getSimpleName(), cacheUser.toString());

    }

    class User {
        String name;
        int age;
        String address;
        String sex;
        String phone;
        boolean isVip;

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", address='" + address + '\'' +
                    ", sex='" + sex + '\'' +
                    ", phone='" + phone + '\'' +
                    ", isVip=" + isVip +
                    '}';
        }
    }

}
```



