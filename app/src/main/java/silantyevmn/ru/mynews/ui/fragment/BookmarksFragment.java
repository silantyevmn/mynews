package silantyevmn.ru.mynews.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import ru.terrakok.cicerone.Router;
import silantyevmn.ru.mynews.App;
import silantyevmn.ru.mynews.R;
import silantyevmn.ru.mynews.model.repo.Repo;
import silantyevmn.ru.mynews.presenter.BookmarksPresenter;
import silantyevmn.ru.mynews.ui.activity.StartActivity;
import silantyevmn.ru.mynews.ui.adapter.RecyclerAdapter;
import silantyevmn.ru.mynews.ui.common.BackButtonListener;
import silantyevmn.ru.mynews.ui.image.ImageLoader;
import silantyevmn.ru.mynews.ui.popup.PopupDialogMessage;
import silantyevmn.ru.mynews.ui.view.BookmarksView;

public class BookmarksFragment extends MvpAppCompatFragment implements BookmarksView, BackButtonListener,SwipeRefreshLayout.OnRefreshListener {
    private RecyclerAdapter adapter;
    private TextView bookmarksTextView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Inject
    PopupDialogMessage popupWindow;

    @Inject
    Repo repo;

    @Inject
    Router router;

    @Inject
    ImageLoader imageLoader;

    @InjectPresenter
    BookmarksPresenter presenter;

    public static BookmarksFragment getNewInstance() {
        return new BookmarksFragment();
    }

    @ProvidePresenter
    public BookmarksPresenter provideGeneralPresenter() {
        return new BookmarksPresenter(AndroidSchedulers.mainThread(), router, repo);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        App.getInstance().getComponent().inject(this);
        super.onCreate(savedInstanceState);
        //throw new RuntimeException("test fabric.io");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.bookmarks_recycler);
        bookmarksTextView = view.findViewById(R.id.bookmarks_text_view);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerAdapter(presenter, imageLoader);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void updateList() {
        adapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showHeadpiece() {
        bookmarksTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideHeadpiece() {
        bookmarksTextView.setVisibility(View.GONE);
    }

    @Override
    public void showSuccess(String message) {
        popupWindow.onSuccess(getView(), message);
    }

    @Override
    public void showLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void showError(String text) {
        popupWindow.error(getView(), text);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        presenter.loadBookmarks();
    }


    @Override
    public void onResume() {
        super.onResume();
        ((StartActivity) getActivity()).initToolbar(getString(R.string.title_bookmarks));
        presenter.loadBookmarks();
    }

    @Override
    public boolean onBackPressed() {
        presenter.onBackPressed();
        return true;
    }
}