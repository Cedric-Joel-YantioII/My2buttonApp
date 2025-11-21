package com.assignment2.my2buttonapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MainActivityUITest {

    private static final String PACKAGE_NAME = "com.assignment2.my2buttonapp";
    private static final int LAUNCH_TIMEOUT = 5000;
    private UiDevice device;

    @Before
    public void setUp() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.pressHome();

        final String launcherPackage = getLauncherPackageName();
        assertNotNull(launcherPackage);
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        Context context = ApplicationProvider.getApplicationContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(PACKAGE_NAME);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        device.wait(Until.hasObject(By.pkg(PACKAGE_NAME).depth(0)), LAUNCH_TIMEOUT);
    }

    @Test
    public void testExplicitButtonNavigationAndVerifyChallenge() throws UiObjectNotFoundException {
        UiObject explicitButton = device.findObject(new UiSelector()
                .text("Start Activity Explicitly")
                .className("android.widget.Button"));

        assertTrue("Explicit button should be displayed", explicitButton.exists());

        explicitButton.click();

        device.wait(Until.hasObject(By.text("Cross-platform compatibility and development complexities.")), LAUNCH_TIMEOUT);

        UiObject challengeText = device.findObject(new UiSelector()
                .textContains("Cross-platform compatibility"));

        assertTrue("Challenge text should be visible in SecondActivity", challengeText.exists());
    }

    private String getLauncherPackageName() {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        PackageManager pm = ApplicationProvider.getApplicationContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.activityInfo.packageName;
    }
}
