package data.sp.lqmsp;

import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;

import data.sp.annotation.LqmSp;

/**
 * Created by Hack on 2016/1/18.
 */
public class LqmSpEntity<T> {

    private final LqmSpManager spManger;
    private final Class<?> spTabEntity;
    private final LinkedHashMap<String, LqmSpKeyEntity> spkeyMap;
    private final String tableName;
    private final int preferenceMode;
    private Constructor<T> constructor;

    public LqmSpEntity(LqmSpManager spManager, Class<T> spTabEntity) throws Throwable {
        this.spManger = spManager;
        this.spTabEntity = spTabEntity;
        this.constructor = spTabEntity.getConstructor();
        this.constructor.setAccessible(true);
        LqmSp lqmSp = spTabEntity.getAnnotation(LqmSp.class);
        this.tableName = lqmSp.name();
        this.preferenceMode = lqmSp.sharedPreferenceMode();
        this.spkeyMap = LqmSpUtil.findSpkeyMap(spTabEntity);


//        for (Map.Entry<String, LqmSpKeyEntity> entry : spkeyMap.entrySet()) {
//            LogUtil.wtf("spkeyMap = " + entry.getKey());
//        }
//        LogUtil.wtf("tableName = " + tableName);
//        LogUtil.wtf("preferenceMode = " + preferenceMode);
    }

    public static <T> LqmSpEntity<T> get(LqmSpManager spManager, Class<T> entityType) throws Throwable {
        LqmSpEntity<T> entity = new LqmSpEntity<>(spManager, entityType);
        return entity;
    }

    public T createEntity() throws Throwable {
        return this.constructor.newInstance();
    }

    public String getTableName() {
        return tableName;
    }

    public int getPreferenceMode() {
        return preferenceMode;
    }

    public LinkedHashMap<String, LqmSpKeyEntity> getSpkeyMap() {
        return spkeyMap;
    }

}
