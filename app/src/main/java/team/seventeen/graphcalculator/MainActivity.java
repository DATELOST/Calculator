package team.seventeen.graphcalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public class MyPageAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> datas;
        ArrayList<String> titles;
        public MyPageAdapter(FragmentManager fm) { super(fm); }
        public void setData(ArrayList<Fragment> datas) { this.datas = datas; }
        public void setTitles(ArrayList<String> titles){ this.titles = titles; }
        @Override
        public Fragment getItem(int position) { return datas == null ? null : datas.get(position); }
        @Override
        public int getCount() { return datas == null ? 0 : datas.size(); }
        @Override
        public CharSequence getPageTitle(int position) { return titles == null ? null : titles.get(position); }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MyPageAdapter myPageAdapter = new MyPageAdapter(this.getSupportFragmentManager());
        ArrayList<Fragment> datas = new ArrayList<Fragment>();
        datas.add(new GraphFragment());
        datas.add(new FunctionFragment());
        myPageAdapter.setData(datas);
        ArrayList<String> titles = new ArrayList<String>();
        titles.add("图像");
        titles.add("函数");
        myPageAdapter.setTitles(titles);
        TabLayout tabLayout = findViewById(R.id.table_layout_header);
        ViewPager viewPager = findViewById(R.id.view_pager_content);
        viewPager.setAdapter(myPageAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}