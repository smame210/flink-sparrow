import cn.hutool.core.io.resource.ResourceUtil;
import com.alibaba.fastjson2.JSONObject;
import com.sparrow.support.script.ScriptExecutor;
import com.sparrow.support.script.ScriptExecutorEnum;
import com.sparrow.support.script.ScriptTypeEnum;
import com.sparrow.support.script.java.JavaScriptExecuteConfig;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

/**
 * @author daitf
 * @date 2024/8/29
 */
class JavaExecuteTest {
    @Test
    void testJavaScriptExecute() throws Exception {
        String script = ResourceUtil.readStr("JavaScript.java", StandardCharsets.UTF_8);

        ScriptExecutor executor = ScriptExecutorEnum.getExecutorByType(ScriptTypeEnum.JAVA);
        JavaScriptExecuteConfig config = new JavaScriptExecuteConfig();
        config.setId("1");
        config.setScript(script);
        config.setArgNames(new String[]{"args"});
        config.setArgTypes(new Class[]{JSONObject.class});
        boolean validated = executor.validate(config);
        System.out.println("script validate result: " + validated);

        executor.load(config);
        Object execute = executor.execute(config, new JSONObject());
        System.out.println("execute result: " + execute);
    }
}
