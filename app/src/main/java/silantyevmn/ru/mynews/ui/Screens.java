package silantyevmn.ru.mynews.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import ru.terrakok.cicerone.android.support.SupportAppScreen;
import silantyevmn.ru.mynews.model.entity.Articles;
import silantyevmn.ru.mynews.ui.activity.StartActivity;
import silantyevmn.ru.mynews.ui.activity.WebActivity;
import silantyevmn.ru.mynews.ui.fragment.BookmarksFragment;
import silantyevmn.ru.mynews.ui.fragment.CategoryFragment;
import silantyevmn.ru.mynews.ui.fragment.HomeFragment;
import silantyevmn.ru.mynews.ui.fragment.SearchFragment;

public class Screens {
    public static final class StartScreen extends SupportAppScreen {
        @Override
        public Intent getActivityIntent(Context context) {
            return new Intent(context, StartActivity.class);
        }
    }

    public static final class HomeScreen extends SupportAppScreen {
        public HomeScreen() {
        }

        @Override
        public Fragment getFragment() {
            return HomeFragment.getNewInstance();
        }
    }

    public static final class SearchScreen extends SupportAppScreen {
        private String query;

        public SearchScreen(String query) {
            this.query = query;
        }

        @Override
        public Fragment getFragment() {
            return SearchFragment.getNewInstance(query);
        }
    }

    public static final class WebScreen extends SupportAppScreen {
        private Articles articles;

        public WebScreen(Articles articles) {
            this.articles = articles;
        }

        @Override
        public Intent getActivityIntent(Context context) {
            Intent webIntent = new Intent(context, WebActivity.class);
            webIntent.putExtra(WebActivity.KEY_WEB, articles);
            return webIntent;
        }
    }

    public static final class BookmarksScreen extends SupportAppScreen {
        public BookmarksScreen() {
        }

        @Override
        public Fragment getFragment() {
            return BookmarksFragment.getNewInstance();
        }
    }

    public static final class CategoryScreen extends SupportAppScreen {
        public CategoryScreen() {
        }

        @Override
        public Fragment getFragment() {
            return CategoryFragment.getNewInstance();
        }
    }
}
