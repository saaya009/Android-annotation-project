package data.sp.lqmsp;

import java.io.Closeable;

/**
 * Created by Hack on 2016/1/18.
 */
public interface LqmSpManager extends Closeable {

    boolean save(Object entity) throws Throwable;

    <T> T get(Class<T> entity) throws Throwable;

}
