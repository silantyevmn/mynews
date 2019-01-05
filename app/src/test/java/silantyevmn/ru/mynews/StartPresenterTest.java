package silantyevmn.ru.mynews;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import ru.terrakok.cicerone.Router;
import ru.terrakok.cicerone.android.support.SupportAppScreen;
import silantyevmn.ru.mynews.presenter.StartPresenter;
import silantyevmn.ru.mynews.ui.Screens;
import silantyevmn.ru.mynews.ui.view.StartView;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
public class StartPresenterTest {
    private StartPresenter presenter;
    private StartView view;
    private Router router;

    @Before
    public void init() {
        router = mock(Router.class);
        presenter = spy(new StartPresenter(router));
        view = mock(StartView.class);
        presenter.attachView(view);
    }

    @Test
    public void testConstructorPresenter() {
        presenter = new StartPresenter(router);
        assertNotNull(presenter);
    }

    @Test
    public void testOnFirstViewAttach() {
        verify(view).init();
    }

    @Test
    public void onBackPressedTest() {
        presenter.onBackPressed();
        verify(router).exit();
    }

    @Test
    public void restartActivityTest() {
        presenter.restartActivity();
        verify(router).replaceScreen(any(Screens.StartScreen.class));
    }

    @Test
    public void homeScreenTest() {
        presenter.homeScreen("home");
        verify(presenter).replaceScreen(eq("home"), any(Screens.HomeScreen.class));
    }

    @Test
    public void searchNewsScreenTest() {
        presenter.searchNewsScreen("search");
        verify(presenter).navigationToScreen(eq("search"), any(Screens.SearchScreen.class));
    }

    @Test
    public void categoryScreenTest() {
        presenter.categoryScreen("category");
        verify(presenter).replaceScreen(eq("category"), any(Screens.CategoryScreen.class));
    }

    @Test
    public void bookmarksScreenTest() {
        presenter.bookmarksScreen("bookmarks");
        verify(presenter).replaceScreen(eq("bookmarks"), any(Screens.BookmarksScreen.class));
    }

    @Test
    public void replaceScreenTest() {
        String titleToolbar = "titleToolbar";
        SupportAppScreen supportAppScreen = mock(SupportAppScreen.class);
        presenter.replaceScreen(titleToolbar, supportAppScreen);
        verify(router).replaceScreen(supportAppScreen);
        verify(view).initToolbar(titleToolbar);
    }

    @Test
    public void navigationToScreenTest() {
        String titleToolbar = "titleToolbar";
        SupportAppScreen supportAppScreen = mock(SupportAppScreen.class);
        presenter.navigationToScreen(titleToolbar, supportAppScreen);
        verify(router).navigateTo(supportAppScreen);
        verify(view).initToolbar(titleToolbar);
    }


}
