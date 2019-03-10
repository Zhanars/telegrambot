import com.vdurmont.emoji.EmojiParser;
import firstmenu.Configuration;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class setInline {
    public static synchronized void setInline(SendMessage sendMessage) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttons1 = new ArrayList<>();
        buttons1.add(new InlineKeyboardButton().setText(EmojiParser.parseToUnicode(":thumbsup:Да")).setCallbackData("yes"));
        buttons1.add(new InlineKeyboardButton().setText(EmojiParser.parseToUnicode(":thumbsdown:Нет")).setCallbackData("no"));
        buttons.add(buttons1);
        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(buttons);
        sendMessage.setReplyMarkup(markupKeyboard);
    }
    public static synchronized void setGoogle(SendMessage sendMessage) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttons1 = new ArrayList<>();
        buttons1.add(new InlineKeyboardButton().setText(EmojiParser.parseToUnicode("Перейти к форме")).setUrl(Configuration.getGoogleForm()));
        buttons.add(buttons1);
        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(buttons);
        sendMessage.setReplyMarkup(markupKeyboard);
    }
    public static synchronized void setSubject(SendMessage sendMessage, String[][] Record) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        int rowCount = Record.length;
        for (int i=0; i < rowCount; i++) {
            List<InlineKeyboardButton> button = new ArrayList<>();
            button.add(new InlineKeyboardButton().setText(Record[i][0]).setCallbackData("SubjectId:" + Record[i][1]));
            buttons.add(button);
        }
        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(buttons);
        sendMessage.setReplyMarkup(markupKeyboard);
    }
    public static synchronized void setTeacher(SendMessage sendMessage, String[][] Record) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        int rowCount = Record.length;
        for (int i=0; i < rowCount; i++) {
            List<InlineKeyboardButton> button = new ArrayList<>();
            button.add(new InlineKeyboardButton().setText(Record[i][0]).setCallbackData("TeachId:" + Record[i][1] + "SubjId:" + Record[i][2]));
            buttons.add(button);
        }
        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(buttons);
        sendMessage.setReplyMarkup(markupKeyboard);
    }
    public static synchronized void setFiles(SendMessage sendMessage, String[][] Record) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        int rowCount = Record.length;
        for (int i=0; i < rowCount; i++) {
            List<InlineKeyboardButton> button = new ArrayList<>();
            button.add(new InlineKeyboardButton().setText(Record[i][0]).setCallbackData("File:" + Record[i][1]+"Teach:"+Record[i][2]));
            buttons.add(button);
        }
        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(buttons);
        sendMessage.setReplyMarkup(markupKeyboard);
    }
    public static synchronized void setAbiturient(SendMessage sendMessage){
        String[][] record = api.getDocs();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        int rowCount = record.length;
        for (int i=0; i < rowCount; i++) {
            List<InlineKeyboardButton> button = new ArrayList<>();
            button.add(new InlineKeyboardButton().setText(record[i][1]).setUrl("http://telegram.atu.kz/" + record[i][3]));
            //button.add(new InlineKeyboardButton().setText(record[i][1]).setUrl("https://ya.ru"));
            buttons.add(button);
        }
        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(buttons);
        sendMessage.setReplyMarkup(markupKeyboard);
    }

    public static synchronized void setQuiz(SendMessage sendMessage) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttons1 = new ArrayList<>();
        //
        //Надо настроить URL на форму анкетирования
        buttons1.add(new InlineKeyboardButton().setText(EmojiParser.parseToUnicode("Перейти к форме")).setUrl("http://ya.ru"));
        buttons.add(buttons1);
        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(buttons);
        sendMessage.setReplyMarkup(markupKeyboard);
    }
}
