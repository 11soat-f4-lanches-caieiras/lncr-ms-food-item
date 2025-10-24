package br.com.tp.lncr.fooditem.datasources.postgres;

import br.com.tp.lncr.core.dtos.fooditem.FoodItemImageDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JpaFoodItemImageRepositoryImplTest {

    @Mock
    private JpaFoodItemImageRepository jpaFoodItemImageRepository;

    @Mock
    private JpaFoodItemMapper jpaFoodItemMapper;

    private JpaFoodItemImageRepositoryImpl repositoryImpl;
    private FoodItemImageDTO foodItemImageDTO;
    private JpaFoodItemImageEntity jpaFoodItemImageEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        repositoryImpl = new JpaFoodItemImageRepositoryImpl();

        foodItemImageDTO = new FoodItemImageDTO();
        foodItemImageDTO.setId(1);
        foodItemImageDTO.setFoodItemId(10);
        foodItemImageDTO.setData("base64data");
        foodItemImageDTO.setFileName("image.jpg");
        foodItemImageDTO.setFileExtension("jpg");

        jpaFoodItemImageEntity = new JpaFoodItemImageEntity();
        jpaFoodItemImageEntity.setId(1);
        jpaFoodItemImageEntity.setFoodItemId(10);
        jpaFoodItemImageEntity.setData("base64data");
        jpaFoodItemImageEntity.setFileName("image.jpg");
        jpaFoodItemImageEntity.setFileExtension("jpg");
    }

    @Test
    void saveAllImagesSuccessfully() {
        List<FoodItemImageDTO> imageDTOList = Collections.singletonList(foodItemImageDTO);
        List<JpaFoodItemImageEntity> imageEntityList = Collections.singletonList(jpaFoodItemImageEntity);

        when(jpaFoodItemMapper.foodItemImageDtoToJpaList(imageDTOList)).thenReturn(imageEntityList);
        when(jpaFoodItemImageRepository.saveAll(imageEntityList)).thenReturn(imageEntityList);

        repositoryImpl.saveAll(imageDTOList, jpaFoodItemImageRepository, jpaFoodItemMapper);

        verify(jpaFoodItemMapper).foodItemImageDtoToJpaList(imageDTOList);
        verify(jpaFoodItemImageRepository).saveAll(imageEntityList);
    }

    @Test
    void findAllByFoodItemIdReturnsImages() {
        List<JpaFoodItemImageEntity> entityList = Collections.singletonList(jpaFoodItemImageEntity);
        when(jpaFoodItemImageRepository.findAllByFoodItemId(10)).thenReturn(entityList);
        when(jpaFoodItemMapper.jpaFoodItemImageToDTO(jpaFoodItemImageEntity)).thenReturn(foodItemImageDTO);

        List<FoodItemImageDTO> result = repositoryImpl.findAllByFoodItemId(10, jpaFoodItemImageRepository, jpaFoodItemMapper);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(foodItemImageDTO.getId(), result.get(0).getId());
        verify(jpaFoodItemImageRepository).findAllByFoodItemId(10);
        verify(jpaFoodItemMapper).jpaFoodItemImageToDTO(jpaFoodItemImageEntity);
    }

    @Test
    void findAllByFoodItemIdReturnsEmptyWhenNoImages() {
        when(jpaFoodItemImageRepository.findAllByFoodItemId(999)).thenReturn(Collections.emptyList());

        List<FoodItemImageDTO> result = repositoryImpl.findAllByFoodItemId(999, jpaFoodItemImageRepository, jpaFoodItemMapper);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(jpaFoodItemImageRepository).findAllByFoodItemId(999);
        verify(jpaFoodItemMapper, never()).jpaFoodItemImageToDTO(any());
    }

    @Test
    void deleteByFoodItemIdRemovesImages() {
        List<FoodItemImageDTO> imageDTOList = Collections.singletonList(foodItemImageDTO);
        List<JpaFoodItemImageEntity> imageEntityList = Collections.singletonList(jpaFoodItemImageEntity);

        when(jpaFoodItemMapper.foodItemImageDtoToJpaList(imageDTOList)).thenReturn(imageEntityList);

        repositoryImpl.deleteByFoodItemId(imageDTOList, jpaFoodItemImageRepository, jpaFoodItemMapper);

        verify(jpaFoodItemMapper).foodItemImageDtoToJpaList(imageDTOList);
        verify(jpaFoodItemImageRepository).deleteAll(imageEntityList);
    }

    @Test
    void saveSingleImageSuccessfully() {
        when(jpaFoodItemMapper.foodItemImageDtoToJpa(foodItemImageDTO)).thenReturn(jpaFoodItemImageEntity);
        when(jpaFoodItemImageRepository.save(jpaFoodItemImageEntity)).thenReturn(jpaFoodItemImageEntity);
        when(jpaFoodItemMapper.jpaFoodItemImageToDTO(jpaFoodItemImageEntity)).thenReturn(foodItemImageDTO);

        FoodItemImageDTO result = repositoryImpl.save(foodItemImageDTO, jpaFoodItemImageRepository, jpaFoodItemMapper);

        assertNotNull(result);
        assertEquals(foodItemImageDTO.getId(), result.getId());
        verify(jpaFoodItemMapper).foodItemImageDtoToJpa(foodItemImageDTO);
        verify(jpaFoodItemImageRepository).save(jpaFoodItemImageEntity);
        verify(jpaFoodItemMapper).jpaFoodItemImageToDTO(jpaFoodItemImageEntity);
    }

    @Test
    void findByIdReturnsImage() {
        when(jpaFoodItemImageRepository.findById(1)).thenReturn(Optional.of(jpaFoodItemImageEntity));
        when(jpaFoodItemMapper.jpaFoodItemImageToDTO(jpaFoodItemImageEntity)).thenReturn(foodItemImageDTO);

        FoodItemImageDTO result = repositoryImpl.findById(1, jpaFoodItemImageRepository, jpaFoodItemMapper);

        assertNotNull(result);
        assertEquals(foodItemImageDTO.getId(), result.getId());
        verify(jpaFoodItemImageRepository).findById(1);
        verify(jpaFoodItemMapper).jpaFoodItemImageToDTO(jpaFoodItemImageEntity);
    }

    @Test
    void findByIdReturnsNullWhenNotFound() {
        when(jpaFoodItemImageRepository.findById(999)).thenReturn(Optional.empty());
        when(jpaFoodItemMapper.jpaFoodItemImageToDTO(null)).thenReturn(null);

        FoodItemImageDTO result = repositoryImpl.findById(999, jpaFoodItemImageRepository, jpaFoodItemMapper);

        assertNull(result);
        verify(jpaFoodItemImageRepository).findById(999);
        verify(jpaFoodItemMapper).jpaFoodItemImageToDTO(null);
    }

    @Test
    void deleteImagesByFoodItemIdReturnsDeletedImages() {
        List<JpaFoodItemImageEntity> entityList = Collections.singletonList(jpaFoodItemImageEntity);
        List<FoodItemImageDTO> imageDTOList = Collections.singletonList(foodItemImageDTO);
        List<JpaFoodItemImageEntity> imageEntityList = Collections.singletonList(jpaFoodItemImageEntity);

        when(jpaFoodItemImageRepository.findAllByFoodItemId(10)).thenReturn(entityList);
        when(jpaFoodItemMapper.jpaFoodItemImageToDTO(jpaFoodItemImageEntity)).thenReturn(foodItemImageDTO);
        when(jpaFoodItemMapper.foodItemImageDtoToJpaList(imageDTOList)).thenReturn(imageEntityList);

        List<FoodItemImageDTO> result = repositoryImpl.deleteImagesByFoodItemId(10, jpaFoodItemImageRepository, jpaFoodItemMapper);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(foodItemImageDTO.getId(), result.get(0).getId());
        verify(jpaFoodItemImageRepository).findAllByFoodItemId(10);
        verify(jpaFoodItemImageRepository).deleteAll(imageEntityList);
        verify(jpaFoodItemMapper).jpaFoodItemImageToDTO(jpaFoodItemImageEntity);
        verify(jpaFoodItemMapper).foodItemImageDtoToJpaList(imageDTOList);
    }

    @Test
    void deleteImagesByFoodItemIdReturnsEmptyWhenNoImages() {
        when(jpaFoodItemImageRepository.findAllByFoodItemId(999)).thenReturn(Collections.emptyList());
        when(jpaFoodItemMapper.foodItemImageDtoToJpaList(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<FoodItemImageDTO> result = repositoryImpl.deleteImagesByFoodItemId(999, jpaFoodItemImageRepository, jpaFoodItemMapper);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(jpaFoodItemImageRepository).findAllByFoodItemId(999);
        verify(jpaFoodItemImageRepository).deleteAll(Collections.emptyList());
        verify(jpaFoodItemMapper, never()).jpaFoodItemImageToDTO(any());
    }

    @Test
    void deleteSingleImageSuccessfully() {
        when(jpaFoodItemMapper.foodItemImageDtoToJpa(foodItemImageDTO)).thenReturn(jpaFoodItemImageEntity);

        repositoryImpl.delete(foodItemImageDTO, jpaFoodItemImageRepository, jpaFoodItemMapper);

        verify(jpaFoodItemMapper).foodItemImageDtoToJpa(foodItemImageDTO);
        verify(jpaFoodItemImageRepository).delete(jpaFoodItemImageEntity);
    }

    @Test
    void saveAllWithEmptyListDoesNothing() {
        List<FoodItemImageDTO> emptyList = Collections.emptyList();
        when(jpaFoodItemMapper.foodItemImageDtoToJpaList(emptyList)).thenReturn(Collections.emptyList());

        repositoryImpl.saveAll(emptyList, jpaFoodItemImageRepository, jpaFoodItemMapper);

        verify(jpaFoodItemMapper).foodItemImageDtoToJpaList(emptyList);
        verify(jpaFoodItemImageRepository).saveAll(Collections.emptyList());
    }

    @Test
    void deleteByFoodItemIdWithEmptyListDoesNothing() {
        List<FoodItemImageDTO> emptyList = Collections.emptyList();
        when(jpaFoodItemMapper.foodItemImageDtoToJpaList(emptyList)).thenReturn(Collections.emptyList());

        repositoryImpl.deleteByFoodItemId(emptyList, jpaFoodItemImageRepository, jpaFoodItemMapper);

        verify(jpaFoodItemMapper).foodItemImageDtoToJpaList(emptyList);
        verify(jpaFoodItemImageRepository).deleteAll(Collections.emptyList());
    }
}
