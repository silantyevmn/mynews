package silantyevmn.ru.mynews.presenter;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import ru.terrakok.cicerone.Router;
import silantyevmn.ru.mynews.model.entity.Articles;
import silantyevmn.ru.mynews.model.repo.Repo;
import silantyevmn.ru.mynews.ui.Screens;
import silantyevmn.ru.mynews.ui.adapter.IAdapter;
import silantyevmn.ru.mynews.ui.view.HomeView;
import silantyevmn.ru.mynews.utils.Messages;
import silantyevmn.ru.mynews.utils.NetworkStatus;

@InjectViewState
public class HomePresenter extends MvpPresenter<HomeView> implements IAdapter {

    private Scheduler scheduler;
    private Router router;
    private Repo repo;
    private List<Articles> articlesList = new ArrayList<>();

    public List<Articles> getArticlesList() {
        return articlesList;
    }

    @SuppressLint("CheckResult")
    public HomePresenter(Scheduler scheduler, Router router, Repo repo) {
        this.scheduler = scheduler;
        this.router = router;
        this.repo = repo;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().init();
    }

    public void loadNews() {
        if (!NetworkStatus.isInternetAvailable()) {
            getViewState().showError(Messages.getErrorNoInternetConnection());
            return;
        }
        getViewState().showLoading();
        repo.getTopNews()
                .subscribeOn(Schedulers.io())
                .observeOn(scheduler)
                .subscribe(news -> {
                    this.articlesList = news.getArticles();
                    getViewState().updateList();
                }, throwable -> {
                    getViewState().showError(Messages.getErrorLoadNetwork());
                });
    }

    @Override
    public void startWebView(Articles articles) {
        router.navigateTo(new Screens.WebScreen(articles));
    }

    @Override
    public void updateStatusBookmarks() {

    }

    @Override
    public void showSuccess(String bookmarkRemoveOrAdd) {
        getViewState().showSuccess(bookmarkRemoveOrAdd);
    }

    @Override
    public void showError(String text) {
        getViewState().showError(text);
    }

    public void onBackPressed() {
        router.exit();
    }
}
