package silantyevmn.ru.mynews.ui.popup;

import android.view.View;

import es.dmoral.toasty.Toasty;


public class PopupDialogToasty implements PopupDialogMessage {
    private int duration = 0; //длительность показа
    private boolean withicon = true; //показать иконку

    public void error(View view, String message) {
        Toasty.error(view.getContext(), message, duration, withicon).show();
    }

    @Override
    public void into(View view, String message) {
        Toasty.info(view.getContext(), message, duration, withicon).show();
    }

    @Override
    public void onSuccess(View view, String message) {
        Toasty.success(view.getContext(), message, duration, withicon).show();
    }
}
