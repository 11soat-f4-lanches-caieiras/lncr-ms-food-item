package br.com.tp.lncr.fooditem.datasources.postgres;

import br.com.tp.lncr.core.dtos.fooditem.FoodItemDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JpaFoodItemRepositoryImplTest {

    @Mock
    private JpaFoodItemReposity jpaFoodItemRepository;

    @Mock
    private JpaFoodItemMapper jpaFoodItemMapper;

    private JpaFoodItemRepositoryImpl repositoryImpl;
    private FoodItemDTO foodItemDTO;
    private JpaFoodItemEntity jpaFoodItemEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        repositoryImpl = new JpaFoodItemRepositoryImpl();

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
    }

    @Test
    void saveFoodItemSuccessfully() {
        when(jpaFoodItemMapper.foodItemDtoToJpa(foodItemDTO)).thenReturn(jpaFoodItemEntity);
        when(jpaFoodItemRepository.save(jpaFoodItemEntity)).thenReturn(jpaFoodItemEntity);
        when(jpaFoodItemMapper.jpaFoodItemToDTO(jpaFoodItemEntity)).thenReturn(foodItemDTO);

        FoodItemDTO result = repositoryImpl.save(foodItemDTO, jpaFoodItemRepository, jpaFoodItemMapper);

        assertNotNull(result);
        assertEquals(foodItemDTO.getId(), result.getId());
        verify(jpaFoodItemMapper).foodItemDtoToJpa(foodItemDTO);
        verify(jpaFoodItemRepository).save(jpaFoodItemEntity);
        verify(jpaFoodItemMapper).jpaFoodItemToDTO(jpaFoodItemEntity);
    }

    @Test
    void checkIfFoodItemExistsByNameReturnsTrue() {
        when(jpaFoodItemRepository.existsByName("X-Bacon")).thenReturn(true);

        boolean result = repositoryImpl.existsByName("X-Bacon", jpaFoodItemRepository);

        assertTrue(result);
        verify(jpaFoodItemRepository).existsByName("X-Bacon");
    }

    @Test
    void checkIfFoodItemExistsByNameReturnsFalse() {
        when(jpaFoodItemRepository.existsByName("NonExistent")).thenReturn(false);

        boolean result = repositoryImpl.existsByName("NonExistent", jpaFoodItemRepository);

        assertFalse(result);
        verify(jpaFoodItemRepository).existsByName("NonExistent");
    }

    @Test
    void getAllFoodItemsWithLimit() {
        Page<JpaFoodItemEntity> page = new PageImpl<>(Collections.singletonList(jpaFoodItemEntity));
        when(jpaFoodItemRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(jpaFoodItemMapper.jpaFoodItemToDTO(jpaFoodItemEntity)).thenReturn(foodItemDTO);

        List<FoodItemDTO> result = repositoryImpl.getAllFoodItems(10, jpaFoodItemRepository, jpaFoodItemMapper);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(foodItemDTO.getId(), result.get(0).getId());
        verify(jpaFoodItemRepository).findAll(any(Pageable.class));
        verify(jpaFoodItemMapper).jpaFoodItemToDTO(jpaFoodItemEntity);
    }

    @Test
    void getAllFoodItemsByCategoryWithLimit() {
        when(jpaFoodItemRepository.findAllByCategory(10, 1)).thenReturn(Collections.singletonList(jpaFoodItemEntity));
        when(jpaFoodItemMapper.jpaFoodItemToDTO(jpaFoodItemEntity)).thenReturn(foodItemDTO);

        List<FoodItemDTO> result = repositoryImpl.getAllFoodItemsByCategory(10, 1, jpaFoodItemRepository, jpaFoodItemMapper);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(foodItemDTO.getId(), result.get(0).getId());
        verify(jpaFoodItemRepository).findAllByCategory(10, 1);
        verify(jpaFoodItemMapper).jpaFoodItemToDTO(jpaFoodItemEntity);
    }

    @Test
    void findFoodItemByIdReturnsItem() {
        when(jpaFoodItemRepository.findById(1)).thenReturn(Optional.of(jpaFoodItemEntity));
        when(jpaFoodItemMapper.jpaFoodItemToDTO(jpaFoodItemEntity)).thenReturn(foodItemDTO);

        FoodItemDTO result = repositoryImpl.findById(1, jpaFoodItemRepository, jpaFoodItemMapper);

        assertNotNull(result);
        assertEquals(foodItemDTO.getId(), result.getId());
        verify(jpaFoodItemRepository).findById(1);
        verify(jpaFoodItemMapper).jpaFoodItemToDTO(jpaFoodItemEntity);
    }

    @Test
    void findFoodItemByIdReturnsNullWhenNotFound() {
        when(jpaFoodItemRepository.findById(999)).thenReturn(Optional.empty());
        when(jpaFoodItemMapper.jpaFoodItemToDTO(null)).thenReturn(null);

        FoodItemDTO result = repositoryImpl.findById(999, jpaFoodItemRepository, jpaFoodItemMapper);

        assertNull(result);
        verify(jpaFoodItemRepository).findById(999);
        verify(jpaFoodItemMapper).jpaFoodItemToDTO(null);
    }

    @Test
    void findByIdListReturnsMatchingItems() {
        List<Integer> ids = Arrays.asList(1, 2, 3);
        when(jpaFoodItemRepository.findByIdList(ids)).thenReturn(Collections.singletonList(jpaFoodItemEntity));
        when(jpaFoodItemMapper.jpaFoodItemToDTO(jpaFoodItemEntity)).thenReturn(foodItemDTO);

        List<FoodItemDTO> result = repositoryImpl.findByIdList(ids, jpaFoodItemRepository, jpaFoodItemMapper);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(foodItemDTO.getId(), result.get(0).getId());
        verify(jpaFoodItemRepository).findByIdList(ids);
        verify(jpaFoodItemMapper).jpaFoodItemToDTO(jpaFoodItemEntity);
    }

    @Test
    void findByIdListReturnsEmptyWhenNoMatches() {
        List<Integer> ids = Arrays.asList(999, 998);
        when(jpaFoodItemRepository.findByIdList(ids)).thenReturn(Collections.emptyList());

        List<FoodItemDTO> result = repositoryImpl.findByIdList(ids, jpaFoodItemRepository, jpaFoodItemMapper);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(jpaFoodItemRepository).findByIdList(ids);
        verify(jpaFoodItemMapper, never()).jpaFoodItemToDTO(any());
    }

    @Test
    void deleteByIdExecutesSuccessfully() {
        repositoryImpl.deleteById(1, jpaFoodItemRepository);

        verify(jpaFoodItemRepository).deleteById(1);
    }

    @Test
    void getAllFoodItemsReturnsEmptyList() {
        Page<JpaFoodItemEntity> emptyPage = new PageImpl<>(Collections.emptyList());
        when(jpaFoodItemRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        List<FoodItemDTO> result = repositoryImpl.getAllFoodItems(10, jpaFoodItemRepository, jpaFoodItemMapper);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(jpaFoodItemRepository).findAll(any(Pageable.class));
        verify(jpaFoodItemMapper, never()).jpaFoodItemToDTO(any());
    }

    @Test
    void getAllFoodItemsByCategoryReturnsEmptyList() {
        when(jpaFoodItemRepository.findAllByCategory(10, 999)).thenReturn(Collections.emptyList());

        List<FoodItemDTO> result = repositoryImpl.getAllFoodItemsByCategory(10, 999, jpaFoodItemRepository, jpaFoodItemMapper);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(jpaFoodItemRepository).findAllByCategory(10, 999);
        verify(jpaFoodItemMapper, never()).jpaFoodItemToDTO(any());
    }
}
