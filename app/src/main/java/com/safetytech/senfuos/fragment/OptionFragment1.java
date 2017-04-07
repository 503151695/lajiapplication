package com.safetytech.senfuos.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.safetytech.senfuos.R;

/**
 * Created by ping6 on 2017/4/1.
 */

public class OptionFragment1 extends Fragment implements View.OnClickListener{
    private Button to_option2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewReco = inflater.inflate(R.layout.fragment_option1,container,false);
        return viewReco;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        to_option2 = (Button) view.findViewById(R.id.to_option2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.to_option2:

                break;
        }
    }
}
