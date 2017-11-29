import org.telegram.telegrambots.*;
import org.telegram.telegrambots.exceptions.*;

public class Main {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi ninjaraBotApi = new TelegramBotsApi();

        try {
            ninjaraBotApi.registerBot(new NinjaraBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
