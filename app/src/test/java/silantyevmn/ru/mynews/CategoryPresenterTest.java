package silantyevmn.ru.mynews;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import ru.terrakok.cicerone.Router;
import silantyevmn.ru.mynews.presenter.CategoryPresenter;
import silantyevmn.ru.mynews.ui.view.CategoryView;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
public class CategoryPresenterTest {
    private CategoryPresenter presenter;
    private CategoryView view;
    private Router router;

    @Before
    public void init() {
        router = mock(Router.class);
        presenter = spy(new CategoryPresenter(router));
        view = mock(CategoryView.class);
        presenter.attachView(view);
    }

    @Test
    public void testConstructorPresenter() {
        presenter = new CategoryPresenter(router);
        assertNotNull(presenter);
    }

    @Test
    public void onBackPressedTest() {
        presenter.onBackPressed();
        verify(router).exit();
    }
}
