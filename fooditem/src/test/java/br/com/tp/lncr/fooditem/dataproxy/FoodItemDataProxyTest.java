package br.com.tp.lncr.fooditem.dataproxy;

import br.com.tp.lncr.fooditem.datasources.postgres.*;
import br.com.tp.lncr.fooditem.datasources.storage.FoodItemImageStorageImpl;
import br.com.tp.lncr.core.dtos.fooditem.FoodItemDTO;
import br.com.tp.lncr.core.dtos.fooditem.FoodItemImageDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FoodItemDataProxyTest {

    @Mock
    private JpaFoodItemRepositoryImpl jpaFoodItemRepositoryImpl;

    @Mock
    private JpaFoodItemReposity jpaFoodItemRepository;

    @Mock
    private JpaFoodItemImageRepositoryImpl jpaFoodItemImageRepositoryImpl;

    @Mock
    private JpaFoodItemImageRepository jpaFoodItemImageRepository;

    @Mock
    private FoodItemImageStorageImpl foodItemImageStorageImpl;

    @Mock
    private JpaFoodItemMapper jpaFoodItemMapper;

    private FoodItemDataProxy foodItemDataProxy;
    private FoodItemDTO foodItemDTO;
    private FoodItemImageDTO foodItemImageDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        foodItemDataProxy = new FoodItemDataProxy(
                jpaFoodItemRepositoryImpl,
                jpaFoodItemRepository,
                jpaFoodItemImageRepositoryImpl,
                jpaFoodItemImageRepository,
                foodItemImageStorageImpl,
                jpaFoodItemMapper
        );

        foodItemDTO = new FoodItemDTO();
        foodItemDTO.setId(1);
        foodItemDTO.setName("X-Bacon");
        foodItemDTO.setDescription("Delicious bacon burger");
        foodItemDTO.setPrice(25.99);
        foodItemDTO.setCategory("Sandwich");

        foodItemImageDTO = new FoodItemImageDTO();
        foodItemImageDTO.setId(1);
        foodItemImageDTO.setFoodItemId(1);
        foodItemImageDTO.setData("base64data");
        foodItemImageDTO.setFileName("11.jpg");
        foodItemImageDTO.setFileExtension("jpg");
    }

    @Test
    void checkIfFoodItemExistsByNameReturnsTrue() {
        when(jpaFoodItemRepositoryImpl.existsByName(eq("X-Bacon"), any())).thenReturn(true);

        boolean result = foodItemDataProxy.existsByName("X-Bacon");

        assertTrue(result);
        verify(jpaFoodItemRepositoryImpl).existsByName("X-Bacon", jpaFoodItemRepository);
    }

    @Test
    void checkIfFoodItemExistsByNameReturnsFalse() {
        when(jpaFoodItemRepositoryImpl.existsByName(eq("NonExistent"), any())).thenReturn(false);

        boolean result = foodItemDataProxy.existsByName("NonExistent");

        assertFalse(result);
        verify(jpaFoodItemRepositoryImpl).existsByName("NonExistent", jpaFoodItemRepository);
    }

    @Test
    void createFoodItemWithoutImages() {
        when(jpaFoodItemRepositoryImpl.save(any(FoodItemDTO.class), any(), any())).thenReturn(foodItemDTO);

        FoodItemDTO result = foodItemDataProxy.create(foodItemDTO);

        assertNotNull(result);
        assertEquals(foodItemDTO.getId(), result.getId());
        verify(jpaFoodItemRepositoryImpl).save(foodItemDTO, jpaFoodItemRepository, jpaFoodItemMapper);
        verify(jpaFoodItemImageRepositoryImpl, never()).saveAll(any(), any(), any());
        verify(foodItemImageStorageImpl, never()).saveImagesFiles(any());
    }

    @Test
    void createFoodItemWithImages() {
        foodItemDTO.setImages(Collections.singletonList(foodItemImageDTO));
        when(jpaFoodItemRepositoryImpl.save(any(FoodItemDTO.class), any(), any())).thenReturn(foodItemDTO);

        FoodItemDTO result = foodItemDataProxy.create(foodItemDTO);

        assertNotNull(result);
        assertEquals(foodItemDTO.getId(), result.getId());
        assertEquals("11.jpg", result.getImages().getFirst().getFileName());
        verify(jpaFoodItemRepositoryImpl).save(foodItemDTO, jpaFoodItemRepository, jpaFoodItemMapper);
        verify(jpaFoodItemImageRepositoryImpl).saveAll(any(List.class), eq(jpaFoodItemImageRepository), eq(jpaFoodItemMapper));
        verify(foodItemImageStorageImpl).saveImagesFiles(any());
    }

    @Test
    void findAllFoodItemsWithoutCategoryAndWithoutImages() {
        List<FoodItemDTO> foodItems = Collections.singletonList(foodItemDTO);
        when(jpaFoodItemRepositoryImpl.getAllFoodItems(eq(10), any(), any())).thenReturn(foodItems);

        List<FoodItemDTO> result = foodItemDataProxy.findAllFoodItems(10, null, false);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertNull(result.getFirst().getImages());
        verify(jpaFoodItemRepositoryImpl).getAllFoodItems(10, jpaFoodItemRepository, jpaFoodItemMapper);
        verify(jpaFoodItemImageRepositoryImpl, never()).findAllByFoodItemId(any(), any(), any());
    }

    @Test
    void findAllFoodItemsWithCategoryAndWithImages() {
        List<FoodItemDTO> foodItems = Collections.singletonList(foodItemDTO);
        List<FoodItemImageDTO> images = Collections.singletonList(foodItemImageDTO);
        when(jpaFoodItemRepositoryImpl.getAllFoodItemsByCategory(eq(10), eq(1), any(), any())).thenReturn(foodItems);
        when(jpaFoodItemImageRepositoryImpl.findAllByFoodItemId(eq(1), any(), any())).thenReturn(images);

        List<FoodItemDTO> result = foodItemDataProxy.findAllFoodItems(10, 1, true);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertNotNull(result.getFirst().getImages());
        assertEquals(1, result.getFirst().getImages().size());
        verify(jpaFoodItemRepositoryImpl).getAllFoodItemsByCategory(10, 1, jpaFoodItemRepository, jpaFoodItemMapper);
        verify(jpaFoodItemImageRepositoryImpl).findAllByFoodItemId(1, jpaFoodItemImageRepository, jpaFoodItemMapper);
    }

    @Test
    void findFoodItemByIdWithImages() {
        List<FoodItemImageDTO> images = Collections.singletonList(foodItemImageDTO);
        when(jpaFoodItemRepositoryImpl.findById(eq(1), any(), any())).thenReturn(foodItemDTO);
        when(jpaFoodItemImageRepositoryImpl.findAllByFoodItemId(eq(1), any(), any())).thenReturn(images);

        FoodItemDTO result = foodItemDataProxy.findFoodItemById(1, true);

        assertNotNull(result);
        assertEquals(foodItemDTO.getId(), result.getId());
        assertNotNull(result.getImages());
        verify(jpaFoodItemRepositoryImpl).findById(1, jpaFoodItemRepository, jpaFoodItemMapper);
        verify(jpaFoodItemImageRepositoryImpl).findAllByFoodItemId(1, jpaFoodItemImageRepository, jpaFoodItemMapper);
    }

    @Test
    void findFoodItemByIdWithoutImages() {
        when(jpaFoodItemRepositoryImpl.findById(eq(1), any(), any())).thenReturn(foodItemDTO);

        FoodItemDTO result = foodItemDataProxy.findFoodItemById(1, false);

        assertNotNull(result);
        assertEquals(foodItemDTO.getId(), result.getId());
        verify(jpaFoodItemRepositoryImpl).findById(1, jpaFoodItemRepository, jpaFoodItemMapper);
        verify(jpaFoodItemImageRepositoryImpl, never()).findAllByFoodItemId(any(), any(), any());
    }

    @Test
    void findFoodItemByIdReturnsNullWhenNotFound() {
        when(jpaFoodItemRepositoryImpl.findById(eq(999), any(), any())).thenReturn(null);

        FoodItemDTO result = foodItemDataProxy.findFoodItemById(999, true);

        assertNull(result);
        verify(jpaFoodItemRepositoryImpl).findById(999, jpaFoodItemRepository, jpaFoodItemMapper);
        verify(jpaFoodItemImageRepositoryImpl, never()).findAllByFoodItemId(any(), any(), any());
    }

    @Test
    void findFoodItemByIdListReturnsMatchingItems() {
        List<Integer> ids = Arrays.asList(1, 2);
        List<FoodItemDTO> foodItems = Collections.singletonList(foodItemDTO);
        when(jpaFoodItemRepositoryImpl.findByIdList(eq(ids), any(), any())).thenReturn(foodItems);

        List<FoodItemDTO> result = foodItemDataProxy.findFoodItemByIdList(ids);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(jpaFoodItemRepositoryImpl).findByIdList(ids, jpaFoodItemRepository, jpaFoodItemMapper);
    }

    @Test
    void findFoodItemImageByIdReturnsImageWithData() throws IOException {
        when(jpaFoodItemImageRepositoryImpl.findById(eq(1), any(), any())).thenReturn(foodItemImageDTO);
        when(foodItemImageStorageImpl.getImgaeData("11.jpg")).thenReturn("base64data");

        FoodItemImageDTO result = foodItemDataProxy.findFoodItemImageById(1);

        assertNotNull(result);
        assertEquals(foodItemImageDTO.getId(), result.getId());
        verify(jpaFoodItemImageRepositoryImpl).findById(1, jpaFoodItemImageRepository, jpaFoodItemMapper);
        verify(foodItemImageStorageImpl).getImgaeData("11.jpg");
    }

    @Test
    void findFoodItemImageByIdHandlesIOException() throws IOException {
        when(jpaFoodItemImageRepositoryImpl.findById(eq(1), any(), any())).thenReturn(foodItemImageDTO);
        when(foodItemImageStorageImpl.getImgaeData("11.jpg")).thenThrow(new IOException("File not found"));

        FoodItemImageDTO result = foodItemDataProxy.findFoodItemImageById(1);

        assertNotNull(result);
        assertEquals(foodItemImageDTO.getId(), result.getId());
        verify(jpaFoodItemImageRepositoryImpl).findById(1, jpaFoodItemImageRepository, jpaFoodItemMapper);
        verify(foodItemImageStorageImpl).getImgaeData("11.jpg");
    }

    @Test
    void findAllFoodItemImagesByFoodItemIdWithData() {
        List<FoodItemImageDTO> images = Collections.singletonList(foodItemImageDTO);
        when(jpaFoodItemImageRepositoryImpl.findAllByFoodItemId(eq(1), any(), any())).thenReturn(images);

        List<FoodItemImageDTO> result = foodItemDataProxy.findAllFoodItemImagesByFoodItemId(1, true);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(jpaFoodItemImageRepositoryImpl).findAllByFoodItemId(1, jpaFoodItemImageRepository, jpaFoodItemMapper);
        verify(foodItemImageStorageImpl).getImagesFiles(images);
    }

    @Test
    void findAllFoodItemImagesByFoodItemIdWithoutData() {
        List<FoodItemImageDTO> images = Collections.singletonList(foodItemImageDTO);
        when(jpaFoodItemImageRepositoryImpl.findAllByFoodItemId(eq(1), any(), any())).thenReturn(images);

        List<FoodItemImageDTO> result = foodItemDataProxy.findAllFoodItemImagesByFoodItemId(1, false);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(jpaFoodItemImageRepositoryImpl).findAllByFoodItemId(1, jpaFoodItemImageRepository, jpaFoodItemMapper);
        verify(foodItemImageStorageImpl, never()).getImagesFiles(any());
    }

    @Test
    void saveFoodItemSuccessfully() {
        when(jpaFoodItemRepositoryImpl.save(eq(foodItemDTO), any(), any())).thenReturn(foodItemDTO);

        FoodItemDTO result = foodItemDataProxy.save(foodItemDTO);

        assertNotNull(result);
        assertEquals(foodItemDTO.getId(), result.getId());
        verify(jpaFoodItemRepositoryImpl).save(foodItemDTO, jpaFoodItemRepository, jpaFoodItemMapper);
    }

    @Test
    void saveFoodItemImageSuccessfully() {
        when(jpaFoodItemImageRepositoryImpl.save(eq(foodItemImageDTO), any(), any())).thenReturn(foodItemImageDTO);

        FoodItemImageDTO result = foodItemDataProxy.save(foodItemImageDTO);

        assertNotNull(result);
        assertEquals(foodItemImageDTO.getId(), result.getId());
        verify(jpaFoodItemImageRepositoryImpl).save(foodItemImageDTO, jpaFoodItemImageRepository, jpaFoodItemMapper);
        verify(foodItemImageStorageImpl).saveImageFile(foodItemImageDTO);
    }

    @Test
    void saveFoodItemImageReturnsNullAndDoesNotSaveFile() {
        when(jpaFoodItemImageRepositoryImpl.save(eq(foodItemImageDTO), any(), any())).thenReturn(null);

        FoodItemImageDTO result = foodItemDataProxy.save(foodItemImageDTO);

        assertNotNull(result);
        verify(jpaFoodItemImageRepositoryImpl).save(foodItemImageDTO, jpaFoodItemImageRepository, jpaFoodItemMapper);
        verify(foodItemImageStorageImpl, never()).saveImageFile(any());
    }

    @Test
    void deleteFoodItemWithImages() {
        foodItemDTO.setImages(Collections.singletonList(foodItemImageDTO));

        foodItemDataProxy.delete(foodItemDTO);

        verify(jpaFoodItemRepositoryImpl).deleteById(1, jpaFoodItemRepository);
        verify(jpaFoodItemImageRepositoryImpl).deleteByFoodItemId(foodItemDTO.getImages(), jpaFoodItemImageRepository, jpaFoodItemMapper);
        verify(foodItemImageStorageImpl).deleteImagesFiles(foodItemDTO.getImages());
    }

    @Test
    void deleteFoodItemWithoutImages() {
        foodItemDTO.setImages(Collections.emptyList());

        foodItemDataProxy.delete(foodItemDTO);

        verify(jpaFoodItemRepositoryImpl).deleteById(1, jpaFoodItemRepository);
        verify(jpaFoodItemImageRepositoryImpl, never()).deleteByFoodItemId(any(), any(), any());
        verify(foodItemImageStorageImpl, never()).deleteImagesFiles(any());
    }

    @Test
    void deleteFoodItemImage() {
        foodItemDataProxy.delete(foodItemImageDTO);

        verify(jpaFoodItemImageRepositoryImpl).delete(foodItemImageDTO, jpaFoodItemImageRepository, jpaFoodItemMapper);
        verify(foodItemImageStorageImpl).deleteImageFile("11.jpg");
    }

    @Test
    void deleteImageFileByFileName() {
        String fileName = "test-image.jpg";

        foodItemDataProxy.deleteImageFile(fileName);

        verify(foodItemImageStorageImpl).deleteImageFile(fileName);
    }

    @Test
    void createFoodItemImageSuccessfully() {
        when(jpaFoodItemImageRepositoryImpl.save(eq(foodItemImageDTO), any(), any())).thenReturn(foodItemImageDTO);

        foodItemDataProxy.create(foodItemImageDTO);

        verify(jpaFoodItemImageRepositoryImpl).save(foodItemImageDTO, jpaFoodItemImageRepository, jpaFoodItemMapper);
        verify(foodItemImageStorageImpl).saveImageFile(foodItemImageDTO);
    }

    @Test
    void deleteImagesByFoodItemIdWithImages() {
        List<FoodItemImageDTO> imagesToDelete = Collections.singletonList(foodItemImageDTO);
        when(jpaFoodItemImageRepositoryImpl.deleteImagesByFoodItemId(eq(1), any(), any())).thenReturn(imagesToDelete);

        foodItemDataProxy.deleteImagesByFoodItemId(1);

        verify(jpaFoodItemImageRepositoryImpl).deleteImagesByFoodItemId(1, jpaFoodItemImageRepository, jpaFoodItemMapper);
        verify(foodItemImageStorageImpl).deleteImagesFiles(imagesToDelete);
    }

    @Test
    void deleteImagesByFoodItemIdWithNoImages() {
        when(jpaFoodItemImageRepositoryImpl.deleteImagesByFoodItemId(eq(1), any(), any())).thenReturn(null);

        foodItemDataProxy.deleteImagesByFoodItemId(1);

        verify(jpaFoodItemImageRepositoryImpl).deleteImagesByFoodItemId(1, jpaFoodItemImageRepository, jpaFoodItemMapper);
        verify(foodItemImageStorageImpl, never()).deleteImagesFiles(any());
    }
}
