package silantyevmn.ru.mynews;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.TestScheduler;
import ru.terrakok.cicerone.Router;
import silantyevmn.ru.mynews.model.entity.Articles;
import silantyevmn.ru.mynews.model.entity.News;
import silantyevmn.ru.mynews.model.repo.Repo;
import silantyevmn.ru.mynews.presenter.BookmarksPresenter;
import silantyevmn.ru.mynews.ui.view.BookmarksView;
import silantyevmn.ru.mynews.utils.Messages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Messages.class})
public class BookmarksPresenterTest {
    private TestScheduler scheduler;
    private Router router;
    private Repo repo;
    private BookmarksPresenter presenter;
    private BookmarksView view;
    private News news;
    private String error = "error";
    private String success = "success";

    @Before
    public void init() {
        scheduler = new TestScheduler();
        router = mock(Router.class);
        repo = mock(Repo.class);
        presenter = spy(new BookmarksPresenter(scheduler, router, repo));
        view = mock(BookmarksView.class);
        presenter.attachView(view);

        List articles = new ArrayList<Articles>();
        articles.add(new Articles(
                null,
                "G20 - live: Macron and Saudi crown prince in extraordinary exchange over Khashoggi murder and Yemen, amid Trump-Putin meeting confusion - The Independent\n",
                null,
                "https://www.independent.co.uk/news/world/americas/g20-live-updates-trump-latest-russia-putin-ukraine-argentina-angela-merkel-a8660311.html",
                null,
                "2018-11-30T18:20:00Z",
                null));
        news = new News("ok", "1", articles);
    }

    @Test
    public void testConstBookmarksPresenter() {
        BookmarksPresenter presenter = new BookmarksPresenter(scheduler, router, repo);
        assertNotNull(presenter);
    }

    @Test
    public void testPresenterLoadBookmarks() throws Exception {
        when(repo.getBookmarksList()).thenReturn(Observable.just(news.getArticles()));

        presenter.loadBookmarks();
        verify(view).showLoading();

        Thread.sleep(100);
        scheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        verify(view).hideHeadpiece();
        verify(view).updateList();
    }

    @Test
    public void testPresenterLoadBookmarksListSizeZero() throws Exception {
        when(repo.getBookmarksList()).thenReturn(Observable.just(new ArrayList<>()));

        presenter.loadBookmarks();
        verify(view).showLoading();

        Thread.sleep(100);
        scheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        verify(view).showHeadpiece();
        verify(view).updateList();
    }

    @Test
    public void testPresenterLoadBookmarksErrorLoadCache() throws Exception {
        when(repo.getBookmarksList()).thenReturn(Observable.error(new RuntimeException()));

        PowerMockito.mockStatic(Messages.class);
        PowerMockito.when(Messages.getErrorLoadCache()).thenReturn(error);
        presenter.loadBookmarks();

        verify(view).showLoading();
        Thread.sleep(100);
        scheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        verify(view).showError(error);
    }

    @Test
    public void startWebViewTest() {
        presenter.startWebView(news.getArticles().get(0));
        verify(presenter).startWebView(news.getArticles().get(0));
    }

    @Test
    public void getArticlesListTest() {
        when(presenter.getArticlesList()).thenReturn(news.getArticles());
        assertEquals(presenter.getArticlesList(), news.getArticles());
    }

    @Test
    public void updateStatusBookmarksTest() throws Exception {
        when(repo.getBookmarksList()).thenReturn(Observable.just(news.getArticles()));
        presenter.updateStatusBookmarks();
        verify(presenter).loadBookmarks();
    }

    @Test
    public void showSuccessTest() {
        presenter.showSuccess(success);
        verify(view).showSuccess(success);
    }

    @Test
    public void showErrorTest() {
        presenter.showError(error);
        verify(view).showError(error);
    }

    @Test
    public void onBackPressedTest() {
        presenter.onBackPressed();
        verify(router).exit();
    }

    @Test
    public void testRepo() {
        when(repo.getBookmarksList()).thenReturn(Observable.just(news.getArticles()));
        repo.getBookmarksList()
                .subscribe(newsList -> {
                    assertEquals(newsList, news.getArticles());
                }, throwable -> {
                    assertEquals(true, false);//red test
                });
    }

    @Test
    public void testRepoError() {
        when(repo.getTopNews()).thenReturn(Observable.error(new RuntimeException(error)));
        repo.getTopNews()
                .subscribe(news -> {
                    assertEquals(true, false); //red test
                }, throwable -> {
                    assertEquals(error, throwable.getMessage());
                });
    }
}
