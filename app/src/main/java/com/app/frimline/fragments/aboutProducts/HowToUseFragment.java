package com.app.frimline.fragments.aboutProducts;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.app.frimline.R;
import com.app.frimline.databinding.FragmentCenterBinding;
import com.app.frimline.databinding.FragmentHowToUseBinding;

public class HowToUseFragment extends Fragment {

    private FragmentHowToUseBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_how_to_use,container,false);

        String  data = "<p>Rinse mouth with undiluted 15 ml DENTE91 twice a day for 30 seconds and expel. Do not rinse mouth with water or eat or drink for 30 minutes post rinse.</p>";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.text.setText(Html.fromHtml(data, Html.FROM_HTML_MODE_COMPACT));
        } else {
            binding.text.setText(Html.fromHtml(data));
        }
        return binding.getRoot();
    }
}