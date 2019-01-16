import com.vdurmont.emoji.EmojiParser;
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
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":school:ИПК Универ")));
        keyboardFirstRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":clock830:Контроль доступа")));
        keyboardFirstRow.add(new KeyboardButton( "Опросы"));
        keyboardSecondRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":bar_chart:Статистика")));
        keyboardSecondRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":newspaper:Для абитуриентов")));
        keyboardSecondRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":telephone_receiver:Контакты")));
        keyboardThirdRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":email:Блог ректора")));
        keyboardRowList.add(keyboardFirstRow);
        keyboardRowList.add(keyboardSecondRow);
        keyboardRowList.add(keyboardThirdRow);
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
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        KeyboardRow keyboardThirdRow = new KeyboardRow();
        KeyboardRow keyboardFourthRow = new KeyboardRow();
        KeyboardRow keyboardFifthRow = new KeyboardRow();
        KeyboardRow keyboardSixthRow = new KeyboardRow();
        if (telegrambotsql.getStatus(sendMessage.getChatId()) == 1){
            keyboardFirstRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":100:Текущие оценки")));
            keyboardFirstRow.add(new KeyboardButton("Успеваемость"));
            keyboardSecondRow.add(new KeyboardButton("Задолжность по оплате"));
            keyboardThirdRow.add(new KeyboardButton("Расписание занятий"));
            keyboardThirdRow.add(new KeyboardButton("Расписание экзаменов"));
            keyboardFourthRow.add(new KeyboardButton("УМКД"));
            keyboardFourthRow.add(new KeyboardButton("Календарь"));
            keyboardFifthRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":telephone_receiver:Контакты эдвайзера")));
            keyboardFifthRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":key:")+"Сброс пароля"));
            keyboardSixthRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":rewind:Вернуться на главную")));
        }else{

            keyboardFirstRow.add(new KeyboardButton("/bla person"));
            keyboardFirstRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":rewind:Вернуться на главную")));
        }
        keyboardRowList.add(keyboardFirstRow);
        keyboardRowList.add(keyboardSecondRow);
        keyboardRowList.add(keyboardThirdRow);
        keyboardRowList.add(keyboardFourthRow);
        keyboardRowList.add(keyboardFifthRow);
        keyboardRowList.add(keyboardSixthRow);
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
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":date:Выписка на месяц")));
        keyboardFirstRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":date:Выписка на прошлый месяц")));
        keyboardSecondRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":rewind:Вернуться на главную")));
        keyboardRowList.add(keyboardFirstRow);
        keyboardRowList.add(keyboardSecondRow);
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
            keyboardFirstRow.add(new KeyboardButton("fd"));
            keyboardFirstRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":rewind:Вернуться на главную")));
        }else{
            keyboardFirstRow.add(new KeyboardButton("/bla person"));
            keyboardFirstRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":rewind:Вернуться на главную")));
        }
        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }
}
