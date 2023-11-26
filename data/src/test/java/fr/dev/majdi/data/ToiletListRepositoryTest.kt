package fr.dev.majdi.data

import fr.dev.majdi.data.datasource.local.AppDatabase
import fr.dev.majdi.data.datasource.remote.ToiletService
import fr.dev.majdi.data.repository.ToiletListRepositoryImp
import fr.dev.majdi.domain.model.Fields
import fr.dev.majdi.domain.model.GeoShape
import fr.dev.majdi.domain.model.Geometry
import fr.dev.majdi.domain.model.Parameters
import fr.dev.majdi.domain.model.Toilet
import fr.dev.majdi.domain.model.ToiletsResponse
import fr.dev.majdi.domain.usecase.base.Result
import fr.dev.majdi.domain.util.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

/**
 * Created by Majdi RABEH on 26/11/2023.
 * Email m.rabeh.majdi@gmail.com
 */
//I Test just this Repository we can test others but now i don't have the time
@ExperimentalCoroutinesApi
class ToiletListRepositoryTest {

    @Mock
    private lateinit var mockDatabase: AppDatabase

    @Mock
    private lateinit var mockToiletService: ToiletService

    private lateinit var toiletListRepository: ToiletListRepositoryImp

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        toiletListRepository = ToiletListRepositoryImp(mockDatabase, mockToiletService)
    }

    @Test
    fun `getToilets returns Success`() = runBlockingTest {
        // Arrange
        val dataset = Constants.DATA_SET
        val start = "0"
        val rows = "10"
        val mockToilets = listOf(
            Toilet(
                dataset, Fields(
                    "Oui", "4  RUE DU COMMANDANT RENE MOUCHOTTE",
                    75015, "4  RUE DU COMMANDANT RENE MOUCHOTTE",
                    listOf(48.83997793898217, 2.3214109225079844),
                    GeoShape(listOf(listOf(2.3214109225079844, 48.83997793898217)), "MultiPoint"),
                    "Toilette publique de la Ville de Paris",
                    "24 h / 24", "http://opendata.paris.fr", "SANISETTE"
                ),
                Geometry(listOf(2.3214109225079844, 48.83997793898217), ""),
                "1cbc4c531185bf0f489922614c0be51e8d84d161",
                "2023-11-04T05:12:00.448Z"
            )
        )
        `when`(mockToiletService.searchToilet(dataset, start, rows)).thenReturn(
            ToiletsResponse(
                nhits = start.toInt(), parameters = Parameters(
                    dataset, "", rows.toInt(), start.toInt(), ""
                ), records = mockToilets
            )
        )

        // Act
        val result = toiletListRepository.getToilets(dataset, start, rows).first()

        // Assert
        assertEquals(Result.Success(mockToilets), result)
    }

    @Test
    fun `getToilets returns Failure when service call fails`() = runBlockingTest {
        val dataset = Constants.DATA_SET
        val start = "0"
        val rows = "10"
        val exception = RuntimeException("Service call failed")
        `when`(mockToiletService.searchToilet(dataset, start, rows)).thenThrow(exception)

        // Act
        val result = toiletListRepository.getToilets(dataset, start, rows).first()

        // Assert
        assertEquals(Result.Failure(exception), result)
    }

}