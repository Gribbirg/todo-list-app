package ru.gribbirg.ui.components

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import ru.gribbirg.theme.custom.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedTopAppBar(
    scrollState: ScrollState,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable (RowScope.() -> Unit) = {},
    startColor: Color = AppTheme.colors.primaryBack,
    scrolledColor: Color = AppTheme.colors.appBar,
    startElevation: Float = AppTheme.dimensions.shadowElevationNo.value,
    scrolledElevation: Float = AppTheme.dimensions.shadowElevationLarge.value,
) {
    val systemUiController = rememberSystemUiController()

    val color = remember(startColor) {
        Animatable(if (scrollState.canScrollBackward) scrolledColor else startColor)
    }
    val elevation = remember {
        Animatable(if (scrollState.canScrollBackward) scrolledElevation else startElevation)
    }

    LaunchedEffect(scrollState.canScrollBackward) {
        launch { elevation.animateTo(if (scrollState.canScrollBackward) scrolledElevation else startElevation) }
        launch { color.animateTo(if (scrollState.canScrollBackward) scrolledColor else startColor) }
    }

    LaunchedEffect(color.value) {
        systemUiController.setStatusBarColor(color.value)
    }

    TopAppBar(
        title = title,
        modifier = modifier
            .shadow(elevation = elevation.value.dp)
            .background(color.value),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent
        ),
        navigationIcon = navigationIcon,
        actions = actions,
    )
}