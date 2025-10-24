package br.com.tp.lncr.fooditem.datasources.postgres;

import br.com.tp.lncr.core.dtos.fooditem.FoodItemDTO;
import br.com.tp.lncr.core.dtos.fooditem.FoodItemImageDTO;
import br.com.tp.lncr.core.enums.FoodItemCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JpaFoodItemMapperTest {

    private JpaFoodItemMapper mapper;
    private FoodItemDTO foodItemDTO;
    private JpaFoodItemEntity jpaFoodItemEntity;
    private FoodItemImageDTO foodItemImageDTO;
    private JpaFoodItemImageEntity jpaFoodItemImageEntity;

    @BeforeEach
    void setUp() {
        mapper = new JpaFoodItemMapper();

        foodItemDTO = new FoodItemDTO();
        foodItemDTO.setId(1);
        foodItemDTO.setName("X-Bacon");
        foodItemDTO.setDescription("Delicious bacon burger");
        foodItemDTO.setPrice(25.99);
        foodItemDTO.setCategory("Sandwich");

        jpaFoodItemEntity = new JpaFoodItemEntity();
        jpaFoodItemEntity.setId(1);
        jpaFoodItemEntity.setName("X-Bacon");
        jpaFoodItemEntity.setDescription("Delicious bacon burger");
        jpaFoodItemEntity.setPrice(25.99);
        jpaFoodItemEntity.setCategoryId(1);

        foodItemImageDTO = new FoodItemImageDTO();
        foodItemImageDTO.setId(1);
        foodItemImageDTO.setFoodItemId(10);
        foodItemImageDTO.setData("base64data");
        foodItemImageDTO.setFileName("image.jpg");
        foodItemImageDTO.setFileExtension("jpg");
        foodItemImageDTO.setImageError(null);

        jpaFoodItemImageEntity = new JpaFoodItemImageEntity();
        jpaFoodItemImageEntity.setId(1);
        jpaFoodItemImageEntity.setFoodItemId(10);
        jpaFoodItemImageEntity.setData("base64data");
        jpaFoodItemImageEntity.setFileName("image.jpg");
        jpaFoodItemImageEntity.setFileExtension("jpg");
        jpaFoodItemImageEntity.setImageError(null);
    }

    @Test
    void mapFoodItemDtoToJpaEntity() {
        JpaFoodItemEntity result = mapper.foodItemDtoToJpa(foodItemDTO);

        assertNotNull(result);
        assertEquals(foodItemDTO.getId(), result.getId());
        assertEquals(foodItemDTO.getName(), result.getName());
        assertEquals(foodItemDTO.getDescription(), result.getDescription());
        assertEquals(foodItemDTO.getPrice(), result.getPrice());
        assertEquals(FoodItemCategory.SANDWICH.getId(), result.getCategoryId());
    }

    @Test
    void mapJpaEntityToFoodItemDto() {
        FoodItemDTO result = mapper.jpaFoodItemToDTO(jpaFoodItemEntity);

        assertNotNull(result);
        assertEquals(jpaFoodItemEntity.getId(), result.getId());
        assertEquals(jpaFoodItemEntity.getName(), result.getName());
        assertEquals(jpaFoodItemEntity.getDescription(), result.getDescription());
        assertEquals(jpaFoodItemEntity.getPrice(), result.getPrice());
        assertEquals(FoodItemCategory.SANDWICH.getDescription(), result.getCategory());
    }

    @Test
    void mapFoodItemImageDtoToJpaEntity() {
        JpaFoodItemImageEntity result = mapper.foodItemImageDtoToJpa(foodItemImageDTO);

        assertNotNull(result);
        assertEquals(foodItemImageDTO.getId(), result.getId());
        assertEquals(foodItemImageDTO.getFoodItemId(), result.getFoodItemId());
        assertEquals(foodItemImageDTO.getData(), result.getData());
        assertEquals(foodItemImageDTO.getFileName(), result.getFileName());
        assertEquals(foodItemImageDTO.getFileExtension(), result.getFileExtension());
        assertEquals(foodItemImageDTO.getImageError(), result.getImageError());
    }

    @Test
    void mapJpaEntityToFoodItemImageDto() {
        FoodItemImageDTO result = mapper.jpaFoodItemImageToDTO(jpaFoodItemImageEntity);

        assertNotNull(result);
        assertEquals(jpaFoodItemImageEntity.getId(), result.getId());
        assertEquals(jpaFoodItemImageEntity.getFoodItemId(), result.getFoodItemId());
        assertEquals(jpaFoodItemImageEntity.getData(), result.getData());
        assertEquals(jpaFoodItemImageEntity.getFileName(), result.getFileName());
        assertEquals(jpaFoodItemImageEntity.getFileExtension(), result.getFileExtension());
        assertEquals(jpaFoodItemImageEntity.getImageError(), result.getImageError());
    }

    @Test
    void mapJpaImageListToImageDtoList() {
        List<JpaFoodItemImageEntity> entities = Collections.singletonList(jpaFoodItemImageEntity);

        List<FoodItemImageDTO> result = mapper.jpaFoodItemImageToDtoList(entities);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(jpaFoodItemImageEntity.getId(), result.get(0).getId());
    }

    @Test
    void mapImageDtoListToJpaImageList() {
        List<FoodItemImageDTO> dtos = Collections.singletonList(foodItemImageDTO);

        List<JpaFoodItemImageEntity> result = mapper.foodItemImageDtoToJpaList(dtos);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(foodItemImageDTO.getId(), result.getFirst().getId());
    }

    @Test
    void mapNullFoodItemDtoReturnsNull() {
        JpaFoodItemEntity result = mapper.foodItemDtoToJpa(null);

        assertNull(result);
    }

    @Test
    void mapNullJpaEntityReturnsNull() {
        FoodItemDTO result = mapper.jpaFoodItemToDTO(null);

        assertNull(result);
    }

    @Test
    void mapNullFoodItemImageDtoReturnsNull() {
        JpaFoodItemImageEntity result = mapper.foodItemImageDtoToJpa(null);

        assertNull(result);
    }

    @Test
    void mapNullJpaImageEntityReturnsNull() {
        FoodItemImageDTO result = mapper.jpaFoodItemImageToDTO(null);

        assertNull(result);
    }

    @Test
    void mapNullImageListReturnsNull() {
        List<FoodItemImageDTO> result = mapper.jpaFoodItemImageToDtoList(null);

        assertNull(result);
    }

    @Test
    void mapNullImageDtoListReturnsNull() {
        List<JpaFoodItemImageEntity> result = mapper.foodItemImageDtoToJpaList(null);

        assertNull(result);
    }

    @Test
    void mapEmptyImageLists() {
        List<FoodItemImageDTO> emptyDtoList = mapper.jpaFoodItemImageToDtoList(Collections.emptyList());
        List<JpaFoodItemImageEntity> emptyEntityList = mapper.foodItemImageDtoToJpaList(Collections.emptyList());

        assertNotNull(emptyDtoList);
        assertNotNull(emptyEntityList);
        assertTrue(emptyDtoList.isEmpty());
        assertTrue(emptyEntityList.isEmpty());
    }
}
