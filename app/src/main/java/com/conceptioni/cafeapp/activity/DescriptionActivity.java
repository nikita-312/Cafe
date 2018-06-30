package com.conceptioni.cafeapp.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.conceptioni.cafeapp.R;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.Timer;
import java.util.TimerTask;

public class DescriptionActivity extends AppCompatActivity {
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    ViewPager viewPager;
    CirclePageIndicator indicator;
    int slider[] = {R.drawable.slider, R.drawable.slider, R.drawable.slider};

    /**/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        init();
    }

    private void init() {
        viewPager = findViewById(R.id.pager);
        indicator = findViewById(R.id.indicator);

        SlidePageAdapter slidePageAdapter = new SlidePageAdapter();
        if (viewPager != null) {
            viewPager.setAdapter(slidePageAdapter);
        }

        CirclePageIndicator indicator =
                findViewById(R.id.indicator);

        indicator.setViewPager(viewPager);

        final float density = getResources().getDisplayMetrics().density;

        indicator.setRadius(4 * density);
        indicator.setStrokeWidth(13);
        indicator.setRadius(10);
        indicator.setStrokeColor(Color.WHITE);

        NUM_PAGES = slider.length;

        final Handler handler = new Handler();
        final Runnable Update = () -> {
            if (currentPage == NUM_PAGES) {
                currentPage = 0;
            }
            viewPager.setCurrentItem(currentPage++, true);
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2000, 2000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });
    }

    class SlidePageAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        @Override
        public int getCount() {
            return slider.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            View view = layoutInflater.inflate(R.layout.row_item_slider, container, false);
            ImageView banneriv = view.findViewById(R.id.banneriv);
            banneriv.setImageResource(slider[position]);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

}