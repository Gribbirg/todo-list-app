package ru.gribbirg.todoapp

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.gribbirg.edit.testing.EditFeatureTestingTags
import ru.gribbirg.list.testing.ListFeatureTestingTags
import ru.gribbirg.todoapp.app.TestApplication
import ru.gribbirg.todoapp.di.TestAppComponent

@RunWith(AndroidJUnit4::class)
class AppUiTest {

    private val dispatcher = Dispatchers.IO

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var appComponent: TestAppComponent

    @Before
    fun setContent() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        appComponent = (context as TestApplication).testAppComponent
        runBlocking {
            appComponent.loginRepo.registerUser("1")
        }
        composeTestRule.setContent {
            TodoComposeApp(appComponent)
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun addItem() {
        CoroutineScope(dispatcher).launch {
            delay(1000)
            composeTestRule.awaitIdle()
            composeTestRule.waitUntilAtLeastOneExists(
                hasTestTag(
                    ListFeatureTestingTags.getItemRowId(
                        "1"
                    )
                )
            )

            composeTestRule.onNodeWithTag(ListFeatureTestingTags.ADD_BUTTON).performClick()
            composeTestRule.onNodeWithTag(EditFeatureTestingTags.TEXT_FIELD)
                .performTextInput("Test task")
            composeTestRule.onNodeWithTag(EditFeatureTestingTags.SAVE_BUTTON).performClick()

            composeTestRule.onNodeWithText("Test task").assertExists()
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun deleteItem() {
        CoroutineScope(dispatcher).launch {
            delay(1000)
            composeTestRule.awaitIdle()
            composeTestRule.waitUntilAtLeastOneExists(
                hasTestTag(
                    ListFeatureTestingTags.getItemRowId(
                        "1"
                    )
                )
            )

            composeTestRule.onNodeWithTag(ListFeatureTestingTags.getItemRowId("1")).onChildAt(2)
                .performClick()
            composeTestRule.onNodeWithTag(EditFeatureTestingTags.DELETE_BUTTON).performClick()

            composeTestRule.onNodeWithTag("1").assertDoesNotExist()
        }
    }
}