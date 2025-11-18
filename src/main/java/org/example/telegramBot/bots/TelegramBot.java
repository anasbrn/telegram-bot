package org.example.telegramBot.bots;

import jakarta.annotation.PostConstruct;
import org.example.telegramBot.web.AIAgentController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.*;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Value("${telegram.api.token}")
    private String token;
    @Value("${telegram.bot.username}")
    private String botUsername;
    private final AIAgentController aiAgentController;

    public TelegramBot(AIAgentController aiAgentController) {
        this.aiAgentController = aiAgentController;
    }

    @Override
    public void onUpdateReceived(Update request) {
        if (!request.hasMessage()) return;
        String messageText = request.getMessage().getText();
        String chatId = request.getMessage().getChatId().toString();

        sendTextMessage(chatId, messageText);
    }

    // method for test connectivity
//    @Override
//    public void onUpdateReceived(Update update) {
//        System.out.println("Received update: " + update);  // DEBUG
//
//        if (update.hasMessage() && update.getMessage().hasText()) {
//            String chatId = update.getMessage().getChatId().toString();
//            String text = update.getMessage().getText();
//            // Simple echo bot logic
//            SendMessage message = new SendMessage();
//            message.setChatId(chatId);
//            message.setText("Bot Connected!");
//            try {
//                execute(message);
//            } catch (TelegramApiException e) {
//                System.err.println(e.getMessage());
//            }
//        }
//    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    private void sendTextMessage(String chatId, String messageText) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendActionType(chatId);
        // reponse from our aiAgent
        sendMessage.setText(aiAgentController.askAgent(messageText));
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendActionType(String chatId) {
        try {
            SendChatAction sendChatAction = new SendChatAction();
            sendChatAction.setChatId(chatId);
            sendChatAction.setAction(ActionType.TYPING);
            execute(sendChatAction);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    private void startBot() throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(this);
        System.out.println("Bot started!");
    }
}
