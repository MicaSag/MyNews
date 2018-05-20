package com.android.sagot.mynews.Utils;

import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

public class UIUtilities {

    // ---------------------------------------------------------------------------------------------
    //                           STATUS BAR Only SDK_VERSION > 21
    // ---------------------------------------------------------------------------------------------

    // Change the color of the status bar
    public static void changeStatusBarColor(Activity content, int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = content.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }
}
