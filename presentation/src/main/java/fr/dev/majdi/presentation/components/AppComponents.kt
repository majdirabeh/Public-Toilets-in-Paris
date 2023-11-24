package fr.dev.majdi.presentation.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import fr.dev.majdi.presentation.R

/**
 * Created by Majdi RABEH on 24/11/2023.
 * Email m.rabeh.majdi@gmail.com
 */
@Composable
fun AppBar(
    title: String,
    actions:
    @Composable RowScope.() -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    TopAppBar(
        title = { Text(text = title) },
        actions = actions,
        navigationIcon = {
            IconButton(onClick = {
                onBackPressed.invoke()
            }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        },
        backgroundColor = colorResource(id = R.color.purple_200),
        contentColor = Color.White
    )
}