package silantyevmn.ru.mynews.presenter;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import ru.terrakok.cicerone.Router;
import silantyevmn.ru.mynews.model.entity.Articles;
import silantyevmn.ru.mynews.model.repo.Repo;
import silantyevmn.ru.mynews.ui.Screens;
import silantyevmn.ru.mynews.ui.adapter.IAdapter;
import silantyevmn.ru.mynews.ui.view.CategoryView;
import silantyevmn.ru.mynews.utils.Messages;
import silantyevmn.ru.mynews.utils.NetworkStatus;

@InjectViewState
public class CategoryPresenter extends MvpPresenter<CategoryView> implements IAdapter {

    private Scheduler scheduler;
    private Router router;
    private Repo repo;
    private List<Articles> articlesList = new ArrayList<>();

    public List<Articles> getArticlesList() {
        return articlesList;
    }

    @SuppressLint("CheckResult")
    public CategoryPresenter(Scheduler scheduler, Router router, Repo repo) {
        this.scheduler = scheduler;
        this.router = router;
        this.repo = repo;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().init();
    }

    public void loadNews(int position) {
        if (!NetworkStatus.isInternetAvailable()) {
            getViewState().showError(Messages.getErrorNoInternetConnection());
            return;
        }
        String category= Messages.getTitleCategory(position);
        repo.getCategoryNews(category)
                .subscribeOn(Schedulers.io())
                .observeOn(scheduler)
                .subscribe(news -> {
                    this.articlesList = news.getArticles();
                    if (news.getArticles().size() == 0) {
                        getViewState().showHeadpiece();//покажем заставку
                    } else {
                        getViewState().hideHeadpiece();//скроем заставку
                    }
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

    public void onTabSelected(int position) {
        loadNews(position);
    }
}
