import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import java.io.IOException;
import java.sql.*;


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
            if (telegrambotsql.checkChatId(message.getChatId())) {
                switch (message.getText()) {
                    case "/help":
                        sendMsg(message, "Чем могу помочь?",1);
                        break;
                    case "/setting":
                        sendMsg(message, "Что будем настраивать?",1);
                        break;
                    case "/start":
                            sendMsg(message, telegrambotsql.getfromBotsName(message.getChatId()),1);

                        break;
                    case "ИПК Универ":

                        try {
                            sendMsg(message, Univer.IIN(telegrambotsql.getIIN(message.getChatId())),11);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "Текущие оценки":
                        break;
                    case "Успеваемость":
                        break;
                    case "Расписание":
                        break;
                    case "Файлы преподователя":
                        break;
                    case "Академический календарь":
                        break;
                    case "Новости с универа":
                        break;
                    case "Новости":
                        break;
                    case "/SKUD":
                        //sendMsg(message, "Введите иин");
                        try {
                            sendMsg(message, MSSQL.getCount(telegrambotsql.getIIN(message.getChatId())),21);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        break;
                    case "/back":
                        sendMsg(message, "Вы вернулись на главную", 1);

                        break;
                    default:
                        try {
                            sendMsg(message, telegrambotsql.registration(message.getText(), message.getChatId(), message.getText().length()),1);

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                }
            } else {

                if(message.getText().length() == 12 && Univer.checkIINPersonalorStudent(message.getText())> 0 ) {
                    try {
                        sendMsg(message, telegrambotsql.registration(message.getText(), message.getChatId(), message.getText().length()),1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }else{
                    sendMsg(message, "Здравствуйте! Для работы с ботом введите иин!",0);
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
    private void sendMsg(Message message, String text, int button) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
            try {

                if (button == 1){
                    ReplyButtons.firstButtons(sendMessage);
                } else if (button == 11){
                    ReplyButtons.UniverButtons(sendMessage);
                } else if (button == 21){
                    ReplyButtons.SKUDButtons(sendMessage);
                }else if (button == 31){
                    ReplyButtons.CodeButtons(sendMessage);
                }
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
