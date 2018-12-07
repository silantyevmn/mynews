package silantyevmn.ru.mynews.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import ru.terrakok.cicerone.Router;
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

    public void toMainScreen() {
        router.replaceScreen(new Screens.HomeScreen());
    }

    public void searchNewsScreen(String query) {
        if (!NetworkStatus.isInternetAvailable()) {
            getViewState().showError(Messages.getErrorNoInternetConnection());
            return;
        }
        router.navigateTo(new Screens.SearchScreen(query));
    }
}
