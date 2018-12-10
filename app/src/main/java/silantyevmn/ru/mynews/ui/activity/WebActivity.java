package silantyevmn.ru.mynews.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import silantyevmn.ru.mynews.App;
import silantyevmn.ru.mynews.R;
import silantyevmn.ru.mynews.model.entity.Articles;
import silantyevmn.ru.mynews.model.repo.Repo;
import silantyevmn.ru.mynews.presenter.WebPresenter;
import silantyevmn.ru.mynews.ui.popup.PopupDialogMessage;
import silantyevmn.ru.mynews.ui.view.WebNewsView;

public class WebActivity extends MvpAppCompatActivity implements WebNewsView {
    public static final String KEY_WEB = "key_web";
    private WebView webViewNews;
    private Toolbar webToolbar;
    private MenuItem bookmarkMenuItem;

    @Inject
    PopupDialogMessage popupWindow;

    @InjectPresenter
    WebPresenter presenter;

    @Inject
    Repo repo;

    @ProvidePresenter
    public WebPresenter provideGeneralPresenter() {
        return new WebPresenter(AndroidSchedulers.mainThread(), repo, (Articles) getIntent().getSerializableExtra(KEY_WEB));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        App.getInstance().getComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        webToolbar = findViewById(R.id.toolbar);
        //init WebView
        webViewNews = findViewById(R.id.news_web_view);
        //webViewNews.getSettings().setJavaScriptEnabled(true);
        webViewNews.getSettings().setLoadWithOverviewMode(true);
        webViewNews.getSettings().setUseWideViewPort(true);
        webViewNews.getSettings().setBuiltInZoomControls(true);
    }

    @Override
    public void init(Articles articles) {
        webToolbar.setTitle(articles.getSource().getName());
        webToolbar.inflateMenu(R.menu.menu_web);
        webToolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);
        webToolbar.setNavigationOnClickListener(click -> {
            onBackPressed();
        });
        bookmarkMenuItem = webToolbar.getMenu().findItem(R.id.web_menu_bookmark);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.web_menu_share: {
                presenter.share();
                return true;
            }
            case R.id.web_menu_bookmark: {
                presenter.updateBookmark();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void loadWebNews(Articles articles) {
        webViewNews.setWebViewClient(new WebViewClient());
        webViewNews.loadUrl(articles.getUrl());
    }

    @Override
    public void updateMenuItemBookmarkIcon(Boolean isFavorite) {
        if (isFavorite) {
            bookmarkMenuItem.setIcon(R.drawable.ic_bookmark_black_24dp);
            bookmarkMenuItem.setTitle(R.string.bookmark_remove);
        } else {
            bookmarkMenuItem.setIcon(R.drawable.ic_bookmark_border_black_24dp);
            bookmarkMenuItem.setTitle(R.string.bookmark_add);
        }
    }

    @Override
    public void showError(String errorString) {
        popupWindow.error(webToolbar.getRootView(), errorString);
    }

    @Override
    public void onSuccess(String text) {
        popupWindow.onSuccess(webToolbar.getRootView(), text);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.loadWebNews();
    }

    @Override
    public void onDestroy() {
        if (webViewNews != null)
            webViewNews.destroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (webViewNews.canGoBack()) {
            webViewNews.goBack();
            return;
        } else {
            super.onBackPressed();
        }

    }
}