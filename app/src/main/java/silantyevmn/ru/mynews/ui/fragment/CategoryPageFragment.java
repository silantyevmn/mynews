package silantyevmn.ru.mynews.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
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
import silantyevmn.ru.mynews.presenter.CategoryPagePresenter;
import silantyevmn.ru.mynews.presenter.CategoryPresenter;
import silantyevmn.ru.mynews.ui.activity.StartActivity;
import silantyevmn.ru.mynews.ui.adapter.RecyclerAdapter;
import silantyevmn.ru.mynews.ui.common.BackButtonListener;
import silantyevmn.ru.mynews.ui.image.ImageLoader;
import silantyevmn.ru.mynews.ui.popup.PopupDialogMessage;
import silantyevmn.ru.mynews.ui.view.CategoryPageView;
import silantyevmn.ru.mynews.ui.view.CategoryView;

public class CategoryPageFragment extends MvpAppCompatFragment implements CategoryPageView, SwipeRefreshLayout.OnRefreshListener, BackButtonListener {
    private static final String KEY_CATEGORY = "silantyevmn.ru.mynews.ui.fragment.CategoryPageFragment";
    private RecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView categoryHeadpiece;

    @Inject
    PopupDialogMessage popupWindow;

    @Inject
    Repo repo;

    @Inject
    Router router;

    @Inject
    ImageLoader imageLoader;

    @InjectPresenter
    CategoryPagePresenter presenter;

    public static CategoryPageFragment getNewInstance(int position) {
        CategoryPageFragment fragment = new CategoryPageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_CATEGORY, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @ProvidePresenter
    public CategoryPagePresenter provideGeneralPresenter() {
        return new CategoryPagePresenter(AndroidSchedulers.mainThread(), router, repo,getArguments().getInt(KEY_CATEGORY,0));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        App.getInstance().getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_page, container, false);
        recyclerView = view.findViewById(R.id.category_recycler);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        categoryHeadpiece = view.findViewById(R.id.category_text_view);
        init();
        return view;
    }

    private void init() {
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
    public void showLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void showSuccess(String message) {
        popupWindow.onSuccess(getView(), message);
    }

    @Override
    public void showHeadpiece() {
        categoryHeadpiece.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideHeadpiece() {
        categoryHeadpiece.setVisibility(View.GONE);
    }

    @Override
    public void onRefresh() {
        presenter.loadNews();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.loadNews();
    }

    @Override
    public boolean onBackPressed() {
        presenter.onBackPressed();
        return true;
    }
}