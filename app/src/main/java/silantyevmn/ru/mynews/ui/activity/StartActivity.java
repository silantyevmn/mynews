package silantyevmn.ru.mynews.ui.activity;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import javax.inject.Inject;

import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;
import ru.terrakok.cicerone.android.support.SupportAppNavigator;
import silantyevmn.ru.mynews.App;
import silantyevmn.ru.mynews.R;
import silantyevmn.ru.mynews.presenter.StartPresenter;
import silantyevmn.ru.mynews.ui.common.BackButtonListener;
import silantyevmn.ru.mynews.ui.popup.PopupDialogMessage;
import silantyevmn.ru.mynews.ui.view.StartView;

public class StartActivity extends MvpAppCompatActivity implements StartView {
    private Toolbar toolbar;
    private TextView titleToolbar;
    private SearchView searchView;

    @Inject
    PopupDialogMessage popupWindow;

    @Inject
    Router router;
    @Inject
    NavigatorHolder navigatorHolder;
    @InjectPresenter
    StartPresenter presenter;
    private Navigator navigator = new SupportAppNavigator(this, R.id.main_container);

    @ProvidePresenter
    public StartPresenter provideMainPresenter() {
        StartPresenter presenter = new StartPresenter(router);
        return presenter;
    }

    public void initToolbar(String title) {
        toolbar.getMenu().findItem(R.id.search).setVisible(true);
        toolbar.setNavigationIcon(null);
        titleToolbar.setText(title);
    }

    public Toolbar getToolbar(){
        return toolbar;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App.getInstance().getComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        toolbar = findViewById(R.id.toolbar);
        titleToolbar = findViewById(R.id.toolbar_title);
        onCreateOptionsMenu(toolbar.getMenu());

        if (savedInstanceState == null) {
            presenter.homeScreen(getString(R.string.title_home));
        }
        BottomNavigationView bottomNavigationView= findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item ->{
            switch (item.getItemId()){
                case R.id.navigation_home:{
                    presenter.homeScreen(getString(R.string.title_home));
                    return true;
                }
                case R.id.navigation_category:{
                    presenter.categoryScreen(getString(R.string.title_category));
                    return true;
                }
                case R.id.navigation_bookmarks:{
                    presenter.bookmarksScreen(getString(R.string.title_bookmarks));
                    return true;
                }
            }
            return false;
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_start, menu);
        MenuItem item = menu.findItem(R.id.search);
        searchView = (SearchView) item.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnSearchClickListener(view -> {
            titleToolbar.setVisibility(View.INVISIBLE);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            toolbar.setNavigationOnClickListener(navIconView -> closeSearch());
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                presenter.searchNewsScreen(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        searchView.setOnQueryTextFocusChangeListener((view1, hasFocus) -> {
            if (!hasFocus) {
                closeSearch();
            }
        });
        return true;
    }

    private void closeSearch() {
        searchView.onActionViewCollapsed();
        titleToolbar.setVisibility(View.VISIBLE);
        toolbar.setNavigationIcon(null);
        toolbar.setNavigationOnClickListener(null);
    }

    @Override
    public void init() {

    }

    @Override
    public void showError(String errorTextMessage) {
        popupWindow.error(toolbar.getRootView(), errorTextMessage);
    }

    @Override
    protected void onPause() {
        navigatorHolder.removeNavigator();
        super.onPause();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        navigatorHolder.setNavigator(navigator);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_container);
        if (fragment != null
                && fragment instanceof BackButtonListener
                && ((BackButtonListener) fragment).onBackPressed()) {
            return;
        } else {
            super.onBackPressed();
        }
    }
}
