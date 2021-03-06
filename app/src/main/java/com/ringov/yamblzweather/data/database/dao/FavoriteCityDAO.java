package com.ringov.yamblzweather.data.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.ringov.yamblzweather.data.database.entity.DBFavoriteCity;

import java.util.List;

import io.reactivex.Flowable;

import static com.ringov.yamblzweather.data.database.DBContract.COLUMN_CITY_ID;
import static com.ringov.yamblzweather.data.database.DBContract.COLUMN_CITY_NAME;
import static com.ringov.yamblzweather.data.database.DBContract.COLUMN_ENABLED;
import static com.ringov.yamblzweather.data.database.DBContract.COLUMN_ID;
import static com.ringov.yamblzweather.data.database.DBContract.TABLE_FAVORITE_CITIES;

@Dao
public interface FavoriteCityDAO {

    @Query("SELECT * FROM " + TABLE_FAVORITE_CITIES)
    Flowable<List<DBFavoriteCity>> getAll();

    @Query("SELECT * FROM " + TABLE_FAVORITE_CITIES + " ORDER BY " + COLUMN_ID + " LIMIT 1")
    DBFavoriteCity getFirst();

    @Query("SELECT * FROM " + TABLE_FAVORITE_CITIES + " WHERE " + COLUMN_ENABLED + " = 1 LIMIT 1")
    DBFavoriteCity getEnabled();

    @Query("SELECT * FROM " + TABLE_FAVORITE_CITIES + " WHERE " + COLUMN_CITY_ID + " = :id LIMIT 1")
    DBFavoriteCity getById(int id);

    @Query("SELECT * FROM " + TABLE_FAVORITE_CITIES + " WHERE " + COLUMN_CITY_NAME + " = :name LIMIT 1")
    DBFavoriteCity getByName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DBFavoriteCity favoriteCity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<DBFavoriteCity> favoriteCities);

    @Delete
    void delete(DBFavoriteCity city);
}
