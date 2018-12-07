package silantyevmn.ru.mynews.ui.popup;

import android.view.View;


public interface PopupDialogMessage {
    void error(View view, String message);

    void into(View view, String message);
}
