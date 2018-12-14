package silantyevmn.ru.mynews.ui.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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
import silantyevmn.ru.mynews.presenter.HomePresenter;
import silantyevmn.ru.mynews.ui.activity.StartActivity;
import silantyevmn.ru.mynews.ui.adapter.RecyclerAdapter;
import silantyevmn.ru.mynews.ui.common.BackButtonListener;
import silantyevmn.ru.mynews.ui.popup.PopupDialogMessage;
import silantyevmn.ru.mynews.ui.view.HomeView;

public class HomeFragment extends MvpAppCompatFragment implements HomeView, BackButtonListener, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

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
    HomePresenter presenter;

    public static HomeFragment getNewInstance() {
        return new HomeFragment();
    }

    @ProvidePresenter
    public HomePresenter provideGeneralPresenter() {
        return new HomePresenter(AndroidSchedulers.mainThread(), router, repo);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        App.getInstance().getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recycler_home);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
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
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(String text) {
        popupWindow.error(getView(), text);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showSuccess(String text) {
        popupWindow.onSuccess(getView(), text);
    }

    @Override
    public void showLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onRefresh() {
        presenter.loadNews();
    }

    @Override
    public void onPause() {
        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();//save
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((StartActivity) getActivity()).initToolbar(getString(R.string.title_home));
        presenter.loadNews();
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