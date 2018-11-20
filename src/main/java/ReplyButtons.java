import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ReplyButtons {
    //knopka v klave
    public static void firstButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboardRowList = new ArrayList<KeyboardRow>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("ИПК Универ"));
        keyboardFirstRow.add(new KeyboardButton("/SKUD"));
        keyboardFirstRow.add(new KeyboardButton("/Anketa"));
        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);


    }
    public static void UniverButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboardRowList = new ArrayList<KeyboardRow>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        if (telegrambotsql.getStatus(sendMessage.getChatId()) == 1){
            keyboardFirstRow.add(new KeyboardButton("Текущие оценки"));
            keyboardFirstRow.add(new KeyboardButton("Успеваемость"));
            keyboardFirstRow.add(new KeyboardButton("Расписание"));
            keyboardFirstRow.add(new KeyboardButton("Расписание экзаменов"));
            keyboardFirstRow.add(new KeyboardButton("Файлы преподователя"));
            keyboardFirstRow.add(new KeyboardButton("Академический календарь"));
            keyboardFirstRow.add(new KeyboardButton("/back"));
        }else{

            keyboardFirstRow.add(new KeyboardButton("/bla person"));
            keyboardFirstRow.add(new KeyboardButton("/bla2 person"));
        }
        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }
    public static void SKUDButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboardRowList = new ArrayList<KeyboardRow>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        if (telegrambotsql.getStatus(sendMessage.getChatId()) == 1){
            keyboardFirstRow.add(new KeyboardButton(""));
            keyboardFirstRow.add(new KeyboardButton("/sfdsfgdsdas"));
        }else{
            keyboardFirstRow.add(new KeyboardButton("/bla person"));
            keyboardFirstRow.add(new KeyboardButton("/bla2 person"));
        }
        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }
    public static void CodeButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboardRowList = new ArrayList<KeyboardRow>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        if (telegrambotsql.getStatus(sendMessage.getChatId()) == 1){
            keyboardFirstRow.add(new KeyboardButton(""));
            keyboardFirstRow.add(new KeyboardButton("/dasdasd"));
        }else{
            keyboardFirstRow.add(new KeyboardButton("/bla person"));
            keyboardFirstRow.add(new KeyboardButton("/bla2 person"));
        }
        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }
}
