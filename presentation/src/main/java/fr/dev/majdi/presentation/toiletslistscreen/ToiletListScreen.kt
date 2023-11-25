package fr.dev.majdi.presentation.toiletslistscreen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.material.BottomDrawer
import androidx.compose.material.BottomDrawerState
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Accessible
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.FilterAltOff
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.NotAccessible
import androidx.compose.material.rememberBottomDrawerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mapbox.geojson.Point
import fr.dev.majdi.domain.model.Toilet
import fr.dev.majdi.domain.util.LocationService
import fr.dev.majdi.presentation.R
import fr.dev.majdi.presentation.toiletdetailscreen.ToiletDetailScreen
import fr.dev.majdi.presentation.util.calculateDistance
import kotlinx.coroutines.launch

/**
 * Created by Majdi RABEH on 24/11/2023.
 * Email m.rabeh.majdi@gmail.com
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ToiletListScreen(toiletListViewModel: ToiletListViewModel) {
    //I have some problem on gradle when using hiltViewModel on compose
    //val toiletListViewModel : ToiletListViewModel = hiltViewModel()
    val isInternetConnected by toiletListViewModel.isInternetAvailable.observeAsState()

    //Call API Or Getting Data from DataBase
    LaunchedEffect(key1 = Unit) {
        if (isInternetConnected == true) {
            toiletListViewModel.getToiletListFromService()
        } else {
            toiletListViewModel.loadToiletsFromLocal()
        }
    }

    // Remember the LazyListState
    val lazyListState = rememberLazyListState()

    //State
    val toiletList by toiletListViewModel.toiletListLiveData.observeAsState()
    val filteredToiletList by toiletListViewModel.filteredToiletListLiveData.observeAsState()

    var accessPmr by remember { mutableStateOf(false) }
    var showAllToilets by remember { mutableStateOf(true) }

    var point: Point? by remember {
        mutableStateOf(null)
    }

    val bottomSheetState = rememberBottomDrawerState(BottomDrawerValue.Closed)

    var showButtons by remember { mutableStateOf(false) }

    //BottomSheet to show detail Toilet
    BottomDrawer(drawerState = bottomSheetState,
        drawerBackgroundColor = Color.White,
        drawerShape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp),
        gesturesEnabled = false,
        drawerContent = {
            ToiletDetailScreen(toiletListViewModel.selectedToilet, bottomSheetState)
        }) {
        // Content List Toilets UI
        Scaffold(floatingActionButton = {

            val actions = listOf(FloatingButtonAction("", Icons.Default.Accessible) {
                showButtons = false
                accessPmr = true
                showAllToilets = false
                toiletListViewModel.filterToiletsByPmr(accessPmr, showAllToilets)
            }, FloatingButtonAction("", Icons.Default.ListAlt) {
                showButtons = false
                accessPmr = true
                showAllToilets = true
                toiletListViewModel.filterToiletsByPmr(accessPmr, showAllToilets)
            }, FloatingButtonAction("", Icons.Default.NotAccessible) {
                showButtons = false
                accessPmr = false
                showAllToilets = false
                toiletListViewModel.filterToiletsByPmr(accessPmr, showAllToilets)
            })

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                if (showButtons) {
                    FloatingActionButtonsRow(
                        actions = actions, modifier = Modifier.padding(16.dp)
                    )
                }
                FloatingActionButton(
                    onClick = { showButtons = !showButtons }, modifier = Modifier.padding(16.dp)
                ) {
                    Icon(
                        imageVector = if (showButtons) Icons.Default.FilterAlt else Icons.Default.FilterAltOff,
                        contentDescription = null
                    )
                }
            }
        }, content = { paddingValue ->
            toiletListViewModel.inProgress.let {
                if (it) {
                    // Loading state
                    Box(
                        modifier = Modifier
                            .background(
                                color = Color.Black.copy(alpha = 0.5f)
                            )
                            .fillMaxSize()
                            .padding(paddingValue), contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color.Blue
                        )
                    }
                } else {
                    // Data loaded, render the list
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(), state = lazyListState
                    ) {
                        val list = if (showAllToilets) toiletList else filteredToiletList
                        items(items = list ?: emptyList()) { toilet ->
                            // Display toilets list
                            ListItemCard(toilet, toiletListViewModel, point, bottomSheetState)
                        }
                        // Check if the user has scrolled to the end of the list
                        if (lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == toiletList?.size?.minus(
                                1
                            ) && isInternetConnected == true
                        ) {
                            // Load more data when reaching the end
                            toiletListViewModel.inProgress = true
                            toiletListViewModel.getToiletListFromService()
                        }
                    }
                }
            }
        })
    }


    var relaunch by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    val permissionRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (!permissions.values.all { it }) {
                //handle permission denied we can show dialog for user or go to settings
            } else {
                relaunch = !relaunch
            }
        })

    LaunchedEffect(key1 = relaunch) {
        try {
            val location = LocationService().getCurrentLocation(context)
            point = Point.fromLngLat(location.longitude, location.latitude)

        } catch (e: LocationService.LocationServiceException) {
            when (e) {
                is LocationService.LocationServiceException.LocationDisabledException -> {
                    //handle location disabled, show dialog or a snack-bar to enable location
                }

                is LocationService.LocationServiceException.MissingPermissionException -> {
                    permissionRequest.launch(
                        arrayOf(
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }

                is LocationService.LocationServiceException.NoNetworkEnabledException -> {
                    //handle no network enabled, show dialog or a snack-bar to enable network
                }

                is LocationService.LocationServiceException.UnknownException -> {
                    //handle unknown exception
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListItemCard(
    item: Toilet,
    toiletListViewModel: ToiletListViewModel,
    point: Point?,
    bottomSheetState: BottomDrawerState
) {

    val coroutineScope = rememberCoroutineScope()

    val distanceLabel = remember {
        mutableStateOf("M")
    }

    val distanceToToilet = remember {
        mutableDoubleStateOf(0.0)
    }

    point?.let {
        val pointToilet =
            Point.fromLngLat(item.geometry.coordinates.first(), item.geometry.coordinates.last())
        distanceToToilet.doubleValue = calculateDistance(it, pointToilet)

        if (distanceToToilet.doubleValue < 1.0) {
            distanceToToilet.doubleValue = distanceToToilet.doubleValue * 1000.0
            distanceLabel.value = "M"
        } else distanceLabel.value = "KM"
    }
    //?: run {
    //We can add other variable to show or not the block of distance
    //}

    val cardBackground = remember {
        mutableStateOf(Color.White)
    }
    val textColor = remember {
        mutableStateOf(Color.Black)
    }

    val tintIconColor = remember {
        mutableStateOf(Color.Gray)
    }

    if (item.fields.acces_pmr == "Non") {
        cardBackground.value = Color.Red
        textColor.value = Color.White
        tintIconColor.value = Color.White
    } else {
        cardBackground.value = Color.White
        textColor.value = Color.Black
        tintIconColor.value = Color.Gray
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable {
                //navController.navigate(
                //route = Screen.ToiletDetailScreen.route
                //)
                toiletListViewModel.isToiletItemClicked = true
                toiletListViewModel.selectedToilet = item
                coroutineScope.launch {
                    bottomSheetState.expand()
                }
            }, colors = CardDefaults.cardColors(
            containerColor = cardBackground.value
        ), elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp, pressedElevation = 15.dp
        ), shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
            ) {
                // Icon for address
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = tintIconColor.value
                )
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = item.fields.adresse.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor.value
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start
            ) {
                // Icon for opening hours
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = tintIconColor.value
                )
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = stringResource(id = R.string.list_item_opening) + " ${item.fields.horaire.toString()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor.value
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
            ) {
                // Icon for PMR accessibility
                Icon(
                    imageVector = Icons.Default.Accessible,
                    contentDescription = null,
                    tint = tintIconColor.value
                )
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = stringResource(id = R.string.list_item_pmr) + " ${item.fields.acces_pmr.toString()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor.value
                )
            }
            if (point != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                ) {
                    // Icon for Navigation to toilet
                    Icon(
                        imageVector = Icons.Default.Navigation,
                        contentDescription = null,
                        tint = tintIconColor.value
                    )
                    Text(
                        modifier = Modifier.padding(start = 5.dp),
                        text = stringResource(id = R.string.list_item_distance) + " ${
                            distanceToToilet.doubleValue.toInt().toString()
                        } ${distanceLabel.value}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor.value
                    )
                }
            }
        }
    }
}

@Composable
fun FloatingActionButtonsRow(
    actions: List<FloatingButtonAction>, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        for (action in actions) {
            FloatingActionButton(
                onClick = {
                    action.onClick()
                }, modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = action.icon, contentDescription = action.label
                )
            }
        }
    }
}

data class FloatingButtonAction(
    val label: String, val icon: ImageVector, val onClick: () -> Unit
)
