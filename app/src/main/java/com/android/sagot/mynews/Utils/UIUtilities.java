package com.android.sagot.mynews.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class UIUtilities {

    /**
     * Method allowing to change the color of bar of status
     * Only SDK_VERSION > 21
     * @param content Activity , int color of the toolbar
     */
    // Change the color of the status bar
    public static void changeStatusBarColor(Activity content, int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = content.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }
    
    /**************************************************************************
     *  Class allowing to add a separator in various item from a recyclerView
     */ 
    public static class DividerItemDecoration extends RecyclerView.ItemDecoration {

        private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

        private Drawable divider;

        /**
         * Default divider will be used
         */
        public DividerItemDecoration(Context context) {
            final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
            divider = styledAttributes.getDrawable(0);
            styledAttributes.recycle();
        }

        /**
         * Custom divider will be used
         */
        public DividerItemDecoration(Context context, int resId) {
            divider = ContextCompat.getDrawable(context, resId);
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + divider.getIntrinsicHeight();

                divider.setBounds(left, top, right, bottom);
                divider.draw(c);
            }
        }
    }
      
    /**
     * Checking whether network is connected
     * @param context Context to get {@link ConnectivityManager}
     * @return true if Network is connected, else false
     */
    public static boolean isNetworkAvailable(Context context) {
        int[] networkTypes = {  ConnectivityManager.TYPE_MOBILE,
                                ConnectivityManager.TYPE_WIFI};
        try {
            // Get ConnectivityManager
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            // // Look if the connection is a Wifi or Mobile connection 
            for (int networkType : networkTypes) {
                // Get ActiveNetworkInfo
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                // see if a network connection is established and Wifi or Mobile connection  
                if (activeNetworkInfo != null &&
                        activeNetworkInfo.getType() == networkType)
                    return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
