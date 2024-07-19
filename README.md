# ToDoApp

[![en](https://img.shields.io/badge/lang-en-blue.svg)](README.md)
[![ru](https://img.shields.io/badge/lang-ru-red.svg)](README.ru.md)

ToDo list application developed at [Yandex summer school](https://yandex.ru/yaintern/schools/mobile).

## Get apk

See [Telegram channel](https://t.me/gribtodoappyandexsummerschool) for getting last release apk.

## Build

To launch app, create `secrets.properties` file in project root:

```properties
# Client id from Yandex ID: https://id.yandex.ru/
YANDEX_CLIENT_ID=

# Telegram bot keys for plugins
TELEGRAM_BOT_API=
TELEGRAM_CHAT_ID=

# Key data for creating signed apk
KEY_STORE_PATH=
KEY_STORE_PASSWORD=
KEY_ALIAS=
KEY_PASSWORD=
```

## Stack

- Compose
- Dagger 2
- Ktor
- Room
- DataStore
- Kotlinx Serialization
- [Yandex login sdk](https://yandex.ru/dev/id/doc/ru/mobileauthsdk/about)
- [Collapsing toolbar](https://github.com/onebone/compose-collapsing-toolbar)