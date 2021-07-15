package com.app.frimline;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.frimline.Common.HELPER;
import com.app.frimline.adapters.MultiSelectionAdapter;
import com.app.frimline.databinding.ActivitySplashBinding;
import com.app.frimline.fragments.CategoryRootFragment;
import com.app.frimline.models.UserListModel;
import com.app.frimline.screens.CategoryRootActivity;
import com.app.frimline.screens.LoginActivity;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends BaseActivity {
    private AnimatorSet animatorSet1;
    private ActivitySplashBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_splash);
        setStatusBarTransparent();
        final ObjectAnimator scaleAnimatiorXX = ObjectAnimator.ofFloat(binding.logo, "scaleX", 0, 1f);
        ObjectAnimator scaleAnimatiorYX = ObjectAnimator.ofFloat(binding.logo, "scaleY", 0, 1f);
        animatorSet1 = new AnimatorSet();
        animatorSet1.playTogether(scaleAnimatiorXX, scaleAnimatiorYX);
        animatorSet1.setDuration(3000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(act, LoginActivity.class);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                finish();
            }
        }, 1000);

//        loadImagesCategory();
    }
//
//    ArrayList<UserListModel> menuModels = new ArrayList<>();
//    ArrayList<UserListModel> selectedUserList = new ArrayList<>();
//
//    //Set Adaptor
//    public void setAdapter() {
//        MultiSelectionAdapter dasboardAddaptor = new MultiSelectionAdapter(menuModels, act);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act, RecyclerView.VERTICAL, false);
//        binding.recycler.setHasFixedSize(true);
//        binding.recycler.setLayoutManager(mLayoutManager);
//        binding.recycler.setAdapter(dasboardAddaptor);
//
//        binding.editFilter.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                dasboardAddaptor.getFilter().filter(s);
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        binding.button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e("Selected Data : ", new Gson().toJson(selectedUserList));
//            }
//        });
//
//        MultiSelectionAdapter.OnCheckedItem onCheckedItem = new MultiSelectionAdapter.OnCheckedItem() {
//            @Override
//            public void onCheck(String flag, UserListModel model, int pos) {
//                if (flag.equalsIgnoreCase("add")) {
//                    selectedUserList.add(model);
//                } else {
//                    //remove
//                    for (int i=0;i<selectedUserList.size();i++){
//                        if (selectedUserList.get(i).getId().equalsIgnoreCase(model.getId())){
//                            selectedUserList.remove(i);
//                            break;
//                        }
//                    }
//                }
//            }
//        };
//
//        dasboardAddaptor.setOnCheckedItem(onCheckedItem);
//
//
//    }
//
//    private void loadImagesCategory() {
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://queryfinders.com/brandmania/public/api/admin/getUsers", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.e("GET_USER_ADMIN : ", response);
//                try {
//
//                    JSONObject jsonObject = new JSONObject(response);
//                    JSONArray dataJson = jsonObject.getJSONArray("data");
//                    for (int i = 0; i < dataJson.length(); i++) {
//                        JSONObject userObject = dataJson.getJSONObject(i);
//                        UserListModel userListModel = new UserListModel();
//                        userListModel.setId(userObject.getString("id"));
//                        userListModel.setFirstName(userObject.getString("first_name"));
//                        userListModel.setLastName(userObject.getString("last_name"));
//                        userListModel.setPhoneNo(userObject.getString("phone"));
//                        userListModel.setEmailId(userObject.getString("email"));
//                        userListModel.setFirebaseToken(userObject.getString("firebase_token"));
//                        menuModels.add(userListModel);
//                    }
//
//
//                    if (menuModels != null && menuModels.size() != 0) {
//                        setAdapter();
//
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                        error.printStackTrace();
//
//                    }
//                }
//        ) {
//            /**
//             * Passing some request headers*
//             */
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Accept", "application/x-www-form-urlencoded");//application/json
//                params.put("Content-Type", "application/x-www-form-urlencoded");
//
//                Log.e("Token", params.toString());
//                return params;
//            }
//
//
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//
//
//                Log.e("DateNdClass", params.toString());
//                //params.put("upload_type_id", String.valueOf(Constant.ADD_NOTICE));
//
//                return params;
//            }
//
//        };
//        RequestQueue queue = Volley.newRequestQueue(act);
//        queue.add(stringRequest);
//    }
}