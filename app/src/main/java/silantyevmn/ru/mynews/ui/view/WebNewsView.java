package silantyevmn.ru.mynews.ui.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import silantyevmn.ru.mynews.model.entity.Articles;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface WebNewsView extends MvpView {
    void init(Articles articles);

    void loadWebNews(Articles articles);

    @StateStrategyType(SkipStrategy.class)
    void showError(String message);

    @StateStrategyType(SkipStrategy.class)
    void showSuccess(String message);

    void updateMenuItemBookmarkIcon(Boolean isFavorite);
}
