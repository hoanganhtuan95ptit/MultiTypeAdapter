package com.tuanha.adapter

import com.tuanha.adapter.provider.AdapterProvider
import java.util.ServiceLoader


internal val provider: List<AdapterProvider> by lazy {

    ServiceLoader.load(AdapterProvider::class.java).toList()
}