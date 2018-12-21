package silantyevmn.ru.mynews.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import javax.inject.Inject;

import ru.terrakok.cicerone.Router;
import silantyevmn.ru.mynews.App;
import silantyevmn.ru.mynews.R;
import silantyevmn.ru.mynews.presenter.CategoryPresenter;
import silantyevmn.ru.mynews.ui.activity.StartActivity;
import silantyevmn.ru.mynews.ui.common.BackButtonListener;
import silantyevmn.ru.mynews.ui.view.CategoryView;

public class CategoryFragment extends MvpAppCompatFragment implements CategoryView, BackButtonListener {

    @Inject
    Router router;

    @InjectPresenter
    CategoryPresenter presenter;

    public static CategoryFragment getNewInstance() {
        return new CategoryFragment();
    }

    @ProvidePresenter
    public CategoryPresenter provideGeneralPresenter() {
        return new CategoryPresenter(router);
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
        ViewPager viewPager = view.findViewById(R.id.viewpager);
        viewPager.setAdapter(new CategoryFragmentPagerAdapter(getChildFragmentManager(),getResources().getStringArray(R.array.category_list_title)));
        // Передаём ViewPager в TabLayout
        TabLayout tabLayout = view.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((StartActivity) getActivity()).initToolbar(getString(R.string.title_category));
    }

    @Override
    public boolean onBackPressed() {
        presenter.onBackPressed();
        return true;
    }
}