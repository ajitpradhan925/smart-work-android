package com.ajitapp.smartwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ajitapp.smartwork.Interfaces.SubTaskClickListInterface;
import com.ajitapp.smartwork.adapters.SubTaskListAdapter;
import com.ajitapp.smartwork.models.SubTaskModel;
import com.ajitapp.smartwork.utils.SharedPreferenceClass;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SubTaskListActivity extends AppCompatActivity implements SubTaskClickListInterface {
    Intent intent;
    String task_id;
    RecyclerView subTask_list_view;

    Toolbar toolbar;
    TextView task_description, task_header;
    private TextView textCartItemCount;
    List<SubTaskModel> arrayList;

    LinearLayout empty_layout;

    private String token;
    SharedPreferences loginPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subtask_list);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubTaskListActivity.this, MainActivity.class);
                intent.putExtra("fragmentNumber",2); //for example
                startActivity(intent);
            }
        });
        intent = getIntent();
        task_id = intent.getStringExtra("task_id");

        empty_layout = findViewById(R.id.empty_layout);

        loginPreferences = getSharedPreferences("user", MODE_PRIVATE);
        if (loginPreferences.contains("token")) {
            token = loginPreferences.getString("token",null);
        }

        task_description = findViewById(R.id.task_description);
        arrayList = new ArrayList<>();
        subTask_list_view = findViewById(R.id.subtask_list);

        task_header = findViewById(R.id.subtask_list_header);

        getTaskLists();

    }

    private void getTaskLists() {

        final ProgressDialog progressDialog = new ProgressDialog(SubTaskListActivity.this);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_dialog_background);

        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );


        subTask_list_view.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));

        final String url = "https://taskprovider.herokuapp.com/admin/subtask/subtask_by_id/?id="+task_id;

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.getBoolean("success")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            JSONObject taskData = jsonObject1.getJSONObject("task");

                            task_description.setText(taskData.getString("taskDescription"));
                            task_header.setText(taskData.getString("taskName") + " Categories");
                            SubTaskModel taskModel = new SubTaskModel();
                                   taskModel.setSubTaskName(jsonObject1.getString("subtask_name"));
                                    taskModel.setSubTaskId(jsonObject1.getString("_id"));

                                    taskModel.setSubTaskImage(jsonObject1.getString("subtask_icon"));

                                    taskModel.setSubTaskDesc(jsonObject1.getString("slogan"));

                                    taskModel.setTaskDescription(taskData.getString("taskDescription"));
                                    taskModel.setTaskName(taskData.getString("_id"));
                                    taskModel.setTaskName(taskData.getString("taskName"));


                            arrayList.add(taskModel);
                        }

                        SubTaskListAdapter taskListAdapter = new SubTaskListAdapter(getApplicationContext(), arrayList,
                                SubTaskListActivity.this);

                        if(taskListAdapter.getItemCount() == 0) {
                            empty_layout.setVisibility(View.VISIBLE);
                            subTask_list_view.setVisibility(View.GONE);
                            task_header.setText("Empty Sub Task");
                            }
                        subTask_list_view.setAdapter(taskListAdapter);
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(SubTaskListActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();

            }
        });

//        {
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("id", task_id);
//                return super.getParams();
//            }
//        };

        int socketTimeout = 3000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    @Override
    public void onSubTaskItemClick(int position) {
        Intent intent = new Intent(SubTaskListActivity.this, ServiceListActivity.class);
        intent.putExtra("subtask_id", arrayList.get(position).getSubTaskId());
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.product_menu, menu);
        final MenuItem menuItem = menu.findItem(R.id.ic_cart);
        View actionView = menuItem.getActionView();
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);


        SharedPreferenceClass sharedPreferenceClass = new SharedPreferenceClass(getApplicationContext());

        int cart_count = sharedPreferenceClass.getValue_int("cart_value");
        setupBadge(cart_count);


        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });


        return true;
    }
    private void setupBadge(int mCartCount) {
        if (textCartItemCount != null) {
            if (mCartCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                    textCartItemCount.setText(String.valueOf(mCartCount));
                }
            }


        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.ic_cart) {
            if (loginPreferences.contains("token")) {
                startActivity(new Intent(getApplicationContext(), CartActivity.class));
            } else {
                showLoginDialog();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }



    private void showLoginDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Login to use cart or view.")
                .setPositiveButton("Login", null) //Set to null. We override the onclick
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(SubTaskListActivity.this, "cancel", Toast.LENGTH_SHORT).show();
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
                        startActivity(new Intent(SubTaskListActivity.this, LoginActivity.class));

                        Toast.makeText(SubTaskListActivity.this, "Positive", Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                        //Dismiss once everything is OK.

                    }
                });
            }
        });
        dialog.show();

    }
}

