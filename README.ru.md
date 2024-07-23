# Список дел

[![en](https://img.shields.io/badge/lang-en-blue.svg)](README.md)
[![ru](https://img.shields.io/badge/lang-ru-red.svg)](README.ru.md)
[![wakatime](https://wakatime.com/badge/user/1d230f86-133e-401a-ace9-7805218f18d8/project/65aeb806-8e59-4e25-b954-7ae03f15a76e.svg)](https://wakatime.com/badge/user/1d230f86-133e-401a-ace9-7805218f18d8/project/65aeb806-8e59-4e25-b954-7ae03f15a76e)

Мобильное приложение со списком дел, написанное в рамках [летней школы Яндекса](https://yandex.ru/yaintern/schools/mobile).

## Загрузить apk

Последняя версия `apk` доступна в [Телергам канале](https://t.me/gribtodoappyandexsummerschool).

## Сборка

Для сборки приложения, создайте `secrets.properties` файл в корне проекта:

```properties
# Client id из Яндекс ID: https://id.yandex.ru/
YANDEX_CLIENT_ID=

# Ключи для телеграмм бота для плагинов
TELEGRAM_BOT_API=
TELEGRAM_CHAT_ID=

# Ключ для сборки signed apk
KEY_STORE_PATH=
KEY_STORE_PASSWORD=
KEY_ALIAS=
KEY_PASSWORD=
```

## Стек

- Compose
- Dagger 2
- Ktor
- Room
- DataStore
- Kotlinx Serialization
- [Yandex login sdk](https://yandex.ru/dev/id/doc/ru/mobileauthsdk/about)
- [Collapsing toolbar](https://github.com/onebone/compose-collapsing-toolbar)