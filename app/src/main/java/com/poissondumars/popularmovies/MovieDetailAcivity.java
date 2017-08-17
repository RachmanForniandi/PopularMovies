package com.poissondumars.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.poissondumars.popularmovies.data.Movie;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailAcivity extends AppCompatActivity {

    private static final int INFO_PAGE_INDEX = 0;
    private static final int TRAILERS_PAGE_INDEX = 1;
    private static final int REVIEWS_PAGE_INDEX = 2;

    @BindView(R.id.tl_movie_tabs)
    TabLayout tabLayout;

    @BindView(R.id.vp_pager)
    ViewPager viewPager;

    private Movie mSelectedMovie;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ButterKnife.bind(this);

        String movieExtraKey = getString(R.string.movie_extra_key);
        if (savedInstanceState != null) {
            mSelectedMovie = savedInstanceState.getParcelable(movieExtraKey);
        }

        Intent intentThatOpenedThisActivity = getIntent();
        if (intentThatOpenedThisActivity != null && intentThatOpenedThisActivity.hasExtra(movieExtraKey)) {
            mSelectedMovie = intentThatOpenedThisActivity.getParcelableExtra(movieExtraKey);
        }

        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putParcelable(getString(R.string.movie_extra_key), mSelectedMovie);

        super.onSaveInstanceState(outState, outPersistentState);
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(MovieFragment.instanseWith(MovieInfoFragment.class, mSelectedMovie),
                getString(R.string.movie_tab_info));
        adapter.addFragment(MovieFragment.instanseWith(MovieTrailersFragment.class, mSelectedMovie),
                getString(R.string.movie_tab_trailers));
        adapter.addFragment(MovieFragment.instanseWith(MovieReviewsFragment.class, mSelectedMovie),
                getString(R.string.movie_tab_reviews));
        viewPager.setAdapter(adapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
    }

}
