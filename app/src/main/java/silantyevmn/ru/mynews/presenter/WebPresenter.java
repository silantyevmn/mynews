package silantyevmn.ru.mynews.presenter;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import io.reactivex.Scheduler;
import ru.terrakok.cicerone.Router;
import silantyevmn.ru.mynews.model.entity.Articles;
import silantyevmn.ru.mynews.ui.view.WebNewsView;
import silantyevmn.ru.mynews.utils.Messages;
import silantyevmn.ru.mynews.utils.NetworkStatus;
import silantyevmn.ru.mynews.utils.PopurManager;

@InjectViewState
public class WebPresenter extends MvpPresenter<WebNewsView> {

    private Scheduler scheduler;
    private Router router;
    private Articles articles;

    @SuppressLint("CheckResult")
    public WebPresenter(Scheduler scheduler, Router router, Articles articles) {
        this.scheduler = scheduler;
        this.router = router;
        this.articles = articles;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().init(articles);
    }

    public void share() {
        if(articles!=null) {
            PopurManager.share(articles);
        }
    }

    public void loadWebNews() {
        if(!NetworkStatus.isInternetAvailable()){
            getViewState().showError(Messages.getErrorNoInternetConnection());
        } else {
            getViewState().loadWebNews(articles);
        }

    }

    public void onBackPressed() {
        router.exit();
    }
}
