<h1 align="center">VK-MailingGroup</h1>
<p align="center">
       <a href="https://github.com/Ferius057/VK-MailingGroup/releases/tag/1.4.1">
    <img src="https://img.shields.io/github/v/release/Ferius057/VK-MailingGroup?style=flat-square">
  </a>
    <a href="https://opensource.org/licenses/MIT">
    <img src="https://img.shields.io/badge/Open%20Source-purple.svg">
  </a>
  <a href="https://www.java.com">
    <img src="https://img.shields.io/badge/java_version-11-orange">
  </a>
  <a href="https://github.com/Ferius057/VK-MailingGroup/blob/main/LICENSE">
    <img src="https://img.shields.io/badge/License-MIT-yellow.svg">
  </a>
  <p align="center">
  <a href="https://github.com/Ferius057/VK-MailingGroup/releases/download/1.4.1/MailingGroup.jar">
    <img src="https://img.shields.io/github/downloads/Ferius057/VK-MailingGroup/total?color=6ff00">
  <a href="https://www.donationalerts.com/r/ferius_057">
    <img src="https://img.shields.io/badge/Donate-DonationAlerts-orange.svg">
  </a>
  </p>
</p>

<hr>

## Требования
- **Java 11**

<hr>

## Возможности
- Быстро сделать рассылку всем пользовалям (тем с кем был диалог)
- Прикреплять такие вложения как: фото, запись (пост)
- Сделать тестовую рассылку определенным пользователям
- Удалить определенных пользователей из рассылки (black list)
- Удобные конфигурации и полная информация по настройке
- Легкая установка

<hr>

## Использование:
   <a href="https://github.com/Ferius057/VK-MailingGroup/releases/download/1.4.1/MailingGroup.jar">
    <img src="https://img.shields.io/github/downloads/Ferius057/VK-MailingGroup/total?color=6ff00">
  </a>

```bash
# Скачать на Linux
$ wget https://github.com/Ferius057/VK-MailingGroup/releases/download/1.4.1/MailingGroup.jar


# Для запуска скрипта
$ java -jar MailingGroup.jar

# После запуска рядом с MailingGroup.jar будет config.yml, его надо будет настроить.
# Обратите внимание на то что бы была верная кодировка для этого нужно использовать -Dfile.encoding=UTF-8
# java -Dfile.encoding=UTF-8 -jar MailingGroup.jar
# ну или на Windows chcp 65001
```

<hr>

## Config
> Подробную информацию про то как добавить запись [ТУТ](https://github.com/Ferius057/VK-MailingGroup/blob/master/docs/wall.md)
```yml
# MailingGroup  by Ferius_057 aka Charles_Grozny
# Разработчик: vk.com/ferius_057
# Исходный код: github.com/Ferius057/VK-MailingGroup
# Подробную информацию про то как добавить запись: https://github.com/Ferius057/VK-MailingGroup/blob/master/docs/wall.md


# Токен группы
token: 'token'


# Тестовая рассылка
testMode:
  # true = будет отправлено только пользователям из списка 'users'
  # false = полноценная рассылка всем пользователям с кем был диалог
  enable: false
  # просто id пользователя(только цифры, id в начале не нужен)
  users:
    - 1
    - 621876211



# Список id пользователей которые не будут получать рассылку
# просто id пользователя(только цифры, id в начале не нужен)
blackList:
  - 271921577



# Максимум вложений в 1 сообщений = 10,
# в ином случае будет добавлены только первые написанные
## Не нужные вложения оставьте строку пустым
## Пример:
##  photo:
##    - ''
##  wall:
##    - ''
attachments:
  # В 1 сообщений можно прикреплять 10 фотографий.
  photo:
    - '1.jpg'
    - '2.jpg'
    - '3.jpg'

  # В 1 сообщений можно прикрепить только 1 пост.
  # Подробную информацию про то как добавить запись: https://github.com/Ferius057/VK-MailingGroup/blob/master/docs/wall.md
  wall:
    - 'wall-200934694_40'



# Текст сообщения который будет отправлен.
# Важно что бы до текста(после "message: |") на каждой строке был отступ в 2 пробела
message: |
  Привет.
  Тест рассылки.

```

<hr>

## Links:
 - [Java 11](https://www.java.com)
 > По всем вопросам/предложениям:
 - Author: [Charles_Grozny](https://vk.me/ferius_057)
