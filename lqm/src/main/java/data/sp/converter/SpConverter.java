package data.sp.converter;

/**
 * Created by Hack on 2016/1/14.
 */
public interface SpConverter {

    //sp的类型*********************************************************
    public final String defal_tb_dev = "dev_";
    public final String defal_tb_acc = "acc_";
    public final String defal_tb_permanent = "permanent_";
    //*********************************************************sp的类型

    public String getSpTableName();

    public String getSpEditKey();

    public Object defalValue();

    public Object getSpEditValue();

    public Object setNullValue();
}
