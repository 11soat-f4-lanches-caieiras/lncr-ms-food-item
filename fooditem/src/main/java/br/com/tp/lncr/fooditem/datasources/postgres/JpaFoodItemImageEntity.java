package br.com.tp.lncr.fooditem.datasources.postgres;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Table(name = "food_item_image",
        schema = "public",
        indexes = {
                @Index(name = "food_item_image_id_idx", columnList = "id"),
                @Index(name = "food_item_image_food_item_id_idx", columnList = "foodItemId")
        })
@Entity
public class JpaFoodItemImageEntity {
    @Id
    private Integer id;
    private Integer foodItemId;
    private String fileName;

    @Transient
    private String data;

    @Transient
    private String location;

    @Transient
    private String fileExtension;

    @Transient
    private String imageError;

    public JpaFoodItemImageEntity() {
    }

    public JpaFoodItemImageEntity(Integer id, Integer foodItemId, String data, String location, String fileName, String fileExtension, String imageError) {
        this.id = id;
        this.foodItemId = foodItemId;
        this.data = data;
        this.location = location;
        this.fileName = fileName;
        this.fileExtension = fileExtension;
        this.imageError = imageError;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(Integer foodItemId) {
        this.foodItemId = foodItemId;
    }

    public void setData(String _data) {
        this.data = _data;
    }

    public String getData() {
        return data;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getImageError() {
        return imageError;
    }

    public void setImageError(String imageError) {
        this.imageError = imageError;
    }


}
