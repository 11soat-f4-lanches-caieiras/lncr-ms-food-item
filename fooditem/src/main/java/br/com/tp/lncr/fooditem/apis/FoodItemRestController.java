package br.com.tp.lncr.fooditem.apis;

import br.com.tp.lncr.commons.model.ResponseListModel;
import br.com.tp.lncr.commons.model.ResponseModel;
import br.com.tp.lncr.core.dtos.fooditem.FoodItemDTO;
import br.com.tp.lncr.core.dtos.fooditem.FoodItemImageDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface FoodItemRestController {

    ResponseEntity<ResponseModel<FoodItemDTO>> createFoodItem(@RequestBody FoodItemDTO foodItemDTO);

    ResponseEntity<ResponseListModel<FoodItemDTO>> getAllFoodItems(@RequestParam(name = "_limit", required = false) Integer _limit,
                                                                   @RequestParam(name = "category", required = false) String category,
                                                                   @RequestParam(name = "includeImages", required = false) Boolean includeImages);

    ResponseEntity<ResponseModel<FoodItemDTO>> getFoodItemById(@PathVariable(name = "foodItemId") Integer foodItemId,
                                                               @RequestParam(name = "includeImages", required = false) Boolean includeImages);

    ResponseEntity<ResponseListModel<FoodItemDTO>> getFoodItemByIdList(@PathVariable(name = "foodItemIdList") List<Integer> foodItemIdList);

    ResponseEntity<ResponseModel<FoodItemDTO>> partialUpdateFoodItemById(@PathVariable(name = "foodItemId") Integer foodItemId,
                                                                         @RequestBody FoodItemDTO foodItemDTO);

    ResponseEntity<ResponseModel<FoodItemDTO>> deleteFoodItemById(@PathVariable("foodItemId") Integer foodItemId);

    ResponseEntity<ResponseModel<FoodItemImageDTO>> createFoodItemImage(@PathVariable("foodItemId") Integer foodItemId, @RequestBody FoodItemImageDTO foodItemImageDTO);

    ResponseEntity<ResponseListModel<FoodItemImageDTO>> getFoodItemImagesByFoodItemId(@PathVariable("foodItemId") Integer foodItemId, @RequestParam(name = "includeData", required = false, defaultValue = "false") Boolean includeData);

    ResponseEntity<ResponseModel<FoodItemImageDTO>> deleteFoodItemImageByFoodItemId(@PathVariable("foodItemId") Integer foodItemId);
}
