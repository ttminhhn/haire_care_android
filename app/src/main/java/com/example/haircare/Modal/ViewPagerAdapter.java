package com.example.haircare.Modal;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/*import com.lqt.duynguyenhairsalon.Fragments.MainApp.AccountFragment;
import com.lqt.duynguyenhairsalon.Fragments.MainApp.FavoritesFragment;
import com.lqt.duynguyenhairsalon.Fragments.MainApp.HomeFragment;
import com.lqt.duynguyenhairsalon.Fragments.MainApp.StoreFragment;*/
import com.example.haircare.Fragments.MainApp.HomeFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
//            case 1:
//                return new FavoritesFragment();
//            case 2:
//                return new StoreFragment();
//            case 3:
//                return new AccountFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
