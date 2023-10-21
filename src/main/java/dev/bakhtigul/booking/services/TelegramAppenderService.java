package dev.bakhtigul.booking.services;

import dev.bakhtigul.booking.utils.TelegramAppender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramAppenderService {
    private final TelegramAppender telegramAppender;
    public void sendReportByTelegram(String report) {
        telegramAppender.sendReportToAdmin(report);
    }
}
