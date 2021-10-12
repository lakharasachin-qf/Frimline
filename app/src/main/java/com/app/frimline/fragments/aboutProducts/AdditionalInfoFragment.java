package com.app.frimline.fragments.aboutProducts;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.HELPER;
import com.app.frimline.R;
import com.app.frimline.databinding.FragmentAdditionalInfoBinding;
import com.app.frimline.fragments.BaseFragment;
import com.app.frimline.models.HomeFragements.ProductModel;

public class AdditionalInfoFragment extends BaseFragment {

    private FragmentAdditionalInfoBinding binding;
    private ProductModel productModel;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_additional_info, parent, false);
        String data = "<table class=\"woocommerce-product-attributes shop_attributes\">\n" +
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

        if (CONSTANT.API_MODE) {
            loadData();
        } else {
            binding.text.setVisibility(View.VISIBLE);
        }
        return binding.getRoot();
    }

    public void loadData() {
        productModel = gson.fromJson(act.getIntent().getStringExtra("model"), ProductModel.class);
        Log.e("attribute",gson.toJson(productModel.getAttribute()));
        if (productModel != null && productModel.getAttribute()!=null) {
            boolean noData=true;
            if (!productModel.getAttribute().getDimWeight().isEmpty()) {
                HELPER.LOAD_HTML(binding.weightTxt, productModel.getAttribute().getDimWeight());
                binding.weightLayout.setVisibility(View.VISIBLE);
                binding.view1.setVisibility(View.VISIBLE);
                noData=false;
            } else {
                binding.weightLayout.setVisibility(View.GONE);
                binding.view1.setVisibility(View.GONE);
            }

            String str = "";
            if (!productModel.getAttribute().getDimLength().isEmpty()) {
                str = productModel.getAttribute().getDimLength();
            }
            if (!productModel.getAttribute().getDimWidth().isEmpty()) {
                str = str + " " + act.getString(R.string.Multiplication) + " " + productModel.getAttribute().getDimWidth();
            }
            if (!productModel.getAttribute().getDimHeight().isEmpty()) {
                str = str + " " + act.getString(R.string.Multiplication) + " " + productModel.getAttribute().getDimHeight();
            }
            if (!str.isEmpty()) {
                HELPER.LOAD_HTML(binding.dimentionTxt, str);
                binding.dimentionLayout.setVisibility(View.VISIBLE);
                binding.view2.setVisibility(View.VISIBLE);
                noData=false;
            } else {
                binding.dimentionLayout.setVisibility(View.GONE);
                binding.view2.setVisibility(View.GONE);
            }


            if (!productModel.getAttribute().getSize().isEmpty()) {
                HELPER.LOAD_HTML(binding.sizeTxt, productModel.getAttribute().getSize());
                binding.sizeLayout.setVisibility(View.VISIBLE);
                binding.view3.setVisibility(View.VISIBLE);
                noData=false;
            } else {
                binding.sizeLayout.setVisibility(View.GONE);
                binding.view3.setVisibility(View.GONE);
            }

            if (noData){
                binding.text.setVisibility(View.VISIBLE);
            }
        }else{
            binding.text.setVisibility(View.VISIBLE);
        }

    }

    public void setProductModel(ProductModel productModel) {
        this.productModel = productModel;
        if (productModel != null) {
            Log.e("attribute",gson.toJson(productModel.getAttribute()));
            if (!productModel.getAttribute().getDimWeight().isEmpty()) {
                HELPER.LOAD_HTML(binding.weightTxt, productModel.getAttribute().getDimWeight());
                binding.weightLayout.setVisibility(View.VISIBLE);
                binding.view1.setVisibility(View.VISIBLE);
            } else {
                binding.weightLayout.setVisibility(View.GONE);
                binding.view1.setVisibility(View.GONE);
            }

            String str = "";
            if (!productModel.getAttribute().getDimLength().isEmpty()) {
                str = productModel.getAttribute().getDimLength();
            }
            if (!productModel.getAttribute().getDimWidth().isEmpty()) {
                str = str + " " + act.getString(R.string.Multiplication) + " " + productModel.getAttribute().getDimWidth();
            }
            if (!productModel.getAttribute().getDimHeight().isEmpty()) {
                str = str + " " + act.getString(R.string.Multiplication) + " " + productModel.getAttribute().getDimHeight();
            }
            if (!str.isEmpty()) {
                HELPER.LOAD_HTML(binding.dimentionTxt, str);
                binding.dimentionLayout.setVisibility(View.VISIBLE);
                binding.view2.setVisibility(View.VISIBLE);
            } else {
                binding.dimentionLayout.setVisibility(View.GONE);
                binding.view2.setVisibility(View.GONE);
            }


            if (!productModel.getAttribute().getSize().isEmpty()) {
                HELPER.LOAD_HTML(binding.sizeTxt, productModel.getAttribute().getSize());
                binding.sizeLayout.setVisibility(View.VISIBLE);
                binding.view3.setVisibility(View.VISIBLE);
            } else {
                binding.sizeLayout.setVisibility(View.GONE);
                binding.view3.setVisibility(View.GONE);
            }
        }
    }
}