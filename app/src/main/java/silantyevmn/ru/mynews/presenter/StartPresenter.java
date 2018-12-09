package silantyevmn.ru.mynews.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import ru.terrakok.cicerone.Router;
import ru.terrakok.cicerone.android.support.SupportAppScreen;
import silantyevmn.ru.mynews.ui.Screens;
import silantyevmn.ru.mynews.ui.view.StartView;
import silantyevmn.ru.mynews.utils.Messages;
import silantyevmn.ru.mynews.utils.NetworkStatus;

@InjectViewState
public class StartPresenter extends MvpPresenter<StartView> {
    private Router router;

    public StartPresenter(Router router) {
        this.router = router;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().init();
    }

    public void onBackPressed() {
        router.exit();
    }

    public void restartActivity() {
        router.replaceScreen(new Screens.StartScreen());
    }

    public void homeScreen(String titleToolbar) {
        replaceScreen(titleToolbar,new Screens.HomeScreen());
    }

    public void searchNewsScreen(String query) {
        navigationToScreen(query, new Screens.SearchScreen(query));
    }

    public void categoryScreen(String titleToolbar) {
        replaceScreen(titleToolbar,new Screens.CategoryScreen());
    }

    public void bookmarksScreen(String titleToolbar) {
        replaceScreen(titleToolbar, new Screens.BookmarksScreen());
    }

    private void replaceScreen(String titleToolbar, SupportAppScreen supportAppScreen) {
        if (isInternetAvailable()) {
            router.replaceScreen(supportAppScreen);
            getViewState().initToolbar(titleToolbar);
        }
    }

    private void navigationToScreen(String titleToolbar, SupportAppScreen supportAppScreen) {
        if (isInternetAvailable()) {
            router.navigateTo(supportAppScreen);
            getViewState().initToolbar(titleToolbar);
        }
    }

    private boolean isInternetAvailable() {
        boolean isInternetOnline = NetworkStatus.isInternetAvailable();
        if (!isInternetOnline) {
            getViewState().showError(Messages.getErrorNoInternetConnection());
        }
        return isInternetOnline;
    }
}
