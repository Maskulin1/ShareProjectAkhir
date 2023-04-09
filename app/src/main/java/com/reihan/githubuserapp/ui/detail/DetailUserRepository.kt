package com.reihan.githubuserapp.ui.detail

import android.app.Application
import androidx.lifecycle.LiveData
import com.reihan.githubuserapp.data.response.database.UserDao
import com.reihan.githubuserapp.data.response.database.UserEntity
import com.reihan.githubuserapp.data.response.database.UserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DetailUserRepository(application: Application) {
    private val mUserDao: UserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = UserRoomDatabase.getDatabase(application)
        mUserDao = db.userDao()
    }

    fun getAllFavoriteData(): LiveData<List<UserEntity>> = mUserDao.getAllFavoriteData()

    fun insert(user: UserEntity) {
        executorService.execute { mUserDao.insert(user) }
    }

    fun delete(user: UserEntity) {
        executorService.execute { mUserDao.delete(user) }
    }

    fun checkFavoriteUser(username: String): LiveData<Boolean> =
        mUserDao.checkFavoriteUser(username)

    fun getDataByUsername(username: String): LiveData<UserEntity> =
        mUserDao.getDataByUsername(username)
}
