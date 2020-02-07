package com.rrcc.appcultural20;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rrcc.appcultural20.TabsInicio.FinDeSemanaFragment;
import com.rrcc.appcultural20.TabsInicio.HoyFragment;
import com.rrcc.appcultural20.TabsInicio.SemanaFragment;

public class InicioFragment extends android.support.v4.app.Fragment {
    private AppBarLayout appBar;
    private TabLayout tabs;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_inicio, container, false);
        View contenedor=(View) container.getParent();
        appBar=(AppBarLayout)contenedor.findViewById(R.id.appbar);
        tabs= new TabLayout(getActivity());
        tabs.setTabTextColors(Color.parseColor("#FFFFFF"),Color.parseColor("#FFFFFF"));
        appBar.addView(tabs);

        viewPager=view.findViewById(R.id.pager);
        ViewPagerAdapter pagerAdapter=new ViewPagerAdapter(getFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabs.setupWithViewPager(viewPager);
        return view;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        appBar.removeView(tabs);
    }
    public class ViewPagerAdapter extends android.support.v4.app.FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager fragmentManager){
            super(fragmentManager);
        }
        String tituloTabs[]={"HOY","SEMANA","FIN DE SEMANA"};

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position){
                case 0: return new HoyFragment();
                case 1: return new SemanaFragment();
                case 2: return new FinDeSemanaFragment();
            }

            return null;
        }
        public int getCount(){
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tituloTabs[position];
        }
    }
}