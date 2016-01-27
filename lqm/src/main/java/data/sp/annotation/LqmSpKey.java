package data.sp.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Hack on 2016/1/14.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface LqmSpKey {

    // name for this key
    String name() default "";

    // default value for this key as a string
    // exp: "1", "false", "-1.F", "${calculate()}"
    String defaultValue() default "";

    // serializer for this key
    Class<?> serializer() default Void.class;

    // whether we should generate OnUpdateListener & setter
    boolean onUpdate() default false;
}
