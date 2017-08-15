package com.poissondumars.popularmovies;

import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ButterKnife.bind(this);

        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MovieInfoFragment(), getString(R.string.movie_tab_info));
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                didSelectPageWithIndex(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void didSelectPageWithIndex(int index) {
        switch (index) {
            case INFO_PAGE_INDEX:
                prepareInfoPage();
                break;
            default:
                throw new UnsupportedOperationException("Unsupported pager index " + index);
        }
    }

    private void prepareInfoPage() {
        ViewPagerAdapter pagerAdapter = (ViewPagerAdapter) viewPager.getAdapter();
        MovieInfoFragment infoFragment = (MovieInfoFragment) pagerAdapter.getFragment(INFO_PAGE_INDEX);

        if (infoFragment == null) {
            return;
        }

        String movieExtraKey = getString(R.string.movie_extra_key);
        Intent intentThatOpenedThisActivity = getIntent();
        if (intentThatOpenedThisActivity.hasExtra(movieExtraKey)) {
            Movie selectedMovie = intentThatOpenedThisActivity.getParcelableExtra(movieExtraKey);
            infoFragment.setMovie(selectedMovie);
        }
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

        Fragment getFragment(int index) {
            return  mFragmentList.get(index);
        }
    }

}
