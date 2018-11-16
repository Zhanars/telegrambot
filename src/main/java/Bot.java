import buttons.ReplyButtons;
import buttons.setInline;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Bot extends TelegramLongPollingBot {
//Запуск

String verification = "";
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
//Получает сообщение с чата
    public void onUpdateReceived(Update update) {
        Model model = new Model();
        Message message = update.getMessage();
        CallbackQuery callbackQuery = update.getCallbackQuery();
System.out.println(message);
        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "/help":
                    sendMsg(message, "Чем могу помочь?");
                    break;
                case "/setting":
                    sendMsg(message, "Что будем настраивать?");
                    break;
                case "/start":
                    sendMsg(message, "Здравствуйте! Введите иин!");
                    break;
                case "/Univer":

                    try {
                        sendMsg(message, Univer.IIN(telegrambotsql.getIIN(message.getChatId())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case "/SKUD":
                    //sendMsg(message, "Введите иин");
                    try {
                        sendMsg(message, MSSQL.getCount(telegrambotsql.getIIN(message.getChatId())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    break;
                default:
                    try {
                        sendMsg(message, telegrambotsql.botsql(message.getText(), message.getChatId(),message.getText().length()));
                    //sendMsg(message, Univer.IIN((message.getText())));
                   // sendMsg(message, MSSQL.getCount(message.getText()));

                } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

            }
        } else if (update.hasCallbackQuery()) {
            String btnMsg = update.getCallbackQuery().getData();
            try {
                answerCallbackQuery(callbackQuery.getId(), Univer.IIN(btnMsg));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }
// otvet na nazhatie knopki()
    private void answerCallbackQuery(String callbackId, String message) {
        AnswerCallbackQuery answer = new AnswerCallbackQuery();
        answer.setCallbackQueryId(callbackId);
        answer.setText(message);
        answer.setShowAlert(true);
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
//otvet na soobshenie
    private void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {

          //  setInline.setInline(sendMessage);

            ReplyButtons.firstButtons(sendMessage);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String getBotUsername() {
        return "Atudemo_bot";
    }

    public String getBotToken() {
        return "745779362:AAEFky83gaEOP4aB8RjUHq4_BcW7hCYSB68";
    }
}
