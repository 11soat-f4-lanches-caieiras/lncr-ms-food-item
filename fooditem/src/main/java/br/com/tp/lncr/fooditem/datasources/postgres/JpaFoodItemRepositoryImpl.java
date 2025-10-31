package br.com.tp.lncr.fooditem.datasources.postgres;

import br.com.tp.lncr.core.dtos.fooditem.FoodItemDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaFoodItemRepositoryImpl {


    public FoodItemDTO save(FoodItemDTO foodItemDTO, JpaFoodItemReposity jpaFoodItemReposity, JpaFoodItemMapper jpaFoodItemMapper) {
        JpaFoodItemEntity jpaFoodItemEntity = jpaFoodItemMapper.foodItemDtoToJpa(foodItemDTO);
        jpaFoodItemEntity = jpaFoodItemReposity.save(jpaFoodItemEntity);
        return jpaFoodItemMapper.jpaFoodItemToDTO(jpaFoodItemEntity);
    }

    public boolean existsByName(String foodItemName, JpaFoodItemReposity jpaFoodItemRepository) {
        return jpaFoodItemRepository.existsByName(foodItemName);
    }

    public List<FoodItemDTO> getAllFoodItems(Integer _limit, JpaFoodItemReposity jpaFoodItemReposity, JpaFoodItemMapper jpaFoodItemMapper) {
        return jpaFoodItemReposity.findAll(Pageable.ofSize(_limit))
                .stream()
                .map(jpaFoodItemMapper::jpaFoodItemToDTO)
                .toList();
    }

    public List<FoodItemDTO> getAllFoodItemsByCategory(Integer _limit, Integer categoryId, JpaFoodItemReposity jpaFoodItemRepository, JpaFoodItemMapper jpaFoodItemMapper) {
        return jpaFoodItemRepository.findAllByCategory(_limit, categoryId)
                .stream()
                .map(jpaFoodItemMapper::jpaFoodItemToDTO)
                .toList();
    }

    public FoodItemDTO findById(Integer foodItemId, JpaFoodItemReposity jpaFoodItemRepository, JpaFoodItemMapper jpaFoodItemMapper) {
        return jpaFoodItemMapper.jpaFoodItemToDTO(
                jpaFoodItemRepository.findById(foodItemId)
                        .orElse(null));
    }

    public List<FoodItemDTO> findByIdList(List<Integer> foodItemIds, JpaFoodItemReposity jpaFoodItemRepository, JpaFoodItemMapper jpaFoodItemMapper) {
        List<JpaFoodItemEntity> jpaFoodItemList = jpaFoodItemRepository.findByIdList(foodItemIds);
        return jpaFoodItemList.stream().map(jpaFoodItemMapper::jpaFoodItemToDTO).toList();
    }

    public void deleteById(Integer foodItemId, JpaFoodItemReposity jpaFoodItemRepository) {
        jpaFoodItemRepository.deleteById(foodItemId);
    }


}
