package cn.bugstack.ai.mcp.client.proxy.trigger;

import cn.bugstack.ai.mcp.client.proxy.api.IOpenAiApiProxy;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/v1/")
public class OpenAiApiProxyController {

    @Resource
    private IOpenAiApiProxy openAiApiProxy;

    /**
     * curl http://127.0.0.1:8771/v1/chat/completions
     */
    @RequestMapping(value = "chat/completions", method = RequestMethod.POST)
    public Object completions(@RequestBody Object request) {
        log.info("请求入参：{}", JSON.toJSONString(request));
        return openAiApiProxy.completions(request).blockingGet();
        /**
         * // execute()返回完整响应
         * Response<Object> response = call.execute();
         * Object data = response.body();        // 实际数据
         * int statusCode = response.code();     // HTTP状态码
         * boolean success = response.isSuccessful(); // 是否成功
         *
         * // blockingGet()直接返回数据
         * Object data = single.blockingGet();   // 直接获取结果
         * // 状态码和错误信息被RxJava内部处理
         */
    }

}
