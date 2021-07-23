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
import com.app.frimline.databinding.FragmentAdditionalInfoBinding;
import com.app.frimline.databinding.FragmentCenterBinding;

public class AdditionalInfoFragment extends Fragment {

    private FragmentAdditionalInfoBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_additional_info,container,false);
        String  data = "<table class=\"woocommerce-product-attributes shop_attributes\">\n" +
                "\t\t\t<tbody><tr class=\"woocommerce-product-attributes-item woocommerce-product-attributes-item--weight\">\n" +
                "\t\t\t<th class=\"woocommerce-product-attributes-item__label\">Weight</th>\n" +
                "\t\t\t<td class=\"woocommerce-product-attributes-item__value\">0.250 kg</td>\n" +
                "\t\t</tr>\n" +
                "\t\t\t<tr class=\"woocommerce-product-attributes-item woocommerce-product-attributes-item--dimensions\">\n" +
                "\t\t\t<th class=\"woocommerce-product-attributes-item__label\">Dimensions</th>\n" +
                "\t\t\t<td class=\"woocommerce-product-attributes-item__value\">6.7 × 6.7 × 13.7 cm</td>\n" +
                "\t\t</tr>\n" +
                "\t\t\t<tr class=\"woocommerce-product-attributes-item woocommerce-product-attributes-item--attribute_pa_size\">\n" +
                "\t\t\t<th class=\"woocommerce-product-attributes-item__label\">size</th>\n" +
                "\t\t\t<td class=\"woocommerce-product-attributes-item__value\"><p>150ml</p>\n" +
                "</td>\n" +
                "\t\t</tr>\n" +
                "\t</tbody></table>";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.text.setText(Html.fromHtml(data, Html.FROM_HTML_MODE_COMPACT));
        } else {
            binding.text.setText(Html.fromHtml(data));
        }
        return binding.getRoot();
    }
}