package br.com.tp.lncr.fooditem.configs;

import br.com.tp.lncr.core.adapters.fooditem.FoodItemControllerImpl;
import br.com.tp.lncr.core.adapters.fooditem.FoodItemImageControllerImpl;
import br.com.tp.lncr.core.adapters.fooditem.FoodItemMapper;
import br.com.tp.lncr.core.interfaces.fooditem.FoodItemController;
import br.com.tp.lncr.core.interfaces.fooditem.FoodItemDatabase;
import br.com.tp.lncr.core.interfaces.fooditem.FoodItemImageController;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "lncr.food-item")
public class FoodItemConfig {
    private String locationPrefix;
    private Integer maxImages;

    @NestedConfigurationProperty
    private ImageConfig image;

    public String getLocationPrefix() {
        return locationPrefix;
    }

    public void setLocationPrefix(String locationPrefix) {
        this.locationPrefix = locationPrefix;
    }

    public Integer getMaxImages() {
        return maxImages;
    }

    public void setMaxImages(Integer maxImages) {
        this.maxImages = maxImages;
    }


    public ImageConfig getImage() {
        return image;
    }

    public void setImage(ImageConfig image) {
        this.image = image;
    }

    public static class ImageConfig {
        private String locationPrefix;
        private String directory;
        private Integer maxSize;
        private Map<String, String> allowedExtensions;

        public String getLocationPrefix() {
            return locationPrefix;
        }

        public void setLocationPrefix(String locationPrefix) {
            this.locationPrefix = locationPrefix;
        }

        public String getDirectory() {
            return directory;
        }

        public void setDirectory(String directory) {
            this.directory = directory;
        }

        public Integer getMaxSize() {
            return maxSize;
        }

        public void setMaxSize(Integer maxSize) {
            this.maxSize = maxSize;
        }

        public Map<String, String> getAllowedExtensions() {
            return allowedExtensions;
        }

        public void setAllowedExtensions(Map<String, String> allowedExtensions) {
            this.allowedExtensions = allowedExtensions;
        }
    }

    @Bean
    public FoodItemImageController foodItemImageController(FoodItemDatabase foodItemDatabase){
        return new FoodItemImageControllerImpl(foodItemDatabase);
    }

    @Bean
    public FoodItemController foodItemController(FoodItemDatabase foodItemDatabase) {
        return new FoodItemControllerImpl(foodItemDatabase);
    }

    @Bean
    public FoodItemMapper foodItemMapper() {
        return new FoodItemMapper();
    }




}
