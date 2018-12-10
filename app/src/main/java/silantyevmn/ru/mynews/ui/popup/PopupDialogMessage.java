package silantyevmn.ru.mynews.ui.popup;

import android.support.annotation.NonNull;
import android.view.View;


public interface PopupDialogMessage {
    void error(@NonNull View view, String message);

    void into(@NonNull View view, String message);

    void onSuccess(@NonNull View view, String message);
}
