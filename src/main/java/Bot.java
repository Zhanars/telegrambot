import com.itextpdf.text.DocumentException;
import com.vdurmont.emoji.EmojiParser;
import okhttp3.*;
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

import javax.swing.text.html.HTML;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class Bot extends TelegramLongPollingBot {
//Запуск
 final String smiling_face_with_heart_eyes = new String(Character.toChars(0x1F60D));
    final String winking_face = new String(Character.toChars(0x1F609));
    final String bouquet = new String(Character.toChars(0x1F490));
    final String party_popper = new String(Character.toChars(0x1F389));
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
        Message message = update.getMessage();
        CallbackQuery callbackQuery = update.getCallbackQuery();
        System.out.println(message);
        if (message != null && message.hasText()) {
            if (telegrambotsql.checkChatId(message.getChatId())) {
                switch (message.getText()) {
                    case "/start":
                            sendMsg(message,smiling_face_with_heart_eyes + "Здравствуйте " +telegrambotsql.getfromBotsName(message.getChatId()),1);
                        break;
                    case "\uD83C\uDFEBИПК Универ":
                            sendMsg(message, "ИПК Универ",11);
                        break;
                    case "\uD83D\uDCAFТекущие оценки":
                        try {
                            sendMsg(message, Univer.getAttendanceforweek(telegrambotsql.getIIN(message.getChatId()),Univer.getProgressforAttendence(telegrambotsql.getIIN(message.getChatId()))),11);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "Успеваемость":
                        pdfMaker.createUniverTranskriptPdf(telegrambotsql.getfromBotsName(message.getChatId()),"Транскрипт", Univer.getTranskript(telegrambotsql.getIIN(message.getChatId())));
                        sendFile(message.getChatId(),telegrambotsql.getfromBotsName(message.getChatId())+".pdf");
                        break;
                    case "Расписание":
                        break;
                    case "Расписание экзаменов":
                        pdfMaker.createUniverExamSchudelePdf(telegrambotsql.getfromBotsName(message.getChatId()),"Расписание экзаменов", Univer.getExamSchedule(telegrambotsql.getIIN(message.getChatId())));
                        sendFile(message.getChatId(),telegrambotsql.getfromBotsName(message.getChatId())+".pdf");
                        break;
                    case "Файлы преподователя":
                        break;
                    case "Опросы":
                        if (!Quiz.hasAnswerQuiz(message.getChatId(), "Вам нравится наш бот?")) {
                            sendMsg(message, "Вам нравится наш бот?", 41);
                        } else {
                            sendMsg(message, "Вы уже прошли опрос", 1);
                        }
                        break;
                    case "Календарь":
                        try {
                            pdfMaker.createNewPdf(telegrambotsql.getfromBotsName(message.getChatId()),"Академический календарь", Univer.getAcademcal(telegrambotsql.getIIN(message.getChatId())));
                            sendFile(message.getChatId(),telegrambotsql.getfromBotsName(message.getChatId())+".pdf");
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        break;
                    case "\uD83D\uDCDEКонтакты":
                        break;
                    case "\uD83D\uDCDEКонтакты эдвайзера":
                        sendMsg(message,Univer.getAdvicer(telegrambotsql.getIIN(message.getChatId())),11);

                        break;
                    case "\uD83D\uDCCAСтатистика":
                        break;
                    case "✉Блог ректора":
                        break;
                    case "Новости с универа":
                        break;
                    case "\uD83D\uDCF0Новости":
                        break;
                    case "\uD83D\uDCC5Выписка на месяц":
                        try {
                            pdfMaker.createGuardPdf(telegrambotsql.getfromBotsName(message.getChatId()),"Контроль прохода", RusGuard.getReportForMonth(telegrambotsql.getIIN(message.getChatId()),0),0);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        sendFile(message.getChatId(),telegrambotsql.getfromBotsName(message.getChatId())+".pdf");
                        break;
                    case "\uD83D\uDCC5Выписка на прошлый месяц":
                        try {
                            pdfMaker.createGuardPdf(telegrambotsql.getfromBotsName(message.getChatId()),"Контроль прохода", RusGuard.getReportForMonth(telegrambotsql.getIIN(message.getChatId()),-1),-1);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        sendFile(message.getChatId(),telegrambotsql.getfromBotsName(message.getChatId())+".pdf");
                        break;
                    case "\uD83D\uDD63Контроль доступа":
                        try {
                            sendMsg(message, RusGuard.getCount(telegrambotsql.getIIN(message.getChatId())),21);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "⏪Вернуться на главную":
                        sendMsg(message, "Вы вернулись на главную", 1);
                        break;
                    case "УМКД":
                        sendMsg(message,"Выберите предмет",16);
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
                    sendMsg(message, EmojiParser.parseToUnicode(":lock:Здравствуйте! Для работы с ботом введите иин!"),0);
                }
            }
        } else if (update.hasCallbackQuery()) {
            if (callbackQuery.getData().indexOf("SubjectId:") >= 0){
                String Subject = callbackQuery.getData().substring(10);
                sendMsg(callbackQuery.getMessage(),Subject,161);
            } else if (callbackQuery.getData().indexOf("TeachId:") >= 0){
                sendMsg(callbackQuery.getMessage(),callbackQuery.getData(),162);
            } else if (callbackQuery.getData().indexOf("File:") >= 0){
                int sIn = callbackQuery.getData().indexOf("Teach:");
                String file = callbackQuery.getData().substring(5, sIn);
                String TeacherId = callbackQuery.getData().substring(sIn+6);
                answerCallbackQuery(callbackQuery.getId(), "Пожалуйста подождите, идет загрузка!");
                umkd.getUMKD(callbackQuery.getMessage().getChatId(), TeacherId, file);
            }else {
                answerCallbackQuery(callbackQuery.getId(), Quiz.saveQuiz(callbackQuery.getMessage(), callbackQuery.getData()));
                sendMsg(callbackQuery.getMessage(), "Спасибо за ответ!\nНам очень важен ваше мнение.", 1);
            }
        }
    }

// otvet na nazhatie knopki()
    private void answerCallbackQuery(String callbackId, String text) {
        AnswerCallbackQuery answer = new AnswerCallbackQuery();
        answer.setCallbackQueryId(callbackId);
        answer.setText(text);
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
        sendMessage.setParseMode("Markdown");
        sendMessage.setText(text);
            try {
                if (button == 1){
                    ReplyButtons.firstButtons(sendMessage);
                } else if (button == 11){
                    ReplyButtons.UniverButtons(sendMessage);
                } else if (button == 21){
                    ReplyButtons.SKUDButtons(sendMessage);
                } else if (button == 31){
                    ReplyButtons.CodeButtons(sendMessage);
                } else if (button == 41){
                    setInline.setInline(sendMessage);
                } else if (button == 16){
                    setInline.setSubject(sendMessage,Univer.getSubject(telegrambotsql.getIIN(message.getChatId())));
                } else if (button == 161){
                    setInline.setTeacher(sendMessage,Univer.getTeachers(telegrambotsql.getIIN(message.getChatId()),text));
                    sendMessage.setText("Выберите преподавателя");
                } else if (button == 162){
                    int sIn = text.indexOf("SubjId:");
                    String TeacherId = text.substring(8, sIn);
                    String SubjectId = text.substring(sIn+7);
                    setInline.setFiles(sendMessage,Univer.getFiles(TeacherId,SubjectId));
                    sendMessage.setText("Выберите документ");
                }
                execute(sendMessage);

            } catch (TelegramApiException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
    }
    public static void sendFile(Long ChatId,String fileSource){
        String url = "https://api.telegram.org/bot745779362:AAEFky83gaEOP4aB8RjUHq4_BcW7hCYSB68/sendDocument?chat_id="+ChatId;
        OkHttpClient client = new OkHttpClient();
        File sourceFile = new File(fileSource);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("document", sourceFile.getName(), RequestBody.create(MediaType.parse("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"), sourceFile))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            String responseString = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new File(fileSource).delete();
    }

    public String getBotUsername() {
        return "Atudemo_bot";
    }

    public String getBotToken() {
        return "745779362:AAEFky83gaEOP4aB8RjUHq4_BcW7hCYSB68";
    }
}
