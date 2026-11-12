package com.sivalabs.bookstore.assistant;

import com.sivalabs.bookstore.assistant.StoreQueries.ProductRow;
import com.sivalabs.bookstore.orders.OrdersApi;
import com.sivalabs.bookstore.orders.domain.models.OrderDto;
import com.sivalabs.bookstore.orders.domain.models.OrderStatus;
import java.math.BigDecimal;
import java.util.List;
import org.jspecify.annotations.Nullable;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

/**
 * The tools the assistant may call. Each one wraps something the store can already do. Return types are
 * small records, so the model only sees the fields we choose.
 */
@Component
public class StoreTools {
    static final String USER_ID = "userId";
    static final String ROLE = "role";
    static final int MAX_ORDERS = 10;

    private final StoreQueries queries;
    private final OrdersApi ordersApi;
    private final PendingActions pendingActions;

    StoreTools(StoreQueries queries, OrdersApi ordersApi, PendingActions pendingActions) {
        this.queries = queries;
        this.ordersApi = ordersApi;
        this.pendingActions = pendingActions;
    }

    public record ProductHit(
            String code, String name, @Nullable String category, @Nullable String brand, BigDecimal price, boolean inStock) {}

    public record ProductDetails(
            String code,
            String name,
            @Nullable String category,
            @Nullable String brand,
            @Nullable String description,
            BigDecimal price,
            boolean inStock) {}

    public record OrderSummary(
            String orderNumber, String productName, int quantity, BigDecimal total, String status, String placedOn) {}

    public record StockLevel(String code, String name, long quantity) {}

    @Tool(description = "Search TechKart products. Every filter is optional. "
            + "Returns at most 8 products, cheapest first, with prices in Indian rupees.")
    public List<ProductHit> searchProducts(
            @ToolParam(required = false, description = "Words to find in the product name or description, e.g. 'gaming' or '16GB'") @Nullable String text,
            @ToolParam(required = false, description = "One of: Phones, Laptops, Audio, Wearables, Tablets, Accessories") @Nullable String category,
            @ToolParam(required = false, description = "Brand name, e.g. Nimbus") @Nullable String brand,
            @ToolParam(required = false, description = "Maximum price in rupees, digits only") @Nullable Integer maxPrice) {
        return queries.search(text, category, brand, maxPrice).stream()
                .map(p -> new ProductHit(p.code(), p.name(), p.category(), p.brand(), p.price(), p.quantity() > 0))
                .toList();
    }

    @Tool(description = "Get the full description, price and availability of one product by its code, e.g. P105.")
    public Object getProductDetails(@ToolParam(description = "Product code from searchProducts") String code) {
        ProductRow p = queries.findByCode(code).orElse(null);
        if (p == null) {
            return "No product with code " + code + ".";
        }
        return new ProductDetails(
                p.code(), p.name(), p.category(), p.brand(), p.description(), p.price(), p.quantity() > 0);
    }

    @Tool(description = "List the signed-in customer's most recent orders with their status.")
    public Object listMyOrders(ToolContext toolContext) {
        Long userId = userId(toolContext);
        if (userId == null) {
            return "Not signed in.";
        }
        return ordersApi.findOrders(userId).stream()
                .limit(MAX_ORDERS)
                .map(view -> ordersApi.findOrder(view.orderNumber(), userId).orElse(null))
                .filter(order -> order != null)
                .map(StoreTools::toSummary)
                .toList();
    }

    @Tool(description = "Propose cancelling one of the signed-in customer's orders. This does NOT cancel it. "
            + "It creates a proposal that the customer must confirm with the Confirm button. "
            + "Use listMyOrders first to get the order number.")
    public String proposeCancelOrder(
            @ToolParam(description = "Order number exactly as returned by listMyOrders") String orderNumber,
            ToolContext toolContext) {
        // Identity comes from the server, never from the model.
        Long userId = userId(toolContext);
        if (userId == null) {
            return "Not signed in.";
        }
        // findOrder only returns the order if it belongs to this user.
        OrderDto order = ordersApi.findOrder(orderNumber.trim(), userId).orElse(null);
        if (order == null) {
            return "This customer has no order with number " + orderNumber + ".";
        }
        if (!order.status().canTransitionTo(OrderStatus.CANCELLED)) {
            return "This order is " + order.status() + " and can no longer be cancelled.";
        }
        var pending = pendingActions.propose(userId, order.orderNumber(), order.item().name(), order.getTotalAmount());
        return "Proposal " + pending.id() + " created to cancel the order for " + order.item().name()
                + ". It is NOT cancelled yet. Ask the customer to press Confirm.";
    }

    @Tool(description = "Admin only. List products whose stock is at or below a threshold, lowest first.")
    public Object lowStockProducts(
            @ToolParam(description = "Stock threshold, e.g. 10") int threshold, ToolContext toolContext) {
        if (!"ROLE_ADMIN".equals(toolContext.getContext().get(ROLE))) {
            return "Not allowed: stock levels are only available to admins.";
        }
        int safeThreshold = Math.max(0, Math.min(threshold, 1000));
        return queries.lowStock(safeThreshold).stream()
                .map(p -> new StockLevel(p.code(), p.name(), p.quantity()))
                .toList();
    }

    private static @Nullable Long userId(ToolContext toolContext) {
        Object value = toolContext.getContext().get(USER_ID);
        return value instanceof Long id ? id : null;
    }

    private static OrderSummary toSummary(OrderDto order) {
        return new OrderSummary(
                order.orderNumber(),
                order.item().name(),
                order.item().quantity(),
                order.getTotalAmount(),
                order.status().name(),
                order.createdAt().toLocalDate().toString());
    }
}
