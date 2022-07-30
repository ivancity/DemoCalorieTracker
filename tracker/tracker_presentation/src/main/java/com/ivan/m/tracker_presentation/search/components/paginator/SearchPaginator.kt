package com.ivan.m.tracker_presentation.search.components.paginator

import com.ivan.m.tracker_presentation.components.pager.Paginator

/**
 * Paginating based on keys, in this case the key will be each of the page
 * number in the API network request. This let us know which page to load. The Item type is generic.
 */
class SearchPaginator<Key, Item>(
    // a key could be for example 0 as the number 0 page.
    private val initialKey: Key,
    // callback for when loading is updated, when loading or finished loading.
    // inline only for optimization to make a copy of the lambda in the body itself without creating something new in memory.
    private inline val onLoadUpdated: (Boolean) -> Unit,
    // function to define how to get the next load of items given a key
    private inline val onRequest: suspend (nextKey: Key) -> Result<List<Item>>,
    // if our next key is a page number is easy, but if it is a string then we need some logic.
    // the API response can have directly some next key that only the response will have it.
    // if they key comes from a network response then we need to change List for the actual Response with the next key.
    private inline val getNextKey: suspend (List<Item>) -> Key,
    private inline val onError: suspend (Throwable?) -> Unit,
    private inline val onSuccess: suspend (items: List<Item>, newKey: Key) -> Unit
): Paginator<Key, Item> {

    private var currentKey: Key = initialKey
    private var isMakingRequest = false

    override suspend fun loadNextItems() {
        if (isMakingRequest) {
            return
        }
        isMakingRequest = true
        onLoadUpdated(true)
        val result = onRequest(currentKey)
        isMakingRequest = false

        // if the result comes back with a success then we get the items,
        // otherwise if we get an error we run the block in the getOrElse
        val items = result.getOrElse {
            onError(it)
            onLoadUpdated(false)
            return
        }

        // use our suspend lambda in order to get the next key based on the logic provided to the paginator.
        currentKey = getNextKey(items)
        onSuccess(items, currentKey)
        onLoadUpdated(false)
    }

    override fun reset() {
        currentKey = initialKey
    }
}