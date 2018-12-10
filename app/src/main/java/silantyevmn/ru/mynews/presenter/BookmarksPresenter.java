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
import silantyevmn.ru.mynews.ui.view.BookmarksView;
import silantyevmn.ru.mynews.utils.Messages;
import silantyevmn.ru.mynews.utils.NetworkStatus;

@InjectViewState
public class BookmarksPresenter extends MvpPresenter<BookmarksView> implements IAdapter {

    private Scheduler scheduler;
    private Router router;
    private Repo repo;
    private List<Articles> articlesList = new ArrayList<>();

    public List<Articles> getArticlesList() {
        return articlesList;
    }

    @SuppressLint("CheckResult")
    public BookmarksPresenter(Scheduler scheduler, Router router, Repo repo) {
        this.scheduler = scheduler;
        this.router = router;
        this.repo = repo;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().init();
    }

    public void loadBookmarks() {
        //загрузка списка закладок
        //если лист пустой то показываем заставку, иначе список
        repo.getBookmarksList()
                .subscribeOn(Schedulers.io())
                .observeOn(scheduler)
                .subscribe(favoriteList -> {
                    articlesList = favoriteList;
                    if (favoriteList.size() == 0) {
                        getViewState().showHeadpiece();//покажем заставку
                    } else {
                        getViewState().hideHeadpiece();//скроем заставку
                    }
                    getViewState().updateList();
                }, throwable -> {
                    getViewState().showError(Messages.getErrorLoadCache());
                });
    }

    @Override
    public void startWebView(Articles articles) {
        if (!NetworkStatus.isInternetAvailable()) {
            getViewState().showError(Messages.getErrorNoInternetConnection());
            return;
        }
        router.navigateTo(new Screens.WebScreen(articles));
    }

    public void onBackPressed() {
        router.exit();
    }
}
