package com.ajitapp.smartwork.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajitapp.smartwork.Interfaces.ServiceClickListInterface;
import com.ajitapp.smartwork.R;
import com.ajitapp.smartwork.ServiceListActivity;
import com.ajitapp.smartwork.models.BookingModel;
import com.ajitapp.smartwork.models.ServiceModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class BookingListAdapter extends RecyclerView.Adapter<BookingListAdapter.MyViewHolder> {

    private List<BookingModel> arrayList;
    private Context context;

    SharedPreferences sharedPreferences;
    String token;
    public BookingListAdapter(Context applicationContext, List<BookingModel> arrayList) {
        this.arrayList = arrayList;
        this.context = applicationContext;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_history_row, parent, false);
        context = parent.getContext(); //to get the activity context use this line.

        final MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final String service_name = arrayList.get(position).getService_name();
        final String service_status = arrayList.get(position).getOrderStatus();
        final String date_of_order = arrayList.get(position).getBookingDate();
        final String service_price = arrayList.get(position).getService_price();
        final String service_image = arrayList.get(position).getService_image();

        final String d_of_order = date_of_order.substring(0, 10);

        holder.service_name.setText(service_name);

        holder.service_price.setText("Rs: "+service_price);
        holder.service_order_date.setText("Order Date " +d_of_order);
        holder.order_status.setText("Status: Processing");


        Picasso.with(context).load(service_image)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.service_image);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView service_name;
        private TextView service_price;
        private TextView service_order_date;
        private TextView order_status;
        private ImageView service_image;
        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);

//            sharedPreferences = context.getSharedPreferences("user", MODE_PRIVATE);
//            if (sharedPreferences.contains("token")) {
//                token = sharedPreferences.getString("token", null);
//            }
            service_name = itemView.findViewById(R.id.service_name);
            service_price = itemView.findViewById(R.id.service_price);
            service_order_date = itemView.findViewById(R.id.order_date);
            service_image = itemView.findViewById(R.id.service_image);
            order_status = itemView.findViewById(R.id.service_status);




//            incrementBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    serviceClickListInterface.incrementBtnClick(arrayList.get(getAdapterPosition()).getService_id(), display_data);
//                }
//            });

//            add_to_cart_tv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    serviceClickListInterface.incrementBtnClick(arrayList.get(getAdapterPosition()).getService_id());
//
//                }
//            });

//            decrementBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String display_data1 = display_data.getText().toString();
//                    int d_data =  Integer.parseInt(display_data1);
//
//                    if(d_data > 0) {
//                        d_data --;
//                        display_data.setText(String.valueOf(d_data));
//                    }
//
//                }
//            });
        }

    }

//    private void addToCart(int data, int position) {
//
//
//        Toast.makeText(context, String.valueOf(arrayList.get(position).getService_name()), Toast.LENGTH_SHORT).show();
//
//
//    }


}
