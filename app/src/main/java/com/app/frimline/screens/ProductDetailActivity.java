package com.app.frimline.screens;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.ColorUtils;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.app.cartcounter.CharOrder;
import com.app.cartcounter.strategy.Strategy;
import com.app.frimline.BaseActivity;
import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.FRIMLINE;
import com.app.frimline.Common.HELPER;
import com.app.frimline.R;
import com.app.frimline.adapters.ProductDetailsTabAdapter;
import com.app.frimline.adapters.ProductImageSliderAdpater;
import com.app.frimline.databaseHelper.CartRoomDatabase;
import com.app.frimline.databinding.ActivityProductDetailBinding;
import com.app.frimline.fragments.aboutProducts.AdditionalInfoFragment;
import com.app.frimline.fragments.aboutProducts.DescriptionFragment;
import com.app.frimline.fragments.aboutProducts.HowToUseFragment;
import com.app.frimline.fragments.aboutProducts.IngredientsFragment;
import com.app.frimline.fragments.aboutProducts.QnAFragment;
import com.app.frimline.fragments.aboutProducts.ReviewsFragment;
import com.app.frimline.models.DataTransferModel;
import com.app.frimline.models.HomeFragements.ProductModel;
import com.app.frimline.models.roomModels.ProductEntity;
import com.app.frimline.views.WrapContentHeightViewPager;
import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;

import java.util.ArrayList;
import java.util.Objects;

public class ProductDetailActivity extends BaseActivity {


    private final boolean redShoeVisible = true;
    private ActivityProductDetailBinding binding;
    private boolean isAddedToCart = false;
    private int observableId;
    private boolean applyThemeColor = false;
    private String defaultColor = "#EF7F1A";
    private CartRoomDatabase cartRoomDatabase;
    private boolean isSameProductEdit = false;
    private final DescriptionFragment descriptionFragment = new DescriptionFragment();
    private final HowToUseFragment howToUseFragment = new HowToUseFragment();
    private final IngredientsFragment ingredientsFragment = new IngredientsFragment();
    private final AdditionalInfoFragment additionalInfoFragment = new AdditionalInfoFragment();
    private final ReviewsFragment reviewsFragment = new ReviewsFragment();
    private final QnAFragment qnAFragment = new QnAFragment();
    private int indicatorWidth;
    private ProductModel productModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_product_detail);
        defaultColor = "#EF7F1A";
        cartRoomDatabase = CartRoomDatabase.getAppDatabase(act);
        if (getIntent().hasExtra("themeColor"))
            applyThemeColor = true;


        if (applyThemeColor) {
            defaultColor = prefManager.getThemeColor();
        } else {
            defaultColor = prefManager.getCategoryColor();
        }
        makeStatusBarSemiTranspenret(binding.toolbar);
        ViewTreeObserver viewTreeObserver = binding.scrollView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                //
            }
        });
        binding.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.ON_BACK_PRESS_ANIM(act);
            }
        });


        binding.titleToolbar.setText("Mouthwash");
        HELPER.changeCartCounterToolbar(act);
        binding.cartActionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(act, MyCartActivity.class);
                i.putExtra("dataModel",gson.toJson(createCartEvent()));
                act.startActivity(i);
                act.overridePendingTransition(R.anim.right_enter_second, R.anim.left_out_second);
            }
        });
        binding.counter.setText("1");
        binding.incrementAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentCounter = Integer.parseInt(binding.counter.getText().toString());
                currentCounter++;
                binding.counter.setText(String.valueOf(currentCounter));
                if (CONSTANT.API_MODE) {
                    if (cartRoomDatabase.productEntityDao().findProductByProductId(productModel.getId()) != null) {
                        isSameProductEdit = true;
                        ProductEntity entity = db.productEntityDao().findProductByProductId(productModel.getId());
                        productModel.setQty(binding.counter.getText().toString());
                        entity.setQty(binding.counter.getText().toString());
                        entity.setCalculatedAmount(HELPER.incrementAction(productModel));
                        productModel.setCalculatedAmount(entity.getCalculatedAmount());
                        db.productEntityDao().updateSpecificProduct((entity));
                        HELPER.LOAD_HTML(binding.price, act.getString(R.string.Rs) + productModel.getCalculatedAmount());
                    } else {
                        productModel.setQty(binding.counter.getText().toString());
                        productModel.setCalculatedAmount(HELPER.incrementAction(productModel));
                        HELPER.LOAD_HTML(binding.price, act.getString(R.string.Rs) + productModel.getCalculatedAmount());
                    }
                }
            }
        });

        binding.decrementAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentCounter = Integer.parseInt(binding.counter.getText().toString());
                if (currentCounter > 1) {
                    currentCounter--;
                    binding.counter.setText(String.valueOf(currentCounter));
                    if (CONSTANT.API_MODE) {
                        if (cartRoomDatabase.productEntityDao().findProductByProductId(productModel.getId()) != null) {
                            isSameProductEdit = true;
                            ProductEntity entity = db.productEntityDao().findProductByProductId(productModel.getId());
                            productModel.setQty(binding.counter.getText().toString());
                            entity.setQty(binding.counter.getText().toString());
                            entity.setCalculatedAmount(HELPER.incrementAction(productModel));
                            productModel.setCalculatedAmount(entity.getCalculatedAmount());
                            db.productEntityDao().updateSpecificProduct((entity));
                            HELPER.LOAD_HTML(binding.price, act.getString(R.string.Rs) + productModel.getCalculatedAmount());
                        } else {
                            productModel.setQty(binding.counter.getText().toString());
                            productModel.setCalculatedAmount(HELPER.incrementAction(productModel));
                            HELPER.LOAD_HTML(binding.price, act.getString(R.string.Rs) + productModel.getCalculatedAmount());
                        }
                    }
                } else {
                    Toast.makeText(act, "At least one quantity required", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.addCartContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (isSameProductEdit) {
//                    ProductEntity entity = cartRoomDatabase.productEntityDao().findProductByProductId(productModel.getId());
//                    entity.setQty(binding.counter.getText().toString());
//                    entity.setCalculatedAmount(HELPER.incrementAction(productModel));
//                    HELPER.LOAD_HTML(binding.price, act.getString(R.string.Rs) + productModel.getCalculatedAmount());
//                    cartRoomDatabase.productEntityDao().updateSpecificProduct(entity);
//                    Toast.makeText(act, "Cart product updated", Toast.LENGTH_SHORT).show();
//                    binding.addTextTxt.setText("Added to cart");
//                    binding.addCartContainer.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(defaultColor)));
//                    binding.cartIcon.setImageTintList(ColorStateList.valueOf(Color.WHITE));
//                    binding.addTextTxt.setTextColor(Color.WHITE);
//                    return;
//                }
                if (isAddedToCart) {
                    binding.addTextTxt.setText("Add to cart");
                    isAddedToCart = false;
                    binding.addCartContainer.setBackgroundTintList(null);
                    binding.cartIcon.setImageTintList(ColorStateList.valueOf(Color.parseColor(defaultColor)));
                    binding.addTextTxt.setTextColor((Color.BLACK));
                    if (CONSTANT.API_MODE) {

                        cartRoomDatabase.productEntityDao().deleteProduct(productModel.getId());

                        observableId = Integer.parseInt(getIntent().getStringExtra("removeCartID"));
                        DataTransferModel model = new DataTransferModel();
                        model.setAdapterPosition(getIntent().getStringExtra("adapterPosition"));
                        model.setProductId(getIntent().getStringExtra("productId"));
                        model.setItemPosition(getIntent().getStringExtra("itemPosition"));
                        model.setProductPosition(getIntent().getStringExtra("productPosition"));
                        model.setLayoutType(getIntent().getStringExtra("layoutType"));
                        FRIMLINE.getInstance().getObserver().setValue(observableId, model);
                        HELPER.changeCartCounterToolbar(act);
                    }
                } else {
                    binding.addTextTxt.setText("Added to cart");
                    isAddedToCart = true;
                    binding.addCartContainer.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(defaultColor)));
                    binding.cartIcon.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                    binding.addTextTxt.setTextColor(Color.WHITE);
                    if (CONSTANT.API_MODE) {
                        productModel.setQty(binding.counter.getText().toString());
                        productModel.setCalculatedAmount(HELPER.incrementAction(productModel));
                        HELPER.LOAD_HTML(binding.price, act.getString(R.string.Rs) + productModel.getCalculatedAmount());
                        cartRoomDatabase.productEntityDao().insert(HELPER.convertToCartObject(productModel));
                        observableId = Integer.parseInt(getIntent().getStringExtra("addToCartID"));
                        DataTransferModel model = new DataTransferModel();
                        model.setAdapterPosition(getIntent().getStringExtra("adapterPosition"));
                        model.setProductId(getIntent().getStringExtra("productId"));
                        model.setItemPosition(getIntent().getStringExtra("itemPosition"));
                        model.setProductPosition(getIntent().getStringExtra("productPosition"));
                        model.setLayoutType(getIntent().getStringExtra("layoutType"));
                        FRIMLINE.getInstance().getObserver().setValue(observableId, model);
                        HELPER.changeCartCounterToolbar(act);

                    }
                }

            }
        });

        binding.counter.setAnimationDuration(150L);
        binding.counter.setTextFontFamily(Objects.requireNonNull(ResourcesCompat.getFont(act, R.font.proxinova_semi_bold)));
        binding.counter.setCharStrategy(Strategy.NormalAnimation());
        binding.counter.addCharOrder(CharOrder.Number);
        binding.counter.setAnimationInterpolator(new AccelerateDecelerateInterpolator());
        binding.counter.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

            }
        });

        setupTabIcons();
        changeTheme();
        setImageSlide();

        if (CONSTANT.API_MODE) {
            binding.scrollView.setVisibility(View.VISIBLE);
            binding.boottomFooter.setVisibility(View.VISIBLE);
            binding.screenLoader.setVisibility(View.GONE);
            loadData();
        }
    }

    public DataTransferModel createCartEvent() {
        DataTransferModel model = new DataTransferModel();
        model.setAdapterPosition(getIntent().getStringExtra("adapterPosition"));
        model.setProductId(getIntent().getStringExtra("productId"));
        model.setItemPosition(getIntent().getStringExtra("itemPosition"));
        model.setProductPosition(getIntent().getStringExtra("productPosition"));
        model.setLayoutType(getIntent().getStringExtra("layoutType"));

        model.setCartAddedId(getIntent().getStringExtra("addToCartID"));
        model.setCartRemovedId(getIntent().getStringExtra("removeCartID"));

        return model;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
        HELPER.changeCartCounterToolbar(act);
    }

    public void setImageSlide() {
        ArrayList<String> outCategoryModels = new ArrayList<>();
        outCategoryModels.add("");
        outCategoryModels.add("");
        outCategoryModels.add("");
        outCategoryModels.add("");
        ProductImageSliderAdpater sliderAdapter = new ProductImageSliderAdpater(outCategoryModels, act);
        binding.productImagesSlider.setAdapter(sliderAdapter);
        binding.dotsIndicator.setViewPager(binding.productImagesSlider);
    }

    public void changeTheme() {


        int code = ColorUtils.setAlphaComponent(Color.parseColor(defaultColor), 100);
        binding.dotsIndicator.setDotsColor(code);
        binding.dotsIndicator.setSelectedDotColor(Color.parseColor(defaultColor));
        binding.tabLayout.setTabTextColors((Color.WHITE), (Color.parseColor(defaultColor)));
        binding.cartBackgroundLayar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(defaultColor)));
        binding.detailContainer.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(defaultColor)));
        binding.price.setTextColor(Color.parseColor(defaultColor));
        binding.cartIcon.setImageTintList(ColorStateList.valueOf(Color.parseColor(defaultColor)));
        GradientDrawable drawable = (GradientDrawable) binding.addCartContainer.getBackground();
        drawable.setStroke(2, Color.parseColor(defaultColor));


        VectorChildFinder vector = new VectorChildFinder(act, R.drawable.ic_not_returnable, binding.nonReturnIcon);
        VectorDrawableCompat.VFullPath path1 = vector.findPathByName("colorGreen");
        path1.setFillColor(Color.parseColor(defaultColor));
        binding.nonReturnIcon.invalidate();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        HELPER.ON_BACK_PRESS_ANIM(act);
    }

    private void setupTabIcons() {
        WrapContentHeightViewPager wrapContentHeightViewPager = findViewById(R.id.viewPager);
        ProductDetailsTabAdapter adapter = new ProductDetailsTabAdapter(getSupportFragmentManager());
        adapter.addFragment(descriptionFragment, "Description");
        adapter.addFragment(howToUseFragment, "How To Use");
        adapter.addFragment(ingredientsFragment, "Ingredients");
        adapter.addFragment(additionalInfoFragment, "Additional Information");
        adapter.addFragment(reviewsFragment, "Reviews");
        adapter.addFragment(qnAFragment, "Q&A");
        wrapContentHeightViewPager.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        wrapContentHeightViewPager.setAdapter(adapter);
        wrapContentHeightViewPager.setOffscreenPageLimit(10);
        binding.tabLayout.setupWithViewPager(wrapContentHeightViewPager);
        binding.tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
        wrapContentHeightViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //  binding.scrollView.smoothScrollTo(binding.viewPager.getLeft(), binding.viewPager.getTop());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void loadData() {
        productModel = gson.fromJson(getIntent().getStringExtra("model"), ProductModel.class);

        ArrayList<String> productImages = productModel.getProductImagesList();
        binding.titleToolbar.setText(productModel.getCategoryName());
        ProductImageSliderAdpater sliderAdapter = new ProductImageSliderAdpater(productImages, act);
        binding.productImagesSlider.setAdapter(sliderAdapter);
        binding.dotsIndicator.setViewPager(binding.productImagesSlider);
        int rate = Integer.parseInt(productModel.getRating());
        binding.ratting.setRating((float) rate);
        HELPER.LOAD_HTML(binding.nameTxt, productModel.getName());
        HELPER.LOAD_HTML(binding.shortDescription, productModel.getShortDescription());
        HELPER.LOAD_HTML(binding.categoryLabel, "<b>Category : </b>" + productModel.getCategoryName());
        HELPER.LOAD_HTML(binding.price, act.getString(R.string.Rs) + productModel.getPrice());

        StringBuilder tagsStr = new StringBuilder();
        if (productModel.getTagsModel() != null && productModel.getTagsModel().size() != 0) {
            for (int i = 0; i < productModel.getTagsModel().size(); i++) {
                tagsStr.append(productModel.getTagsModel().get(i).getTag()).append(", ");
            }
            HELPER.LOAD_HTML(binding.tagsLabel, "<b>Tags : <b>" + tagsStr);
        }
        Log.e("isReturnable", String.valueOf(productModel.isReturnAble())+"ss");
        if (productModel.isReturnAble()){
            binding.returnAbleLAbel.setText("Returnable");
        }else{
            binding.returnAbleLAbel.setText("Non-Returnable");
        }
        ProductEntity entity = cartRoomDatabase.productEntityDao().findProductByProductId(productModel.getId());
        if (entity != null) {
            productModel.setAddedToCart(true);
            productModel.setQty(entity.getQty());
            binding.addTextTxt.setText("Added to cart");
            isAddedToCart = true;
            binding.addCartContainer.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(defaultColor)));
            binding.cartIcon.setImageTintList(ColorStateList.valueOf(Color.WHITE));
            binding.addTextTxt.setTextColor(Color.WHITE);
            binding.counter.setText(productModel.getQty());
            productModel.setCalculatedAmount(entity.getCalculatedAmount());
            HELPER.LOAD_HTML(binding.price, act.getString(R.string.Rs) + productModel.getCalculatedAmount());
        } else {

            binding.addTextTxt.setText("Add to cart");
            isAddedToCart = false;
            binding.addCartContainer.setBackgroundTintList(null);
            binding.cartIcon.setImageTintList(ColorStateList.valueOf(Color.parseColor(defaultColor)));
            binding.addTextTxt.setTextColor((Color.BLACK));
            binding.counter.setText("1");

        }

    }

    public void changeState() {
        binding.addTextTxt.setText("Add to cart");
        binding.addCartContainer.setBackgroundTintList(null);
        binding.cartIcon.setImageTintList(ColorStateList.valueOf(Color.parseColor(defaultColor)));
        binding.addTextTxt.setTextColor(Color.parseColor(defaultColor));
    }
}