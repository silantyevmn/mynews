package silantyevmn.ru.mynews.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import ru.terrakok.cicerone.Router;
import silantyevmn.ru.mynews.ui.view.CategoryView;

@InjectViewState
public class CategoryPresenter extends MvpPresenter<CategoryView> {
    private Router router;

    public CategoryPresenter(Router router) {
        this.router = router;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    public void onBackPressed() {
        router.exit();
    }
}
