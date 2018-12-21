package silantyevmn.ru.mynews.ui.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface CategoryPageView extends MvpView {

    void updateList();

    @StateStrategyType(SkipStrategy.class)
    void showError(String text);

    @StateStrategyType(SkipStrategy.class)
    void showSuccess(String message);

    void showHeadpiece();

    void hideHeadpiece();

    void showLoading();
}
