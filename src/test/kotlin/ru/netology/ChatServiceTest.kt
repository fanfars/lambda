package ru.netology

import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import ru.netology.ChatService.chatId
import ru.netology.ChatService.chats
import ru.netology.ChatService.sendMsg

class ChatServiceTest {

    @Test
    fun delMsg() {
        sendMsg(1, 2, "1")
        sendMsg(2, 3, "2")
        sendMsg(3, 4, "3")
        sendMsg(3, 4, "4")

        val expected: List<Message> = listOf( Message(3, 3, "3"))
        ChatService.delMsg(4)

        val actual = ChatService.chats[2].messages
        assertEquals(expected, actual)
    }

    @Test(expected = MessageNotFoundException::class)
    fun delMsgException() {
        val expected: List<Message> = listOf( Message(3, 3, "3"))
        ChatService.delMsg(55)

        val actual = ChatService.chats[2].messages
        assertEquals(expected, actual)
    }

    @Test
    fun sendFirstMsg() {
        val expectedMsg: Message = Message(2, 2, "2")
        val actualMsg = ChatService.chats[1].messages[0]
        assertEquals(expectedMsg, actualMsg)
    }


    @Test
    fun sendSecondMsg() {
        val expectedMsg: Message = Message(1, 1, "1")
        val actualMsg = ChatService.chats[0].messages[0]
        assertEquals(expectedMsg, actualMsg)
    }


    @Test
    fun getAllMessage() {
        val expectedList: List<Message> = listOf(Message(1, 1, "1", true))

        val actualList = ChatService.getAllMessage(1)
        assertEquals(expectedList, actualList)
    }

    @Test(expected = ChatNotFoundException::class)
    fun getAllMessageException() {
        val expectedList: List<Message> = listOf(Message(1, 1, "1", true))

        val actualList = ChatService.getAllMessage(50)
        assertEquals(expectedList, actualList)

    }

    @Test
    fun getNextPageMessage() {
        val expectedList: List<Message> = listOf(Message(1, 1, "1", true))

        val actualList = ChatService.getNextPageMessage(1, 0)
        assertEquals(expectedList, actualList)
    }

    @Test(expected = ChatNotFoundException::class)
    fun getNextPageMessageException() {
        val expectedList: List<Message> = listOf(Message(1, 1, "1", true))

        val actualList = ChatService.getNextPageMessage(10, 0)
        assertEquals(expectedList, actualList)
    }

    @Test
    fun getMessages() {
        val expectedList: List<Message> = listOf(Message(1, 1, "1", true))

        val actualList = ChatService.getMessages(1, 1)
        assertEquals(expectedList, actualList)
    }

    @Test(expected = ChatNotFoundException::class)
    fun getMessagesException() {
        val expectedList: List<Message> = listOf(Message(1, 1, "1", true))

        val actualList = ChatService.getMessages(10, 0)
        assertEquals(expectedList, actualList)
    }

    @Test
    fun getUnreadChatsCount() {
        val expected = 2

        val actual = ChatService.getUnreadChatsCount()
        assertEquals(expected, actual)
    }

    @Test
    fun getChats() {
        val expectedList: List<Chat> = listOf(
            Chat(1, listOf(1, 2), listOf(Message(1, 1, "1", true)))
        )

        val actualList = ChatService.getChats(1)
        assertEquals(expectedList, actualList)
    }

    @Test(expected = UserNotFoundException::class)
    fun getChatsException() {
        val expectedList: List<Chat> = listOf(
            Chat(1, listOf(1, 2), listOf(Message(1, 1, "1", true)))
        )

        val actualList = ChatService.getChats(10)
        assertEquals(expectedList, actualList)
    }


}