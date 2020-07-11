package com.ajitapp.smartwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ajitapp.smartwork.fragments.BookingFragment;

public class SuccessOrderActivity extends AppCompatActivity {
    private Button bookingBtn;
    private TextView msg;
    Intent intent;
    String order_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_order);

        intent = getIntent();
        order_id = intent.getStringExtra("order_id");

        msg = findViewById(R.id.success_text);

        msg.setText("Yoou successfully booked at Smart Work, wait for our response. Your Book Id is BOOK"+order_id);
        bookingBtn = findViewById(R.id.bookings);

        bookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuccessOrderActivity.this, MainActivity.class);
                intent.putExtra("fragmentNumber",1); //for example
                startActivity(intent);
            }
        });


    }
}