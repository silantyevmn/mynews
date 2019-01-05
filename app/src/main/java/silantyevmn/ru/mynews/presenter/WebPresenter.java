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
import silantyevmn.ru.mynews.utils.PopupClass;

@InjectViewState
public class WebPresenter extends MvpPresenter<WebNewsView> {
    private Articles articles;
    private Repo repo;
    private Scheduler scheduler;
    private PopupClass pop;

    public WebPresenter(Scheduler scheduler, Repo repo, PopupClass pop, Articles articles) {
        this.scheduler = scheduler;
        this.repo = repo;
        this.articles = articles;
        this.pop = pop;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().init(articles);
    }

    public void loadWebNews() {
        //проверим избранное и отобразим значок
        repo.findBookmark(articles)
                .subscribeOn(Schedulers.io())
                .observeOn(scheduler)
                .subscribe(isBookmark -> {
                    getViewState().updateMenuItemBookmarkIcon(isBookmark);
                }, throwable -> getViewState().showError(Messages.getErrorStatusBookmark()));
        if (!NetworkStatus.isInternetAvailable()) {
            getViewState().showError(Messages.getErrorNoInternetConnection());
        } else {
            getViewState().loadWebNews(articles);
        }
    }

    public void share() {
        if (articles != null) {
            pop.share(articles);
        }
    }

    public void updateBookmark() {
        repo.updateBookmark(articles)
                .subscribeOn(Schedulers.io())
                .observeOn(scheduler)
                .subscribe(isBookmark -> {
                    getViewState().updateMenuItemBookmarkIcon(isBookmark);
                    if (isBookmark) {
                        getViewState().showSuccess(Messages.getBookmarkSuccessAdd());
                    } else {
                        getViewState().showSuccess(Messages.getBookmarkSuccessRemove());
                    }
                }, throwable -> getViewState().showError(Messages.getErrorUpdateBookmark()));
    }

    public void openInBrowser() {
        if (articles != null) {
            pop.openInBrowser(articles);
        }
    }


    public void copy() {
        pop.copy(articles);
        getViewState().showSuccess(Messages.getTextLinkCopied());
    }
}
