package silantyevmn.ru.mynews;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.TestScheduler;
import silantyevmn.ru.mynews.model.entity.Articles;
import silantyevmn.ru.mynews.model.repo.Repo;
import silantyevmn.ru.mynews.presenter.WebPresenter;
import silantyevmn.ru.mynews.ui.view.WebNewsView;
import silantyevmn.ru.mynews.utils.Messages;
import silantyevmn.ru.mynews.utils.NetworkStatus;
import silantyevmn.ru.mynews.utils.PopupClass;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({NetworkStatus.class, Messages.class})
public class WebPresenterTest {
    private TestScheduler scheduler;
    private Repo repo;
    private WebPresenter presenter;
    private WebNewsView view;
    private String error = "error";
    private String success = "success";
    private PopupClass pop;
    private Articles articles;

    @Before
    public void init() {
        scheduler = new TestScheduler();
        pop = mock(PopupClass.class);
        repo = mock(Repo.class);

        articles = new Articles(
                null,
                "G20 - live: Macron and Saudi crown prince in extraordinary exchange over Khashoggi murder and Yemen, amid Trump-Putin meeting confusion - The Independent\n",
                null,
                "https://www.independent.co.uk/news/world/americas/g20-live-updates-trump-latest-russia-putin-ukraine-argentina-angela-merkel-a8660311.html",
                null,
                "2018-11-30T18:20:00Z",
                null);
        presenter = spy(new WebPresenter(scheduler, repo, pop, articles));
        view = mock(WebNewsView.class);
        presenter.attachView(view);
    }

    @Test
    public void testConstructorPresenter() {
        presenter = new WebPresenter(scheduler, repo, pop, articles);
        assertNotNull(presenter);
    }

    @Test
    public void testOnFirstViewAttach() {
        verify(view).init(articles);
    }

    @Test
    public void testLoadWebNewsInternetOnline() throws InterruptedException {
        PowerMockito.mockStatic(NetworkStatus.class);
        PowerMockito.when(NetworkStatus.isInternetAvailable()).thenReturn(true);
        when(repo.findBookmark(articles)).thenReturn(Observable.just(true));
        presenter.loadWebNews();
        verify(view).loadWebNews(articles);
    }

    @Test
    public void testLoadWebNewsInternetOffline() {
        //no internet
        PowerMockito.mockStatic(NetworkStatus.class);
        PowerMockito.when(NetworkStatus.isInternetAvailable()).thenReturn(false);
        String text = "no internet connection";
        PowerMockito.mockStatic(Messages.class);
        PowerMockito.when(Messages.getErrorNoInternetConnection()).thenReturn(text);
        when(repo.findBookmark(articles)).thenReturn(Observable.just(true));
        presenter.loadWebNews();
        verify(view).showError(text);
    }

    @Test
    public void testLoadWebNewsFindBookmark() throws InterruptedException {
        PowerMockito.mockStatic(NetworkStatus.class);
        PowerMockito.when(NetworkStatus.isInternetAvailable()).thenReturn(true);
        when(repo.findBookmark(articles)).thenReturn(Observable.just(true));
        presenter.loadWebNews();
        Thread.sleep(100);
        scheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        verify(view).updateMenuItemBookmarkIcon(true);
    }

    @Test
    public void testLoadWebNewsFindBookmarkError() throws InterruptedException {
        PowerMockito.mockStatic(NetworkStatus.class);
        PowerMockito.when(NetworkStatus.isInternetAvailable()).thenReturn(true);

        when(repo.findBookmark(articles)).thenReturn(Observable.error(new RuntimeException()));

        String textError="Error status bookmark";
        PowerMockito.mockStatic(Messages.class);
        PowerMockito.when(Messages.getErrorStatusBookmark()).thenReturn(textError);

        presenter.loadWebNews();
        Thread.sleep(100);
        scheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        verify(view).showError(textError);
    }

    @Test
    public void shareTest() {
        presenter.share();
        verify(pop).share(articles);
    }

    @Test
    public void updateBookmarkTestOnSuccessAdd() throws Exception {
        PowerMockito.mockStatic(Messages.class);
        PowerMockito.when(Messages.getBookmarkSuccessAdd()).thenReturn(success);

        when(repo.updateBookmark(articles)).thenReturn(Observable.just(true));
        presenter.updateBookmark();
        verify(repo).updateBookmark(articles);
        Thread.sleep(100);
        scheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        verify(view).updateMenuItemBookmarkIcon(true);
        verify(view).showSuccess(success);
    }

    @Test
    public void updateBookmarkTestOnSuccessRemove() throws Exception {
        PowerMockito.mockStatic(Messages.class);
        PowerMockito.when(Messages.getBookmarkSuccessRemove()).thenReturn(success);

        when(repo.updateBookmark(articles)).thenReturn(Observable.just(false));
        presenter.updateBookmark();
        verify(repo).updateBookmark(articles);
        Thread.sleep(100);
        scheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        verify(view).updateMenuItemBookmarkIcon(false);
        verify(view).showSuccess(success);
    }

    @Test
    public void openInBrowserTest() {
        presenter.openInBrowser();
        verify(pop).openInBrowser(articles);
    }

    @Test
    public void copyTest() {
        String text = "link copied";
        PowerMockito.mockStatic(Messages.class);
        PowerMockito.when(Messages.getTextLinkCopied()).thenReturn(text);

        presenter.copy();
        verify(pop).copy(articles);
        verify(view).showSuccess(text);
    }
}
