package br.com.tp.lncr.fooditem.datasources.postgres;

import jakarta.persistence.*;

@Table(name = "food_item",
        schema = "public",
        uniqueConstraints = @UniqueConstraint(name = "food_item_name_uk", columnNames = "name"),
        indexes = {
                @Index(name = "food_item_id_idx", columnList = "id"),
                @Index(name = "food_item_category_idx", columnList = "categoryId")
        })
@Entity
public class JpaFoodItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "food_item_id_seq")
    @SequenceGenerator(name = "food_item_id_seq", sequenceName = "food_item_id_seq", allocationSize = 1)
    private Integer id;
    private String name;
    private String description;
    private Double price;
    private Integer categoryId;

    public JpaFoodItemEntity() {
    }

    public JpaFoodItemEntity(Integer id, String name, String description, Double price, Integer categoryId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
