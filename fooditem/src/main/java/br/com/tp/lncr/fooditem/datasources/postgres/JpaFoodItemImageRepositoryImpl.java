package br.com.tp.lncr.fooditem.datasources.postgres;

import br.com.tp.lncr.core.dtos.fooditem.FoodItemImageDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaFoodItemImageRepositoryImpl {

    public void saveAll(List<FoodItemImageDTO> foodItemImageDTOList, JpaFoodItemImageRepository jpaFoodItemImageRepository, JpaFoodItemMapper jpaFoodItemMapper) {
        List<JpaFoodItemImageEntity> jpaFoodItemImageEntityList = jpaFoodItemMapper.foodItemImageDtoToJpaList(foodItemImageDTOList);
        jpaFoodItemImageRepository.saveAll(jpaFoodItemImageEntityList);
    }

    public List<FoodItemImageDTO> findAllByFoodItemId(Integer id, JpaFoodItemImageRepository jpaFoodItemImageRepository, JpaFoodItemMapper jpaFoodItemMapper) {
        return jpaFoodItemImageRepository.findAllByFoodItemId(id)
                .stream()
                .map(jpaFoodItemMapper::jpaFoodItemImageToDTO)
                .toList();
    }

    public void deleteByFoodItemId(List<FoodItemImageDTO> foodItemImageDTOList, JpaFoodItemImageRepository jpaFoodItemImageRepository, JpaFoodItemMapper jpaFoodItemMapper) {
        jpaFoodItemImageRepository.deleteAll(jpaFoodItemMapper.foodItemImageDtoToJpaList(foodItemImageDTOList));
    }

    public FoodItemImageDTO save(FoodItemImageDTO foodItemImageDTO, JpaFoodItemImageRepository jpaFoodItemImageRepository, JpaFoodItemMapper jpaFoodItemMapper) {
        JpaFoodItemImageEntity newJpaImage = jpaFoodItemMapper.foodItemImageDtoToJpa(foodItemImageDTO);
        newJpaImage = jpaFoodItemImageRepository.save(newJpaImage);
        return jpaFoodItemMapper.jpaFoodItemImageToDTO(newJpaImage);
    }

    public FoodItemImageDTO findById(Integer foodItemImageId, JpaFoodItemImageRepository jpaFoodItemImageRepository, JpaFoodItemMapper jpaFoodItemMapper) {
        return jpaFoodItemMapper.jpaFoodItemImageToDTO(
                jpaFoodItemImageRepository.findById(foodItemImageId)
                        .orElse(null));
    }

    public List<FoodItemImageDTO> deleteImagesByFoodItemId(Integer foodItemId, JpaFoodItemImageRepository jpaFoodItemImageRepository, JpaFoodItemMapper jpaFoodItemMapper) {
        List<FoodItemImageDTO> listImagestoDelete = findAllByFoodItemId(foodItemId, jpaFoodItemImageRepository, jpaFoodItemMapper);
        jpaFoodItemImageRepository.deleteAll(jpaFoodItemMapper.foodItemImageDtoToJpaList(listImagestoDelete));
        return listImagestoDelete;
    }

    public void delete(FoodItemImageDTO foodItemImageDTO, JpaFoodItemImageRepository jpaFoodItemImageRepository, JpaFoodItemMapper jpaFoodItemMapper) {
        jpaFoodItemImageRepository.delete(jpaFoodItemMapper.foodItemImageDtoToJpa(foodItemImageDTO));
    }
}
