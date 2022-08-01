
package com.example.hardwarestock

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class NotificationTest {

    private val timeout: Long = 7000

    private val notificationText = "update your stock"

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity>
            = ActivityScenarioRule(MainActivity::class.java)

    private lateinit var uiDevice: UiDevice

    //Open notifiaction and check notification text is showed before timeout
    @Before
    fun setup() {
        uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        uiDevice.openNotification()
        uiDevice.wait(Until.hasObject(By.textContains(notificationText)), timeout)
    }

    //Check notification is pushed
    @Test
    fun notification_scheduled() {
        val notification = uiDevice.findObject(UiSelector().textContains(notificationText)).exists()
        assertTrue("Could not find text 'update your stock'", notification)
        uiDevice.pressHome()
    }

    //Check that after notification click, package name is the one for the app
    @Test
    fun notification_click() {
        val notification = uiDevice.findObject(UiSelector().textContains(notificationText))
        notification.click()
        uiDevice.wait(Until.hasObject(By.pkg("com.example.hardwarestock")
            .depth(0)), 1000)
        val pkg = uiDevice.findObject(UiSelector().packageName("com.example.hardwarestock"))
            .exists()
        assertTrue("Could not find package", pkg)
    }
}
