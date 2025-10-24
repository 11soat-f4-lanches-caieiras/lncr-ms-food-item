package br.com.tp.lncr.fooditem.datasources.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface JpaFoodItemImageRepository extends JpaRepository<JpaFoodItemImageEntity, Integer> {

    @Query(value = "select * from food_item_image where food_item_id = :foodItemId", nativeQuery = true)
    List<JpaFoodItemImageEntity> findAllByFoodItemId(@Param("foodItemId")Integer foodItemId);

}
