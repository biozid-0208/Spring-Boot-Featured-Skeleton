package com.example.webservice.chat.controllers.web

import com.example.webservice.chat.services.ChatRoomService
import com.example.webservice.commons.PageAttr
import com.example.webservice.config.security.SecurityContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam


@Controller
@RequestMapping("/messages")
class ChatClientController @Autowired constructor(
        private val chatRoomService: ChatRoomService
) {

    @GetMapping("")
    fun chatPage(@RequestParam("q", defaultValue = "") query: String,
                 @RequestParam("page", defaultValue = "0") page: Int,
                 model: Model): String {
        val username = SecurityContext.getLoggedInUsername()
        val chatRooms = this.chatRoomService.search(query, username, page, 100)

        model.addAttribute("username", username)
        model.addAttribute("chatRooms", chatRooms)
        return "material/pages/chat2"
    }
}