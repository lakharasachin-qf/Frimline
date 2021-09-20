package com.app.frimline.models.roomModels.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.app.frimline.models.roomModels.ProductEntity;
import com.app.frimline.models.roomModels.WishlistEntity;

import java.util.List;

@Dao
public interface ProductEntityDao {
    @Query("Select * from cart")
    List<ProductEntity> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ProductEntity entity);

    @Query("DELETE FROM cart")
    void deleteAll();

    @Query("DELETE FROM cart WHERE id=:productId")
    void deleteProduct(String productId);

    @Update
    void updateSpecificProduct(ProductEntity entity);

    @Query("SELECT * FROM cart WHERE id = :productId")
    ProductEntity findProductByProductId(String productId);



        /*{
    "code": "200",
    "data": [
        {
            "product_name": "Test Product 5 Dente91 Lactoferrin Mouthwash Pack of 3 (Copy)",
            "ID": "94",
            "prod_id": "9905",
            "quantity": "1",
            "user_id": "66",
            "wishlist_id": "33",
            "original_price": "597.000"
        }
    ]
}*/
}
