package fr.dev.majdi.presentation.toiletsmapscreen

import android.annotation.SuppressLint
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.NoOpUpdate
import androidx.core.graphics.drawable.toBitmap
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.plugin.animation.CameraAnimatorOptions.Companion.cameraAnimatorOptions
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import fr.dev.majdi.domain.model.Toilet
import fr.dev.majdi.domain.util.LocationService
import fr.dev.majdi.presentation.R
import fr.dev.majdi.presentation.util.areCoordinatesEqual
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Created by Majdi RABEH on 24/11/2023.
 * Email m.rabeh.majdi@gmail.com
 */

val mapBox = mutableStateOf<MapboxMap?>(null)

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ToiletMapScreen(toiletMapViewModel: ToiletMapViewModel) {

    //I have some problem on gradle when using hiltViewModel on compose
    //val toiletMapViewModel: ToiletMapViewModel = hiltViewModel()
    LaunchedEffect(key1 = Unit) {
        toiletMapViewModel.loadLocalToilets()
    }

    toiletMapViewModel.inProgress.let {
        if (it) {
            // Loading state
            Box(
                modifier = Modifier
                    .background(
                        color = Color.Black.copy(alpha = 0.5f)
                    )
                    .fillMaxSize()
                    .padding(16.dp), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color.Blue
                )
            }
        } else {
            val toiletList by toiletMapViewModel.toiletListLiveData.observeAsState()
            toiletList?.let { listToilet ->
                MapScreen(listToilet)
            }
        }
    }
}

@Composable
fun MapScreen(toilets: List<Toilet> = listOf()) {

    var point: Point? by remember {
        mutableStateOf(null)
    }
    var relaunch by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    val permissionRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (!permissions.values.all { it }) {
                //handle permission denied
            } else {
                relaunch = !relaunch
            }
        }
    )

    val lazyListState = rememberLazyListState()
    val clickedMarker = remember {
        mutableStateOf<Toilet?>(null)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        MapBoxMap(
            onPointChange = {
                clickedMarker.value = it
            },
            point = point,
            modifier = Modifier
                .weight(0.75f)
                .fillMaxHeight()
                .fillMaxWidth(),
            toilets = toilets
        )
        // Display scrollable cards
        LazyRow(
            state = lazyListState,
            modifier = Modifier
                .padding(16.dp)
                .weight(0.25f)
                .fillMaxSize()
        ) {
            items(toilets) { toilet ->
                ToiletCard(toilet = toilet, mapBox.value)
            }
        }
    }

    val index = toilets.indexOf(clickedMarker.value)
    LaunchedEffect(index) {
        if (index > 0) {
            lazyListState.scrollToItem(index)
        }
    }

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

@SuppressLint("CheckResult")
@Composable
fun MapBoxMap(
    modifier: Modifier = Modifier,
    onPointChange: (Toilet) -> Unit,
    point: Point?,
    toilets: List<Toilet>
) {

    val context = LocalContext.current

    val myLocation = remember(context) {
        context.getDrawable(R.drawable.mylocation)?.toBitmap()
    }

    val markerToilet = remember(context) {
        context.getDrawable(R.drawable.marker_toilet)?.toBitmap()
    }

    var pointAnnotationManager: PointAnnotationManager? by remember {
        mutableStateOf(null)
    }

    AndroidView(
        factory = {
            MapView(it).also { map ->
                map.getMapboxMap().loadStyleUri(Style.TRAFFIC_DAY)
                val annotationApi = map.annotations
                pointAnnotationManager = annotationApi.createPointAnnotationManager()

                map.getMapboxMap().addOnMapClickListener { clickedPoint ->
                    // Find the toilet corresponding to the clicked marker
                    if (toilets.isNotEmpty()) {
                        val clickedToilet = toilets
                            .firstOrNull { toilet ->
                                areCoordinatesEqual(
                                    toilet.geometry.coordinates.first(),
                                    toilet.geometry.coordinates.last(),
                                    clickedPoint.coordinates().first(),
                                    clickedPoint.coordinates().last()
                                )
                            }

                        clickedToilet?.let { toilet ->
                            onPointChange(toilet)
                        }
                    }
                    true
                }
            }
        },
        update = { map ->
            mapBox.value = map.getMapboxMap()
            if (point != null) {
                pointAnnotationManager?.let {
                    it.deleteAll()

                    Observable.just(point)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { pt ->
                            val pointAnnotationOptions = PointAnnotationOptions()
                                .withPoint(pt)
                                .withIconAnchor(IconAnchor.CENTER)
                                .withIconSize(0.5)
                                .withIconImage(myLocation!!)
                            it.create(pointAnnotationOptions)
                        }

                    Observable.fromIterable(toilets)
                        .flatMap { toilet ->
                            val toiletPoint = Point.fromLngLat(
                                toilet.fields.geo_point_2d.first(),
                                toilet.fields.geo_point_2d.last()
                            )
                            // Create a marker for each MapObject
                            val toiletPointAnnotationOptions = PointAnnotationOptions()
                                .withPoint(toiletPoint)
                                .withIconAnchor(IconAnchor.CENTER)
                                .withIconSize(0.5)
                                //.withData(data)
                                .withIconImage(markerToilet!!)
                            Log.e("RXJAVA", "flatMap $toiletPoint")
                            Observable.just(toiletPointAnnotationOptions)

                        }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnTerminate {
                            Log.e("RXJAVA", "doOnTerminate")
                        }.doFinally {
                            Log.e("RXJAVA", "doFinally")
                            //Center the map on Paris
                            val paris = Point.fromLngLat(2.3509871, 48.8566667)

                            map.getMapboxMap()
                                .flyTo(
                                    CameraOptions.Builder()
                                        .zoom(10.0)
                                        .center(paris)
                                        .build()
                                )
                            //animate camera
                            map.camera.apply {
                                val bearing = createBearingAnimator(cameraAnimatorOptions(-45.0)) {
                                    duration = 4000
                                    interpolator = AccelerateDecelerateInterpolator()
                                }
                                val zoom = createZoomAnimator(
                                    cameraAnimatorOptions(11.0) {
                                        startValue(3.0)
                                    }
                                ) {
                                    duration = 4000
                                    interpolator = AccelerateDecelerateInterpolator()
                                }
                                val pitch = createPitchAnimator(
                                    cameraAnimatorOptions(55.0) {
                                        startValue(0.0)
                                    }
                                ) {
                                    duration = 4000
                                    interpolator = AccelerateDecelerateInterpolator()
                                }
                                playAnimatorsSequentially(zoom, pitch, bearing)
                            }
                        }
                        .doOnComplete {
                            Log.e("RXJAVA", "doOnComplete")
                        }
                        .subscribe { toiletPointAnnotationOptions ->
                            // Add the marker to the map
                            it.create(toiletPointAnnotationOptions)
                            Log.e("RXJAVA", "subscribe")
                        }
                }
            }
            NoOpUpdate
        },
        modifier = modifier
    )

}

@Composable
fun ToiletCard(toilet: Toilet, mapBox: MapboxMap?) {
    val cardBackground = remember {
        mutableStateOf(Color.White)
    }
    val textColor = remember {
        mutableStateOf(Color.Black)
    }
    if (toilet.fields.acces_pmr == "Non") {
        cardBackground.value = Color.Red
        textColor.value = Color.White
    } else {
        cardBackground.value = Color.White
        textColor.value = Color.Black
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clickable {
                val toiletPoint = Point.fromLngLat(
                    toilet.fields.geo_point_2d.first(),
                    toilet.fields.geo_point_2d.last()
                )
                mapBox?.let {
                    mapBox.flyTo(
                        CameraOptions
                            .Builder()
                            .zoom(16.0)
                            .center(toiletPoint)
                            .build()
                    )
                }
            }
            .padding(vertical = 16.dp, horizontal = 5.dp),
        elevation = 4.dp,
        backgroundColor = cardBackground.value,
        shape = RoundedCornerShape(16.dp)
    ) {
        // Customize the content of the card based on the Toilet data
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(start = 5.dp),
                text = toilet.fields.adresse.toString(),
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.padding(start = 5.dp),
                text = stringResource(id = R.string.list_item_pmr) + " ${toilet.fields.acces_pmr.toString()}",
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.padding(start = 5.dp),
                text = stringResource(id = R.string.list_item_opening) + " ${toilet.fields.horaire.toString()}",
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            // we can add more details if needed
        }
    }
}
