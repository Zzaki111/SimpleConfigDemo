import com.deepwise.cloud.listener.IConfigScanerListener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @Author: Zzaki
 * @Description:
 * @Date: Created on 2018/5/29
 * @Company: DeepWise
 */
public class test {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Field field = demo.class.getDeclaredField("field");
        System.out.println(field.toString());
        Field field1 = IConfigScanerListener.class.getDeclaredField("tttt");
        System.out.printf(field1.toString());
        Object obj = field1.get(null);


    }


}
class demo{
    private String field;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
