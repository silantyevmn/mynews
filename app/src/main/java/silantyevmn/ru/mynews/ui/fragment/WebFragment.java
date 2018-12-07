package silantyevmn.ru.mynews.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import ru.terrakok.cicerone.Router;
import silantyevmn.ru.mynews.App;
import silantyevmn.ru.mynews.R;
import silantyevmn.ru.mynews.model.entity.Articles;
import silantyevmn.ru.mynews.ui.popup.PopupDialogMessage;
import silantyevmn.ru.mynews.presenter.WebPresenter;
import silantyevmn.ru.mynews.ui.common.BackButtonListener;
import silantyevmn.ru.mynews.ui.view.WebNewsView;

public class WebFragment extends MvpAppCompatFragment implements WebNewsView, BackButtonListener {
    private static final String KEY_WEB = "key_web";
    private WebView webViewNews;
    private Toolbar webToolbar;

    @Inject
    PopupDialogMessage popupWindow;

    @Inject
    Router router;

    @InjectPresenter
    WebPresenter presenter;

    public static WebFragment getNewInstance(Articles articles) {
        WebFragment fragment = new WebFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_WEB, articles);
        fragment.setArguments(bundle);
        return fragment;
    }

    @ProvidePresenter
    public WebPresenter provideGeneralPresenter() {
        return new WebPresenter(AndroidSchedulers.mainThread(), router, (Articles) getArguments().getSerializable(KEY_WEB));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        App.getInstance().getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web, container, false);
        //ButterKnife.bind(this, view);
        webToolbar = view.findViewById(R.id.web_toolbar);
        //init WebView
        webViewNews = view.findViewById(R.id.news_web_view);
        //webViewNews.getSettings().setJavaScriptEnabled(true);
        webViewNews.getSettings().setLoadWithOverviewMode(true);
        webViewNews.getSettings().setUseWideViewPort(true);
        webViewNews.getSettings().setBuiltInZoomControls(true);
        return view;
    }

    @Override
    public void init(Articles articles) {
        TextView titleWebToolbar = webToolbar.findViewById(R.id.webToolbarTitle);
        titleWebToolbar.setText(articles.getSource().getName());

        webToolbar.inflateMenu(R.menu.menu_web);
        webToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.web_menu_share: {
                    presenter.share();
                    return true;
                }
            }
            return false;
        });

        webToolbar.setNavigationOnClickListener(click -> {
            onBackPressed();
        });

    }

    @Override
    public void loadWebNews(Articles articles) {
        webViewNews.setWebViewClient(new WebViewClient());
        webViewNews.loadUrl(articles.getUrl());
    }

    @Override
    public void showError(String errorString) {
        popupWindow.error(getView(),errorString+"_web_fragment");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().findViewById(R.id.toolbar).setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        if (webViewNews != null)
            webViewNews.destroy();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.loadWebNews();
    }

    @Override
    public boolean onBackPressed() {
        if (webViewNews.canGoBack()) {
            webViewNews.goBack();
            return false;
        } else {
            if (webToolbar != null) {
                webToolbar.setVisibility(View.GONE);
            }
            getActivity().findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
            presenter.onBackPressed();
            return true;
        }

    }
}