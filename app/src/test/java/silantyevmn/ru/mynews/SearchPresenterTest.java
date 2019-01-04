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
import silantyevmn.ru.mynews.presenter.SearchPresenter;
import silantyevmn.ru.mynews.ui.view.SearchNewsView;
import silantyevmn.ru.mynews.utils.Messages;
import silantyevmn.ru.mynews.utils.NetworkStatus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({NetworkStatus.class,Messages.class})
public class SearchPresenterTest {
    private TestScheduler scheduler;
    private Router router;
    private Repo repo;
    private SearchPresenter presenter;
    private SearchNewsView view;
    private News news;
    private String error = "error";
    private String success = "success";
    private String query = "putin";

    @Before
    public void init() {
        scheduler = new TestScheduler();
        router = mock(Router.class);
        repo = mock(Repo.class);
        presenter = spy(new SearchPresenter(scheduler, router, repo, query));
        view = mock(SearchNewsView.class);
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
    public void testConstructorPresenter() {
        SearchPresenter presenter = new SearchPresenter(scheduler, router, repo, query);
        assertNotNull(presenter);
    }

    @Test
    public void testPresenterLoadRepo() throws Exception {
        when(repo.getSearchNews(query)).thenReturn(Observable.just(news));

        PowerMockito.mockStatic(NetworkStatus.class);
        PowerMockito.when(NetworkStatus.isInternetAvailable()).thenReturn(true);

        presenter.loadSearchNews();
        verify(view).showLoading();

        Thread.sleep(100);
        scheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        verify(view).updateList();
    }

    @Test
    public void testPresenterLoadRepoListSizeZero() throws Exception {
        when(repo.getSearchNews(query)).thenReturn(Observable.just(new News("ok",
                "1" ,new ArrayList<>())));

        PowerMockito.mockStatic(NetworkStatus.class);
        PowerMockito.when(NetworkStatus.isInternetAvailable()).thenReturn(true);

        presenter.loadSearchNews();
        verify(view).showLoading();

        String text="list size 0";
        PowerMockito.mockStatic(Messages.class);
        PowerMockito.when(Messages.getNoNewsFound()).thenReturn(text);

        Thread.sleep(100);
        scheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        verify(view).showInfo(text);
        verify(view).updateList();
    }

    @Test
    public void testPresenterLoadRepoErrorNoInternetConnection() {
        PowerMockito.mockStatic(NetworkStatus.class);
        PowerMockito.when(NetworkStatus.isInternetAvailable()).thenReturn(false);

        PowerMockito.mockStatic(Messages.class);
        PowerMockito.when(Messages.getErrorNoInternetConnection()).thenReturn(error);
        presenter.loadSearchNews();

        verify(view).showError(error);
    }

    @Test
    public void testPresenterLoadRepoErrorException() throws Exception{
        when(repo.getSearchNews(query)).thenReturn(Observable.error(new RuntimeException("")));
        PowerMockito.mockStatic(NetworkStatus.class);
        PowerMockito.when(NetworkStatus.isInternetAvailable()).thenReturn(true);

        PowerMockito.mockStatic(Messages.class);
        PowerMockito.when(Messages.getErrorLoadNetwork()).thenReturn(error);
        presenter.loadSearchNews();
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
    public void updateStatusBookmarksTest()  {
        when(repo.getBookmarksList()).thenReturn(Observable.just(news.getArticles()));
        presenter.updateStatusBookmarks();
        verify(presenter).updateStatusBookmarks();
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
        when(repo.getSearchNews(query)).thenReturn(Observable.just(news));
        repo.getSearchNews(query)
                .subscribe(tempNews -> {
                    assertEquals(tempNews, news);
                }, throwable -> {
                    assertEquals(true, false);//red test
                });
    }

    @Test
    public void testRepoError() {
        when(repo.getSearchNews(query)).thenReturn(Observable.error(new RuntimeException(error)));
        repo.getSearchNews(query)
                .subscribe(news -> {
                    assertEquals(true, false); //red test
                }, throwable -> {
                    assertEquals(error, throwable.getMessage());
                });
    }
}
