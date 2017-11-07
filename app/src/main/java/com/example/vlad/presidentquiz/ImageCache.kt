package com.example.vlad.presidentquiz

import android.graphics.Bitmap
import android.util.LruCache

/**
 * Created by vlad on 07/11/17.
 */
object ImageCache {
    val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    val cacheSize = maxMemory / 8

    val memoryCache = object: LruCache<String, Bitmap>(cacheSize) {
        override fun sizeOf(key: String, image: Bitmap): Int {
            return image.getByteCount() / 1024
        }
    }

    fun put(key: String, image: Bitmap) {
        memoryCache.put(key, image)
    }

    fun get(key: String): Bitmap? {
        return memoryCache.get(key)
    }

}