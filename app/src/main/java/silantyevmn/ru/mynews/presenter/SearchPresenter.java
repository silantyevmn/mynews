package silantyevmn.ru.mynews.presenter;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import ru.terrakok.cicerone.Router;
import silantyevmn.ru.mynews.R;
import silantyevmn.ru.mynews.model.entity.Articles;
import silantyevmn.ru.mynews.model.repo.Repo;
import silantyevmn.ru.mynews.ui.Screens;
import silantyevmn.ru.mynews.ui.adapter.IAdapter;
import silantyevmn.ru.mynews.ui.view.SearchNewsView;
import silantyevmn.ru.mynews.utils.Messages;
import silantyevmn.ru.mynews.utils.NetworkStatus;

@InjectViewState
public class SearchPresenter extends MvpPresenter<SearchNewsView> implements IAdapter {

    private Scheduler scheduler;
    private Router router;
    private Repo repo;
    private List<Articles> articlesList = new ArrayList<>();
    private String query;

    public List<Articles> getArticlesList() {
        return articlesList;
    }

    @SuppressLint("CheckResult")
    public SearchPresenter(Scheduler scheduler, Router router, Repo repo, String query) {
        this.scheduler = scheduler;
        this.router = router;
        this.repo = repo;
        this.query = query;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().init();
    }

    public void loadSearchNews() {
        if (!NetworkStatus.isInternetAvailable()) {
            getViewState().showError(Messages.getErrorNoInternetConnection());
            return;
        }
        getViewState().showLoading();
        repo.getSearchNews(query)
                .subscribeOn(Schedulers.io())
                .observeOn(scheduler)
                .subscribe(news -> {
                    this.articlesList = news.getArticles();
                    if(news.getArticles().size()==0) getViewState().showInfo(Messages.getNoNewsFound());
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
    public void showSuccess(String message) {
        getViewState().showSuccess(message);
    }

    @Override
    public void showError(String message) {
        getViewState().showError(message);
    }

    public void onBackPressed() {
        router.exit();
    }
}
