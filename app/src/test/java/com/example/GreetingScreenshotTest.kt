package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.core.designsystem.MindRestTheme
import com.example.features.authentication.presentation.screen.OnboardingScreen
import com.example.features.authentication.presentation.screen.LoginScreen
import com.example.features.authentication.presentation.screen.RegisterScreen
import com.example.features.home.presentation.screen.HomeScreen
import com.example.features.ikigai.presentation.screen.IkigaiDashboardScreen
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class GreetingScreenshotTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun onboarding_screenshot() {
    composeTestRule.setContent { 
      MindRestTheme { 
        OnboardingScreen(onComplete = {}) 
      } 
    }

    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/onboarding.png")
  }

  @Test
  fun login_screenshot() {
    composeTestRule.setContent { 
      MindRestTheme { 
        LoginScreen(onLoginSuccess = {}, onNavigateToRegister = {}) 
      } 
    }

    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/login.png")
  }

  @Test
  fun register_screenshot() {
    composeTestRule.setContent { 
      MindRestTheme { 
        RegisterScreen(onRegisterSuccess = {}, onNavigateToLogin = {}) 
      } 
    }

    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/register.png")
  }

  @Test
  fun home_screenshot() {
    composeTestRule.setContent { 
      MindRestTheme { 
        HomeScreen({}, {}, {}, {}, {}) 
      } 
    }

    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/home.png")
  }

  @Test
  fun ikigai_screenshot() {
    composeTestRule.setContent { 
      MindRestTheme { 
        IkigaiDashboardScreen(onNavigateBack = {}) 
      } 
    }

    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/ikigai.png")
  }
}
