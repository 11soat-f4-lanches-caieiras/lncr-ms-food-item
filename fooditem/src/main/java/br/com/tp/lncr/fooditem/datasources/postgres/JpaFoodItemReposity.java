package br.com.tp.lncr.fooditem.datasources.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface JpaFoodItemReposity extends JpaRepository<JpaFoodItemEntity, Integer> {

    @Query(value = "SELECT * FROM food_item WHERE category_id = :categoryId LIMIT :limit", nativeQuery = true)
    List<JpaFoodItemEntity> findAllByCategory(@Param("limit") Integer limit, @Param("categoryId") Integer categoryId);

    @Query(value = "SELECT * FROM food_item WHERE id in(:foodItemIds)", nativeQuery = true)
    List<JpaFoodItemEntity> findByIdList(@Param("foodItemIds") List<Integer> foodItemIds);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM food_item WHERE UPPER(name) = UPPER(:foodItemName)", nativeQuery = true)
    boolean existsByName(@Param("foodItemName") String foodItemName);



}
