package br.com.tp.lncr.fooditem.datasources.postgres;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class JpaFoodItemEntityTest {

    @Test
    void createEntityWithAllParameters() {
        JpaFoodItemEntity entity = new JpaFoodItemEntity(1, "X-Bacon", "Delicious bacon burger", 25.99, 1);

        assertEquals(1, entity.getId());
        assertEquals("X-Bacon", entity.getName());
        assertEquals("Delicious bacon burger", entity.getDescription());
        assertEquals(25.99, entity.getPrice());
        assertEquals(1, entity.getCategoryId());
    }

    @Test
    void createEntityWithDefaultConstructor() {
        JpaFoodItemEntity entity = new JpaFoodItemEntity();

        assertNull(entity.getId());
        assertNull(entity.getName());
        assertNull(entity.getDescription());
        assertNull(entity.getPrice());
        assertNull(entity.getCategoryId());
    }

    @Test
    void setAndGetId() {
        JpaFoodItemEntity entity = new JpaFoodItemEntity();
        entity.setId(10);

        assertEquals(10, entity.getId());
    }

    @Test
    void setAndGetName() {
        JpaFoodItemEntity entity = new JpaFoodItemEntity();
        entity.setName("Pizza Margherita");

        assertEquals("Pizza Margherita", entity.getName());
    }

    @Test
    void setAndGetDescription() {
        JpaFoodItemEntity entity = new JpaFoodItemEntity();
        entity.setDescription("Traditional Italian pizza");

        assertEquals("Traditional Italian pizza", entity.getDescription());
    }

    @Test
    void setAndGetPrice() {
        JpaFoodItemEntity entity = new JpaFoodItemEntity();
        entity.setPrice(35.50);

        assertEquals(35.50, entity.getPrice());
    }

    @Test
    void setAndGetCategoryId() {
        JpaFoodItemEntity entity = new JpaFoodItemEntity();
        entity.setCategoryId(2);

        assertEquals(2, entity.getCategoryId());
    }

    @Test
    void setNullValues() {
        JpaFoodItemEntity entity = new JpaFoodItemEntity(1, "Test", "Test", 10.0, 1);

        entity.setId(null);
        entity.setName(null);
        entity.setDescription(null);
        entity.setPrice(null);
        entity.setCategoryId(null);

        assertNull(entity.getId());
        assertNull(entity.getName());
        assertNull(entity.getDescription());
        assertNull(entity.getPrice());
        assertNull(entity.getCategoryId());
    }
}
