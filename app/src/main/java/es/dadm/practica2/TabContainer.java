package es.dadm.practica2;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dadm.practica2.util.SectionsPageAdapter;


public class TabContainer extends AppCompatActivity {
    @BindView(R.id.vpContent) ViewPager mViewPager;
    @BindView(R.id.tlTabs) TabLayout mTabLayout;

    private SectionsPageAdapter mSectionsPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_container);

        ButterKnife.bind(this);

        setUpViewPager(mViewPager);

        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void setUpViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());

        adapter.addFragment(new fragmentList(), getString(R.string.LIST_TAB_TITLE));
        adapter.addFragment(new fragmentTiles(), getString(R.string.TILES_TAB_TITLE));
        adapter.addFragment(new fragmentCards(), getString(R.string.CARDS_TAB_TITLE));

        viewPager.setAdapter(adapter);
    }
}
