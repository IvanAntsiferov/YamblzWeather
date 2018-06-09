package com.ivanantsiferov.yamblzweather.ui;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.ringov.yamblzweather.R;
import com.ringov.yamblzweather.presentation.ui.main.MainActivity;
import com.ringov.yamblzweather.presentation.ui.main.about.AboutFragment;
import com.ringov.yamblzweather.presentation.ui.main.add_city.AddCityFragment;
import com.ringov.yamblzweather.presentation.ui.main.settings.SettingsFragment;
import com.ringov.yamblzweather.presentation.ui.splash.SplashActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class SideMenuNavigationTest {

    @Rule
    public ActivityTestRule<SplashActivity> splashActivityTestRule =
            new ActivityTestRule<>(SplashActivity.class);

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    private Context context;

    @Before
    public void prepare() {
        context = mainActivityTestRule.getActivity().getApplicationContext();
    }

    @Test
    public void openAddCityScreen() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.rl_add_city)).perform(click());

        assertActivityTitle(R.string.add_city_title);

        Fragment fragment = getFragmentByTag(AddCityFragment.class.getSimpleName());
        assertTrue(fragment instanceof AddCityFragment);
    }

    @Test
    public void openSettingsScreen() {
        openDrawerAndNavigateTo(R.id.nav_settings);

        waitForScreenTransition();

        assertActivityTitle(R.string.prefs_title);

        Fragment fragment = getFragmentByTag(SettingsFragment.class.getSimpleName());
        assertTrue(fragment instanceof SettingsFragment);
    }

    @Test
    public void openAboutScreen() {
        openDrawerAndNavigateTo(R.id.nav_about);

        waitForScreenTransition();

        assertActivityTitle(R.string.about_title);

        Fragment fragment = getFragmentByTag(AboutFragment.class.getSimpleName());
        assertTrue(fragment instanceof AboutFragment);
    }

    private void openDrawerAndNavigateTo(@IdRes int menuItemId) {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.navigation_view)).perform(NavigationViewActions.navigateTo(menuItemId));
    }

    private void waitForScreenTransition() {
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            Log.e(getClass().getSimpleName(), "waitForScreenTransition", e);
        }
    }

    private void assertActivityTitle(@StringRes int expectedTitleId) {
        String expectedTitle = context.getString(expectedTitleId);
        String activityTitle = mainActivityTestRule.getActivity().getTitle().toString();
        assertEquals(expectedTitle, activityTitle);
    }

    private Fragment getFragmentByTag(String tag) {
        return mainActivityTestRule
                .getActivity()
                .getSupportFragmentManager()
                .findFragmentByTag(tag);
    }
}
