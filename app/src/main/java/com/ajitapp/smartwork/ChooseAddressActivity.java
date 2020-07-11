package com.ajitapp.smartwork;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ajitapp.smartwork.Interfaces.AddressClickListener;
import com.ajitapp.smartwork.adapters.AddressListAdapter;
import com.ajitapp.smartwork.adapters.CartListAdapter;
import com.ajitapp.smartwork.models.AddressModal;
import com.ajitapp.smartwork.utils.SharedPreferenceClass;
import com.ajitapp.smartwork.utils.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseAddressActivity extends AppCompatActivity implements AddressClickListener {
    private SharedPreferenceClass sharedPreferenceClass;
    private RecyclerView recyclerView;
    private List<AddressModal> arrayList;
    private AddressListAdapter addressListAdapter;

    String token;

    LayoutInflater factory;
    AlertDialog addressDialog, paymentDialog;
    private TextView add_address;
    ImageView close_dialog;
    ProgressBar progressBar;

    LinearLayout empty_layout;

    private int total_price;

    EditText addr1Field, addr2Field, stateField, countryField, postalCodeField, cityField;
    String addr1Value, addr2Value, stateValue, countryValue, postalCodeValue, cityValue;
    Button addButton, makePayment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_address);


        Intent intent = getIntent();

        total_price = intent.getIntExtra("total_price", 0);



        empty_layout = findViewById(R.id.empty_layout);

        recyclerView = findViewById(R.id.address_list);

        add_address = findViewById(R.id.add_address);

        makePayment = findViewById(R.id.buy_btn);

        makePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPaymentDialog();
            }
        });


        sharedPreferenceClass = new SharedPreferenceClass(this);
        token = sharedPreferenceClass.getValue_string("token");


        add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddAddressDialog();
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        getAuthUser(token);
    }

    private void showAddAddressDialog() {

        factory = LayoutInflater.from(ChooseAddressActivity.this);
        final View addressDialogView = factory.inflate(R.layout.address_custom_alert_layout, null);
        addressDialog = new AlertDialog.Builder(ChooseAddressActivity.this, R.style.myFullscreenAlertDialogStyle).create();

        close_dialog = addressDialogView.findViewById(R.id.close_alert);

        close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressDialog.dismiss();
            }
        });

            addButton = addressDialogView.findViewById(R.id.addAddressBtn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addr1Field = addressDialogView.findViewById(R.id.addr1);
                addr2Field = addressDialogView.findViewById(R.id.addr2);
                stateField = addressDialogView.findViewById(R.id.state);
                countryField = addressDialogView.findViewById(R.id.country);
                postalCodeField = addressDialogView.findViewById(R.id.postalCode);
                cityField = addressDialogView.findViewById(R.id.city);

                progressBar = addressDialogView.findViewById(R.id.progressBar);


                addr1Value = addr1Field.getText().toString();
                addr2Value = addr2Field.getText().toString();
                cityValue = cityField.getText().toString();

                stateValue = stateField.getText().toString();
                countryValue = countryField.getText().toString();
                postalCodeValue = postalCodeField.getText().toString();



                if(validateAddress()) {
                    addAddress(addr1Value, addr2Value, cityValue, stateValue, countryValue, postalCodeValue);
                }
            }
        });



        addressDialog.setView(addressDialogView);

        addressDialog.show();
    }

    private void showPaymentDialog() {

        factory = LayoutInflater.from(ChooseAddressActivity.this);
        final View paymentDialogView = factory.inflate(R.layout.payment_alert_sheet, null);
        paymentDialog = new AlertDialog.Builder(ChooseAddressActivity.this, R.style.myFullscreenAlertDialogStyle).create();


        TextView cash_payment = paymentDialogView.findViewById(R.id.cash_payment);
        cash_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showConfirmOrderDialog();
            }
        });



//        close_dialog = paymentDialogView.findViewById(R.id.close_alert);
//
//        close_dialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                paymentDialog.dismiss();
//            }
//        });


        paymentDialog.setView(paymentDialogView);

        paymentDialog.show();

    }


    private void showConfirmOrderDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Confirm Order")
                .setPositiveButton("Yes", null) //Set to null. We override the onclick
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(ServiceListActivity.this, "cancel", Toast.LENGTH_SHORT).show();
                    }
                })
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        addToOrder();

                        dialog.dismiss();
                        //Dismiss once everything is OK.

                    }
                });
            }
        });
        dialog.show();

    }

    private void addToOrder() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_dialog_background);

        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        String apiKey = "https://taskprovider.herokuapp.com/user/order/add_order";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, apiKey, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if(response.getBoolean("success")) {
                        JSONObject jsonObject = response.getJSONObject("cart");
                        Intent intent = new Intent(ChooseAddressActivity.this, SuccessOrderActivity.class);
                        intent.putExtra("order_id", jsonObject.getString("_id"));

                        startActivity(intent);
                        finish();


                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                            }
                        }, 4000);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                        }
                    }, 4000);

                    Toast.makeText(ChooseAddressActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(ChooseAddressActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept","application/json");
                params.put("Content-Type","application/json");
                params.put("x-auth-token", token);

                return params;
            }


        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        jsonObjectRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }

    private void addAddress(String addr1, String addr2, String city, String state, String country, String postalCode) {

        progressBar.setVisibility(View.VISIBLE);
        HashMap<String, String> body = new HashMap<>();
        body.put("addr1", addr1);
        body.put("addr2", addr2);
        body.put("city", city);
        body.put("state", state);
        body.put("country", country);
        body.put("postalCode", postalCode);

        String apiKey = "https://taskprovider.herokuapp.com/user/address/add_address";
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, apiKey, new JSONObject(body),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (response.getBoolean("success")) {

                                addressDialog.dismiss();


                                getAuthUser(token);

                            }

                            progressBar.setVisibility(View.GONE);

                        } catch (JSONException j) {
                            j.printStackTrace();
                            progressBar.setVisibility(View.GONE);

                            Toast.makeText(ChooseAddressActivity.this, j.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));

                        JSONObject obj = new JSONObject(res);
                        Toast.makeText(ChooseAddressActivity.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();

                        progressBar.setVisibility(View.GONE);

                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                        Toast.makeText(ChooseAddressActivity.this, e1.toString(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);

                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        Toast.makeText(ChooseAddressActivity.this, e2.toString(), Toast.LENGTH_SHORT).show();
                        e2.printStackTrace();
                        progressBar.setVisibility(View.GONE);

                    }
                }
            }
        })  {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept","application/json");
                params.put("Content-Type","application/json");
                params.put("x-auth-token", token);

                return params;
            }


        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    public boolean validateAddress() {
        boolean isValidated = false;

        if(!TextUtils.isEmpty(addr1Value)) {
            if(!TextUtils.isEmpty(addr2Value)) {
                if(!TextUtils.isEmpty(cityValue)) {
                    if(!TextUtils.isEmpty(stateValue)) {

                        if(!TextUtils.isEmpty(countryValue)) {

                            if(!TextUtils.isEmpty(postalCodeValue)) {

                                isValidated = true;
                            }else {
                                addr1Field.setError("Enter postal code");

                            }
                        } else {
                            countryField.setError("Enter country name");
                        }
                    } else {
                        stateField.setError("Enter state name");
                    }

                } else {
                    cityField.setError("Enter city name");
                }
            }else {
                addr2Field.setError("Enter address line 2");
            }

        } else {
            addr1Field.setError("Enter address line 1");
        }

        return isValidated;
    }

    private void getAuthUser(final String token) {

        arrayList = new ArrayList<>();

        String url = "https://taskprovider.herokuapp.com/user/auth";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject jsonObject = response.getJSONObject("user");
                    JSONArray addressArray = jsonObject.getJSONArray("address");

                    for( int i = 0; i < addressArray.length(); i ++) {
                        JSONObject jsonObject1 = addressArray.getJSONObject(i);

                        AddressModal addressModal = new AddressModal();


                        addressModal.setAddressId(jsonObject1.getString("_id"));


                        addressModal.setAddr1(jsonObject1.getString("addr1"));
                        addressModal.setAddr2(jsonObject1.getString("addr2"));
                        addressModal.setCity(jsonObject1.getString("city"));
                        addressModal.setCountry(jsonObject1.getString("country"));
                        addressModal.setState(jsonObject1.getString("state"));
                        addressModal.setPostalCode(jsonObject1.getString("postalCode"));


                        arrayList.add(addressModal);
                    }

                    addressListAdapter = new AddressListAdapter(getApplicationContext(), arrayList, ChooseAddressActivity.this);

                    if(addressListAdapter.getItemCount() == 0) {

                        recyclerView.setVisibility(View.GONE);
                        makePayment.setVisibility(View.GONE);
                        empty_layout.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "Empty Item", Toast.LENGTH_SHORT).show();

                    }
                    recyclerView.setAdapter(addressListAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept","application/json");
                params.put("Content-Type","application/json");
                params.put("x-auth-token", token);

                return params;
            }


        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        jsonObjectRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }

    @Override
    public void clickAddressRadioButton(int position) {
        makePayment.setVisibility(View.VISIBLE);
        Toast.makeText(this, arrayList.get(position).getAddressId(), Toast.LENGTH_SHORT).show();
    }
}
