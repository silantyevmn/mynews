package silantyevmn.ru.mynews.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import silantyevmn.ru.mynews.presenter.SearchPresenter;
import silantyevmn.ru.mynews.ui.activity.StartActivity;
import silantyevmn.ru.mynews.ui.adapter.RecyclerAdapter;
import silantyevmn.ru.mynews.ui.common.BackButtonListener;
import silantyevmn.ru.mynews.ui.popup.PopupDialogMessage;
import silantyevmn.ru.mynews.ui.view.SearchNewsView;

public class SearchFragment extends MvpAppCompatFragment implements SearchNewsView, BackButtonListener {
    private static final String KEY_SEARCH = "key_search";
    private RecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private Toolbar toolbar;

    @Inject
    PopupDialogMessage popupWindow;

    @Inject
    Repo repo;

    @Inject
    Router router;

    @Inject
    ImageLoader imageLoader;

    @InjectPresenter
    SearchPresenter presenter;

    public static SearchFragment getNewInstance(String query) {
        SearchFragment fragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_SEARCH, query);
        fragment.setArguments(bundle);
        return fragment;
    }

    @ProvidePresenter
    public SearchPresenter provideGeneralPresenter() {
        return new SearchPresenter(AndroidSchedulers.mainThread(), router, repo, getArguments().getString(KEY_SEARCH));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        App.getInstance().getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = view.findViewById(R.id.recycler_search);
        toolbar = ((StartActivity) getActivity()).getToolbar();
        return view;
    }

    @Override
    public void init() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
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
    public void showMessage(String text) {
        popupWindow.into(getView(), text);
    }

    @Override
    public void onResume() {
        super.onResume();
        initToolbar();
        presenter.loadSearchNews();
    }

    private void initToolbar() {
        toolbar.getMenu().findItem(R.id.search).setVisible(false); //убираем значок поиска
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp); //выводим кнопку назад
        toolbar.setNavigationOnClickListener(l -> {
            onBackPressed();
        });
    }

    private void closeToolbar() {
        toolbar.getMenu().findItem(R.id.search).setVisible(true); //возвращаем значок поиска
        toolbar.setNavigationIcon(null); //убираем кнопку назад
        toolbar.setNavigationOnClickListener(null);
    }

    @Override
    public boolean onBackPressed() {
        closeToolbar();
        presenter.onBackPressed();
        return true;
    }

}