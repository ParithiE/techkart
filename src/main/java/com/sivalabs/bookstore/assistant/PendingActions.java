package com.sivalabs.bookstore.assistant;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

/**
 * Write actions the assistant has proposed but the user has not confirmed yet.
 *
 * <p>Kept in memory for the course. Use a database table in a real system, so proposals survive a restart
 * and work across several application instances.
 */
@Component
class PendingActions {
    static final Duration TTL = Duration.ofMinutes(10);

    record PendingCancel(
            String id, Long userId, String orderNumber, String productName, BigDecimal total, Instant createdAt) {}

    private final Map<String, PendingCancel> pending = new ConcurrentHashMap<>();

    PendingCancel propose(Long userId, String orderNumber, String productName, BigDecimal total) {
        PendingCancel action = new PendingCancel(
                UUID.randomUUID().toString(), userId, orderNumber, productName, total, Instant.now());
        pending.put(action.id(), action);
        return action;
    }

    /** Latest proposal this user created at or after the given time, if any. */
    Optional<PendingCancel> latestSince(Long userId, Instant since) {
        return pending.values().stream()
                .filter(p -> p.userId().equals(userId) && !p.createdAt().isBefore(since))
                .max(Comparator.comparing(PendingCancel::createdAt));
    }

    /**
     * Removes and returns the proposal, only if it belongs to this user and has not expired. A proposal can
     * be confirmed once.
     */
    Optional<PendingCancel> take(String id, Long userId) {
        PendingCancel action = pending.get(id);
        if (action == null || !action.userId().equals(userId)) {
            return Optional.empty();
        }
        pending.remove(id);
        if (action.createdAt().plus(TTL).isBefore(Instant.now())) {
            return Optional.empty();
        }
        return Optional.of(action);
    }
}
