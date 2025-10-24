package br.com.tp.lncr.fooditem.datasources.postgres;

import br.com.tp.lncr.core.dtos.fooditem.FoodItemDTO;
import br.com.tp.lncr.core.dtos.fooditem.FoodItemImageDTO;
import br.com.tp.lncr.core.enums.FoodItemCategory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class JpaFoodItemMapper {
    public JpaFoodItemEntity foodItemDtoToJpa(FoodItemDTO foodItemDTO) {
        if (foodItemDTO == null) return null;
        JpaFoodItemEntity entity = new JpaFoodItemEntity();
        entity.setId(foodItemDTO.getId());
        entity.setName(foodItemDTO.getName());
        entity.setDescription(foodItemDTO.getDescription());
        entity.setPrice(foodItemDTO.getPrice());
        entity.setCategoryId(FoodItemCategory.fromDescription(foodItemDTO.getCategory()).getId());
        return entity;
    }

    public FoodItemDTO jpaFoodItemToDTO(JpaFoodItemEntity entity) {
        if (entity == null) return null;
        FoodItemDTO dto = new FoodItemDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setCategory(FoodItemCategory.fromId(entity.getCategoryId()).getDescription());
        return dto;
    }

    public JpaFoodItemImageEntity foodItemImageDtoToJpa(FoodItemImageDTO dto) {
        if (dto == null) return null;
        JpaFoodItemImageEntity entity = new JpaFoodItemImageEntity();
        entity.setId(dto.getId());
        entity.setFoodItemId(dto.getFoodItemId());
        entity.setData(dto.getData());
        entity.setLocation(dto.getLocation());
        entity.setFileName(dto.getFileName());
        entity.setFileExtension(dto.getFileExtension());
        entity.setImageError(dto.getImageError());

        return entity;
    }

    public FoodItemImageDTO jpaFoodItemImageToDTO(JpaFoodItemImageEntity entity) {
        if (entity == null) return null;
        FoodItemImageDTO dto = new FoodItemImageDTO();
        dto.setId(entity.getId());
        dto.setFoodItemId(entity.getFoodItemId());
        dto.setData(entity.getData());
        dto.setFileName(entity.getFileName());
        dto.setFileExtension(entity.getFileExtension());
        dto.setImageError(entity.getImageError());
        return dto;
    }

    public List<FoodItemImageDTO> jpaFoodItemImageToDtoList(List<JpaFoodItemImageEntity> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream().map(this::jpaFoodItemImageToDTO).toList();
    }

    public List<JpaFoodItemImageEntity> foodItemImageDtoToJpaList(List<FoodItemImageDTO> foodItemImageDTOS) {
        if (foodItemImageDTOS == null) return Collections.emptyList();
        return foodItemImageDTOS.stream().map(this::foodItemImageDtoToJpa).toList();
    }
}
