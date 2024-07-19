package ru.gribbirg.about.di

class AboutFeatureFactory(
    deps: AboutFeatureDependencies
) {
    private val component: AboutFeatureComponent = DaggerAboutFeatureComponent.factory().create(deps)

    fun createViewModel() = component.viewModel
}