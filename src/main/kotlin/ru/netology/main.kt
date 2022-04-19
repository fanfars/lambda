package ru.netology

fun main() {

    ChatService.sendMsg(1, 2, "1")
    ChatService.sendMsg(2, 3, "2")
    ChatService.sendMsg(3, 4, "3")
    ChatService.sendMsg(3, 4, "4")

    print("${ChatService.getUnreadChatsCount()}")
}