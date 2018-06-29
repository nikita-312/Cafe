package com.conceptioni.cafeapp.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.conceptioni.cafeapp.R;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.Timer;
import java.util.TimerTask;

public class DescriptionActivity extends AppCompatActivity {
    ViewPager viewPager;
    CirclePageIndicator indicator;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    int slider[]={R.drawable.slider,R.drawable.slider,R.drawable.slider};

    /**/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        init();
    }

    private void init() {
        viewPager=findViewById(R.id.pager);
        indicator=findViewById(R.id.indicator);

        SlidePageAdapter slidePageAdapter = new SlidePageAdapter();
        if (viewPager != null) {
            viewPager.setAdapter(slidePageAdapter);
        }

        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(viewPager);

        final float density = getResources().getDisplayMetrics().density;

        indicator.setRadius(4 * density);
        indicator.setStrokeWidth(13);
        indicator.setRadius(10);
        indicator.setStrokeColor(Color.WHITE);

        NUM_PAGES = slider.length;

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
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
        private String imgurl;
        private LayoutInflater layoutInflater;

//        @Override
//        public float getPageWidth(int position) {
//            return 0.80f;
//        }

        @Override
        public int getCount() {
            return slider.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.row_item_slider, container, false);
            ImageView banneriv = (ImageView) view.findViewById(R.id.banneriv);
               banneriv.setImageResource(slider[position]);
//            String url = bannerModelArrayList.get(position).getVar_link();
//            if (!url.startsWith("http://") && !url.startsWith("https://"))
//                url = "http://" + url;

//            banneriv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(homeSliderModelsArray.get(position).getVar_link()));
//                    startActivity(browserIntent);
//                }
            //  });
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

}
