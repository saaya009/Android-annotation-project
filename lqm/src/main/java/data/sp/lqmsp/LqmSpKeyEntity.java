package data.sp.lqmsp;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import data.sp.annotation.LqmSpKey;
import utils.LogUtil;

/**
 * Created by Hack on 2016/1/18.
 */
public class LqmSpKeyEntity<T> {

    private final Field columnField;
    private final String keyName;
    private final String defaultValue;
    private final Class<?> serializer;
    private final boolean isUpDate;
    //    private final int keyId;
    private final Method setMethod;
    private final Class<?> fieldType;

    public LqmSpKeyEntity(Class<?> entityType, Field field, LqmSpKey annotation) {
        field.setAccessible(true);
        this.columnField = field;
        this.keyName = annotation.name();
        this.defaultValue = annotation.defaultValue();
        this.serializer = annotation.serializer();
        this.isUpDate = annotation.onUpdate();
//        this.keyId = annotation.keyId();
        this.fieldType = field.getType();
        this.setMethod = LqmSpUtil.findSetMethod(entityType, field);
        if (this.setMethod != null && !this.setMethod.isAccessible()) {
            this.setMethod.setAccessible(true);
        }
    }

    public Class<?> getFieldType() {
        return fieldType;
    }

    public Object getFieldValue(Object entity) {
        Object fieldValue = null;
        if (entity != null) {
            try {
                fieldValue = this.columnField.get(entity);
            } catch (Throwable e) {
                LogUtil.wtf(e);
            }
        }
        return fieldValue;
    }

    public void setValueFromSp(Object entity, Object value) {
        if (value == null) return;
        boolean b = setMethod != null;
        LogUtil.wtf("is setMethod !=  null ? = " + b);
        LogUtil.wtf("setValueFromSp = " + entity.getClass() + "----- value = " + value);
        if (b) {
            try {
                setMethod.invoke(entity, value);
            } catch (Throwable e) {
                LogUtil.wtf(e.toString());
            }
        } else {
            try {
                this.columnField.set(entity, value);
            } catch (Throwable e) {
                LogUtil.wtf(e.toString());
            }
        }
    }

    public void setFieldValue(Object defaultObject, Object defl) {
        try {
            if (defaultObject instanceof String) {
                this.columnField.set(defaultObject, defl.toString());
            } else if (defaultObject instanceof Integer) {
                this.columnField.setInt(defaultObject, (Integer) defl);
            } else if (defaultObject instanceof Boolean) {
                this.columnField.setBoolean(defaultObject, (Boolean) defl);
            } else if (defaultObject instanceof Float) {
                this.columnField.setFloat(defaultObject, (Float) defl);
            } else if (defaultObject instanceof Long) {
                this.columnField.setLong(defaultObject, (Long) defl);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public String getKeyName() {
        return keyName;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public Class<?> getSerializer() {
        return serializer;
    }

    public boolean isUpDate() {
        return isUpDate;
    }

//    public int getKeyId() {
//        return keyId;
//    }

    public Field getColumnField() {
        return columnField;
    }
}
