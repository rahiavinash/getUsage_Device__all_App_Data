package com.vasanth.compose.ui.details

import com.vasanth.compose.domain.Details
import com.vasanth.compose.util.formatDate

data class DetailsUiState(
    val detail: Details = Details(),
    val offline: Boolean = false
) {
    val formattedUserSince = formatDate(detail.userSince)
}