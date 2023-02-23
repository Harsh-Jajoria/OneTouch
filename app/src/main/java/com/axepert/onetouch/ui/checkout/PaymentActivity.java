package com.axepert.onetouch.ui.checkout;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.axepert.onetouch.databinding.ActivityPaymentBinding;
import com.razorpay.Checkout;
import com.razorpay.Order;
import com.razorpay.PaymentResultListener;
import com.razorpay.RazorpayClient;

import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {
    private ActivityPaymentBinding binding;
    private static final String TAG = "Payment_Activity";
    final Activity activity = this;
    private String orderId, trnxId, total, email, phone;
    private int finalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Checkout.preload(getApplicationContext());
        getDataFromIntent();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            orderId = intent.getStringExtra("order_id");
            trnxId = intent.getStringExtra("trnx_id");
            total = intent.getStringExtra("amount");
            email = intent.getStringExtra("email");
            phone = intent.getStringExtra("phone");
            int amt = Integer.parseInt(total);
            finalAmount = amt * 100;
            Log.d(TAG, String.format("Order_id - %s\nTrnx_id - %s\nTotal - %s\nEmail - %s\nPhone - %s", orderId, trnxId, total, email, phone));
            makePayment();
       //     createOrder();
        }
    }

    private void createOrder() {
        try {
            RazorpayClient razorpay = new RazorpayClient("rzp_test_tkhRiPIeDW5760", "j46YDUAo0k2KpqekZB4R0qey");
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", finalAmount); // amount in the smallest currency unit
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "receipt#1");
            Order order = razorpay.Orders.create(orderRequest);
            Log.d(TAG, "createOrder: " + order);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "createOrder: " + e.getMessage());
        }
    }

    private void makePayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_hiCNPTXxEZEtZo");
        try {
            JSONObject options = new JSONObject();
            options.put("name", "OneTouch");
            options.put("description", "OneTouch");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg");
           // options.put("order_id", orderId);
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", finalAmount);//pass amount in currency subunits
            options.put("prefill.email", email);
            options.put("prefill.contact",phone);
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);
            checkout.open(activity, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Log.d(TAG, "onPaymentSuccess: " + s);
        Intent intent = new Intent();
        intent.putExtra("razorpay_id", s);
        setResult(200, intent);
        super.onBackPressed();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.d(TAG, "onPaymentError: " + s);
        Intent intent = new Intent();
        intent.putExtra("error", s);
        setResult(400, intent);
        super.onBackPressed();
    }
}