package cn.bugstack.ai.mcp.client.proxy.test;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.util.Arrays;

/**
 * 协议对象；JSONRPCMessage
 * <p>
 * 测试说明；
 * 1. debug 启动服务端，8771 端口
 * 2. 在 OpenAiApiProxyController 执行接口方法的日志上打断点
 * 3. debug 调试 test 方法，观察 mcp 协议
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {
    /**
     *
     * Spring AI ChatClient
     *     ↓ (通过ToolCallbacks集成)
     * SyncMcpToolCallbackProvider
     *     ↓ (提供工具回调)
     * McpClient (MCP协议客户端)
     *     ↓ (通过SSE传输)
     * HttpClientSseClientTransport
     *     ↓ (网络连接)
     * MCP服务端
     */

    @Resource
    private ChatClient.Builder chatClientBuilder;

    @Test
    public void test() {
//        SyncMcpToolCallbackProvider：这是Spring AI提供的MCP工具回调提供者，负责将MCP协议的工具能力集成到ChatClient中。
        ChatClient chatClient = chatClientBuilder.defaultOptions(
                        OpenAiChatOptions.builder()
                                .model("gpt-4.1-mini")
                                .toolCallbacks(new SyncMcpToolCallbackProvider(sseMcpClient()).getToolCallbacks())
                                .build())
                .build();

        log.info("测试结果:{}", chatClient.prompt("有哪些工具可以使用").call().content());
    }

    public McpSyncClient sseMcpClient() {
//        HttpClientSseClientTransport：MCP协议的HTTP SSE传输层实现，负责与MCP服务端建立持久连接。
//        1. 建立HTTP连接 → 2. 升级到SSE → 3. 持续监听事件 → 4. 实时接收工具调用请求
        HttpClientSseClientTransport sseClientTransport = HttpClientSseClientTransport
                .builder("http://localhost:8701/sse")
                .build();

//        McpClient：MCP协议的客户端实现，负责与MCP服务端进行通信。
        McpSyncClient mcpSyncClient = McpClient.sync(sseClientTransport).requestTimeout(Duration.ofMinutes(360)).build();
        var init_sse = mcpSyncClient.initialize();
        log.info("Tool SSE MCP Initialized {}", init_sse);

        return mcpSyncClient;
    }

}
