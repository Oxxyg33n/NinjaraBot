import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import twitter4j.*;

import java.util.ArrayList;
import java.util.List;

public class NinjaraBot extends TelegramLongPollingBot {

    public static final String UK_Flag = EmojiParser.parseToUnicode(":gb:");
    public static final String US_Flag = EmojiParser.parseToUnicode(":us:");
    public static final String RU_Flag = EmojiParser.parseToUnicode(":ru:");
    public static final String JP_Flag = EmojiParser.parseToUnicode(":jp:");
    public static final String EU_Flag = EmojiParser.parseToUnicode(":earth_africa:");

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()) {
            Message message = update.getMessage();

            String messageText = message.getText();
            if(message.getText().toLowerCase().equals("nintendo") || messageText.equals("#Nintendo")) {
                searchTwitterQuery(message, "#Nintendo");
            } else if(messageText.equals(US_Flag + " Nintendo America")) {
                searchTwitterQuery(message, "@NintendoAmerica");
            } else if(messageText.equals(UK_Flag + " Nintendo UK")) {
                searchTwitterQuery(message, "@NintendoUK");
            } else if(messageText.equals(RU_Flag + " Nintendo Russia")) {
                searchTwitterQuery(message, "@NintendoRU");
            } else if(messageText.equals(JP_Flag + " Nintendo Japan")) {
                searchTwitterQuery(message, "@Nintendo");
            } else if(messageText.equals(EU_Flag + " Nintendo Europe")) {
                searchTwitterQuery(message, "@NintendoEurope");
            } else {
                sendMessage(message, "To start using @NinjaraBot, press one of the buttons or write \"Nintendo\"!");
                sendMessage(message, "@NinjaraBot is created by Aleksandr Logvinenko, IVSB11 as a Personal Java Homework Project");
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "NinjaraBot";
    }

    @Override
    public String getBotToken() {
        return System.getenv("NINJARA_TELEGRAMTOKEN");
    }

    private QueryResult twitterQuery(String q) throws TwitterException {
        Twitter twitter = TwitterFactory.getSingleton();
        Query query = new Query(q);

        return twitter.search(query);
    }

    private void sendMessage(Message message, String msgText) {
        SendMessage s = new SendMessage();

        // Create keyboard
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        s.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        // Create rows and buttons
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add("#Nintendo");
        keyboardFirstRow.add(UK_Flag + " Nintendo UK");
        keyboardFirstRow.add(EU_Flag + " Nintendo Europe");

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(US_Flag + " Nintendo America");
        keyboardSecondRow.add(RU_Flag + " Nintendo Russia");
        keyboardSecondRow.add(JP_Flag + " Nintendo Japan");

        // Add rows to the keyboard
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);

        // Add buttons to the user's keyboard
        replyKeyboardMarkup.setKeyboard(keyboard);

        s.setChatId(message.getChatId());
        s.setText(msgText);
        try {
            execute(s);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void searchTwitterQuery(Message message, String query) {
        try {
            QueryResult result = twitterQuery(query);

            for (Status status : result.getTweets()) {
                String tgMsg = "@" + status.getUser().getScreenName() + ": " + status.getText();
                //String tgMsg = status.getUser().getScreenName();
                sendMessage(message, tgMsg);
            }

            Thread.sleep(5000);
        } catch (TwitterException e) {
            e.printStackTrace();
            sendMessage(message, "Failed to search tweets: "+ e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
