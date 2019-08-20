package com.julive.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SharedPreferencesManager {

    private static final String DEFAULT_NAME = "APP_MAP";

    private static SharedPreferencesManager mInstance;

    private Context mContext;

    private SharedPreferences mPreferences;

    private SharedPreferences.Editor mEditor;

    private Map mMemoryMap;

    private SharedPreferencesManager() {

    }

    public static SharedPreferencesManager getInstance() {
        if (mInstance == null) {
            synchronized (SharedPreferencesManager.class) {
                if (mInstance == null) {
                    mInstance = new SharedPreferencesManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化
     *
     * @param context 上下文
     */
    public void init(Context context) {
        init(context, DEFAULT_NAME);
    }

    /**
     * 初始化
     *
     * @param context               上下文
     * @param sharedPreferencesName 磁盘缓存 sharedPreferences 文件命名
     */
    @SuppressLint("CommitPrefEdits")
    public void init(Context context, String sharedPreferencesName) {
        mContext = context;
        mMemoryMap = new ConcurrentHashMap();
        mPreferences = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mMemoryMap.putAll(mPreferences.getAll());
    }


    private JsonParserStrategy mJsonParserStrategy;

    /**
     * 暴露给调用者去实现的对象序列化和反序列化接口
     *
     * @param jsonParserStrategy json 解析策略
     */
    public void setJsonParserStrategy(JsonParserStrategy jsonParserStrategy) {
        if (jsonParserStrategy != null) {
            mJsonParserStrategy = jsonParserStrategy;
        }
    }


    /**
     * @param key   键
     * @param value 值
     */
    public void put(String key, Object value) {
        legalInspection(key);
        if (null == value) {
            throw new IllegalArgumentException("Value can not is null!");
        }
        mMemoryMap.put(key, value);
        if (value instanceof String) {
            mEditor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            mEditor.putInt(key, (int) value);
        } else if (value instanceof Boolean) {
            mEditor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            mEditor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            mEditor.putLong(key, (Long) value);
        } else {
            String jsonString = mJsonParserStrategy.encode(value);
            if (jsonString != null) {
                mEditor.putString(key, jsonString);
            }
        }
        mEditor.apply();
    }

    /**
     * @param key   键
     * @param value 值
     * @return 返回本次磁盘缓存是否成功
     */
    public boolean putHasResult(String key, Object value) {
        legalInspection(key);
        if (null == value) {
            throw new IllegalArgumentException("Value can not is null!");
        }
        mMemoryMap.put(key, value);
        if (value instanceof String) {
            mEditor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            mEditor.putInt(key, (int) value);
        } else if (value instanceof Boolean) {
            mEditor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            mEditor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            mEditor.putLong(key, (Long) value);
        } else {
            String jsonString = mJsonParserStrategy.encode(value);
            if (jsonString != null) {
                mEditor.putString(key, jsonString);
            }
        }
        return mEditor.commit();
    }

    /**
     * @param key          键
     * @param defaultValue 默认值 如果没有在缓存 和 磁盘中查询到数据则返回默认值
     * @return 返回值 查询到的数据和默认值传入类型一致，外部还需要做一次强制转换
     */
    public Object get(String key, Object defaultValue) {
        legalInspection(key);
        if (mMemoryMap.containsKey(key)) {
            return mMemoryMap.get(key);
        } else if (mPreferences.contains(key)) {
            if (defaultValue instanceof String) {
                return mPreferences.getString(key, (String) defaultValue);
            } else if (defaultValue instanceof Integer) {
                return mPreferences.getInt(key, (Integer) defaultValue);
            } else if (defaultValue instanceof Boolean) {
                return mPreferences.getBoolean(key, (Boolean) defaultValue);
            } else if (defaultValue instanceof Float) {
                return mPreferences.getFloat(key, (Float) defaultValue);
            } else if (defaultValue instanceof Long) {
                return mPreferences.getLong(key, (Long) defaultValue);
            }
        }
        Object o = mJsonParserStrategy.decode(mPreferences.getString(key, null), defaultValue.getClass());
        return o == null ? defaultValue : o;
    }

    /**
     * 清除所有缓存
     */
    public void clearAll() {
        if (mContext == null) {
            throw new IllegalStateException("SharedPreferencesManager must invoke init!");
        }
        mMemoryMap.clear();
        mEditor.clear();
        mEditor.apply();
    }

    /**
     * 根据键移除某个值
     *
     * @param key 键
     */
    public void remove(String key) {
        legalInspection(key);
        mMemoryMap.remove(key);
        mEditor.remove(key);
        mEditor.apply();
    }

    /**
     * 注册 SharedPreferences 变化监听的包装接口
     *
     * @param listener SharedPreferences.OnSharedPreferenceChangeListener
     */
    public void registerSharedPreferencesChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        if (mContext == null) {
            throw new IllegalStateException("SharedPreferencesManager must invoke init!");
        }
        if (listener != null) {
            mPreferences.registerOnSharedPreferenceChangeListener(listener);
        }
    }

    /**
     * 反注册 SharedPreferences 变化监听的包装接口
     *
     * @param listener SharedPreferences.OnSharedPreferenceChangeListener
     */
    public void unRegisterSharedPreferencesChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        if (mContext == null) {
            throw new IllegalStateException("SharedPreferencesManager must invoke init!");
        }
        if (listener != null) {
            mPreferences.unregisterOnSharedPreferenceChangeListener(listener);
        }
    }

    private void legalInspection(String key) {
        if (mContext == null) {
            throw new IllegalStateException("SharedPreferencesManager must invoke init!");
        }
        if (null == key || TextUtils.isEmpty(key)) {
            throw new IllegalArgumentException("Key can not is null or empty!");
        }
    }


}
