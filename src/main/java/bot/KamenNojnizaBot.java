package bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Random;

public class KamenNojnizaBot extends TelegramLongPollingBot {

    private final String BOT_USERNAME = "Game_bot"; // <- вставь сюда username бота
    private final String BOT_TOKEN = "7964186537:AAGZr8oRyDceiQaiKUY7a5QYNchrSthCZw0";      // <- вставь сюда токен

    private final String[] options = {"камень", "ножницы", "бумага"};
    private final Random random = new Random();

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText().toLowerCase();
            long chatId = update.getMessage().getChatId();

            if (text.equals("/start")) {
                sendMsg(chatId, "Привет! Давай сыграем в Камень, Ножницы, Бумага!\nВыбери свой ход:", getKeyboard());
            } else {
                sendMsg(chatId, "Пожалуйста, используй кнопки для выбора хода. Напиши /start для начала игры.");
            }
        } else if (update.hasCallbackQuery()) {
            handleCallback(update.getCallbackQuery());
        }
    }

    private void handleCallback(CallbackQuery callbackQuery) {
        String userChoice = callbackQuery.getData();
        long chatId = callbackQuery.getMessage().getChatId();

        String computerChoice = options[random.nextInt(options.length)];

        String result;
        if (userChoice.equals(computerChoice)) {
            result = "Ничья!";
        } else if (
                (userChoice.equals("камень") && computerChoice.equals("ножницы")) ||
                        (userChoice.equals("ножницы") && computerChoice.equals("бумага")) ||
                        (userChoice.equals("бумага") && computerChoice.equals("камень"))
        ) {
            result = "Вы выиграли!";
        } else {
            result = "Вы проиграли!";
        }

        String message = String.format("Вы выбрали: %s\nКомпьютер выбрал: %s\n\n%s\n\nСыграем ещё?", userChoice, computerChoice, result);
        sendMsg(chatId, message, getKeyboard());
    }

    private InlineKeyboardMarkup getKeyboard() {
        InlineKeyboardButton rock = new InlineKeyboardButton();
        rock.setText("камень");
        rock.setCallbackData("камень");

        InlineKeyboardButton scissors = new InlineKeyboardButton();
        scissors.setText("ножницы");
        scissors.setCallbackData("ножницы");

        InlineKeyboardButton paper = new InlineKeyboardButton();
        paper.setText("бумага");
        paper.setCallbackData("бумага");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(List.of(List.of(rock, scissors, paper)));
        return markup;
    }

    private void sendMsg(long chatId, String text, InlineKeyboardMarkup keyboard) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        if (keyboard != null) {
            message.setReplyMarkup(keyboard);
        }

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendMsg(long chatId, String text) {
        sendMsg(chatId, text, null);
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}
