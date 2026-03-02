package cn.bugstack.ai.mcp.client.proxy.config;

import io.micrometer.observation.ObservationRegistry;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.DefaultChatClientBuilder;
import org.springframework.ai.chat.client.observation.ChatClientObservationConvention;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Bean
    public ChatClient.Builder chatClientBuilder(OpenAiChatModel chatModel) {
        /**
         * OpenAiChatModel 底层规定的就是会讲请求发送到baseurl+/v1/chat/completions/，若项目将这个地址规定为代理的入口，那么请求就会被发送到代理入口
         */
        return new DefaultChatClientBuilder(chatModel, ObservationRegistry.NOOP, (ChatClientObservationConvention) null);
    }

}
