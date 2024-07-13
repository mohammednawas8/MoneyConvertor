interface Syncable {

    suspend fun syncWith(synchronizer: Synchronizer)

}

class Synchronizer {
    suspend fun <T> start(
        fetchRemoteData: suspend () -> T,
        deleteLocalData: suspend () -> Unit,
        updateLocalData: suspend (T) -> Unit
    ) {
        val remoteData = fetchRemoteData()
        deleteLocalData()
        updateLocalData(remoteData)
    }
}