package silantyevmn.ru.mynews.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import io.reactivex.Scheduler;
import silantyevmn.ru.mynews.model.entity.Articles;
import silantyevmn.ru.mynews.model.repo.Repo;
import silantyevmn.ru.mynews.utils.PopupClass;
import silantyevmn.ru.mynews.ui.adapter.IBookmark;
import silantyevmn.ru.mynews.ui.view.WebNewsView;
import silantyevmn.ru.mynews.utils.Messages;
import silantyevmn.ru.mynews.utils.NetworkStatus;

@InjectViewState
public class WebPresenter extends MvpPresenter<WebNewsView> {
    private Articles articles;
    private Repo repo;
    private Scheduler scheduler;
    private PopupClass pop;

    public WebPresenter(Scheduler scheduler, Repo repo, Articles articles) {
        this.scheduler = scheduler;
        this.repo = repo;
        this.articles = articles;
        this.pop = new PopupClass();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().init(articles);
    }

    public void loadWebNews() {
        //проверим избранное и отобразим значок
        pop.getStatusBookmark(articles, new IBookmark() {
            @Override
            public void onSuccess(boolean isBookmark) {
                getViewState().updateMenuItemBookmarkIcon(isBookmark);
            }

            @Override
            public void onError(String message) {
                getViewState().showError(message);
            }
        });
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
        pop.updateBookmark(articles, new IBookmark() {
            @Override
            public void onSuccess(boolean isBookmark) {
                getViewState().updateMenuItemBookmarkIcon(isBookmark);
                if (isBookmark) {
                    getViewState().showSuccess(Messages.getBookmarkSuccessAdd());
                } else {
                    getViewState().showSuccess(Messages.getBookmarkSuccessRemove());
                }
            }

            @Override
            public void onError(String message) {
                getViewState().showError(message);
            }
        });
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
