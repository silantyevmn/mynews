package silantyevmn.ru.mynews.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import silantyevmn.ru.mynews.model.entity.Articles;
import silantyevmn.ru.mynews.model.repo.Repo;
import silantyevmn.ru.mynews.ui.view.WebNewsView;
import silantyevmn.ru.mynews.utils.Messages;
import silantyevmn.ru.mynews.utils.NetworkStatus;
import silantyevmn.ru.mynews.utils.PopurManager;

@InjectViewState
public class WebPresenter extends MvpPresenter<WebNewsView> {
    private Articles articles;
    private Repo repo;
    private Scheduler scheduler;

    public WebPresenter(Scheduler scheduler, Repo repo, Articles articles) {
        this.scheduler = scheduler;
        this.repo = repo;
        this.articles = articles;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().init(articles);
    }

    public void share() {
        if (articles != null) {
            PopurManager.share(articles);
        }
    }

    public void loadWebNews() {
        //проверим избранное и отобразим значок
        repo.findBookmark(articles)
                .subscribeOn(Schedulers.io())
                .observeOn(scheduler)
                .subscribe(isBookmark -> {
                    getViewState().updateMenuItemBookmarkIcon(isBookmark);
                }, throwable -> {
                    getViewState().showError(throwable.getMessage());
                });

        if (!NetworkStatus.isInternetAvailable()) {
            getViewState().showError(Messages.getErrorNoInternetConnection());
        } else {
            getViewState().loadWebNews(articles);
        }

    }

    public void updateBookmark() {
        if (!NetworkStatus.isInternetAvailable()) {
            getViewState().showError(Messages.getErrorNoInternetConnection());
        } else {
            repo.updateBookmark(articles)
                    .subscribeOn(Schedulers.io())
                    .observeOn(scheduler)
                    .subscribe(isBookmark -> {
                                getViewState().onSuccess("Закладка успешно обновлена.");
                                getViewState().updateMenuItemBookmarkIcon(isBookmark);
                            },
                            throwable -> getViewState().showError("Ошибка! Избранное не обновлено!"));
        }
    }
}
