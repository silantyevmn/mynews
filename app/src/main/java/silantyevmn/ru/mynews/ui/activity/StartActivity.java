package silantyevmn.ru.mynews.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;

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
import silantyevmn.ru.mynews.ui.popup.PopupDialogMessage;
import silantyevmn.ru.mynews.presenter.StartPresenter;
import silantyevmn.ru.mynews.ui.common.BackButtonListener;
import silantyevmn.ru.mynews.ui.view.StartView;

public class StartActivity extends MvpAppCompatActivity implements StartView {
    private Toolbar toolbar;

    @Inject
    PopupDialogMessage popupDialog;

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

    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App.getInstance().getComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        toolbar = findViewById(R.id.toolbar);
        onCreateOptionsMenu(toolbar.getMenu());

        if (savedInstanceState == null) {
            presenter.toMainScreen();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_start, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //searchView.clearFocus();
                presenter.searchNewsScreen(query);
                //searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        searchView.setOnQueryTextFocusChangeListener((view1, hasFocus) -> {
            if (!hasFocus) {
                //presenter.closeSearchFragment();
                searchView.onActionViewCollapsed();
            }
        });
        return true;
    }

    @Override
    public void init() {

    }

    @Override
    public void showError(String errorTextMessage) {

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
