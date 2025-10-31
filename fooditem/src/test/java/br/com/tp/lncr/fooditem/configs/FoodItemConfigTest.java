package br.com.tp.lncr.fooditem.configs;

import br.com.tp.lncr.core.adapters.fooditem.FoodItemMapper;
import br.com.tp.lncr.core.interfaces.fooditem.FoodItemController;
import br.com.tp.lncr.core.interfaces.fooditem.FoodItemDatabase;
import br.com.tp.lncr.core.interfaces.fooditem.FoodItemImageController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FoodItemConfigTest {

    @Mock
    private FoodItemDatabase foodItemDatabase;

    private FoodItemConfig foodItemConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        foodItemConfig = new FoodItemConfig();
    }

    @Test
    void deveDefinirEObterLocationPrefix() {
        foodItemConfig.setLocationPrefix("/api/fooditems");

        assertEquals("/api/fooditems", foodItemConfig.getLocationPrefix());
    }

    @Test
    void deveDefinirEObterMaxImages() {
        foodItemConfig.setMaxImages(10);

        assertEquals(10, foodItemConfig.getMaxImages());
    }

    @Test
    void deveDefinirEObterImageConfig() {
        FoodItemConfig.ImageConfig imageConfig = new FoodItemConfig.ImageConfig();
        imageConfig.setDirectory("/images");

        foodItemConfig.setImage(imageConfig);

        assertNotNull(foodItemConfig.getImage());
        assertEquals("/images", foodItemConfig.getImage().getDirectory());
    }

    @Test
    void imageConfigDeveDefinirEObterLocationPrefix() {
        FoodItemConfig.ImageConfig imageConfig = new FoodItemConfig.ImageConfig();
        imageConfig.setLocationPrefix("/api/images");

        assertEquals("/api/images", imageConfig.getLocationPrefix());
    }

    @Test
    void imageConfigDeveDefinirEObterDirectory() {
        FoodItemConfig.ImageConfig imageConfig = new FoodItemConfig.ImageConfig();
        imageConfig.setDirectory("/var/www/images");

        assertEquals("/var/www/images", imageConfig.getDirectory());
    }

    @Test
    void imageConfigDeveDefinirEObterMaxSize() {
        FoodItemConfig.ImageConfig imageConfig = new FoodItemConfig.ImageConfig();
        imageConfig.setMaxSize(5242880); // 5MB

        assertEquals(5242880, imageConfig.getMaxSize());
    }

    @Test
    void imageConfigDeveDefinirEObterAllowedExtensions() {
        FoodItemConfig.ImageConfig imageConfig = new FoodItemConfig.ImageConfig();
        Map<String, String> extensions = new HashMap<>();
        extensions.put(".jpg", "FFD8");
        extensions.put(".png", "89504E47");

        imageConfig.setAllowedExtensions(extensions);

        assertNotNull(imageConfig.getAllowedExtensions());
        assertEquals(2, imageConfig.getAllowedExtensions().size());
        assertTrue(imageConfig.getAllowedExtensions().containsKey(".jpg"));
        assertTrue(imageConfig.getAllowedExtensions().containsKey(".png"));
    }

    @Test
    void deveCriarBeanFoodItemImageController() {
        FoodItemImageController controller = foodItemConfig.foodItemImageController(foodItemDatabase);

        assertNotNull(controller);
    }

    @Test
    void deveCriarBeanFoodItemController() {
        FoodItemController controller = foodItemConfig.foodItemController(foodItemDatabase);

        assertNotNull(controller);
    }

    @Test
    void deveCriarBeanFoodItemMapper() {
        FoodItemMapper mapper = foodItemConfig.foodItemMapper();

        assertNotNull(mapper);
    }

    @Test
    void imageConfigDevePermitirValoresNulos() {
        FoodItemConfig.ImageConfig imageConfig = new FoodItemConfig.ImageConfig();

        assertNull(imageConfig.getLocationPrefix());
        assertNull(imageConfig.getDirectory());
        assertNull(imageConfig.getMaxSize());
        assertNull(imageConfig.getAllowedExtensions());
    }

    @Test
    void foodItemConfigDevePermitirValoresNulos() {
        assertNull(foodItemConfig.getLocationPrefix());
        assertNull(foodItemConfig.getMaxImages());
        assertNull(foodItemConfig.getImage());
    }

    @Test
    void deveDefinirConfiguracoesCompletas() {
        // Configuração principal
        foodItemConfig.setLocationPrefix("/api/v1/fooditems");
        foodItemConfig.setMaxImages(5);

        // Configuração de imagem
        FoodItemConfig.ImageConfig imageConfig = new FoodItemConfig.ImageConfig();
        imageConfig.setLocationPrefix("/api/v1/images");
        imageConfig.setDirectory("/var/fooditem/images");
        imageConfig.setMaxSize(10485760); // 10MB

        Map<String, String> extensions = new HashMap<>();
        extensions.put(".jpg", "FFD8");
        extensions.put(".jpeg", "FFD8");
        extensions.put(".png", "89504E47");
        extensions.put(".gif", "474946");
        imageConfig.setAllowedExtensions(extensions);

        foodItemConfig.setImage(imageConfig);

        // Verificações
        assertEquals("/api/v1/fooditems", foodItemConfig.getLocationPrefix());
        assertEquals(5, foodItemConfig.getMaxImages());
        assertNotNull(foodItemConfig.getImage());
        assertEquals("/api/v1/images", foodItemConfig.getImage().getLocationPrefix());
        assertEquals("/var/fooditem/images", foodItemConfig.getImage().getDirectory());
        assertEquals(10485760, foodItemConfig.getImage().getMaxSize());
        assertEquals(4, foodItemConfig.getImage().getAllowedExtensions().size());
    }
}

