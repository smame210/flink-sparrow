import com.alibaba.fastjson2.JSONObject;

public class JavaTemplate {

    /**
     * must have a main method
     *
     * @param data       a Object class data as input and parameter name must be "data"
     * @return return    a Object class data as output
     */
    public Object main(Object data) {
        // TODO: write your code here
        JSONObject dataJson = (JSONObject) data;
        Integer result = dataJson.getInteger("a");
        test1(result);
        return test2(result);
    }

    public void test1(Integer data) {
        System.out.println(data);
    }

    public Integer test2(Integer data) {
        return data == null ? null : data + 1;
    }
}