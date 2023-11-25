package fr.dev.majdi.presentation.toiletdetailscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomDrawerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Accessible
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Source
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.dev.majdi.domain.model.Toilet
import fr.dev.majdi.presentation.R
import kotlinx.coroutines.launch

/**
 * Created by Majdi RABEH on 24/11/2023.
 * Email m.rabeh.majdi@gmail.com
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ToiletDetailScreen(selectedToilet: Toilet?, bottomSheetState: BottomDrawerState) {

    val coroutineScope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()  // Set the bottom sheet to full screen
                .background(Color.White)
        ) {
            //Check if toilet item is selected
            selectedToilet?.let { item ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(top = 20.dp)
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
                            tint = Color.Gray
                        )
                        Text(
                            modifier = Modifier.padding(start = 5.dp),
                            text = item.fields.adresse.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        // Icon for opening hours
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                        Text(
                            modifier = Modifier.padding(start = 5.dp),
                            text = stringResource(id = R.string.list_item_opening) + " ${item.fields.horaire.toString()}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black
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
                            tint = Color.Gray
                        )
                        Text(
                            modifier = Modifier.padding(start = 5.dp),
                            text = stringResource(id = R.string.list_item_pmr) + " ${item.fields.acces_pmr.toString()}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        // Icon for arrondissement
                        Icon(
                            imageVector = Icons.Default.LocationCity,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                        Text(
                            modifier = Modifier.padding(start = 5.dp),
                            text = " ${item.fields.arrondissement.toString()}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        // Icon for gestionnaire
                        Icon(
                            imageVector = Icons.Default.Business,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                        Text(
                            modifier = Modifier.padding(start = 5.dp),
                            text = " ${item.fields.gestionnaire.toString()}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        // Icon for type
                        Icon(
                            imageVector = Icons.Default.Celebration,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                        Text(
                            modifier = Modifier.padding(start = 5.dp),
                            text = " ${item.fields.type.toString()}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        // Icon for source
                        Icon(
                            imageVector = Icons.Default.Source,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                        Text(
                            modifier = Modifier.padding(start = 5.dp),
                            text = " ${item.fields.source.toString()}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black
                        )
                    }
                }
            } ?: run {
                Text(
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black, text = "Aucun élément sélectionné"
                )
            }

            // Bouton pour fermer la feuille inférieure
            //Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        bottomSheetState.close()
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)  // Position the close button at the end

            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
            }
        }
    }

}