package com.vasanth.compose.repository

import com.vasanth.compose.database.AppDatabase
import com.vasanth.compose.database.asDomainModel
import com.vasanth.compose.domain.User
import com.vasanth.compose.network.UsersApi
import com.vasanth.compose.network.model.asDatabaseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class UsersRepository @Inject constructor(
    private val usersApi: UsersApi,
    private val appDatabase: AppDatabase
) {

    val users: Flow<List<User>?> =
        appDatabase.usersDao.getUsers().map { it?.asDomainModel() }

    suspend fun refreshUsers() {
        try {
            val users = usersApi.getUsers()
            appDatabase.usersDao.insertUsers(users.asDatabaseModel())
        } catch (e: Exception) {
            Timber.w(e)
        }
    }
}