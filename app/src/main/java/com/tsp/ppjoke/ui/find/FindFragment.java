package com.tsp.ppjoke.ui.find;

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
 * desc   :
 * version: 1.0
 */
@FragmentDestination(pageUrl = "main/tabs/find")
public class FindFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        return view;
    }
}