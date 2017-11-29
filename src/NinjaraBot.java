import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class NinjaraBot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage()         // Create a SendMessage object with mandatory fields
                .setChatId(update.getMessage().getChatId())
                .setText(update.getMessage().getText());
            try {
                execute(message);
            } catch (TelegramApiException e) {
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

}
