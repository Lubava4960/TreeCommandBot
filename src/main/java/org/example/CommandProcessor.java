package org.example;

import org.example.config.TelegramBotConfiguration;
import org.example.config.TelegramBotListener;

public class CommandProcessor {
    public static class TelegramBotListenerBuilder {
        private TelegramBotConfiguration config;

        public TelegramBotListenerBuilder setConfig(TelegramBotConfiguration config) {
            this.config = config;
            return this;
        }

        public TelegramBotListener createTelegramBotListener() {
            return new TelegramBotListener(config);
        }
    }
}
