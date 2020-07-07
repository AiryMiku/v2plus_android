package com.airy.v2plus

import android.content.Context
import com.airy.v2plus.api.V2plusRetrofitService
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.UrlUriLoader
import com.bumptech.glide.load.model.stream.UrlLoader
import com.bumptech.glide.module.AppGlideModule
import java.io.InputStream


/**
 * Created by Airy on 2019-12-03
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

@GlideModule
class V2plusGlideModule: AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val memoryCacheSizeBytes = 1024 * 1024 * 10 // 10mb
        val diskCacheSizeBytes = 1024 * 1024 * 32L // 32mb
        val calculator = MemorySizeCalculator.Builder(context)
            .setBitmapPoolScreens(2f)
            .build()

        builder.apply {
            setMemoryCache(LruResourceCache(memoryCacheSizeBytes.toLong()))
            setDiskCache(InternalCacheDiskCacheFactory(context, diskCacheSizeBytes))
            setBitmapPool(LruBitmapPool(calculator.bitmapPoolSize.toLong()))
        }
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(V2plusRetrofitService.client))
    }
}