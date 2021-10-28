package com.tsp.ppjoke.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.tsp.libnavannotation.FragmentDestination;
import com.tsp.main.R;

/**
 * author : shengping.tian
 * time   : 2021/10/28
 * desc   : HomeFragment 页面
 * version: 1.0
 */
@FragmentDestination(pageUrl = "main/tabs/home", asStarter = true)
public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }
}
