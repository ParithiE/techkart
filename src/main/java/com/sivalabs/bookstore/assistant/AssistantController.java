package com.sivalabs.bookstore.assistant;

import com.sivalabs.bookstore.assistant.AssistantService.Reply;
import com.sivalabs.bookstore.users.UserContextUtils;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Deliberately not under /api: this app allows cross-origin calls to /api/**, and the assistant must only
 * be called from the store's own pages.
 */
@RestController
@RequestMapping("/assistant")
class AssistantController {
    static final int MAX_MESSAGE_LENGTH = 1000;

    private final AssistantService assistant;

    AssistantController(AssistantService assistant) {
        this.assistant = assistant;
    }

    record ChatRequest(@Nullable String message) {}

    @PostMapping("/chat")
    Reply chat(@RequestBody ChatRequest request, Authentication authentication, HttpSession session) {
        String message = request.message();
        if (message == null || message.isBlank() || message.length() > MAX_MESSAGE_LENGTH) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Message is required, at most " + MAX_MESSAGE_LENGTH + " characters.");
        }
        Long userId = UserContextUtils.getCurrentUserIdOrThrow();
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER");
        // Built on the server, so one user can never read another user's conversation.
        String conversationId = userId + ":" + session.getId();
        return assistant.chat(conversationId, userId, role, message.trim());
    }

    @PostMapping("/confirm/{id}")
    Map<String, String> confirm(@PathVariable String id) {
        Long userId = UserContextUtils.getCurrentUserIdOrThrow();
        return Map.of("reply", assistant.confirm(id, userId));
    }
}
