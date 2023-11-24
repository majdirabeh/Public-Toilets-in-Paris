package fr.dev.majdi.presentation.toiletslistscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Accessible
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fr.dev.majdi.domain.model.Toilet
import fr.dev.majdi.presentation.R
import fr.dev.majdi.presentation.ui.Screen

/**
 * Created by Majdi RABEH on 24/11/2023.
 * Email m.rabeh.majdi@gmail.com
 */
@Composable
fun ToiletListScreen(toiletListViewModel: ToiletListViewModel, navController: NavHostController) {
    //Call API
    LaunchedEffect(key1 = Unit) {
        toiletListViewModel.loadToiletList()
    }

    // Remember the LazyListState
    val lazyListState = rememberLazyListState()

    //State
    val toiletList by toiletListViewModel.toiletListLiveData.observeAsState()

    // UI
    Scaffold(
        content = { paddingValue ->
            toiletListViewModel.inProgress.let {
                if (it) {
                    // Loading state
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValue),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color.Blue
                        )
                    }
                } else {
                    // Data loaded, render the list
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = lazyListState
                    ) {
                        items(items = toiletList ?: emptyList()) { toilet ->
                            // Display toilets list
                            ListItemCard(toilet, navController)
                        }
                        // Check if the user has scrolled to the end of the list
                        if (lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == toiletList?.size?.minus(1)) {
                            // Load more data when reaching the end
                            toiletListViewModel.inProgress = true
                            toiletListViewModel.loadToiletList()
                        }
                    }
                }
            }
        }
    )

}

@Composable
fun ListItemCard(item: Toilet, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                navController.navigate(
                    route = Screen.ToiletDetailScreen.route
                )
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                // Icon for address
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = item.fields.adresse + "",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                // Icon for opening hours
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = stringResource(id = R.string.list_item_opening) + item.fields.horaire,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,

            ) {
                // Icon for PMR accessibility
                Icon(
                    imageVector = Icons.Default.Accessible,
                    contentDescription = null,
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = stringResource(id = R.string.list_item_pmr) + item.fields.acces_pmr,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}
