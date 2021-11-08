package com.app.frimline.models.roomModels.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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

}
