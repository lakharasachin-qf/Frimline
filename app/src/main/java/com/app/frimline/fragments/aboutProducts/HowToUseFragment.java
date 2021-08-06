package com.app.frimline.fragments.aboutProducts;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.HELPER;
import com.app.frimline.R;
import com.app.frimline.databinding.FragmentHowToUseBinding;
import com.app.frimline.fragments.BaseFragment;
import com.app.frimline.models.HomeFragements.ProductModel;

import org.json.JSONException;
import org.json.JSONObject;

public class HowToUseFragment extends BaseFragment {

    private FragmentHowToUseBinding binding;
    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_how_to_use, parent, false);

        String data = "<p>Rinse mouth with undiluted 15 ml DENTE91 twice a day for 30 seconds and expel. Do not rinse mouth with water or eat or drink for 30 minutes post rinse.</p>";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.text.setText(Html.fromHtml(data, Html.FROM_HTML_MODE_COMPACT));
        } else {
            binding.text.setText(Html.fromHtml(data));
        }
        if (CONSTANT.API_MODE) {
            loadData();
        }
        return binding.getRoot();
    }

    private ProductModel productModel;

    public void loadData() {
        productModel = gson.fromJson(act.getIntent().getStringExtra("model"), ProductModel.class);
        HELPER.LOAD_HTML(binding.text, productModel.getAttribute().getHowToUse());
    }
}