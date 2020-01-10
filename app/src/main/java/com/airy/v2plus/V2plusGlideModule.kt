package com.airy.v2plus

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.module.AppGlideModule


/**
 * Created by Airy on 2019-12-03
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

@GlideModule
class V2plusGlideModule: AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val memoryCacheSizeBytes = 1024 * 1024 * 10 // 10mb
        builder.setMemoryCache(LruResourceCache(memoryCacheSizeBytes.toLong()))

        val calculator = MemorySizeCalculator.Builder(context)
            .setBitmapPoolScreens(2f)
            .build()
        builder.setBitmapPool(LruBitmapPool(calculator.bitmapPoolSize.toLong()))
    }
}