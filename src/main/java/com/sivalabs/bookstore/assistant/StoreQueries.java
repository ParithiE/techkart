package com.sivalabs.bookstore.assistant;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

/**
 * Read-only SQL for the assistant.
 *
 * <p>Shortcut for the course: the catalog and inventory modules expose no search API, so these queries read
 * their tables directly. In a real modular application, add a search method to each module's API instead.
 * Every value that can come from the model is passed as a bound parameter, never concatenated into SQL.
 */
@Component
class StoreQueries {
    static final int MAX_RESULTS = 8;

    record ProductRow(
            String code,
            String name,
            @Nullable String category,
            @Nullable String brand,
            @Nullable String description,
            BigDecimal price,
            long quantity) {}

    private static final String SELECT = """
            select p.code, p.name, p.category, p.brand, p.description, p.price, coalesce(i.quantity, 0) as quantity
            from catalog.products p
            left join inventory.inventory i on i.product_code = p.code
            where p.deleted_at is null
            """;

    private final JdbcClient jdbc;

    StoreQueries(DataSource dataSource) {
        this.jdbc = JdbcClient.create(dataSource);
    }

    List<ProductRow> search(
            @Nullable String text, @Nullable String category, @Nullable String brand, @Nullable Integer maxPrice) {
        StringBuilder sql = new StringBuilder(SELECT);
        Map<String, Object> params = new HashMap<>();
        if (text != null && !text.isBlank()) {
            sql.append(" and (p.name ilike :pattern or p.description ilike :pattern)");
            params.put("pattern", "%" + text.trim() + "%");
        }
        if (category != null && !category.isBlank()) {
            sql.append(" and lower(p.category) = lower(:category)");
            params.put("category", category.trim());
        }
        if (brand != null && !brand.isBlank()) {
            sql.append(" and lower(p.brand) = lower(:brand)");
            params.put("brand", brand.trim());
        }
        if (maxPrice != null) {
            sql.append(" and p.price <= :maxPrice");
            params.put("maxPrice", maxPrice);
        }
        sql.append(" order by p.price asc limit ").append(MAX_RESULTS);
        return jdbc.sql(sql.toString()).params(params).query(StoreQueries::mapRow).list();
    }

    Optional<ProductRow> findByCode(String code) {
        return jdbc.sql(SELECT + " and p.code = :code")
                .param("code", code.trim())
                .query(StoreQueries::mapRow)
                .optional();
    }

    List<ProductRow> lowStock(int threshold) {
        return jdbc.sql(SELECT + " and coalesce(i.quantity, 0) <= :threshold order by quantity asc, p.code limit 20")
                .param("threshold", threshold)
                .query(StoreQueries::mapRow)
                .list();
    }

    private static ProductRow mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        return new ProductRow(
                rs.getString("code"),
                rs.getString("name"),
                rs.getString("category"),
                rs.getString("brand"),
                rs.getString("description"),
                rs.getBigDecimal("price"),
                rs.getLong("quantity"));
    }
}
