package data.sp.lqmsp;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedHashMap;

import data.sp.annotation.LqmSpKey;
import utils.LogUtil;

/**
 * Created by Hack on 2016/1/18.
 */
final class LqmSpUtil {


    private LqmSpUtil() {
    }

    /**
     * 达到的目的就是 将 所有的 key以 map的形式拿出来
     *
     * @param entityType
     * @return
     */
    /* package */
    static synchronized LinkedHashMap<String, LqmSpKeyEntity> findSpkeyMap(Class<?> entityType) {
        LinkedHashMap<String, LqmSpKeyEntity> keysMap = new LinkedHashMap<String, LqmSpKeyEntity>();
        addKey2Map(entityType, keysMap);
        return keysMap;
    }

    private static void addKey2Map(Class<?> entityType, HashMap<String, LqmSpKeyEntity> keysMap) {
        if (Object.class.equals(entityType)) return;
        try {
            Field[] fields = entityType.getDeclaredFields();
            for (Field field : fields) {
                int modify = field.getModifiers();
                if (Modifier.isStatic(modify) || Modifier.isTransient(modify)) {
                    continue;
                }
                LqmSpKey annotation = field.getAnnotation(LqmSpKey.class);
                if (null != annotation) {
                    LqmSpKeyEntity column = new LqmSpKeyEntity(entityType, field, annotation);
                    if (!keysMap.containsKey(column.getKeyName())) {
                        keysMap.put(column.getKeyName(), column);
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.wtf("LqmSpUtil --- addKey2Map", "e == " + e.toString());
        }

    }

    /* package */
    static Method findGetMethod(Class<?> entityType, Field field) {
        if (Object.class.equals(entityType)) return null;

        String fieldName = field.getName();
        Method getMethod = null;
        if (getMethod == null) {
            String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            try {
                getMethod = entityType.getDeclaredMethod(methodName);
            } catch (NoSuchMethodException e) {
                LogUtil.wtf(entityType.getName() + "#" + methodName + " not exist");
            }
        }
        if (getMethod == null) {
            return findGetMethod(entityType.getSuperclass(), field);
        }
        return getMethod;
    }

    /* package */
    static Method findSetMethod(Class<?> entityType, Field field) {
        if (Object.class.equals(entityType)) return null;

        String fieldName = field.getName();
        Class<?> fieldType = field.getType();
        Method setMethod = null;

        if (isBoolean(fieldType)) {
            setMethod = findBooleanSetMethod(entityType, fieldName, fieldType);
        }

        if (setMethod == null) {
            String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            try {
                setMethod = entityType.getDeclaredMethod(methodName, fieldType);
            } catch (NoSuchMethodException e) {
                LogUtil.wtf(entityType.getName() + "#" + methodName + " not exist");
            }
        }

        if (setMethod == null) {
            return findSetMethod(entityType.getSuperclass(), field);
        }
        return setMethod;
    }

    private static Method findBooleanSetMethod(Class<?> entityType, final String fieldName, Class<?> fieldType) {
        String methodName = null;
        if (fieldName.startsWith("is")) {
            methodName = "set" + fieldName.substring(2, 3).toUpperCase() + fieldName.substring(3);
        } else {
            methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        }
        try {
            return entityType.getDeclaredMethod(methodName, fieldType);
        } catch (NoSuchMethodException e) {
            LogUtil.wtf(entityType.getName() + "#" + methodName + " not exist");
        }
        return null;
    }

    public static boolean isBoolean(Class<?> fieldType) {
        return Boolean.class.equals(fieldType);
    }

}
