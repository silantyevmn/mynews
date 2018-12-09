package silantyevmn.ru.mynews.ui;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.view.View;

import static java.lang.Math.min;
import static java.lang.Math.max;


/**
 * Created by silan on 07.12.2018.
 */

public class BottonNavigationBehavior extends CoordinatorLayout.Behavior<BottomNavigationView> {

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull BottomNavigationView child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        //return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
        return axes== ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull BottomNavigationView child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        child.setTranslationY(max(0f,min(child.getHeight(),child.getTranslationY()+dy)));
    }
}
