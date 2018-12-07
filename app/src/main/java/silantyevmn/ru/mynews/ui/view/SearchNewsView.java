package silantyevmn.ru.mynews.ui.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface SearchNewsView extends MvpView {
    void init();

    void updateList();

    @StateStrategyType(SkipStrategy.class)
    void showError(String text);

    void showMessage(String text);
}
