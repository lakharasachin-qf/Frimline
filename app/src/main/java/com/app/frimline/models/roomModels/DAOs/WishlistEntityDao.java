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
public interface WishlistEntityDao {

    @Query("Select * from wishlist")
    List<WishlistEntity> getWishlist();

    @Query("DELETE FROM wishlist")
    void deleteWishlist();

    @Query("DELETE FROM wishlist WHERE prod_id=:productId")
    void deleteWishlistItem(String productId);

    @Query("SELECT * FROM wishlist WHERE prod_id = :productId")
    WishlistEntity getWishlistProduct(String productId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(WishlistEntity entity);
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
