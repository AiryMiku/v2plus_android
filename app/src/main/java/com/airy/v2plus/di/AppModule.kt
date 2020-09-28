package com.airy.v2plus.di

import android.content.Context
import com.airy.v2plus.App
import com.airy.v2plus.db.V2plusDb
import com.airy.v2plus.repository.NodeRepository
import com.airy.v2plus.ui.node.NodeDetailViewModel
import com.airy.v2plus.ui.node.NodesViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Airy on 2020/9/5
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

val nodeModule = module {

    single { getDatabase(androidApplication()).nodeDao() }

    single { NodeRepository(get()) }

    viewModel {
        NodesViewModel(get())
    }

    viewModel {
        NodeDetailViewModel(get())
    }
}

val allModules = nodeModule

private fun getDatabase(context: Context) = V2plusDb.getDb(context)