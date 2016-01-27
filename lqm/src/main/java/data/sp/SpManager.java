package data.sp;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import data.sp.converter.SpConverter;

/**
 * Created by Hack on 2016/1/12.
 */
public class SpManager {

    public static void storeSpData(Context context, SpConverter spObj) {
        put(context, spObj);
    }

    public static Object getSpData(Context context, SpConverter spobj) {
        return get(context, spobj);
    }

    public static void clearSpKeyData(Context context, SpConverter spobj) {
        clearKey(context, spobj);
    }

    public static void clearSpDatall(Context context, SpConverter spobj) {
        clearAll(context, spobj);
    }

    private static void clearKey(Context context, SpConverter spObj) {
        SharedPreferences.Editor editor = getEditor(context, spObj);
        String key = spObj.getSpEditKey();
        Object object = spObj.setNullValue();
        putData2Sp(editor, key, object);
    }


    private static void clearAll(Context context, SpConverter spObj) {
        SharedPreferences.Editor editor = getEditor(context, spObj);
        editor.clear().commit();
    }


    private static Object get(Context context, SpConverter spObj) {
        SharedPreferences sp = initSp(context, spObj.getSpTableName());
        String key = spObj.getSpEditKey();
        Object defaultObject = spObj.defalValue();
        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }


    private static void put(Context context, SpConverter spObj) {
        SharedPreferences.Editor editor = getEditor(context, spObj);
        String key = spObj.getSpEditKey();
        Object object = spObj.getSpEditValue();
        putData2Sp(editor, key, object);
    }

    public static SharedPreferences.Editor getEditor(Context context, SpConverter spObj) {
        return getEdit(initSp(context, spObj.getSpTableName()));
    }

    public static void putData2Sp(SharedPreferences.Editor editor, String key, Object object) {
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }

    private static SharedPreferences.Editor getEdit(SharedPreferences sp) {
        return sp.edit();
    }

    private static SharedPreferences initSp(Context context, String tableName) {
        SharedPreferences preferences = context.getSharedPreferences(tableName, context.MODE_PRIVATE);
        return preferences;
    }


}
