package org.example.mcpserver.web;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Arrays;

@RestController
@RequestMapping("/api")
public class AIAgentController {
    private final ChatClient chatClient;

    public AIAgentController(ChatClient.Builder builder, ChatMemory memory, ToolCallbackProvider tools) {
        Arrays.stream(tools.getToolCallbacks()).forEach(callbackTool -> {
            System.out.println("========tools=========");
            System.out.println(callbackTool.getToolDefinition().name());
        });
        this.chatClient = builder
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(memory).build()
                )
                .defaultToolCallbacks(tools)
                .build();
    }

    @GetMapping("/ask")
    public Flux<String> askAgent(@RequestParam(name = "question") String question) {
        return chatClient
                .prompt()
                .user(question)
                .stream()
                .content();
    }
}
