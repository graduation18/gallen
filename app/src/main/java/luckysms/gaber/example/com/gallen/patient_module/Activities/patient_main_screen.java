
package luckysms.gaber.example.com.gallen.patient_module.Activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.ViewPagerAdapter_with_titles;
import luckysms.gaber.example.com.gallen.patient_module.Fragments.patient_appointments;
import luckysms.gaber.example.com.gallen.patient_module.Fragments.patient_favorites;
import luckysms.gaber.example.com.gallen.patient_module.Fragments.patient_more;
import luckysms.gaber.example.com.gallen.patient_module.Fragments.patient_more_visitor;
import luckysms.gaber.example.com.gallen.patient_module.Fragments.patient_search;

public class patient_main_screen extends AppCompatActivity  {
    private ViewPager viewPager;
    MenuItem prevMenuItem;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_patient_main_screen);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.more:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.favorites:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.myappointments:
                                viewPager.setCurrentItem(2);
                                break;
                            case R.id.search:
                                viewPager.setCurrentItem(3);
                                break;


                        }
                        return false;
                    }
                });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: "+position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

       /*   //Disable ViewPager Swipe
       viewPager.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });
        */
        setupViewPager(viewPager);



    }
    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter_with_titles adapter = new ViewPagerAdapter_with_titles(getSupportFragmentManager());
        patient_more_visitor patient_more_visitor_fragment = new patient_more_visitor();
        patient_more patient_more_fragment = new patient_more();
        patient_favorites patient_favorites_fragment =new patient_favorites();
        patient_appointments patient_appointments_fragment =new patient_appointments();
        patient_search patient_search_fragment =new patient_search();
        Bundle bundle=new Bundle();
        bundle.putBoolean("visitor",getIntent().getBooleanExtra("visitor",false));
        patient_search_fragment.setArguments(bundle);
        if (getIntent().getBooleanExtra("visitor",false)) {
            adapter.addFragment(patient_more_visitor_fragment);

        }else {
            adapter.addFragment(patient_more_fragment);

        }
        adapter.addFragment(patient_favorites_fragment);
        adapter.addFragment(patient_appointments_fragment);
        adapter.addFragment(patient_search_fragment);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(3);
    }



}