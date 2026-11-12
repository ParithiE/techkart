package com.sivalabs.bookstore.assistant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.sivalabs.bookstore.orders.OrdersApi;
import com.sivalabs.bookstore.orders.domain.models.Customer;
import com.sivalabs.bookstore.orders.domain.models.OrderDto;
import com.sivalabs.bookstore.orders.domain.models.OrderItem;
import com.sivalabs.bookstore.orders.domain.models.OrderStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ToolContext;

/** Tool tests need no model and no API key, so they run on every build. */
class StoreToolsTests {
    private static final Long KARTHIK = 3L;
    private static final Long SOMEONE_ELSE = 99L;
    private static final ToolContext CUSTOMER =
            new ToolContext(Map.of(StoreTools.USER_ID, KARTHIK, StoreTools.ROLE, "ROLE_USER"));
    private static final ToolContext OTHER_CUSTOMER =
            new ToolContext(Map.of(StoreTools.USER_ID, SOMEONE_ELSE, StoreTools.ROLE, "ROLE_USER"));
    private static final ToolContext ADMIN =
            new ToolContext(Map.of(StoreTools.USER_ID, 4L, StoreTools.ROLE, "ROLE_ADMIN"));

    private OrdersApi ordersApi;
    private StoreQueries queries;
    private PendingActions pendingActions;
    private StoreTools tools;

    @BeforeEach
    void setUp() {
        ordersApi = mock(OrdersApi.class);
        queries = mock(StoreQueries.class);
        pendingActions = new PendingActions();
        tools = new StoreTools(queries, ordersApi, pendingActions);
        given(ordersApi.findOrder("ORD-NEW", KARTHIK)).willReturn(Optional.of(order("ORD-NEW", OrderStatus.NEW)));
        given(ordersApi.findOrder("ORD-DONE", KARTHIK))
                .willReturn(Optional.of(order("ORD-DONE", OrderStatus.DELIVERED)));
    }

    private static OrderDto order(String orderNumber, OrderStatus status) {
        return new OrderDto(
                orderNumber,
                KARTHIK,
                new OrderItem("P105", "Nimbus Air 14 i5 16GB/512GB", new BigDecimal("54999"), 1),
                new Customer("Karthik", "karthik@techkart.test", "9840012345"),
                "Anna Nagar, Chennai",
                status,
                LocalDateTime.now());
    }

    @Test
    void customerCanProposeCancellingTheirNewOrder() {
        Instant before = Instant.now();
        String result = tools.proposeCancelOrder("ORD-NEW", CUSTOMER);
        assertThat(result).contains("NOT cancelled yet");
        assertThat(pendingActions.latestSince(KARTHIK, before)).isPresent();
    }

    @Test
    void deliveredOrderCannotBeCancelled() {
        Instant before = Instant.now();
        String result = tools.proposeCancelOrder("ORD-DONE", CUSTOMER);
        assertThat(result).contains("can no longer be cancelled");
        assertThat(pendingActions.latestSince(KARTHIK, before)).isEmpty();
    }

    @Test
    void customerCannotTouchAnotherCustomersOrder() {
        Instant before = Instant.now();
        String result = tools.proposeCancelOrder("ORD-NEW", OTHER_CUSTOMER);
        assertThat(result).contains("has no order");
        assertThat(pendingActions.latestSince(SOMEONE_ELSE, before)).isEmpty();
    }

    @Test
    void stockLevelsAreForAdminsOnly() {
        assertThat(tools.lowStockProducts(10, CUSTOMER)).isEqualTo("Not allowed: stock levels are only available to admins.");
        assertThat(tools.lowStockProducts(10, ADMIN)).isInstanceOf(java.util.List.class);
    }

    @Test
    void aProposalCanOnlyBeTakenOnceAndOnlyByItsOwner() {
        Instant before = Instant.now();
        tools.proposeCancelOrder("ORD-NEW", CUSTOMER);
        String id = pendingActions.latestSince(KARTHIK, before).orElseThrow().id();
        assertThat(pendingActions.take(id, SOMEONE_ELSE)).isEmpty();
        assertThat(pendingActions.take(id, KARTHIK)).isPresent();
        assertThat(pendingActions.take(id, KARTHIK)).isEmpty();
    }
}
