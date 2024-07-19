package ru.gribbirg.todoapp.di

import dagger.Subcomponent
import ru.gribbirg.about.AboutViewModel

@Subcomponent
interface AboutFeatureComponent {
    val viewModel: AboutViewModel
}