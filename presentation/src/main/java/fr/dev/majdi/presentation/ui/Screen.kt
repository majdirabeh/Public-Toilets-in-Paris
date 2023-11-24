package fr.dev.majdi.presentation.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Place
import androidx.compose.ui.graphics.vector.ImageVector
import fr.dev.majdi.presentation.R

/**
 * Created by Majdi RABEH on 24/11/2023.
 * Email m.rabeh.majdi@gmail.com
 */
sealed class Screen(
    val route: String,
    val icon: ImageVector,
    @StringRes val resourceId: Int
) {
    object ToiletListScreen :
        Screen("toiletListScreen", Icons.Default.List, R.string.toilet_list_title)

    object ToiletMapScreen :
        Screen("toiletMapScreen", Icons.Default.Place, R.string.toilet_map_title)

    object ToiletDetailScreen :
        Screen("toiletDetailScreen", Icons.Default.Info, R.string.toilet_detail_title)
}