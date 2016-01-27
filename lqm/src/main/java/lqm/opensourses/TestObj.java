package lqm.opensourses;

import data.sp.annotation.LqmSp;
import data.sp.annotation.LqmSpKey;

/**
 * Created by Hack on 2016/1/27.
 */
@LqmSp(name = "whoseTheSmartOne", sharedPreferenceMode = 1)
public class TestObj {

    @LqmSpKey(name = "name", defaultValue = "Iam", serializer = TestObj.class, onUpdate = true)
    private String name;

    @LqmSpKey(name = "age", defaultValue = "25")
    private String age;

    @LqmSpKey(name = "birthday", defaultValue = "1991-01-01")
    private String birthday;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "TestObj{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", birthday='" + birthday + '\'' +
                '}';
    }
}
