import com.vdurmont.emoji.EmojiParser;
import firstmenu.Configuration;
import okhttp3.*;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;


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

                        sendMsg(message, smiling_face_with_heart_eyes + "Здравствуйте " + telegrambotsql.getfromBotsName(message.getChatId()), 1);

                        break;
                    case "\uD83C\uDFEBИПК Универ":
                        sendMsg(message, "Информационно-программный комплекс «Univer.atu.kz» (UNIVER) представляет собой систему администрирования и управления учебной деятельностью университета с полным циклом охвата учебного процесса.", 11);
                        break;
                    case "\uD83D\uDCAFТекущие оценки":
                        try {
                            System.out.println(Univer.getAttendanceforweek(telegrambotsql.getIIN(message.getChatId()), Univer.getProgressforAttendence(telegrambotsql.getIIN(message.getChatId()))));
                            if (!Univer.getAttendanceforweek(telegrambotsql.getIIN(message.getChatId()), Univer.getProgressforAttendence(telegrambotsql.getIIN(message.getChatId()))).equals("")) {
                                sendMsg(message, Univer.getAttendanceforweek(telegrambotsql.getIIN(message.getChatId()), Univer.getProgressforAttendence(telegrambotsql.getIIN(message.getChatId()))), 11);
                            } else {
                                sendMsg(message, "Подготовка файла, подождите", 11);
                                pdfMaker.AttendenceCatch(telegrambotsql.getfromBotsName(message.getChatId()), "Оценки за семестр", Univer.getProgressforAttendence(telegrambotsql.getIIN(message.getChatId())), Univer.getSemestr(telegrambotsql.getIIN(message.getChatId())));
                                sendFile(message.getChatId(), telegrambotsql.getfromBotsName(message.getChatId()) + ".pdf");

                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "\uD83D\uDD16Транскрипт":
                        sendMsg(message, "Подготовка файла, подождите", 11);
                        pdfMaker.createUniverTranskriptPdf(telegrambotsql.getfromBotsName(message.getChatId()), "Транскрипт", Univer.getTranskript(telegrambotsql.getIIN(message.getChatId())));
                        sendFile(message.getChatId(), telegrambotsql.getfromBotsName(message.getChatId()) + ".pdf");
                        break;
                    case "\uD83D\uDD11Сброс пароля":
                        String email = Univer.getEmail(telegrambotsql.getIIN(message.getChatId()));
                        if (!email.equals("")) {
                            telegrambotsql.sendCodeEmail(email, message.getChatId());
                            int ind1 = email.indexOf("_");
                            if (ind1 >= 0) {
                                email = email.substring(0, ind1) + "-" + email.substring(ind1 + 1);
                            }
                            sendMsg(message, "Bведите код подтверждения отправленную на почту " + email + ". Email можно изменить в системе универ.", 11);
                        } else {
                            sendMsg(message, "Заполните поле Email в системе универ!", 11);
                        }
                        break;
                    case "\uD83D\uDDFAРасписание занятий":
                        sendMsg(message, "Подготовка файла, подождите", 11);
                        pdfMaker.Schedulepdf(telegrambotsql.getfromBotsName(message.getChatId()), "Расписание занятий", Univer.getSchedule(telegrambotsql.getIIN(message.getChatId())), Univer.getSemestr(telegrambotsql.getIIN(message.getChatId())));
                        sendFile(message.getChatId(), telegrambotsql.getfromBotsName(message.getChatId()) + ".pdf");
                        break;
                    case "\uD83D\uDDFAРасписание экзаменов":
                        sendMsg(message, "Подготовка файла, подождите", 11);
                        pdfMaker.createUniverExamSchudelePdf(telegrambotsql.getfromBotsName(message.getChatId()), "Расписание экзаменов", Univer.getExamSchedule(telegrambotsql.getIIN(message.getChatId())));
                        sendFile(message.getChatId(), telegrambotsql.getfromBotsName(message.getChatId()) + ".pdf");
                        break;
                    case "Файлы преподователя":
                        break;
                    case "\uD83D\uDCCBОпросы":
                        //sendMsg(message, "В форме вы можете пройти анкетирование \n Напоминаем что все анонимно.", 51);
                        if (!Quiz.hasAnswerQuiz(message.getChatId(), "Вам нравится наш бот?")) {
                            sendMsg(message, "Вам нравится наш бот?", 41);
                        } else {
                            sendMsg(message, "Вы уже прошли опрос", 1);
                        }
                        break;
                    case "\uD83D\uDDD3Календарь":
                        try {
                            sendMsg(message, "Подготовка файла, подождите", 11);
                            pdfMaker.createNewPdf(telegrambotsql.getfromBotsName(message.getChatId()), "Академический календарь", Univer.getAcademcal(telegrambotsql.getIIN(message.getChatId())));
                            sendFile(message.getChatId(), telegrambotsql.getfromBotsName(message.getChatId()) + ".pdf");
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        break;

                    case "\uD83D\uDCDEКонтакты эдвайзера":
                        sendMsg(message, Univer.getAdvicer(telegrambotsql.getIIN(message.getChatId())), 11);
                        break;
                    case "\uD83D\uDCCAСтатистика":
                       // telegrambotsql.manageStatistics();
                        sendMsg(message,"Какой статистика вас интересует",4);
                        break;
                    case "По курсу":
                        sendMsg(message,"Какой рейтинг вас интересует",411);
                        break;
                    case "Рейтинг по GPA":
                        sendMsg(message,telegrambotsql.getRatingforCourse(telegrambotsql.getIIN(message.getChatId())),411);
                        break;
                    case "Отчет по общежитию":
                         sendMsg(message, "Подготовка файла, подождите", 4);
                         pdfMaker.createHostelPdf(api.getHostel());
                         sendFile(message.getChatId(), "hostel_report.pdf");
                        break;
                    case "\uD83D\uDC68\u200D\uD83C\uDF93 \uD83D\uDC69\u200D\uD83C\uDF93Список вакансий":
                        try {
                            String[] result = api.getVacancies(Univer.getSpeciality(telegrambotsql.getIIN(message.getChatId())));
                            for (int i = 0; i<result.length; i++){
                                sendMsg(message,result[i],1);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            sendMsg(message,"Пока вакансии нет",1);
                        }
                        break;
                    case "По факультету":
                        sendMsg(message,telegrambotsql.getRatingforFacultet(telegrambotsql.getIIN(message.getChatId())),411);
                        break;
                    case "По специальности":
                        sendMsg(message,telegrambotsql.getRatingforSpecial(telegrambotsql.getIIN(message.getChatId())),411);
                        break;
                    case "\uD83D\uDCF0Для абитуриентов":
                        sendMsg(message, "Выберите что Вас интересует", 10);
                        break;
                    case "\uD83D\uDCDEКонтакты":
                        sendMsg(message, "*АЛМАТИНСКИЙ ТЕХНОЛОГИЧЕСКИЙ УНИВЕРСИТЕТ* \n \uD83D\uDCDE: +7(727) 293-52-95, 221-88-08,\n 317-00-53, 293-52-96 \n \uD83C\uDFE0: 050012, г. Алматы, ул. Толе би 100 \n ✉️: rector@atu.kz", 1);
                        SendLocation sendLocation = new SendLocation();
                        sendLocation.setChatId(message.getChatId());
                        sendLocation.setLatitude((float) 43.252442);
                        sendLocation.setLongitude((float) 76.926615);
                        try {
                            execute(sendLocation);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "✉Обратная связь":
                        sendMsg(message, "В форме вы можете оставить предложения, замечания, благодарности, нарушения и т.д. \n Напоминаем что все анонимно.", 71);
                        break;
                    case "\uD83D\uDCC5Выписка на месяц":
                        try {
                            sendMsg(message, "Подготовка файла, подождите", 21);
                            pdfMaker.createGuardPdf(telegrambotsql.getfromBotsName(message.getChatId()), "Контроль прохода", RusGuard.getReportForMonth(telegrambotsql.getIIN(message.getChatId()), 0), 0);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        sendFile(message.getChatId(), telegrambotsql.getfromBotsName(message.getChatId()) + ".pdf");
                        break;
                    case "\uD83D\uDCC5Выписка на прошлый месяц":
                        try {
                            sendMsg(message, "Подготовка файла, подождите", 21);
                            pdfMaker.createGuardPdf(telegrambotsql.getfromBotsName(message.getChatId()), "Контроль прохода", RusGuard.getReportForMonth(telegrambotsql.getIIN(message.getChatId()), -1), -1);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        sendFile(message.getChatId(), telegrambotsql.getfromBotsName(message.getChatId()) + ".pdf");
                        break;
                    case "\uD83D\uDD63Контроль доступа":
                        try {
                            sendMsg(message, RusGuard.getCount(telegrambotsql.getIIN(message.getChatId())), 21);
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
                    case "\uD83D\uDCC1УМКД":
                        sendMsg(message, "Выберите предмет", 16);
                        break;
                    default:
                        try {
                            sendMsg(message, telegrambotsql.registration(message.getText(), message.getChatId(), message.getText().length()), 201);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                }
            } else {
                if (message.getText().equals("\uD83D\uDCF0Для абитуриентов")) {
                    sendMsg(message, "Выберите что Вас интересует", 10);
                } else if (message.getText().equals("\uD83D\uDCDEКонтакты")) {
                    sendMsg(message, "*АЛМАТИНСКИЙ ТЕХНОЛОГИЧЕСКИЙ УНИВЕРСИТЕТ* \n \uD83D\uDCDE: +7(727) 293-52-95, 221-88-08,\n 317-00-53, 293-52-96 \n \uD83C\uDFE0: 050012, г. Алматы, ул. Толе би 100 \n ✉️: rector@atu.kz \n \uD83D\uDD79 https://www.instagram.com/atu_media/ ", 0);
                    SendLocation sendLocation = new SendLocation();
                    sendLocation.setChatId(message.getChatId());
                    sendLocation.setLatitude((float) 43.252442);
                    sendLocation.setLongitude((float) 76.926615);
                    try {
                        execute(sendLocation);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (message.getText().equals("\uD83D\uDCF0Буклет")) {
                    sendFile(message.getChatId(), "1.pdf");
                } else if (message.getText().equals("⏪Вернуться на главную")){
                    sendMsg(message, "Вы вернулись на главную", 0);
                } else if (message.getText().equals("\uD83C\uDFACВидео")) {
                    sendMsg(message,"Файл подготовливается, подождите", 10);
                    SendVideo sendVideo = new SendVideo();
                    sendVideo.setChatId(message.getChatId());
                    File file = new File("Kazakhstan.mp4");
                    sendVideo.setVideo(file);
                    sendVideo.setCaption("ATUKazakhstan");
                    try {
                        execute(sendVideo);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (message.getText().length() == 12 && Univer.checkIINPersonalorStudent(message.getText()) > 0) {
                    try {
                        sendMsg(message, telegrambotsql.registration(message.getText(), message.getChatId(), message.getText().length()), 201);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    sendMsg(message, EmojiParser.parseToUnicode(":lock:Здравствуйте! Для работы с ботом введите иин!"), 0);
                }
            }
        } else if (message.hasContact() && (message.getContact().getFirstName().equals(message.getFrom().getFirstName())) && (message.getContact().getLastName().equals(message.getFrom().getLastName()))) {
            try {
                sendMsg(message, telegrambotsql.ContactBot(message.getChatId(), message.getFrom().getLastName(), message.getFrom().getFirstName(), message.getContact().getPhoneNumber()), 1);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else if (update.hasCallbackQuery()) {
            if (callbackQuery.getData().indexOf("SubjectId:") >= 0) {
                String Subject = callbackQuery.getData().substring(10);
                sendMsg(callbackQuery.getMessage(), Subject, 161);
            } else if (callbackQuery.getData().indexOf("TeachId:") >= 0) {
                sendMsg(callbackQuery.getMessage(), callbackQuery.getData(), 162);
            } else if (callbackQuery.getData().indexOf("File:") >= 0) {
                int sIn = callbackQuery.getData().indexOf("Teach:");
                String file = callbackQuery.getData().substring(5, sIn);
                String TeacherId = callbackQuery.getData().substring(sIn + 6);
                answerCallbackQuery(callbackQuery.getId(), "Пожалуйста подождите, идет загрузка!");
                umkd.getUMKD(callbackQuery.getMessage().getChatId(), TeacherId, file);
            } else {
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
    public void sendMsg(Message message, String text, int button) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId());
        //sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
            try {
                if (button == 1){
                    //Тут главый меню
                    ReplyButtons.firstButtons(sendMessage);
                }else if (button == 201){
                    //Тут контакт кнопки
                    ReplyButtons.ContactButtons(sendMessage);
                }
                else if (button == 0){
                    ReplyButtons.zeroButtons(sendMessage);
                } else if (button == 10){
                    //Тут кнопки для абитуриентов
                    setInline.setAbiturient(sendMessage);
                    sendMessage.setText(api.forAbiturient());
                } else if (button == 51){
                    //Тут анкетирование
                    setInline.setQuiz(sendMessage);
                    sendMessage.setText(api.forAbiturient());
                } else if (button == 11){
                    //Тут кнопки ИПК Универ
                    ReplyButtons.UniverButtons(sendMessage);
                } else if (button == 21){
                    //Тут кнопки электронной проходной
                    ReplyButtons.SKUDButtons(sendMessage);
                } else if (button == 31){
                    ReplyButtons.CodeButtons(sendMessage);
                } else if (button == 4){
                    //Тут кнопки статистики
                    ReplyButtons.staticButtons(sendMessage);
                } else if (button == 411){
                    //Тут кнопки рейтинг
                    ReplyButtons.ratingButtons(sendMessage);
                } else if (button == 41){
                    setInline.setInline(sendMessage);
                } else if (button == 16){
                    //Тут кнопки для выбора УМКД предметы
                    setInline.setSubject(sendMessage,Univer.getSubject(telegrambotsql.getIIN(message.getChatId())));
                } else if (button == 161){
                    //Тут кнопки для выбора преподавателя УМКД предметы
                    setInline.setTeacher(sendMessage,Univer.getTeachers(telegrambotsql.getIIN(message.getChatId()),text));
                    sendMessage.setText("Выберите преподавателя");
                } else if (button == 162){
                    //Тут кнопки для выбора УМКД предметы
                    int sIn = text.indexOf("SubjId:");
                    String TeacherId = text.substring(8, sIn);
                    String SubjectId = text.substring(sIn+7);
                    setInline.setFiles(sendMessage,Univer.getFiles(TeacherId,SubjectId));
                    sendMessage.setText("Выберите документ");
                } else if (button == 71) {
                    setInline.setGoogle(sendMessage);
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
    public static void sendFile(Long ChatId,String fileSource) {
        String url = "https://api.telegram.org/bot" + Configuration.getBottoken() + "/";
        if (!fileSource.equals("Kazakhstan.mp4")) {
            url += "sendDocument?chat_id=" + ChatId;
        } else {
            url += "sendVideo?chat_id=" + ChatId;
        }
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
        System.out.println(requestBody);
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
        if (!fileSource.equals("1.pdf") && !fileSource.equals("Kazakhstan.mp4"))
            new File(fileSource).delete();
    }

    public String getBotUsername() {
        return Configuration.getBotName();
    }

    public String getBotToken() {
        return Configuration.getBottoken();
    }
}