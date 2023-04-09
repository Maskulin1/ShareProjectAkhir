package com.reihan.githubuserapp.data.response.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: UserEntity)

    @Update
    fun update(user: UserEntity)

    @Delete
    fun delete(user: UserEntity)

    @Query("SELECT EXISTS(SELECT * from GithubUser where username = :username)")
    fun checkFavoriteUser(username: String): LiveData<Boolean>

    @Query("SELECT * from GithubUser ORDER BY id ASC")
    fun getAllFavoriteData(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM GithubUser WHERE username = :username")
    fun getDataByUsername(username: String): LiveData<UserEntity>

}