package br.com.tp.lncr.fooditem.apis;

import br.com.tp.lncr.fooditem.apis.FoodItemRestControllerImpl;
import br.com.tp.lncr.commons.model.ResponseListModel;
import br.com.tp.lncr.commons.model.ResponseModel;
import br.com.tp.lncr.fooditem.configs.FoodItemConfig;
import br.com.tp.lncr.fooditem.dataproxy.FoodItemDataProxy;
import br.com.tp.lncr.core.dtos.fooditem.FoodItemDTO;
import br.com.tp.lncr.core.dtos.fooditem.FoodItemImageDTO;
import br.com.tp.lncr.core.interfaces.fooditem.FoodItemController;
import br.com.tp.lncr.core.utils.FoodItemImageRules;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FoodItemRestControllerImplTest {

    @Mock
    private FoodItemDataProxy foodItemDataProxy;

    @Mock
    private FoodItemController foodItemController;

    @Mock
    private FoodItemConfig foodItemConfig;

    @Mock
    private FoodItemConfig.ImageConfig imageConfig;

    @InjectMocks
    private FoodItemRestControllerImpl foodItemRestController;

    private FoodItemDTO foodItemDTO;
    private FoodItemImageDTO foodItemImageDTO;

    @BeforeEach
    void setUp() {
        foodItemDTO = new FoodItemDTO();
        foodItemDTO.setId(1);
        foodItemDTO.setName("Hambúrguer");
        foodItemDTO.setCategory("SANDWICH");
        foodItemDTO.setPrice(25.99);

        foodItemImageDTO = new FoodItemImageDTO();
        foodItemImageDTO.setId(1);
        foodItemImageDTO.setFoodItemId(1);
        foodItemImageDTO.setFileName("burger.jpg");

        Map<String, String> allowedExtensions = new HashMap<>();
        allowedExtensions.put(".jpg", "FFD8");
        allowedExtensions.put(".png", "89504E47");

        when(foodItemConfig.getLocationPrefix()).thenReturn("/foodItems");
        when(foodItemConfig.getImage()).thenReturn(imageConfig);
        when(imageConfig.getLocationPrefix()).thenReturn("/images");
        when(foodItemConfig.getMaxImages()).thenReturn(5);
        when(imageConfig.getMaxSize()).thenReturn(1024*512*1024); // 512 MB
        when(imageConfig.getAllowedExtensions()).thenReturn(allowedExtensions);
    }

    @Test
    void deveCriarFoodItemComSucesso() {
        when(foodItemController.create(eq(foodItemDTO), eq(foodItemDataProxy), any(FoodItemImageRules.class))).thenReturn(foodItemDTO);

        ResponseEntity<ResponseModel<FoodItemDTO>> response = foodItemRestController.createFoodItem(foodItemDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(foodItemDTO, response.getBody().getContent());
        assertTrue(response.getHeaders().getLocation().toString().contains("/foodItems/1"));
        verify(foodItemController).create(eq(foodItemDTO), eq(foodItemDataProxy), any(FoodItemImageRules.class));
    }

    @Test
    void deveRetornarTodosFoodItemsSemFiltros() {
        List<FoodItemDTO> foodItems = Collections.singletonList(foodItemDTO);
        when(foodItemController.getAll(eq(null), eq(null), eq(false), eq(foodItemDataProxy), any(FoodItemImageRules.class))).thenReturn(foodItems);

        ResponseEntity<ResponseListModel<FoodItemDTO>> response = foodItemRestController.getAllFoodItems(null, null, false);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(foodItems, response.getBody().getContent());
        verify(foodItemController).getAll(eq(null), eq(null), eq(false), eq(foodItemDataProxy), any(FoodItemImageRules.class));
    }

    @Test
    void deveRetornarFoodItemsComLimiteECategoria() {
        List<FoodItemDTO> foodItems = Collections.singletonList(foodItemDTO);
        when(foodItemController.getAll(eq(10), eq("LANCHE"), eq(true), eq(foodItemDataProxy), any(FoodItemImageRules.class))).thenReturn(foodItems);

        ResponseEntity<ResponseListModel<FoodItemDTO>> response = foodItemRestController.getAllFoodItems(10, "LANCHE", true);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(foodItems, response.getBody().getContent());
        verify(foodItemController).getAll(eq(10), eq("LANCHE"), eq(true), eq(foodItemDataProxy), any(FoodItemImageRules.class));
    }

    @Test
    void deveRetornarFoodItemPorIdSemImagens() {
        when(foodItemController.getById(eq(1), eq(false), eq(foodItemDataProxy), any(FoodItemImageRules.class))).thenReturn(foodItemDTO);

        ResponseEntity<ResponseModel<FoodItemDTO>> response = foodItemRestController.getFoodItemById(1, false);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(foodItemDTO, response.getBody().getContent());
        verify(foodItemController).getById(eq(1), eq(false), eq(foodItemDataProxy), any(FoodItemImageRules.class));
    }

    @Test
    void deveRetornarFoodItemPorIdComImagens() {
        when(foodItemController.getById(eq(1), eq(true), eq(foodItemDataProxy), any(FoodItemImageRules.class))).thenReturn(foodItemDTO);

        ResponseEntity<ResponseModel<FoodItemDTO>> response = foodItemRestController.getFoodItemById(1, true);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(foodItemDTO, response.getBody().getContent());
        verify(foodItemController).getById(eq(1), eq(true), eq(foodItemDataProxy), any(FoodItemImageRules.class));
    }

    @Test
    void deveRetornarFoodItemsPorListaDeIds() {
        List<Integer> ids = Arrays.asList(1, 2);
        List<FoodItemDTO> foodItems = Collections.singletonList(foodItemDTO);
        when(foodItemController.getByIdList(ids, foodItemDataProxy)).thenReturn(foodItems);

        ResponseEntity<ResponseListModel<FoodItemDTO>> response = foodItemRestController.getFoodItemByIdList(ids);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(foodItems, response.getBody().getContent());
        verify(foodItemController).getByIdList(ids, foodItemDataProxy);
    }

    @Test
    void deveAtualizarFoodItemParcialmente() {
        FoodItemDTO updatedFoodItem = new FoodItemDTO();
        updatedFoodItem.setId(1);
        updatedFoodItem.setName("Hambúrguer Duplo");
        when(foodItemController.partialUpdateById(eq(1), eq(foodItemDTO), eq(foodItemDataProxy), any(FoodItemImageRules.class))).thenReturn(updatedFoodItem);

        ResponseEntity<ResponseModel<FoodItemDTO>> response = foodItemRestController.partialUpdateFoodItemById(1, foodItemDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(updatedFoodItem, response.getBody().getContent());
        verify(foodItemController).partialUpdateById(eq(1), eq(foodItemDTO), eq(foodItemDataProxy), any(FoodItemImageRules.class));
    }

    @Test
    void deveDeletarFoodItemPorId() {
        doNothing().when(foodItemController).deleteById(1, foodItemDataProxy);

        ResponseEntity<ResponseModel<FoodItemDTO>> response = foodItemRestController.deleteFoodItemById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getContent());
        verify(foodItemController).deleteById(1, foodItemDataProxy);
    }

    @Test
    void deveCriarImagemParaFoodItem() {
        when(foodItemController.create(eq(1), eq(foodItemImageDTO), eq(foodItemDataProxy), any(FoodItemImageRules.class))).thenReturn(foodItemImageDTO);

        ResponseEntity<ResponseModel<FoodItemImageDTO>> response = foodItemRestController.createFoodItemImage(1, foodItemImageDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(foodItemImageDTO, response.getBody().getContent());
        verify(foodItemController).create(eq(1), eq(foodItemImageDTO), eq(foodItemDataProxy), any(FoodItemImageRules.class));
    }

    @Test
    void deveRetornarImagensPorFoodItemIdSemDados() {
        List<FoodItemImageDTO> images = Collections.singletonList(foodItemImageDTO);
        when(foodItemController.getFoodItemImagesByFoodItemId(eq(1), eq(false), eq(foodItemDataProxy), any(FoodItemImageRules.class))).thenReturn(images);

        ResponseEntity<ResponseListModel<FoodItemImageDTO>> response = foodItemRestController.getFoodItemImagesByFoodItemId(1, false);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(images, response.getBody().getContent());
        verify(foodItemController).getFoodItemImagesByFoodItemId(eq(1), eq(false), eq(foodItemDataProxy), any(FoodItemImageRules.class));
    }

    @Test
    void deveRetornarImagensPorFoodItemIdComDados() {
        List<FoodItemImageDTO> images = Collections.singletonList(foodItemImageDTO);
        when(foodItemController.getFoodItemImagesByFoodItemId(eq(1), eq(true), eq(foodItemDataProxy), any(FoodItemImageRules.class))).thenReturn(images);

        ResponseEntity<ResponseListModel<FoodItemImageDTO>> response = foodItemRestController.getFoodItemImagesByFoodItemId(1, true);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(images, response.getBody().getContent());
        verify(foodItemController).getFoodItemImagesByFoodItemId(eq(1), eq(true), eq(foodItemDataProxy), any(FoodItemImageRules.class));
    }

    @Test
    void deveDeletarTodasImagensDoFoodItem() {
        doNothing().when(foodItemController).deleteImagesByFoodItemId(1, foodItemDataProxy);

        ResponseEntity<ResponseModel<FoodItemImageDTO>> response = foodItemRestController.deleteFoodItemImageByFoodItemId(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getContent());
        verify(foodItemController).deleteImagesByFoodItemId(1, foodItemDataProxy);
    }
}
