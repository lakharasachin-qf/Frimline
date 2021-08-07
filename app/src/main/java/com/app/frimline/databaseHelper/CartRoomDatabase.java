package com.app.frimline.databaseHelper;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.app.frimline.models.roomModels.DAOs.ProductEntityDao;
import com.app.frimline.models.roomModels.ProductEntity;

@Database(entities = {ProductEntity.class}, version = 1, exportSchema = false)
public abstract class CartRoomDatabase extends RoomDatabase {


    private static CartRoomDatabase mINSTANCE;

    public abstract ProductEntityDao productEntityDao();

    public static CartRoomDatabase getAppDatabase(Context context) {
        if (mINSTANCE == null) {
            mINSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), CartRoomDatabase.class, "cart")
                            .allowMainThreadQueries()
                            .build();
        }
        return mINSTANCE;
    }

    public static void destroyInstance() {
        mINSTANCE = null;
    }
}