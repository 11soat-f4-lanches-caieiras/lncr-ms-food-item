package br.com.tp.lncr.fooditem.apis;

import br.com.tp.lncr.commons.model.ResponseModel;
import br.com.tp.lncr.commons.utils.ResponseEntityModelUtil;
import br.com.tp.lncr.core.dtos.fooditem.FoodItemImageDTO;
import br.com.tp.lncr.core.interfaces.fooditem.FoodItemImageController;
import br.com.tp.lncr.core.utils.FoodItemImageRules;
import br.com.tp.lncr.fooditem.configs.FoodItemConfig;
import br.com.tp.lncr.fooditem.dataproxy.FoodItemDataProxy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/foodItems/image")
public class FoodItemImageRestControllerImpl implements FoodItemImageRestController {

    private final FoodItemDataProxy foodItemDataProxy;
    private final FoodItemImageController foodItemImageController;
    private final FoodItemConfig foodItemConfig;

    public FoodItemImageRestControllerImpl(FoodItemDataProxy foodItemDataProxy, FoodItemImageController foodItemImageController, FoodItemConfig foodItemConfig) {
        this.foodItemDataProxy = foodItemDataProxy;
        this.foodItemImageController = foodItemImageController;
        this.foodItemConfig = foodItemConfig;
    }

    @Override
    @GetMapping("/{foodItemImageId}")
    public ResponseEntity<ResponseModel<FoodItemImageDTO>> getFoodItemImageById(@PathVariable(name = "foodItemImageId") Integer foodItemImageId) {
        FoodItemImageDTO foodItemImageDTO = this.foodItemImageController.getImageById(foodItemImageId, this.foodItemDataProxy, foodItemConfig.getImage().getLocationPrefix());
        return ResponseEntityModelUtil.ok(foodItemImageDTO);
    }

    @Override
    @PutMapping("/{foodItemImageId}")
    public ResponseEntity<ResponseModel<FoodItemImageDTO>> updateFoodItemImageById(@PathVariable(name = "foodItemImageId") Integer foodItemImageId, @RequestBody FoodItemImageDTO foodItemImageDTO) {
        foodItemImageDTO = this.foodItemImageController.updateImageById(foodItemImageId, foodItemImageDTO, this.foodItemDataProxy, newFoodItemImageRule());
        return ResponseEntityModelUtil.ok(foodItemImageDTO);
    }


    @Override
    @DeleteMapping("{foodItemImageId}")
    public ResponseEntity<ResponseModel<FoodItemImageDTO>> deleteFoodItemImageById(@PathVariable(name="foodItemImageId") Integer foodItemImageId) {
        this.foodItemImageController.deleteImageById(foodItemImageId, this.foodItemDataProxy);
        return ResponseEntityModelUtil.ok(null);
    }

    private FoodItemImageRules newFoodItemImageRule() {
        return new FoodItemImageRules(foodItemConfig.getImage().getLocationPrefix(), foodItemConfig.getMaxImages(), foodItemConfig.getImage().getMaxSize(), foodItemConfig.getImage().getAllowedExtensions());
    }

}
