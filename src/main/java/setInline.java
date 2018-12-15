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
        int colCount = 5;
        int rowCount = Record.length;
        System.out.println(Record.length);
        String subjId="";
        for (int i=1; i < rowCount; i++){
            if (!Record[i][colCount].equals(subjId)) {
                List<InlineKeyboardButton> button = new ArrayList<>();
                subjId = Record[i][colCount];
                button.add(new InlineKeyboardButton().setText(Record[i][4]).setCallbackData("SubjectId:"+subjId));
                buttons.add(button);
            }
        }

        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(buttons);
        sendMessage.setReplyMarkup(markupKeyboard);
    }
    public static void setTeacher(SendMessage sendMessage, String[][] Record, String Subject) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        int rowCount = Record.length;
        String teacherId="";
        for (int i=1; i < rowCount; i++){
            if (Record[i][5].equals(Subject)) {
                if (!Record[i][0].equals(teacherId)) {
                    List<InlineKeyboardButton> button = new ArrayList<>();
                    teacherId = Record[i][0];
                    button.add(new InlineKeyboardButton().setText(Record[i][3]).setCallbackData("TeachId:" + teacherId+"SubjId:" + Subject));
                    buttons.add(button);
                }
            }
        }

        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(buttons);
        sendMessage.setReplyMarkup(markupKeyboard);
    }
}
