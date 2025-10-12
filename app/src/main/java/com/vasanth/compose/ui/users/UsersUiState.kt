package com.vasanth.compose.ui.users

import com.vasanth.compose.domain.User

data class UsersUiState(
    val list: List<User> = listOf(),
    val offline: Boolean = false
)