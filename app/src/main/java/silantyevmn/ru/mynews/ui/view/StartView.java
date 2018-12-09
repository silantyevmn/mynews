package silantyevmn.ru.mynews.ui.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface StartView extends MvpView {
    void init();

    @StateStrategyType(SkipStrategy.class)
    void showError(String errorTextMessage);

    void initToolbar(String titleToolbar);
}
