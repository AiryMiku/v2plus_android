package com.airy.v2plus.di

import com.airy.v2plus.repository.NodeRepository
import com.airy.v2plus.ui.node.NodeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Airy on 2020/9/5
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

val nodeModule = module {
    single { NodeRepository() }
    viewModel { NodeViewModel(get()) }
}

val allModules = nodeModule