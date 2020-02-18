package com.google.android.material.bottomsheet;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import java.lang.ref.WeakReference;

public class ViewPagerBottomSheetBehavior<V extends View> extends BottomSheetBehavior<V> {

    public ViewPagerBottomSheetBehavior() {
    }

    public ViewPagerBottomSheetBehavior(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @VisibleForTesting
    @Nullable
    @Override
    View findScrollingChild(View view) {
        if (view instanceof ViewPager) {
            ViewPager viewPager = ((ViewPager) view);
            View currentPage = viewPager.getChildAt(viewPager.getCurrentItem());
            if (currentPage != null) {
//                Log.e("Loi", "event page : " + viewPager.getCurrentItem());
                return super.findScrollingChild(currentPage);
            }
        }
        return super.findScrollingChild(view);
    }

    public void updateScrollingChild() {
        if (viewRef != null) {
            if (viewRef.get() != null) {
                View scrollingChild = findScrollingChild(viewRef.get());
                nestedScrollingChildRef = new WeakReference<>(scrollingChild);
            }
        }
    }

    /**
     * A utility function to get the {@link ViewPagerBottomSheetBehavior} associated with the {@code view}.
     *
     * @param view The {@link View} with {@link ViewPagerBottomSheetBehavior}.
     * @return The {@link ViewPagerBottomSheetBehavior} associated with the {@code view}.
     */
    @SuppressWarnings("unchecked")
    public static <V extends View> ViewPagerBottomSheetBehavior<V> from(V view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (!(params instanceof CoordinatorLayout.LayoutParams)) {
            throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
        }
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) params).getBehavior();
        if (!(behavior instanceof ViewPagerBottomSheetBehavior)) {
            throw new IllegalArgumentException(
                    "The view is not associated with ViewPagerBottomSheetBehavior");
        }
        return (ViewPagerBottomSheetBehavior<V>) behavior;
    }
}
