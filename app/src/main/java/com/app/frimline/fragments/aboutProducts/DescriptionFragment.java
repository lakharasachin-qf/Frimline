package com.app.frimline.fragments.aboutProducts;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.HELPER;
import com.app.frimline.R;
import com.app.frimline.databinding.FragmentDescriptionBinding;
import com.app.frimline.fragments.BaseFragment;
import com.app.frimline.models.HomeFragements.ProductModel;

public class DescriptionFragment extends BaseFragment {

    private FragmentDescriptionBinding binding;
    private ProductModel productModel;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_description, parent, false);


        String data = "<div class=\"woocommerce-Tabs-panel woocommerce-Tabs-panel--description panel entry-content wc-tab\" id=\"tab-description\" role=\"tabpanel\" aria-labelledby=\"tab-title-description\" style=\"\">\n" +
                "\t\t\t\t\n" +
                "\n" +
                "<p>Dente91 Cool Mint Lactoferrin Mouthwash gives you protection against plaque, bad breath, and gum problems. Its anti-bacterial germ defense property gives you fresh breath daily.</p>\n\n <br><br>" +
                "<p><strong>Why Lactoferrin Mouthwash?</strong></p>\n<br><br>" +
                "<p>The Lactoferrin not only destroys and fights harmful bacteria but also protects the good bacteria, unlike other mouthwashes. Its effectiveness, specificity, and safety aspects not only reduce the risk of oral diseases but also enhances the overall biological equilibrium within the oral cavity by supplementing saliva’s natural protective mechanisms. This way, Dente91 not only maintains oral health naturally but also protects the oral Eco-flora.</p>\n" +
                "<ul>\n\n<br><br>" +
                "<li> Lactoferrin-based Mouthwash not only helps in maintaining oral health in a natural way but also maintains and protects the oral Eco-flora.</li>\n" +
                "<li> Lactoferrin Mouthwash preserves oral homeostasis by supplementing saliva’s natural protective mechanisms via Lactoferrin.</li>\n" +
                "<li> Lactoferrin Mouthwash protects enamel and dentin from demineralization and erosion.</li>\n" +
                "<li> It is free from Alcohol, Fluoride, Sugar, and Whitener</li>\n" +
                "</ul>\n<br><br>" +
                "<p><strong>Lactoferrin Mouthwash Liquid helps</strong></p>\n<br><br>" +
                "<ul>\n" +
                "<li> Neutralizes morning breath &amp; food odor.</li>\n" +
                "<li> In the maintenance of good Oral Hygiene.</li>\n" +
                "<li> In the prevention of Bad Breath.</li>\n" +
                "<li> Prevent Plaque and Tartar.</li>\n" +
                "<li> Fight against Gingivitis, Periodontitis, Oral fungal and Bacterial infections.</li>\n" +
                "<li> Non-Alcohol formula with a mint flavor that doesn’t sting or burn.</li>\n" +
                "</ul>\n" +
                "\t\t\t</div>";
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

    public void loadData() {
        productModel = gson.fromJson(act.getIntent().getStringExtra("model"), ProductModel.class);
        if (productModel != null && productModel.getAttribute()!=null) {
            HELPER.LOAD_HTML(binding.text, productModel.getAttribute().getDescription());
        }
    }

    public void setProductModel(ProductModel productModel) {
        this.productModel = productModel;
        if (this.productModel != null) {
            Log.e("print", gson.toJson(this.productModel));
            HELPER.LOAD_HTML(binding.text, this.productModel.getAttribute().getDescription());
        }
    }

}