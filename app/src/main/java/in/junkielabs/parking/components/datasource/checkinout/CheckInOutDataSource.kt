package `in`.junkielabs.parking.components.datasource.checkinout

import `in`.junkielabs.parking.components.api.base.ApiResponse
import `in`.junkielabs.parking.components.api.models.checkinout.ParamCheckInOut
import `in`.junkielabs.parking.components.api.repository.ApiRepoCheckInOut
import `in`.junkielabs.parking.components.firebase.auth.FirebaseToken
import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.jetbrains.anko.info
import retrofit2.http.Query

/**
 * Created by Niraj on 10-12-2021.
 */
class CheckInOutDataSource(
    var apiRepoCheckInOut: ApiRepoCheckInOut,
    var parkingAreaId: String, var status: Int
) :
    PagingSource<String, ParamCheckInOut>() {

    companion object{
        val PAZE_SIZE = 10
    }
    override fun getRefreshKey(state: PagingState<String, ParamCheckInOut>): String? {
        return null
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, ParamCheckInOut> {

        var token: String? =
            FirebaseToken.getToken() ?: return LoadResult.Error(Throwable("Token not found"))

        try {

//            info { "QuestionDataSource Key 1: " }


            var response = apiRepoCheckInOut.checkInOuts(token!!, parkingAreaId, status, params.key)
            var nextKey = null

            if (response.status == ApiResponse.Status.SUCCESS) {

                if(response.data!=null){
                    if (response.data!!.result.size == response.data!!.pageSize) {
                        return LoadResult.Page(
                            data = response.data!!.result,
                            prevKey = null,
                            nextKey = response.data!!.result.last().id
                        )

                    } else {
                        return LoadResult.Page(
                            data = response.data!!.result,
                            prevKey = null,
                            nextKey = null
                        )
                    }
                }
                return LoadResult.Page(
                    data = listOf(),
                    prevKey = null,
                    nextKey = null
                )

            } else if (response.status == ApiResponse.Status.ERROR) {
                return LoadResult.Error(
                    Throwable(response.message ?: "Server Error !!")
                )
            }


            /* return LoadResult.Page(
                    data = response.toObjects(ParamQuestion::class.java),
                    prevKey = null,
                    nextKey = nextQuery
                )*/

        } catch (e: Exception) {
            e.printStackTrace()
            return LoadResult.Error(e)
        }

       return LoadResult.Error(Throwable("Something well wrong !!"))

        /*
        info { "QuestionDataSource load: " }
            try {

                info { "QuestionDataSource Key 1: " }
                // Step 1
                var currentPage: QuerySnapshot;

                if (params.key != null) {

                    info { "QuestionDataSource Key 2: " }
    //                query = query.startAfter(params.key)
                    currentPage = params.key!!.get().await()
                } else {
                    info { "QuestionDataSource Key 3: " }
    //                query = query
                    currentPage = query.limit(PAZE_SIZE.toLong()).get().await()
                }
                var nextQuery: Query? = null
                if (currentPage.size() == PAZE_SIZE) {
                    nextQuery =  query.startAfter(currentPage.documents[currentPage.size() - 1]).limit(PAZE_SIZE.toLong())

                }
    //            info { "datasource size: ${currentPage.size()} ${currentPage.documents[currentPage.size() - 1]?.id} " }

                *//*   // Step 2
               val lastDocumentSnapshot = currentPage.documents[currentPage.size() - 1]

               // Step 3
               val nextPage = query.limit(10).startAfter(lastDocumentSnapshot)
                   .get()
                   .await()
   *//*
            // Step 4
            return LoadResult.Page(
                data = currentPage.toObjects(ParamQuestion::class.java),
                prevKey = null,
                nextKey = nextQuery
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return LoadResult.Error(e)
        }*/
    }
}