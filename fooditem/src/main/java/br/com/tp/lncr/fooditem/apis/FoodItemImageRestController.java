package br.com.tp.lncr.fooditem.apis;

import br.com.tp.lncr.commons.model.ResponseModel;
import br.com.tp.lncr.core.dtos.fooditem.FoodItemImageDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface FoodItemImageRestController {

    ResponseEntity<ResponseModel<FoodItemImageDTO>> getFoodItemImageById(@PathVariable("foodItemImageId") Integer foodItemImageId);

    ResponseEntity<ResponseModel<FoodItemImageDTO>> updateFoodItemImageById(@PathVariable("foodItemImageId") Integer foodItemImageId, @RequestBody FoodItemImageDTO foodItemImageDTO);

    ResponseEntity<ResponseModel<FoodItemImageDTO>> deleteFoodItemImageById(@PathVariable("foodItemImageId") Integer foodItemImageId);
}
