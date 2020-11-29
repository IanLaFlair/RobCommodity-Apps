package com.kls.robcommodity.utils;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.Closeable;
import java.util.Objects;

import androidx.annotation.NonNull;

/**
 * @author Muhammad Irfan
 * @since 09/07/2018 16.14
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class SharedPreferenceManager implements Closeable {
    private static final transient String TAG = SharedPreferenceManager.class.getSimpleName();
    public static String NAME = "Rob Commodity";

    private static SharedPreferenceManager INSTANCE;

    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;

    protected SharedPreferenceManager() {}

    public static SharedPreferenceManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SharedPreferenceManager();
        }

        return INSTANCE;
    }



    public static <T> T get(String key, Class<T> clazz, T defaultValue) {
        try (SharedPreferenceManager sharedPreferenceManager = getInstance()) {
            return sharedPreferenceManager.getValue(key, clazz, defaultValue);
        }
    }

    public static <T> T get(String key, Class<T> clazz) {
        return get(key, clazz, null);
    }

    public static <T> T get(SharedPreferenceKey sharedPreferenceKey, Class<T> clazz, T defaultValue) {
        return get(sharedPreferenceKey.name(), clazz, defaultValue);
    }

    public static <T> T get(SharedPreferenceKey sharedPreferenceKey, Class<T> clazz) {
        return get(sharedPreferenceKey, clazz, null);
    }

    public static <T> boolean contain(String key) {
        try (SharedPreferenceManager sharedPreferenceManager = getInstance()) {
            return sharedPreferenceManager.sharedPreferences.contains(key);
        }
    }

    public static <T> boolean contain(SharedPreferenceKey sharedPreferenceKey) {
        return contain(sharedPreferenceKey.name());
    }

    @SuppressLint("CommitPrefEdits")
    public void begin() {
        if (this.editor == null) {
            this.editor = this.sharedPreferences.edit();
        }
    }

    public <T> void put(String key, @NonNull T value) {
        if (this.editor == null) {
            Log.e(TAG, "You must call begin() first.");
            return;
        }

        if (value instanceof String) {
            this.editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            this.editor.putInt(key, (Integer) value);
        } else if (value instanceof Float) {
            this.editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            this.editor.putLong(key, (Long) value);
        } else if (value instanceof Boolean) {
            this.editor.putBoolean(key, (Boolean) value);
        } else {
            Log.e(TAG, "Unsupported value type.");
        }
    }

    public <T> void put(SharedPreferenceKey sharedPreferenceKey, @NonNull T value) {
       put(sharedPreferenceKey.name(), value);
    }

    public void remove(String key) {
        if (this.editor == null) {
            Log.e(TAG, "You must call begin() first.");
            return;
        }

        this.editor.remove(key);
    }

    public void remove(SharedPreferenceKey sharedPreferenceKey) {
        remove(sharedPreferenceKey.name());
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(String key, Class<T> clazz, T defaultValue) {
        if (Objects.equals(String.class, clazz)) {
            return (T) this.sharedPreferences.getString(key, defaultValue != null ? (String) defaultValue : null);
        } else if (Objects.equals(Integer.class, clazz)) {
            return (T) Integer.valueOf(this.sharedPreferences.getInt(key, defaultValue != null ? (Integer) defaultValue : -9999));
        } else if (Objects.equals(Float.class, clazz)) {
            return (T) Float.valueOf(this.sharedPreferences.getFloat(key, defaultValue != null ? (Float) defaultValue : -9999F));
        } else if (Objects.equals(Long.class, clazz)) {
            return (T) Long.valueOf(this.sharedPreferences.getLong(key, defaultValue != null ? (Long) defaultValue : -9999L));
        } else if (Objects.equals(Boolean.class, clazz)) {
            return (T) Boolean.valueOf(this.sharedPreferences.getBoolean(key, defaultValue != null && (Boolean) defaultValue));
        } else {
            return null;
        }
    }

    public <T> T getValue(String key, Class<T> clazz) {
        return getValue(key, clazz, null);
    }

    public <T> T getValue(SharedPreferenceKey sharedPreferenceKey, Class<T> clazz, T defaultValue) {
        return getValue(sharedPreferenceKey.name(), clazz, defaultValue);
    }

    public <T> T getValue(SharedPreferenceKey sharedPreferenceKey, Class<T> clazz) {
        return getValue(sharedPreferenceKey, clazz, null);
    }

    public <T> boolean containValue(String key) {
        return this.sharedPreferences.contains(key);
    }

    public <T> boolean containValue(SharedPreferenceKey sharedPreferenceKey) {
        return containValue(sharedPreferenceKey.name());
    }

    public void commit() {
        if (this.editor == null) {
            Log.e(TAG, "You must call begin() first.");
            return;
        }

        this.editor.apply();

    }

    @Override
    public void close() {
        this.editor = null;
    }

    public void setSharedPreferences(SharedPreferences mSharedPreferences) {
        this.sharedPreferences = mSharedPreferences;
    }
}