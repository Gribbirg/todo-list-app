package ru.gribbirg.about.di

import dagger.Component
import ru.gribbirg.about.AboutViewModel

@Component(dependencies = [AboutFeatureDependencies::class])
internal interface AboutFeatureComponent {
    @Component.Factory
    interface Factory {
        fun create(deps: AboutFeatureDependencies): AboutFeatureComponent
    }

    val viewModel: AboutViewModel
}