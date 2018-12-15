import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class setInline {
    public static void setInline(SendMessage sendMessage) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttons1 = new ArrayList<>();
        buttons1.add(new InlineKeyboardButton().setText(EmojiParser.parseToUnicode(":thumbsup:Да")).setCallbackData("yes"));
        buttons1.add(new InlineKeyboardButton().setText(EmojiParser.parseToUnicode(":thumbsdown:Нет")).setCallbackData("no"));
        buttons.add(buttons1);
        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(buttons);
        sendMessage.setReplyMarkup(markupKeyboard);
    }
    public static void setSubject(SendMessage sendMessage, String[][] Record) {
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
    public static void setTeacher(SendMessage sendMessage, String[][] Record) {
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
    public static void setFiles(SendMessage sendMessage, String[][] Record) {
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
}
