package silantyevmn.ru.mynews.ui.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import silantyevmn.ru.mynews.model.image.ImageLoader;
import silantyevmn.ru.mynews.model.repo.Repo;
import silantyevmn.ru.mynews.presenter.BookmarksPresenter;
import silantyevmn.ru.mynews.ui.activity.StartActivity;
import silantyevmn.ru.mynews.ui.adapter.RecyclerAdapter;
import silantyevmn.ru.mynews.ui.common.BackButtonListener;
import silantyevmn.ru.mynews.ui.popup.PopupDialogMessage;
import silantyevmn.ru.mynews.ui.view.BookmarksView;

public class BookmarksFragment extends MvpAppCompatFragment implements BookmarksView, BackButtonListener {
    private RecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private TextView bookmarksTextView;

    private Parcelable recyclerViewState; //храним состояние списка

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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        recyclerView = view.findViewById(R.id.bookmarks_recycler);
        bookmarksTextView= view.findViewById(R.id.bookmarks_text_view);
        return view;
    }

    @Override
    public void init() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerAdapter(presenter, imageLoader);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void updateList() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String text) {
        popupWindow.error(getView(), text);
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
    public void onPause() {
        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();//save
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((StartActivity) getActivity()).initToolbar(getString(R.string.title_bookmarks));
        presenter.loadBookmarks();
        if (recyclerViewState != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);//restore
        }
    }

    @Override
    public boolean onBackPressed() {
        presenter.onBackPressed();
        return true;
    }
}