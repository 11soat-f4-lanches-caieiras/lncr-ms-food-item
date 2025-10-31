package br.com.tp.lncr.fooditem.datasources.postgres;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class JpaFoodItemImageEntityTest {
    private final String BASE64_PNG = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAIAAACQd1PeAAAADElEQVR4nGP4";
    @Test
    void createEntityWithAllParameters() {
        JpaFoodItemImageEntity entity = new JpaFoodItemImageEntity(1, 10, BASE64_PNG, "/images/", "image.jpg", "jpg", null);

        assertEquals(1, entity.getId());
        assertEquals(10, entity.getFoodItemId());
        assertEquals(BASE64_PNG, entity.getData());
        assertEquals("/images/", entity.getLocation());
        assertEquals("image.jpg", entity.getFileName());
        assertEquals("jpg", entity.getFileExtension());
        assertNull(entity.getImageError());
    }

    @Test
    void createEntityWithDefaultConstructor() {
        JpaFoodItemImageEntity entity = new JpaFoodItemImageEntity();

        assertNull(entity.getId());
        assertNull(entity.getFoodItemId());
        assertNull(entity.getData());
        assertNull(entity.getLocation());
        assertNull(entity.getFileName());
        assertNull(entity.getFileExtension());
        assertNull(entity.getImageError());
    }

    @Test
    void setAndGetId() {
        JpaFoodItemImageEntity entity = new JpaFoodItemImageEntity();
        entity.setId(5);

        assertEquals(5, entity.getId());
    }

    @Test
    void setAndGetFoodItemId() {
        JpaFoodItemImageEntity entity = new JpaFoodItemImageEntity();
        entity.setFoodItemId(20);

        assertEquals(20, entity.getFoodItemId());
    }

    @Test
    void setAndGetData() {
        JpaFoodItemImageEntity entity = new JpaFoodItemImageEntity();
        entity.setData(BASE64_PNG);

        assertEquals(BASE64_PNG, entity.getData());
    }

    @Test
    void setAndGetLocation() {
        JpaFoodItemImageEntity entity = new JpaFoodItemImageEntity();
        entity.setLocation("/storage/images/");

        assertEquals("/storage/images/", entity.getLocation());
    }

    @Test
    void setAndGetFileName() {
        JpaFoodItemImageEntity entity = new JpaFoodItemImageEntity();
        entity.setFileName("pizza.png");

        assertEquals("pizza.png", entity.getFileName());
    }

    @Test
    void setAndGetFileExtension() {
        JpaFoodItemImageEntity entity = new JpaFoodItemImageEntity();
        entity.setFileExtension("png");

        assertEquals("png", entity.getFileExtension());
    }

    @Test
    void setAndGetImageError() {
        JpaFoodItemImageEntity entity = new JpaFoodItemImageEntity();
        entity.setImageError("Invalid format");

        assertEquals("Invalid format", entity.getImageError());
    }

    @Test
    void setNullValues() {
        JpaFoodItemImageEntity entity = new JpaFoodItemImageEntity(1, 1, "data", "location", "file", "ext", "error");

        entity.setId(null);
        entity.setFoodItemId(null);
        entity.setData(null);
        entity.setLocation(null);
        entity.setFileName(null);
        entity.setFileExtension(null);
        entity.setImageError(null);

        assertNull(entity.getId());
        assertNull(entity.getFoodItemId());
        assertNull(entity.getData());
        assertNull(entity.getLocation());
        assertNull(entity.getFileName());
        assertNull(entity.getFileExtension());
        assertNull(entity.getImageError());
    }

    @Test
    void createEntityWithErrorMessage() {
        JpaFoodItemImageEntity entity = new JpaFoodItemImageEntity(null, 1, null, null, null, null, "File too large");

        assertNull(entity.getId());
        assertEquals(1, entity.getFoodItemId());
        assertEquals("File too large", entity.getImageError());
    }
}
