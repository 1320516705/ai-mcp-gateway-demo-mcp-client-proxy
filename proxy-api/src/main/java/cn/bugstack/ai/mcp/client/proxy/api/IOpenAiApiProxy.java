package cn.bugstack.ai.mcp.client.proxy.api;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * <a href="https://dash.cloudflare.com/">可代理api</a>
 */
public interface IOpenAiApiProxy {
//    这个接口有实现，但是是由Retrofit框架在运行时动态生成的代理实现，而不是我们手写的显式实现类。 这正是Retrofit框架的核心价值之一——通过注解驱动的方式减少样板代码。

    /**
     *    // 当你调用时
     *    openAiApiProxy.completions(request);
     *    // 实际执行的是Retrofit生成的代理代码，大致相当于：
     *    // 1. 解析@POST注解 -> 构造POST请求
     *    // 2. 解析@Body注解 -> 将request对象序列化为JSON
     *    // 3. 发送HTTP请求到 baseUrl + "v1/chat/completions"
     *    // 4. 接收响应并反序列化
     */

    @POST("v1/chat/completions")
    Single<Object> completions(@Body Object request);

}
