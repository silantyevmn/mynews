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
import silantyevmn.ru.mynews.presenter.CategoryPagePresenter;
import silantyevmn.ru.mynews.ui.view.CategoryPageView;
import silantyevmn.ru.mynews.utils.Messages;
import silantyevmn.ru.mynews.utils.NetworkStatus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({NetworkStatus.class, Messages.class})
public class CategoryPagePresenterTest {
    private TestScheduler scheduler;
    private Router router;
    private Repo repo;
    private CategoryPagePresenter presenter;
    private CategoryPageView view;
    private News news;
    private String error = "error";
    private String success = "success";
    private int position = 1;

    @Before
    public void init() {
        scheduler = new TestScheduler();
        router = mock(Router.class);
        repo = mock(Repo.class);
        presenter = spy(new CategoryPagePresenter(scheduler, router, repo, position));
        view = mock(CategoryPageView.class);
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
        presenter = new CategoryPagePresenter(scheduler, router, repo, position);
        assertNotNull(presenter);
    }

    @Test
    public void getArticlesListTest() {
        when(presenter.getArticlesList()).thenReturn(news.getArticles());
        assertEquals(presenter.getArticlesList(), news.getArticles());
    }

    @Test
    public void loadNewsInternetOnlineTest() throws Exception {
        PowerMockito.mockStatic(NetworkStatus.class);
        PowerMockito.when(NetworkStatus.isInternetAvailable()).thenReturn(true);

        String messagesText="top";
        PowerMockito.mockStatic(Messages.class);
        PowerMockito.when(Messages.getTitleCategory(position)).thenReturn(messagesText);

        when(repo.getCategoryNews(anyString())).thenReturn(Observable.just(news));

        presenter.loadNews();
        verify(view).showLoading();

        Thread.sleep(100);
        scheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        verify(view).hideHeadpiece();
        verify(view).updateList();
    }

    @Test
    public void loadNewsInternetOfflineTest() throws Exception {
        PowerMockito.mockStatic(NetworkStatus.class);
        PowerMockito.when(NetworkStatus.isInternetAvailable()).thenReturn(false);

        String messagesText="error";
        PowerMockito.mockStatic(Messages.class);
        PowerMockito.when(Messages.getErrorNoInternetConnection()).thenReturn(messagesText);

        presenter.loadNews();
        verify(view).showError(messagesText);
    }

    @Test
    public void loadNewsRepoErrorTest() throws Exception {
        PowerMockito.mockStatic(NetworkStatus.class);
        PowerMockito.when(NetworkStatus.isInternetAvailable()).thenReturn(true);

        String messagesText="top";
        PowerMockito.mockStatic(Messages.class);
        PowerMockito.when(Messages.getTitleCategory(position)).thenReturn(messagesText);
        PowerMockito.when(Messages.getErrorLoadNetwork()).thenReturn(error);

        when(repo.getCategoryNews(anyString())).thenReturn(Observable.error(new RuntimeException()));

        presenter.loadNews();
        verify(view).showLoading();

        Thread.sleep(100);
        scheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        verify(view).showError(error);
    }

    @Test
    public void loadNewsRepoGetArticlesZeroTest() throws Exception {
        PowerMockito.mockStatic(NetworkStatus.class);
        PowerMockito.when(NetworkStatus.isInternetAvailable()).thenReturn(true);

        String messagesText="top";
        PowerMockito.mockStatic(Messages.class);
        PowerMockito.when(Messages.getTitleCategory(position)).thenReturn(messagesText);
        News tempNews=new News("ok","0",new ArrayList<>());
        when(repo.getCategoryNews(anyString())).thenReturn(Observable.just(tempNews));

        presenter.loadNews();
        verify(view).showLoading();

        Thread.sleep(100);
        scheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        verify(view).showHeadpiece();
        verify(view).updateList();
    }

    @Test
    public void startWebViewTest() {
        presenter.startWebView(news.getArticles().get(0));
        verify(presenter).startWebView(news.getArticles().get(0));
    }

    @Test
    public void updateStatusBookmarksTest(){
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
        when(repo.getCategoryNews("top")).thenReturn(Observable.just(news));
        repo.getCategoryNews("top")
                .subscribe(tempNews -> {
                    assertEquals(tempNews, news);
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
