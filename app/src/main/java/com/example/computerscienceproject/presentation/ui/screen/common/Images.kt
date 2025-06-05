package com.example.computerscienceproject.presentation.ui.screen.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.example.computerscienceproject.core.data.networking.ApiConstants.BASE_URL

@Composable
fun SafeImage(
    modifier: Modifier = Modifier,
    shimmerModifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    imageUrl: String,
    onClick: () -> Unit = {},
    isClickable: Boolean = false,
    onSuccess: () -> Unit = {},
    showBlurredCard: (Boolean) -> Unit = {},
) {

    var showShimmer by remember { mutableStateOf(true) }

    Box {
        if (showShimmer) {
            Box(modifier = modifier.shimmerEffect())
        }

        AsyncImage(
            model = "$BASE_URL$imageUrl",
            contentDescription = null,
            modifier = modifier
                .then(if (isClickable) Modifier.clickable { if (!showShimmer) onClick() } else Modifier
                ),
            contentScale = contentScale,
            onSuccess = {
                showShimmer = false
                showBlurredCard(true)
                onSuccess()
            },
            onError = {
                showShimmer = false
                showBlurredCard(true)
            },
            onLoading = {
                showShimmer = true
                showBlurredCard(false)
            })
    }
}