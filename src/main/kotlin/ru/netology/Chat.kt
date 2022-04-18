package ru.netology

data class Chat(
    val chatId: Int,
    val users: List<Int>,
    val messages: List<Message>
)

data class Message(
    val msgId: Int,
    val ownerId: Int,
    val message: String,
    val isRead: Boolean = false
)

object ChatService {
    val chats = mutableListOf<Chat>()


    var chatId = 1
    var msgId = 1

    fun sendMsg(
        senderId: Int,
        recipientId: Int,
        text: String
    ) {
        val message = Message(
            msgId = msgId++,
            ownerId = senderId,
            message = text
        )
        val newChat = chats.firstOrNull { chat -> chat.users.containsAll(listOf(senderId, recipientId)) }
            ?.let { chat ->
                chat.copy(messages = chat.messages + message)
            } ?: Chat(
            chatId = chatId++,
            users = listOf(senderId, recipientId),
            messages = listOf(message)
        )
        chats.ifEmpty { chats.add(newChat) }
        chats.forEachIndexed { index, chat ->
            if (newChat.chatId == chat.chatId) {
                chats[index] = newChat
                return@forEachIndexed
            }
        }

        chats.firstOrNull { chat -> chat.users.containsAll(listOf(senderId, recipientId)) }
            ?.let { } ?: chats.add(newChat)

    }

    fun getAllMessage(chatId: Int): List<Message> {
        val chat = chats.firstOrNull() { it.chatId == chatId } ?: throw ChatNotFoundException("Чаты с указанным id '${chatId}' не найдены")
        val updatedMessages = chat.messages
            .map { it.copy(isRead = true) }

        val updatedChat = chat.copy(messages = updatedMessages)
        chats.removeIf { updatedChat.chatId == it.chatId }
        chats.add(updatedChat)
        return updatedMessages
    }

    fun getNextPageMessage(chatId: Int, lastMessageId: Int): List<Message> {
        val chat = chats.firstOrNull { it.chatId == chatId } ?: throw ChatNotFoundException("Чаты с указанным id '${chatId}' не найдены")
        val updatedMessages = chat.messages.filter { it.msgId > lastMessageId }
            .map { it.copy(isRead = true) }

        val updatedChat = chat.copy(messages = updatedMessages)
        chats.removeIf { updatedChat.chatId == it.chatId }
        chats.add(updatedChat)
        return updatedMessages
    }

    fun getMessages(chatId: Int, messageCount: Int): List<Message> {
        val chat = chats.firstOrNull{ it.chatId == chatId } ?: throw ChatNotFoundException("Чаты с указанным id '${chatId}' не найдены")
        val updatedMessages = chat.messages.take(messageCount)
            .map { it.copy(isRead = true) }

        val updatedChat = chat.copy(messages = updatedMessages)
        chats.removeIf { updatedChat.chatId == it.chatId }
        chats.add(updatedChat)
        return updatedMessages
    }

    fun getUnreadChatsCount(): Int {
        val chatsCount = chats.sumOf { chat ->
            chat
                .messages.count { message ->
                    when {
                        (message.isRead == false) -> return@sumOf 1
                        else -> return@sumOf 0
                    }
                }
        }
        return chatsCount
    }

    fun getChats(userId: Int): List<Chat> {
        val chatsList: List<Chat> = chats.filter { chat ->
            chat.messages.any { it.ownerId == userId }
        }
        if (chatsList.isEmpty()) throw UserNotFoundException("Пользователь с указанным id '$userId' не найден")
        return chatsList
    }

    fun delMsg(msgId: Int) {
        val chatWithMsgDeleted = chats.firstOrNull { chat -> chat.messages.any { return@any it.msgId == msgId } }
            ?.let { chat ->
                chat.copy(messages = chat.messages - chat.messages.first { it.msgId == msgId })
            } ?: throw MessageNotFoundException("Сообщение с указанным id '$msgId' не найдено")
        chatWithMsgDeleted.messages.ifEmpty { chats.remove(chatWithMsgDeleted) }
        chats.forEachIndexed { index, chat ->
            if (chat.chatId == chatWithMsgDeleted.chatId) {
                chats[index] = chatWithMsgDeleted
                return@forEachIndexed
            }
        }
    }

}

class MessageNotFoundException(message: String) : RuntimeException(message)
class ChatNotFoundException(message: String) : RuntimeException(message)
class UserNotFoundException(message: String) : RuntimeException(message)

