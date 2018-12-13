package silantyevmn.ru.mynews.ui.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndStrategy.class)
public interface BookmarksView extends MvpView {
    void init();

    void updateList();

    @StateStrategyType(SkipStrategy.class)
    void showError(String text);

    void showHeadpiece();

    void hideHeadpiece();

    @StateStrategyType(SkipStrategy.class)
    void showSuccess(String message);
}
