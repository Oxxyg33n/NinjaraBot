import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import twitter4j.*;

public class NinjaraBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {

        //check if the update has a message
        if(update.hasMessage()){
            Message message = update.getMessage();

            //Сheck if the message has text.
            if(message.hasText()){
                if(message.getText().toLowerCase().equals("nintendo")) try {
                    QueryResult result = twitterQuery("#nintendo");
                    SendMessage sendMessageRequest = new SendMessage();
                    for (Status status : result.getTweets()) {
                        sendMessageRequest.setChatId(update.getMessage().getChatId());
                        sendMessageRequest.setText("https://twitter.com/" + status.getUser().getScreenName() + ": " + status.getText());
                        execute(sendMessageRequest);
                    }

                    Thread.sleep(3000);
                } catch (TwitterException te) {
                    te.printStackTrace();
                    System.out.println("Failed to search tweets: " + te.getMessage());
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (InterruptedException | TelegramApiException e) {
                    e.printStackTrace();
                }
                else {
                    SendMessage sendMessageRequest = new SendMessage();
                    sendMessageRequest.setChatId(update.getMessage().getChatId());
                    sendMessageRequest.setText("Sorry, I don't understand you, try writing: \"Nintendo\"");
                    try {
                        execute(sendMessageRequest);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }//end if()
        }//end  if()

    }//end onUpdateReceived()
    
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
        QueryResult result = twitter.search(query);

        return result;
    }

}
