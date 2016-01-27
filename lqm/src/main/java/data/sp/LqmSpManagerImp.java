package data.sp;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import data.sp.lqmsp.LqmSpEntity;
import data.sp.lqmsp.LqmSpKeyEntity;
import data.sp.lqmsp.LqmSpManager;
import utils.LogUtil;

/**
 * Created by Hack on 2016/1/18.
 */
public class LqmSpManagerImp implements LqmSpManager {

    private static Context context;

    private LqmSpManagerImp() {
    }

    ;

    private static LqmSpManagerImp mLqmSpManagerImp;

    public static LqmSpManagerImp init(Context ccontext) {
        context = ccontext;
        if (null == mLqmSpManagerImp)
            mLqmSpManagerImp = new LqmSpManagerImp();
        return mLqmSpManagerImp;
    }

    @Override
    public boolean save(Object entity) throws Throwable {
        if (entity instanceof List) {
            List<?> entities = (List<?>) entity;
            if (entities.isEmpty()) return false;
            LqmSpEntity<?> lqmSpEntity = LqmSpEntity.get(this, entities.get(0).getClass());
            SharedPreferences table = createTable(lqmSpEntity);
            SharedPreferences.Editor edit = table.edit();
            for (Object item : entities) {
                List<KeyValue> keyValues = entity2KeyValueList(lqmSpEntity, item);
                for (KeyValue kv : keyValues) {
                    LogUtil.wtf("list ---"+kv.toString());
                    putData2Sp(edit, kv.key, kv.getValueStr());
                }
                SharedPreferencesCompat.apply(edit);
            }
        } else {
            LqmSpEntity<?> lqmSpEntity = LqmSpEntity.get(this, entity.getClass());
            SharedPreferences table = createTable(lqmSpEntity);
            SharedPreferences.Editor edit = table.edit();
            List<KeyValue> keyValues = entity2KeyValueList(lqmSpEntity, entity);
            for (KeyValue kv : keyValues) {
                LogUtil.wtf("obj --- "+kv.toString());
                putData2Sp(edit, kv.key, kv.value);
            }
            SharedPreferencesCompat.apply(edit);
        }
        return true;
    }

    private SharedPreferences createTable(LqmSpEntity<?> lqmSpEntity) throws Exception {
        String tableName = lqmSpEntity.getTableName();
        int preferenceMode = lqmSpEntity.getPreferenceMode();
        return context.getSharedPreferences(tableName, preferenceMode);
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
    }

    public static List<KeyValue> entity2KeyValueList(LqmSpEntity<?> table, Object entity) {
        List<KeyValue> keyValueList = new ArrayList<KeyValue>();
        Collection<LqmSpKeyEntity> columns = table.getSpkeyMap().values();
        for (LqmSpKeyEntity column : columns) {
            KeyValue kv = column2KeyValue(entity, column);
            if (kv != null) {
                keyValueList.add(kv);
            }
        }
        return keyValueList;
    }

    private static KeyValue column2KeyValue(Object entity, LqmSpKeyEntity column) {
        String key = column.getKeyName();
        Object value = column.getFieldValue(entity);
        return new KeyValue(key, value);
    }

    @Override
    public <T> T get(Class<T> entity) throws Throwable {
        LqmSpEntity<T> lqmSpEntity = LqmSpEntity.get(this, entity);
        SharedPreferences table = createTable(lqmSpEntity);
        return LqmSpKeyUtil.getEntity(lqmSpEntity, table);
    }

    @Override
    public void close() throws IOException {

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
}
