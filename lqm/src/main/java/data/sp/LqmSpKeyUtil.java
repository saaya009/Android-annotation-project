/*
 * Copyright (c) 2013. wyouflf (wyouflf@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package data.sp;

import android.content.SharedPreferences;
import android.database.Cursor;

import java.util.HashMap;
import java.util.Map;

import data.sp.lqmsp.LqmSpEntity;
import data.sp.lqmsp.LqmSpKeyEntity;
import utils.LogUtil;

public final class LqmSpKeyUtil {

    public static <T> T getEntity(LqmSpEntity<T> table, SharedPreferences sharedPreferences) throws Throwable {
        T entity = table.createEntity();
        HashMap<String, LqmSpKeyEntity> spkeyMap = table.getSpkeyMap();
        for (Map.Entry<String, LqmSpKeyEntity> entry : spkeyMap.entrySet()) {
            String entryKey = entry.getKey();
            LqmSpKeyEntity lqmSpKeyEntity = spkeyMap.get(entryKey);
            Object spValue = getSharedSpValue(sharedPreferences, lqmSpKeyEntity.getFieldType(), lqmSpKeyEntity.getKeyName(), lqmSpKeyEntity.getDefaultValue());
            lqmSpKeyEntity.setValueFromSp(entity, spValue);
        }
        return entity;
    }

    private static Object getSharedSpValue(SharedPreferences sharedPreferences, Class<?> type, String key, String defValue) {
        LogUtil.wtf(type.getName());
        if (String.class.equals(type)) {
            return sharedPreferences.getString(key, (String) defValue);
        } else if (Boolean.class.equals(type)) {
            return sharedPreferences.getBoolean(key, Boolean.valueOf(defValue));
        } else if (Float.class.equals(type)) {
            return sharedPreferences.getFloat(key, Float.valueOf(defValue));
        } else if (Integer.class.equals(type)) {
            return sharedPreferences.getInt(key, Integer.valueOf(defValue));
        } else if (Long.class.equals(type)) {
            return sharedPreferences.getLong(key, Long.valueOf(defValue));
        } else if ("boolean".equals(type.getName())) {
            return sharedPreferences.getBoolean(key, Boolean.valueOf(defValue));
        } else if ("long".equals(type.getName())) {
            return sharedPreferences.getLong(key, Long.valueOf(defValue));
        } else if ("int".equals(type.getName())) {
            return sharedPreferences.getInt(key, Integer.valueOf(defValue));
        } else if ("float".equals(type.getName())) {
            return sharedPreferences.getFloat(key, Float.valueOf(defValue));
        }
        return null;
    }
}
