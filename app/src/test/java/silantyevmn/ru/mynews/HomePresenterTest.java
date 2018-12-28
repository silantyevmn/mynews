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
import silantyevmn.ru.mynews.presenter.HomePresenter;
import silantyevmn.ru.mynews.ui.view.HomeView;
import silantyevmn.ru.mynews.utils.Messages;
import silantyevmn.ru.mynews.utils.NetworkStatus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({NetworkStatus.class, Messages.class})
public class HomePresenterTest {
    private TestScheduler scheduler;
    private Router router;
    private Repo repo;
    private HomeView view;
    private News news;
    private String error = "error";
    private String success = "success";

    @Before
    public void init() {
        scheduler = new TestScheduler();
        router = mock(Router.class);
        repo = mock(Repo.class);
        view = mock(HomeView.class);
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
    public void testConstHomePresenter() {
        HomePresenter presenter = new HomePresenter(scheduler, router, repo);
        assertNotNull(presenter);
    }

    @Test
    public void testPresenterInternetOnline() throws Exception {
        when(repo.getTopNews()).thenReturn(Observable.just(news));

        HomePresenter presenter = spy(new HomePresenter(scheduler, router, repo));
        presenter.attachView(view);

        PowerMockito.mockStatic(NetworkStatus.class);
        PowerMockito.when(NetworkStatus.isInternetAvailable()).thenReturn(true);

        presenter.loadNews();
        verify(view).showLoading();
        Thread.sleep(100);
        scheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        verify(view).updateList();
    }

    @Test
    public void testPresenterInternetOnlineApiError() throws Exception {
        when(repo.getTopNews()).thenReturn(Observable.error(new RuntimeException()));

        HomePresenter presenter = spy(new HomePresenter(scheduler, router, repo));
        presenter.attachView(view);

        PowerMockito.mockStatic(NetworkStatus.class);
        PowerMockito.when(NetworkStatus.isInternetAvailable()).thenReturn(true);

        PowerMockito.mockStatic(Messages.class);
        PowerMockito.when(Messages.getErrorLoadNetwork()).thenReturn(error);
        presenter.loadNews();

        verify(view).showLoading();
        Thread.sleep(100);
        scheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        verify(view).showError(error);
    }

    @Test
    public void testPresenterInternetOffline() {
        when(repo.getTopNews()).thenReturn(Observable.just(news));
        HomePresenter presenter = spy(new HomePresenter(scheduler, router, repo));
        presenter.attachView(view);

        PowerMockito.mockStatic(NetworkStatus.class);
        PowerMockito.when(NetworkStatus.isInternetAvailable()).thenReturn(false);

        PowerMockito.mockStatic(Messages.class);
        PowerMockito.when(Messages.getErrorNoInternetConnection()).thenReturn(error);
        presenter.loadNews();
        verify(view,times(1)).showError(error);
    }

    @Test
    public void startWebViewTest() {
        HomePresenter presenter = spy(new HomePresenter(scheduler, router, repo));
        presenter.startWebView(news.getArticles().get(0));
        verify(presenter).startWebView(news.getArticles().get(0));
    }

    @Test
    public void getArticlesListTest() {
        HomePresenter presenter = spy(new HomePresenter(scheduler, router, repo));
        when(presenter.getArticlesList()).thenReturn(news.getArticles());
        assertEquals(presenter.getArticlesList(), news.getArticles());
    }

    @Test
    public void updateStatusBookmarksTest() {
        HomePresenter presenter = spy(new HomePresenter(scheduler, router, repo));
        presenter.updateStatusBookmarks();
        verify(presenter).updateStatusBookmarks();
    }

    @Test
    public void showSuccessTest() {
        HomePresenter presenter = spy(new HomePresenter(scheduler, router, repo));
        presenter.attachView(view);
        presenter.showSuccess(success);
        verify(view).showSuccess(success);
    }

    @Test
    public void showErrorTest() {
        HomePresenter presenter = spy(new HomePresenter(scheduler, router, repo));
        presenter.attachView(view);
        presenter.showError(error);
        verify(view).showError(error);
    }

    @Test
    public void onBackPressedTest() {
        HomePresenter presenter = spy(new HomePresenter(scheduler, router, repo));
        presenter.attachView(view);
        presenter.onBackPressed();
        verify(router).exit();
    }

    @Test
    public void testRepo() {
        when(repo.getTopNews()).thenReturn(Observable.just(news));
        repo.getTopNews()
                .subscribe(newsList -> {
                    assertEquals(newsList, news);
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
