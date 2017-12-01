import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import twitter4j.*;

public class NinjaraBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()){
            Message message = update.getMessage();
            String text = message.getText().toLowerCase();
            if(text.equals("/start")) sendMessage(message, "To start using @NinjaraBot, press one of the buttons or write \"Nintendo\"!");
            else if(text.equals("nintendo")) try {
                QueryResult result = twitterQuery("#nintendo");
                for (Status status : result.getTweets()) {
                    String tgMsg = "@" + status.getUser().getScreenName() + ": " +status.getText();
                    sendMessage(message, tgMsg);
                }

                Thread.sleep(3000);
            } catch (TwitterException e) {
                e.printStackTrace();
                sendMessage(message, "Failed to search for tweets: " + e.getMessage());
            } catch (InterruptedException e) {
                e.printStackTrace();
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

    private void sendMessage(Message msg, String text) {
        SendMessage s = new SendMessage();
        s.setChatId(msg.getChatId());
        s.setText(text);
        try {
            execute(s);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
