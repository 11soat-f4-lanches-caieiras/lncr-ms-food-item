package br.com.tp.lncr.fooditem.dataproxy;

import br.com.tp.lncr.core.dtos.fooditem.FoodItemDTO;
import br.com.tp.lncr.core.dtos.fooditem.FoodItemImageDTO;
import br.com.tp.lncr.core.interfaces.fooditem.FoodItemDatabase;
import br.com.tp.lncr.fooditem.datasources.postgres.*;
import br.com.tp.lncr.fooditem.datasources.storage.FoodItemImageStorageImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Component
public class FoodItemDataProxy implements FoodItemDatabase {

    private static final Logger log = LoggerFactory.getLogger(FoodItemDataProxy.class);

    private final JpaFoodItemRepositoryImpl jpaFoodItemRepositoryImpl;
    private final JpaFoodItemReposity jpaFoodItemRepository;
    private final JpaFoodItemImageRepositoryImpl jpaFoodItemImageRepositoryImpl;
    private final JpaFoodItemImageRepository jpaFoodItemImageRepository;
    private final FoodItemImageStorageImpl foodItemImageStorageImpl;
    private final JpaFoodItemMapper jpaFoodItemMapper;

    public FoodItemDataProxy(JpaFoodItemRepositoryImpl jpaFoodItemRepositoryImpl, JpaFoodItemReposity jpaFoodItemRepository, JpaFoodItemImageRepositoryImpl jpaFoodItemImageRepositoryImpl, JpaFoodItemImageRepository jpaFoodItemImageRepository, FoodItemImageStorageImpl foodItemImageStorageImpl, JpaFoodItemMapper jpaFoodItemMapper) {
        this.jpaFoodItemRepositoryImpl = jpaFoodItemRepositoryImpl;
        this.jpaFoodItemRepository = jpaFoodItemRepository;
        this.jpaFoodItemImageRepositoryImpl = jpaFoodItemImageRepositoryImpl;
        this.jpaFoodItemImageRepository = jpaFoodItemImageRepository;
        this.foodItemImageStorageImpl = foodItemImageStorageImpl;
        this.jpaFoodItemMapper = jpaFoodItemMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByName(String foodItemName) {
        return jpaFoodItemRepositoryImpl.existsByName(foodItemName, jpaFoodItemRepository);
    }

    @Override
    public FoodItemDTO create(FoodItemDTO foodItemDTO) {
        FoodItemDTO savedFoodItemDTO = jpaFoodItemRepositoryImpl.save(foodItemDTO,jpaFoodItemRepository,jpaFoodItemMapper);
        savedFoodItemDTO.setImages(foodItemDTO.getImages());
        if (savedFoodItemDTO.getImages() != null && !savedFoodItemDTO.getImages().isEmpty()) {
            for (int i = 0; i < savedFoodItemDTO.getImages().size(); i++) {
                savedFoodItemDTO.getImages().get(i).setFoodItemId(savedFoodItemDTO.getId());
                int imageIndex = i + 1;
                String imageId = String.valueOf(savedFoodItemDTO.getId()) + imageIndex;
                savedFoodItemDTO.getImages().get(i).setId(Integer.parseInt(imageId));
                savedFoodItemDTO.getImages().get(i).setFileName(imageId + "." + savedFoodItemDTO.getImages().get(i).getFileExtension());
            }
            jpaFoodItemImageRepositoryImpl.saveAll(savedFoodItemDTO.getImages(),jpaFoodItemImageRepository,jpaFoodItemMapper);
            foodItemImageStorageImpl.saveImagesFiles(savedFoodItemDTO.getImages());
            return savedFoodItemDTO;
        }
        return savedFoodItemDTO;
    }
    @Transactional(readOnly = true)
    @Override
    public List<FoodItemDTO> findAllFoodItems(Integer _limit, Integer categoryId, Boolean includeImages) {
        List<FoodItemDTO> foodItemsDTOList;

        if (categoryId == null) {
            foodItemsDTOList = jpaFoodItemRepositoryImpl.getAllFoodItems(_limit,jpaFoodItemRepository,jpaFoodItemMapper);
        } else {
            foodItemsDTOList = jpaFoodItemRepositoryImpl.getAllFoodItemsByCategory(_limit, categoryId,jpaFoodItemRepository,jpaFoodItemMapper);
        }

        if (includeImages != null && includeImages) {
            for (FoodItemDTO foodItemDTO : foodItemsDTOList) {
                List<FoodItemImageDTO> foodItemImages = jpaFoodItemImageRepositoryImpl.findAllByFoodItemId(foodItemDTO.getId(),jpaFoodItemImageRepository,jpaFoodItemMapper);
                foodItemDTO.setImages(foodItemImages);
            }
        } else {
            for (FoodItemDTO foodItemDTO : foodItemsDTOList) {
                foodItemDTO.setImages(null);
            }
        }
        return foodItemsDTOList;
    }

    @Transactional(readOnly = true)
    @Override
    public FoodItemDTO findFoodItemById(Integer foodItemId, Boolean includeImages) {
        FoodItemDTO foodItemDTO = this.jpaFoodItemRepositoryImpl.findById(foodItemId,jpaFoodItemRepository,jpaFoodItemMapper);
        if (includeImages && foodItemDTO != null) {
            foodItemDTO.setImages(this.jpaFoodItemImageRepositoryImpl.findAllByFoodItemId(foodItemId, jpaFoodItemImageRepository, jpaFoodItemMapper));
            return foodItemDTO;
        }
        return foodItemDTO;
    }

    @Transactional(readOnly = true)
    @Override
    public List<FoodItemDTO> findFoodItemByIdList(List<Integer> foodItemIds) {
        return this.jpaFoodItemRepositoryImpl.findByIdList(foodItemIds, jpaFoodItemRepository, jpaFoodItemMapper);
    }

    @Transactional(readOnly = true)
    @Override
    public FoodItemImageDTO findFoodItemImageById(Integer foodItemImageId) {
        FoodItemImageDTO foodItemImageDTO =  jpaFoodItemImageRepositoryImpl.findById(foodItemImageId,jpaFoodItemImageRepository,jpaFoodItemMapper);
        try {
            foodItemImageDTO.setData(this.foodItemImageStorageImpl.getImgaeData(foodItemImageDTO.getFileName()));
        } catch (IOException e) {
            log.error("Erro ao busar o arquivo {}", foodItemImageDTO.getFileName());
        }
        return foodItemImageDTO;
    }

    @Transactional(readOnly = true)
    @Override
    public List<FoodItemImageDTO> findAllFoodItemImagesByFoodItemId(Integer foodItemId, Boolean includeData) {
        List<FoodItemImageDTO> foodItemImageDTOList = jpaFoodItemImageRepositoryImpl.findAllByFoodItemId(foodItemId, jpaFoodItemImageRepository, jpaFoodItemMapper);
        if (includeData != null && includeData) {
            this.foodItemImageStorageImpl.getImagesFiles(foodItemImageDTOList);
        }
        return foodItemImageDTOList;
    }



    @Override
    public FoodItemDTO save(FoodItemDTO foodItemDTO) {
        return this.jpaFoodItemRepositoryImpl.save(foodItemDTO, jpaFoodItemRepository, jpaFoodItemMapper);
    }

    @Override
    public FoodItemImageDTO save(FoodItemImageDTO foodItemImageDTO) {
        FoodItemImageDTO newFoodItemImageDTO = this.jpaFoodItemImageRepositoryImpl.save(foodItemImageDTO,jpaFoodItemImageRepository,jpaFoodItemMapper);
        if (newFoodItemImageDTO != null) {
            this.foodItemImageStorageImpl.saveImageFile(foodItemImageDTO);
        }
        return foodItemImageDTO;
    }

    @Override
    public void delete(FoodItemDTO foodItemDTO) {
        this.jpaFoodItemRepositoryImpl.deleteById(foodItemDTO.getId(),jpaFoodItemRepository);
        if (!foodItemDTO.getImages().isEmpty()) {
            this.jpaFoodItemImageRepositoryImpl.deleteByFoodItemId(foodItemDTO.getImages(),jpaFoodItemImageRepository,jpaFoodItemMapper);
            this.foodItemImageStorageImpl.deleteImagesFiles(foodItemDTO.getImages());
        }
    }

    @Override
    public void delete(FoodItemImageDTO foodItemImageDTO) {
        this.jpaFoodItemImageRepositoryImpl.delete(foodItemImageDTO,jpaFoodItemImageRepository,jpaFoodItemMapper);
        this.foodItemImageStorageImpl.deleteImageFile(foodItemImageDTO.getFileName());
    }

    @Override
    public void deleteImageFile(String fileName) {
        this.foodItemImageStorageImpl.deleteImageFile(fileName);
    }

    @Override
    public void create(FoodItemImageDTO foodItemImageDTO) {
        this.jpaFoodItemImageRepositoryImpl.save(foodItemImageDTO, jpaFoodItemImageRepository, jpaFoodItemMapper);
        this.foodItemImageStorageImpl.saveImageFile(foodItemImageDTO);
    }

    @Override
    public void deleteImagesByFoodItemId(Integer foodItemId) {
        List<FoodItemImageDTO> listImagesToDelete = this.jpaFoodItemImageRepositoryImpl.deleteImagesByFoodItemId(foodItemId, jpaFoodItemImageRepository, jpaFoodItemMapper);
        if (listImagesToDelete != null) {
            this.foodItemImageStorageImpl.deleteImagesFiles(listImagesToDelete);
        }
    }
}
