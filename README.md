# jenkins-telegrambot
A plugin for Jenkins which posts build results to a Telegram Bot

# Configuration

Create a new Telegram Bot using the BotFather bot and save the token. See https://core.telegram.org/bots#botfather for details.

To obtain the chatId, first add the bot to a chat. Say somethingin the chat and execute the getUpdates method for your bot. This is done by visiting the url https://api.telegram.org/bot<token>/getUpdates

Install the plugin and add the "Post result to a Telegram Bot" post-build Action. Use the acquired token and chatId as input.
