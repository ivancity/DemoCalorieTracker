package com.ivan.m.tracker_presentation.components.pager

interface Paginator<Key, Item> {
    suspend fun loadNextItems()
    fun reset()
}