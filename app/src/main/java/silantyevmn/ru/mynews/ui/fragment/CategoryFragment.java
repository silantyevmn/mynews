package silantyevmn.ru.mynews.ui.fragment;

import android.os.Bundle;
import android.os.Parcelable;
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
import silantyevmn.ru.mynews.model.image.ImageLoader;
import silantyevmn.ru.mynews.model.repo.Repo;
import silantyevmn.ru.mynews.presenter.CategoryPresenter;
import silantyevmn.ru.mynews.ui.activity.StartActivity;
import silantyevmn.ru.mynews.ui.adapter.RecyclerAdapter;
import silantyevmn.ru.mynews.ui.common.BackButtonListener;
import silantyevmn.ru.mynews.ui.popup.PopupDialogMessage;
import silantyevmn.ru.mynews.ui.view.CategoryView;

public class CategoryFragment extends MvpAppCompatFragment implements CategoryView, SwipeRefreshLayout.OnRefreshListener, BackButtonListener {
    private RecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TabLayout tabLayout;
    private TextView categoryHeadpiece;
    private int currentTabPosition = 0;

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
    CategoryPresenter presenter;

    public static CategoryFragment getNewInstance() {
        return new CategoryFragment();
    }

    @ProvidePresenter
    public CategoryPresenter provideGeneralPresenter() {
        return new CategoryPresenter(AndroidSchedulers.mainThread(), router, repo);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        App.getInstance().getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        recyclerView = view.findViewById(R.id.category_recycler);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        tabLayout = view.findViewById(R.id.tablayout);
        categoryHeadpiece = view.findViewById(R.id.category_text_view);
        return view;
    }

    @Override
    public void init() {
        inflateTabLayout();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                presenter.onTabSelected(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerAdapter(presenter, imageLoader);
        recyclerView.setAdapter(adapter);
    }

    private void inflateTabLayout() {
        for (String titleCategory : getResources().getStringArray(R.array.category_list_title)) {
            tabLayout.addTab(tabLayout.newTab().setText(titleCategory));
        }
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
        presenter.loadNews(tabLayout.getSelectedTabPosition());
    }

    @Override
    public void onPause() {
        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();//save
        currentTabPosition = tabLayout.getSelectedTabPosition();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((StartActivity) getActivity()).initToolbar(getString(R.string.title_category));
        presenter.loadNews(currentTabPosition);
        tabLayout.setScrollPosition(currentTabPosition, 0f, true);
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