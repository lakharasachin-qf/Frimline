package com.app.frimline.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.frimline.Common.APIs;
import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.FRIMLINE;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.MySingleton;
import com.app.frimline.Common.ObserverActionID;
import com.app.frimline.Common.PREF;
import com.app.frimline.Common.ResponseHandler;
import com.app.frimline.R;
import com.app.frimline.databinding.FragmentMyAccountBinding;
import com.app.frimline.models.Billing;
import com.app.frimline.models.ProfileModel;
import com.app.frimline.screens.AddressesActivity;
import com.app.frimline.screens.EditProfileActivity;
import com.app.frimline.screens.LoginActivity;
import com.app.frimline.screens.SignupActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class MyAccountFragment extends BaseFragment {
    public interface OnDrawerAction {
        void changeFragment();
    }

    private OnDrawerAction drawerAction;

    public void setDrawerAction(OnDrawerAction drawerAction) {
        this.drawerAction = drawerAction;
    }

    private FragmentMyAccountBinding binding;
    ProfileModel model;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_account, parent, false);
        binding.editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.SIMPLE_ROUTE(getActivity(), EditProfileActivity.class);
            }
        });
        binding.addressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.SIMPLE_ROUTE(getActivity(), AddressesActivity.class);
            }
        });
        if (CONSTANT.API_MODE) {
            if (pref.isLogin()) {
                binding.NoDataFound.setVisibility(View.GONE);
                binding.loginContent.setVisibility(View.VISIBLE);
            } else {
                binding.NoDataFound.setVisibility(View.VISIBLE);
                binding.loginContent.setVisibility(View.GONE);
            }
        }
        binding.screenLoader.getIndeterminateDrawable().setColorFilter(Color.parseColor(pref.getThemeColor()), android.graphics.PorterDuff.Mode.MULTIPLY);
        binding.screenLoader.setProgressTintList(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));
        binding.includeLoginBtn.button.setText("Sign In");
        binding.includeSignupBtn.button.setText("Sign Up");
        binding.includeLoginBtn.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.SIMPLE_ROUTE(getActivity(), LoginActivity.class);
            }
        });
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.Logout();

                FRIMLINE.getInstance().getObserver().setValue(ObserverActionID.LOGOUT);
                if (CONSTANT.API_MODE) {
                    binding.NoDataFound.setVisibility(View.VISIBLE);
                    binding.loginContent.setVisibility(View.GONE);

                } else {
                    HELPER.SIMPLE_ROUTE(getActivity(), LoginActivity.class);
                }

            }
        });


        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FRIMLINE.getInstance().getObserver().setValue(ObserverActionID.LOGOUT);
                HELPER.SIMPLE_ROUTE(getActivity(), LoginActivity.class);
            }
        });

        binding.includeSignupBtn.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.SIMPLE_ROUTE(getActivity(), SignupActivity.class);
            }
        });

        if (CONSTANT.API_MODE) {
            model = pref.getUser();

            if (pref.isLogin() && model != null) {
                if (!model.getDisplayName().isEmpty()) {
                    binding.nameTxt.setText(model.getDisplayName());
                }

                if (!model.getEmail().isEmpty()) {
                    binding.emailTxt.setText(model.getEmail());
                } else {
                    binding.emailTxt.setVisibility(View.GONE);

                }


                if (model.getPhoneNo() != null && !model.getPhoneNo().isEmpty()) {
                    binding.mobileNo.setText(model.getPhoneNo());
                } else {
                    binding.mobileNo.setVisibility(View.GONE);

                }
                //binding..setText(model.getDisplayName());
            }
        }
        if (CONSTANT.API_MODE) {
            if (pref.isLogin()) {
                binding.loginContent.setVisibility(View.GONE);
                binding.NoDataFound.setVisibility(View.GONE);
                binding.screenLoader.setVisibility(View.VISIBLE);
                loadProfile();
            }
        }
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        pref = new PREF(act);
        if (CONSTANT.API_MODE) {
            model = pref.getUser();
            if (model != null) {
                if (!model.getDisplayName().isEmpty()) {
                    binding.nameTxt.setText(model.getDisplayName());
                }

                if (!model.getEmail().isEmpty()) {
                    binding.emailTxt.setText(model.getEmail());
                } else {
                    binding.emailTxt.setVisibility(View.GONE);

                }

                if (model.getPhoneNo() !=null && !model.getPhoneNo().isEmpty()) {
                    binding.mobileNo.setText(model.getPhoneNo());
                } else {
                    binding.mobileNo.setVisibility(View.GONE);

                }
                //binding..setText(model.getDisplayName());
            }
        }

    }

    @Override
    public void onViewStateRestored(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        changeColor();
    }

    public void changeColor() {
        PREF pref = new PREF(getActivity());

        HELPER.backgroundTint(act, binding.logoutBtn, true);
        HELPER.backgroundTint(act, binding.includeSignupBtn.button, true);
        HELPER.backgroundTint(act, binding.includeLoginBtn.button, true);
        HELPER.backgroundTint(act, binding.button, true);
        HELPER.backgroundTint(act, binding.orderIcon, true);
        HELPER.backgroundTint(act, binding.accountIcon, true);
        HELPER.backgroundTint(act, binding.addressIcon, true);
        HELPER.backgroundTint(act, binding.editIcon, true);
        HELPER.imageTint(act, binding.backgroundLayar, true);


        binding.addressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GradientDrawable drawable = (GradientDrawable) binding.container.getBackground();
                drawable.setStroke(2, Color.parseColor(pref.getThemeColor()));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        HELPER.SIMPLE_ROUTE(getActivity(), AddressesActivity.class);
                        drawable.setStroke(2, ContextCompat.getColor(getActivity(), R.color.cardViewBorder));
                    }
                }, 40);
            }
        });
        binding.orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GradientDrawable drawable = (GradientDrawable) binding.orderContainer.getBackground();
                drawable.setStroke(2, Color.parseColor(pref.getThemeColor()));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawerAction.changeFragment();
                        drawable.setStroke(2, ContextCompat.getColor(getActivity(), R.color.cardViewBorder));
                    }
                }, 40);
            }
        });
        binding.accountDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GradientDrawable drawable = (GradientDrawable) binding.accountDetailsContainer.getBackground();
                drawable.setStroke(2, Color.parseColor(pref.getThemeColor()));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        HELPER.SIMPLE_ROUTE(getActivity(), EditProfileActivity.class);
                        drawable.setStroke(2, ContextCompat.getColor(getActivity(), R.color.cardViewBorder));
                    }
                }, 40);
            }
        });
    }

    private boolean isLoading = false;

    private void loadProfile() {

        if (!isLoading)
            isLoading = true;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isLoading = false;
                binding.screenLoader.setVisibility(View.GONE);
                if (pref.isLogin()) {
                    binding.NoDataFound.setVisibility(View.GONE);
                    binding.loginContent.setVisibility(View.VISIBLE);
                } else {
                    binding.NoDataFound.setVisibility(View.VISIBLE);
                    binding.loginContent.setVisibility(View.GONE);
                }
                JSONObject object = ResponseHandler.createJsonObject(response);
                if (object != null) {
                    ProfileModel model = new ProfileModel();
                    model.setPhoneNo(ResponseHandler.getString(object, "user_phone"));
                    model.setDisplayName(ResponseHandler.getString(object, "user_display_name"));
                    model.setUserId(ResponseHandler.getString(object, "id"));
                    model.setEmail(ResponseHandler.getString(object, "email"));
                    model.setFirstName(ResponseHandler.getString(object, "first_name"));
                    model.setLastName(ResponseHandler.getString(object, "last_name"));
                    model.setRole(ResponseHandler.getString(object, "role"));
                    model.setUserName(ResponseHandler.getString(object, "username"));
                    model.setUserName(ResponseHandler.getString(object, "username"));
                    model.setAvatar(ResponseHandler.getString(object, "avatar_url"));


                    Billing billingAddress = new Billing();
                    billingAddress.setFirstName(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "billing"), "first_name"));
                    billingAddress.setLastName(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "billing"), "last_name"));
                    billingAddress.setCompany(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "billing"), "company"));
                    billingAddress.setAddress1(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "billing"), "address_1"));
                    billingAddress.setAddress2(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "billing"), "address_2"));
                    billingAddress.setCity(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "billing"), "city"));
                    billingAddress.setPostCode(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "billing"), "postcode"));
                    billingAddress.setCountry(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "billing"), "country"));
                    billingAddress.setState(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "billing"), "state"));
                    billingAddress.setEmail(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "billing"), "email"));
                    billingAddress.setPhone(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "billing"), "phone"));
                    model.setBillingAddress(billingAddress);

                    billingAddress = new Billing();
                    billingAddress.setFirstName(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "shipping"), "first_name"));
                    billingAddress.setLastName(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "shipping"), "last_name"));
                    billingAddress.setCompany(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "shipping"), "company"));
                    billingAddress.setAddress1(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "shipping"), "address_1"));
                    billingAddress.setAddress2(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "shipping"), "address_2"));
                    billingAddress.setCity(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "shipping"), "city"));
                    billingAddress.setPostCode(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "shipping"), "postcode"));
                    billingAddress.setCountry(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "shipping"), "country"));
                    billingAddress.setState(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "shipping"), "state"));
                    model.setShippingAddress(billingAddress);

                    pref.setUser(model);
                    loadApiData();
                }


            }


        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        isLoading = false;
                        binding.loginContent.setVisibility(View.GONE);
                        binding.NoDataFound.setVisibility(View.VISIBLE);
                        binding.screenLoader.setVisibility(View.GONE);
                    }
                }
        ) {
            /**
             * Passing some request headers*
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = getHeader();
                Log.e("HEADER", getHeader().toString());
                return getHeader();
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    public void loadApiData() {
        model = pref.getUser();
        if (model != null) {
            if (!model.getDisplayName().isEmpty()) {
                binding.nameTxt.setText(model.getDisplayName());
            }

            if (!model.getEmail().isEmpty()) {
                binding.emailTxt.setText(model.getEmail());
            } else {
                binding.emailTxt.setVisibility(View.GONE);
            }

            if (model.getPhoneNo() !=null && !model.getPhoneNo().isEmpty()) {
                binding.mobileNo.setText(model.getPhoneNo());
            } else {
                binding.mobileNo.setVisibility(View.GONE);

            }
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        super.update(observable, data);
        if (frimline.getObserver().getValue() == ObserverActionID.LOGIN) {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    binding.loginContent.setVisibility(View.GONE);
                    binding.NoDataFound.setVisibility(View.GONE);
                    binding.screenLoader.setVisibility(View.VISIBLE);
                    loadProfile();
                }
            });
        }

        if (frimline.getObserver().getValue() == ObserverActionID.LOGOUT) {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!new PREF(act).isLogin()) {

                        binding.loginContent.setVisibility(View.GONE);
                        binding.NoDataFound.setVisibility(View.VISIBLE);
                        binding.screenLoader.setVisibility(View.GONE);
                    }

                }
            });
        }
    }
}