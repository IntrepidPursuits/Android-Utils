package io.intrepid.commonutils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

public class PackageUtils {
    /**
     * Checks if an app is installed and enabled
     *
     * @param context     the context
     * @param packageName the package name of the app
     * @return true if the packageName exists and is enabled, false if otherwise
     */
    public static boolean isPackageInstalled(@NonNull Context context, @NonNull String packageName) {
        PackageManager packageManager = context.getPackageManager();
        try {
            final ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            return applicationInfo.enabled;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
