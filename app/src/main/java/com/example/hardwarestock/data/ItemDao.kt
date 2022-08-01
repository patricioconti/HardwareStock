package com.example.hardwarestock.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    //Insert item
    //The OnConflictStrategy.IGNORE strategy ignores a new item if it's primary key is already in the database
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    //Update item
    @Update
    suspend fun update(item: Item)

    //Delete item
    @Delete
    suspend fun delete(item: Item)

    //Get item by id
    @Query("SELECT * from item WHERE id = :id")
    fun getItem(id: Int): Flow<Item>

    //Get items order by name ascendent
    @Query("SELECT * from item ORDER BY name ASC")
    fun getItems(): Flow<List<Item>>

    //Get items by type order by name ascendent
    @Query("SELECT * from item WHERE type = :type ORDER BY name ASC")
    fun getItemsByType(type:String): Flow<List<Item>>

}