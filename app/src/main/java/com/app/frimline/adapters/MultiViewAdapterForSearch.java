package com.app.frimline.adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.ObserverActionID;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.databaseHelper.CartRoomDatabase;
import com.app.frimline.databinding.ItemProductSectionOneLayoutBinding;
import com.app.frimline.databinding.ItemProductSectionThreeLayoutBinding;
import com.app.frimline.databinding.ItemProductSectionTwoLayoutBinding;
import com.app.frimline.models.HomeFragements.ProductModel;
import com.app.frimline.models.HomeModel;
import com.app.frimline.models.LAYOUT_TYPE;
import com.app.frimline.models.roomModels.ProductEntity;
import com.app.frimline.screens.ProductDetailActivity;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;


public class MultiViewAdapterForSearch extends RecyclerView.Adapter {
    Activity activity;
    String colorCode = "";
    int parentLayoutPosition;
    private final ArrayList<HomeModel> dashBoardItemList;
    private boolean applyThemeColor = false;
    private final CartRoomDatabase cartRoomDatabase;


    public MultiViewAdapterForSearch(ArrayList<HomeModel> dashBoardItemList, Activity activity) {
        this.dashBoardItemList = dashBoardItemList;
        this.activity = activity;
        Gson gson = new Gson();
        cartRoomDatabase = CartRoomDatabase.getAppDatabase(activity);
    }

    public void setApplyThemeColor(boolean applyThemeColor) {
        this.applyThemeColor = applyThemeColor;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case LAYOUT_TYPE
                    .LAYOUT_ONE_PRODUCT:
                ItemProductSectionOneLayoutBinding layoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_product_section_one_layout, viewGroup, false);
                return new OneProductViewHolder(layoutBinding);
            case LAYOUT_TYPE
                    .LAYOUT_TWO_PRODUCT:
                ItemProductSectionTwoLayoutBinding twoLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_product_section_two_layout, viewGroup, false);
                return new TwoProductViewHolder(twoLayoutBinding);
            case LAYOUT_TYPE
                    .LAYOUT_THREE_PRODUCT:
                ItemProductSectionThreeLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_product_section_three_layout, viewGroup, false);
                return new ThreeProductViewHolder(binding);

        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {

        switch (dashBoardItemList.get(position).getLayoutType()) {
            case 0:
                return LAYOUT_TYPE
                        .LAYOUT_ONE_PRODUCT;
            case 1:
                return LAYOUT_TYPE
                        .LAYOUT_TWO_PRODUCT;

            case 2:
                return LAYOUT_TYPE
                        .LAYOUT_THREE_PRODUCT;
            default:
                return -1;
        }

    }

    @Override
    public int getItemCount() {
        return dashBoardItemList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final HomeModel model = dashBoardItemList.get(position);
        if (model != null) {
            switch (model.getLayoutType()) {
                case LAYOUT_TYPE.LAYOUT_ONE_PRODUCT:
                    loadDataForOneLayout(((OneProductViewHolder) holder).binding, dashBoardItemList.get(position), position);
                    break;
                case LAYOUT_TYPE.LAYOUT_TWO_PRODUCT:
                    loadDataForTwoLayout(((TwoProductViewHolder) holder).binding, dashBoardItemList.get(position), position);
                    break;
                case LAYOUT_TYPE.LAYOUT_THREE_PRODUCT:
                    loadDataForLayoutThree(((ThreeProductViewHolder) holder).binding, dashBoardItemList.get(position), position);
                    break;
            }
        }
    }

    private void loadDataForOneLayout(ItemProductSectionOneLayoutBinding binding, HomeModel model, int position) {
        if (applyThemeColor) {
            colorCode = new PREF(activity).getThemeColor();
        } else {
            colorCode = new PREF(activity).getCategoryColor();
        }
        ArrayList<ProductModel> productList = model.getApiProductModel();

        binding.productLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.SIMPLE_ROUTE(activity, ProductDetailActivity.class);
            }
        });

        //check cart db
        ProductEntity entity = cartRoomDatabase.productEntityDao().findProductByProductId(productList.get(0).getId());
        productList.get(0).setAddedToCart(entity != null);

        if (productList.get(0).isAddedToCart()) {
            binding.addCart1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorCode)));
        }else{
            binding.addCart1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
        }


        binding.addCart1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!productList.get(0).isAddedToCart()) {
                    productList.get(0).setAddedToCart(true);
                    Toast.makeText(activity, "Added to cart", Toast.LENGTH_SHORT).show();
                    binding.addCart1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorCode)));
                    cartRoomDatabase.productEntityDao().insert(HELPER.convertToCartObject(productList.get(0)));
                } else {
                    productList.get(0).setAddedToCart(false);
                    Toast.makeText(activity, "Removed from cart", Toast.LENGTH_SHORT).show();
                    binding.addCart1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
                    cartRoomDatabase.productEntityDao().deleteProduct(productList.get(0).getId());
                }
                //  HELPER.changeCartCounter(activity);

            }
        });
        Glide.with(activity).load(productList.get(0).getProductImagesList().get(0))
                .placeholder(R.drawable.ic_square_place_holder)
                .error(R.drawable.ic_square_place_holder)
                .into(binding.productImage1);
        HELPER.LOAD_HTML(binding.productName1, productList.get(0).getName());
        HELPER.LOAD_HTML(binding.productPrice1, activity.getString(R.string.Rs) + productList.get(0).getPrice());
        //first product
        binding.productLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, ProductDetailActivity.class);
                i.putExtra("productPosition", "0");
                i.putExtra("layoutType", String.valueOf(model.getLayoutType()));
                i.putExtra("itemPosition", String.valueOf(parentLayoutPosition));
                i.putExtra("adapterPosition", String.valueOf(position));
                i.putExtra("model", new Gson().toJson(productList.get(0)));
                i.putExtra("addToCartID", String.valueOf(ObserverActionID.SEARCH_ADDED_TO_CART));
                i.putExtra("removeCartID", String.valueOf(ObserverActionID.SEARCH_REMOVE_FROM_CART));
                activity.startActivity(i);
                activity.overridePendingTransition(R.anim.right_enter_second, R.anim.left_out_second);
            }
        });

    }

    public void loadDataForLayoutThree(ItemProductSectionThreeLayoutBinding binding, HomeModel position, int adapterPosition) {

        if (applyThemeColor) {
            colorCode = new PREF(activity).getThemeColor();
        } else {
            colorCode = new PREF(activity).getCategoryColor();
        }

        if (CONSTANT.API_MODE) {

            ArrayList<ProductModel> productList = position.getApiProductModel();

            HELPER.LOAD_HTML(binding.productName1, productList.get(0).getName());
            HELPER.LOAD_HTML(binding.productName2, productList.get(1).getName());
            HELPER.LOAD_HTML(binding.productName3, productList.get(2).getName());

            HELPER.LOAD_HTML(binding.productPrice1, activity.getString(R.string.Rs) + productList.get(0).getPrice());
            HELPER.LOAD_HTML(binding.productPrice2, activity.getString(R.string.Rs) + productList.get(1).getPrice());
            HELPER.LOAD_HTML(binding.productPrice3, activity.getString(R.string.Rs) + productList.get(2).getPrice());

            Glide.with(activity).load(productList.get(0).getProductImagesList().get(0))
                    .placeholder(R.drawable.ic_square_place_holder)
                    .error(R.drawable.ic_square_place_holder)
                    .into(binding.productImage1);
            Glide.with(activity).load(productList.get(1).getProductImagesList().get(0))
                    .placeholder(R.drawable.ic_square_place_holder)
                    .error(R.drawable.ic_square_place_holder)
                    .into(binding.productImage2);
            Glide.with(activity).load(productList.get(2).getProductImagesList().get(0))
                    .placeholder(R.drawable.ic_square_place_holder)
                    .error(R.drawable.ic_square_place_holder)
                    .into(binding.productImage3);


            //check cart db
            ProductEntity entity = cartRoomDatabase.productEntityDao().findProductByProductId(productList.get(0).getId());
            productList.get(0).setAddedToCart(entity != null);
            entity = cartRoomDatabase.productEntityDao().findProductByProductId(productList.get(1).getId());
            productList.get(1).setAddedToCart(entity != null);
            entity = cartRoomDatabase.productEntityDao().findProductByProductId(productList.get(2).getId());
            productList.get(2).setAddedToCart(entity != null);


            if (productList.get(0).isAddedToCart()) {
                binding.addCart1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorCode)));
            }else{
                binding.addCart1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
            }

            if (productList.get(1).isAddedToCart()) {
                binding.addCart2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorCode)));
            }else{
                binding.addCart2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
            }

            if (productList.get(2).isAddedToCart()) {
                binding.addCart3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorCode)));
            }else{
                binding.addCart3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
            }

            //first product
            binding.productLayout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(activity, ProductDetailActivity.class);
                    i.putExtra("productPosition", "0");
                    i.putExtra("layoutType", String.valueOf(position.getLayoutType()));
                    i.putExtra("itemPosition", String.valueOf(parentLayoutPosition));
                    i.putExtra("adapterPosition", String.valueOf(adapterPosition));
                    i.putExtra("model", new Gson().toJson(productList.get(0)));
                    i.putExtra("addToCartID", String.valueOf(ObserverActionID.SEARCH_ADDED_TO_CART));
                    i.putExtra("removeCartID", String.valueOf(ObserverActionID.SEARCH_REMOVE_FROM_CART));
                    activity.startActivity(i);
                    activity.overridePendingTransition(R.anim.right_enter_second, R.anim.left_out_second);
                }
            });
            binding.productLayout2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(activity, ProductDetailActivity.class);
                    i.putExtra("productPosition", "1");
                    i.putExtra("layoutType", String.valueOf(position.getLayoutType()));
                    i.putExtra("itemPosition", String.valueOf(parentLayoutPosition));
                    i.putExtra("adapterPosition", String.valueOf(adapterPosition));
                    i.putExtra("model", new Gson().toJson(productList.get(1)));
                    i.putExtra("addToCartID", String.valueOf(ObserverActionID.SEARCH_ADDED_TO_CART));
                    i.putExtra("removeCartID", String.valueOf(ObserverActionID.SEARCH_REMOVE_FROM_CART));
                    activity.startActivity(i);
                    activity.overridePendingTransition(R.anim.right_enter_second, R.anim.left_out_second);

                }
            });
            binding.productLayout3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(activity, ProductDetailActivity.class);
                    i.putExtra("productPosition", "2");
                    i.putExtra("layoutType", String.valueOf(position.getLayoutType()));
                    i.putExtra("itemPosition", String.valueOf(parentLayoutPosition));
                    i.putExtra("adapterPosition", String.valueOf(adapterPosition));
                    i.putExtra("model", new Gson().toJson(productList.get(2)));
                    i.putExtra("addToCartID", String.valueOf(ObserverActionID.SEARCH_ADDED_TO_CART));
                    i.putExtra("removeCartID", String.valueOf(ObserverActionID.SEARCH_REMOVE_FROM_CART));
                    activity.startActivity(i);
                    activity.overridePendingTransition(R.anim.right_enter_second, R.anim.left_out_second);
                }
            });

            if (applyThemeColor) {
                colorCode = new PREF(activity).getThemeColor();
            } else {
                colorCode = new PREF(activity).getCategoryColor();
            }

            binding.addCart1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!productList.get(0).isAddedToCart()) {
                        productList.get(0).setAddedToCart(true);

                        Toast.makeText(activity, "Added to cart", Toast.LENGTH_SHORT).show();
                        binding.addCart1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorCode)));
                        cartRoomDatabase.productEntityDao().insert(HELPER.convertToCartObject(productList.get(0)));
                    } else {
                        productList.get(0).setAddedToCart(false);

                        Toast.makeText(activity, "Removed from cart", Toast.LENGTH_SHORT).show();
                        binding.addCart1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
                        cartRoomDatabase.productEntityDao().deleteProduct(productList.get(0).getId());
                    }

                    //HELPER.changeCartCounter(activity);
                }
            });
            binding.addCart2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!productList.get(1).isAddedToCart()) {
                        productList.get(1).setAddedToCart(true);
                        Toast.makeText(activity, "Added to cart", Toast.LENGTH_SHORT).show();
                        binding.addCart2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorCode)));
                        cartRoomDatabase.productEntityDao().insert(HELPER.convertToCartObject(productList.get(1)));
                    } else {
                        productList.get(1).setAddedToCart(false);
                        Toast.makeText(activity, "Removed from cart", Toast.LENGTH_SHORT).show();
                        binding.addCart2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
                        cartRoomDatabase.productEntityDao().deleteProduct(productList.get(1).getId());
                    }

                    // HELPER.changeCartCounter(activity);
                }
            });
            binding.addCart3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!productList.get(2).isAddedToCart()) {
                        productList.get(2).setAddedToCart(true);
                        binding.addCart3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorCode)));
                        Toast.makeText(activity, "Added to cart", Toast.LENGTH_SHORT).show();
                        cartRoomDatabase.productEntityDao().insert(HELPER.convertToCartObject(productList.get(2)));
                    } else {
                        productList.get(2).setAddedToCart(false);
                        Toast.makeText(activity, "Removed from cart", Toast.LENGTH_SHORT).show();
                        binding.addCart3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
                        cartRoomDatabase.productEntityDao().deleteProduct(productList.get(2).getId());
                    }
                    //HELPER.changeCartCounter(activity);

                }
            });


        } else {


            binding.productLayout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HELPER.SIMPLE_ROUTE(activity, ProductDetailActivity.class);
                }
            });
            binding.productLayout2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HELPER.SIMPLE_ROUTE(activity, ProductDetailActivity.class);
                }
            });
            binding.productLayout3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HELPER.SIMPLE_ROUTE(activity, ProductDetailActivity.class);
                }
            });

            binding.addCart1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position.getProductList().get(0).isAddedToCart()) {
                        position.getProductList().get(0).setAddedToCart(true);
                        Toast.makeText(activity, "Added to cart", Toast.LENGTH_SHORT).show();
                        binding.addCart1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorCode)));
                    } else {
                        position.getProductList().get(0).setAddedToCart(false);
                        Toast.makeText(activity, "Removed from cart", Toast.LENGTH_SHORT).show();
                        binding.addCart1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
                    }


                }
            });

            binding.addCart2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position.getProductList().get(1).isAddedToCart()) {
                        position.getProductList().get(1).setAddedToCart(true);
                        Toast.makeText(activity, "Added to cart", Toast.LENGTH_SHORT).show();
                        binding.addCart2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorCode)));
                    } else {
                        position.getProductList().get(1).setAddedToCart(false);
                        Toast.makeText(activity, "Removed from cart", Toast.LENGTH_SHORT).show();
                        binding.addCart2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
                    }


                }
            });

            binding.addCart3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position.getProductList().get(2).isAddedToCart()) {
                        position.getProductList().get(2).setAddedToCart(true);
                        binding.addCart3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorCode)));
                        Toast.makeText(activity, "Added to cart", Toast.LENGTH_SHORT).show();
                    } else {
                        position.getProductList().get(2).setAddedToCart(false);
                        Toast.makeText(activity, "Removed from cart", Toast.LENGTH_SHORT).show();
                        binding.addCart3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
                    }

                }
            });
        }

    }

    public void loadDataForTwoLayout(ItemProductSectionTwoLayoutBinding binding, HomeModel position, int adapterPosition) {
        if (applyThemeColor) {
            colorCode = new PREF(activity).getThemeColor();
        } else {
            colorCode = new PREF(activity).getCategoryColor();
        }
        ArrayList<ProductModel> productList = position.getApiProductModel();
        HELPER.LOAD_HTML(binding.productName1, productList.get(0).getName());
        HELPER.LOAD_HTML(binding.productName2, productList.get(1).getName());
        HELPER.LOAD_HTML(binding.productPrice1, activity.getString(R.string.Rs) + productList.get(0).getPrice());
        HELPER.LOAD_HTML(binding.productPrice2, activity.getString(R.string.Rs) + productList.get(1).getPrice());


        //check cart db
        ProductEntity entity = cartRoomDatabase.productEntityDao().findProductByProductId(productList.get(0).getId());
        productList.get(0).setAddedToCart(entity != null);
        entity = cartRoomDatabase.productEntityDao().findProductByProductId(productList.get(1).getId());
        productList.get(1).setAddedToCart(entity != null);



        if (productList.get(0).isAddedToCart()) {
            binding.addCart1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorCode)));
        }else{
            binding.addCart1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
        }

        if (productList.get(1).isAddedToCart()) {
            binding.addCart2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorCode)));
        }else{
            binding.addCart2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
        }

        //load data
        Glide.with(activity).load(productList.get(0).getProductImagesList().get(0))
                .placeholder(R.drawable.ic_square_place_holder)
                .error(R.drawable.ic_square_place_holder)
                .into(binding.productImage1);
        Glide.with(activity).load(productList.get(1).getProductImagesList().get(0))
                .placeholder(R.drawable.ic_square_place_holder)
                .error(R.drawable.ic_square_place_holder)
                .into(binding.productImage2);

        binding.productLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, ProductDetailActivity.class);
                i.putExtra("productPosition", "0");
                i.putExtra("layoutType", String.valueOf(position.getLayoutType()));
                i.putExtra("itemPosition", String.valueOf(parentLayoutPosition));
                i.putExtra("adapterPosition", String.valueOf(adapterPosition));
                i.putExtra("model", new Gson().toJson(productList.get(0)));
                i.putExtra("addToCartID", String.valueOf(ObserverActionID.SEARCH_ADDED_TO_CART));
                i.putExtra("removeCartID", String.valueOf(ObserverActionID.SEARCH_REMOVE_FROM_CART));
                activity.startActivity(i);
                activity.overridePendingTransition(R.anim.right_enter_second, R.anim.left_out_second);
            }
        });
        binding.productLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, ProductDetailActivity.class);
                i.putExtra("productPosition", "1");
                i.putExtra("layoutType", String.valueOf(position.getLayoutType()));
                i.putExtra("itemPosition", String.valueOf(parentLayoutPosition));
                i.putExtra("adapterPosition", String.valueOf(adapterPosition));
                i.putExtra("model", new Gson().toJson(productList.get(1)));
                i.putExtra("addToCartID", String.valueOf(ObserverActionID.SEARCH_ADDED_TO_CART));
                i.putExtra("removeCartID", String.valueOf(ObserverActionID.SEARCH_REMOVE_FROM_CART));
                activity.startActivity(i);
                activity.overridePendingTransition(R.anim.right_enter_second, R.anim.left_out_second);
            }
        });

        binding.addCart1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!productList.get(0).isAddedToCart()) {
                    productList.get(0).setAddedToCart(true);
                    Toast.makeText(activity, "Added to cart", Toast.LENGTH_SHORT).show();
                    binding.addCart1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorCode)));
                    cartRoomDatabase.productEntityDao().insert(HELPER.convertToCartObject(productList.get(0)));
                } else {
                    productList.get(0).setAddedToCart(false);
                    Toast.makeText(activity, "Removed from cart", Toast.LENGTH_SHORT).show();
                    binding.addCart1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
                    cartRoomDatabase.productEntityDao().deleteProduct(productList.get(0).getId());
                }
                //HELPER.changeCartCounter(activity);

            }
        });
        binding.addCart2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!productList.get(1).isAddedToCart()) {
                    productList.get(1).setAddedToCart(true);
                    Toast.makeText(activity, "Added to cart", Toast.LENGTH_SHORT).show();
                    binding.addCart2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorCode)));
                    cartRoomDatabase.productEntityDao().insert(HELPER.convertToCartObject(productList.get(1)));
                } else {
                    productList.get(1).setAddedToCart(false);
                    Toast.makeText(activity, "Removed from cart", Toast.LENGTH_SHORT).show();
                    binding.addCart2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
                    cartRoomDatabase.productEntityDao().deleteProduct(productList.get(1).getId());
                }
                // HELPER.changeCartCounter(activity);
            }
        });
    }

    public void setPosition(int position) {
        parentLayoutPosition = position;
    }


    public class OneProductViewHolder extends RecyclerView.ViewHolder {
        ItemProductSectionOneLayoutBinding binding;

        public OneProductViewHolder(ItemProductSectionOneLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            changeColor();
        }

        public void changeColor() {
            PREF pref = new PREF(activity);
            binding.underLine1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));
            binding.addCart1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
        }
    }


    public class TwoProductViewHolder extends RecyclerView.ViewHolder {
        ItemProductSectionTwoLayoutBinding binding;

        public TwoProductViewHolder(ItemProductSectionTwoLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            changeColor();
        }
        public void changeColor(){
            PREF pref = new PREF(activity);
            String colorCode = "";
            if (applyThemeColor) {
                colorCode = pref.getThemeColor();
            } else {
                colorCode = pref.getCategoryColor();
            }
            binding.addCart1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
            binding.addCart2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
        }
    }

    public class ThreeProductViewHolder extends RecyclerView.ViewHolder {
        ItemProductSectionThreeLayoutBinding binding;

        public ThreeProductViewHolder(ItemProductSectionThreeLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            changeColor();
        }

        public void changeColor() {
            PREF pref = new PREF(activity);
            String colorCode = "";
            if (applyThemeColor) {
                colorCode = pref.getThemeColor();
            } else {
                colorCode = pref.getCategoryColor();
            }
            binding.underLineRight1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorCode)));
            binding.underLineRight.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorCode)));
            binding.underLine1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorCode)));
            binding.addCart1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
            binding.addCart2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
            binding.addCart3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
        }
    }


}

