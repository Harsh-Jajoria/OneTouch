package com.axepert.onetouch.ui.bookservice;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ObbInfo;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.axepert.onetouch.R;
import com.axepert.onetouch.databinding.FragmentBookServiceBinding;
import com.axepert.onetouch.requests.BookServiceRequest;
import com.axepert.onetouch.responses.BookServiceResponse;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class BookServiceFragment extends Fragment {
    private FragmentBookServiceBinding binding;
    private BookServiceViewModel viewModel;
    String service_id, service_name;
    TextView tvServiceType, tvName, tvContactNo, tvDate, tvEmail, tvAddress, tvNote, tvTime;
    ImageView imgClose;
    String[] timeSlots;
    ArrayAdapter<String> timeSlotAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBookServiceBinding.inflate(getLayoutInflater(), container, false);
        viewModel = new ViewModelProvider(this).get(BookServiceViewModel.class);

        if (getArguments() != null) {
            service_id = getArguments().getString("service_id");
            service_name = getArguments().getString("service_name");
        }
        setListener();
        return binding.getRoot();
    }

    private void setListener() {
        binding.btnBook.setOnClickListener(v -> {
            if (isValid()) {
                bookService();
            }
        });

        binding.tvDate.setOnClickListener(v -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select Date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build();

            datePicker.show(getActivity().getSupportFragmentManager(), "tag");

            datePicker.addOnPositiveButtonClickListener(selection -> {
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.setTimeInMillis(selection);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String formattedDate = format.format(calendar.getTime());
                binding.tvDate.setText(formattedDate);
            });
        });

        timeSlots = getResources().getStringArray(R.array.time_slot);
        timeSlotAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, timeSlots);
        binding.timeSlotAutoCompleteTV.setAdapter(timeSlotAdapter);

    }

    private void bookService() {
        BookServiceRequest bookServiceRequest = new BookServiceRequest(
                service_id,
                binding.tvName.getText().toString().trim(),
                binding.tvEmail.getText().toString().trim(),
                binding.tvContact.getText().toString().trim(),
                binding.tvAddress.getText().toString().trim(),
                binding.tvDate.getText().toString().trim(),
                binding.timeSlotAutoCompleteTV.getText().toString().trim(),
                binding.tvNote.getText().toString().trim());

        viewModel.bookService(bookServiceRequest).observe(requireActivity(), bookServiceResponse -> {
            if (bookServiceResponse != null) {
                if (bookServiceResponse.code == 200) {
                    new AlertDialog.Builder(requireActivity())
                            .setTitle("Service Booked")
                            .setMessage("Your service is successfully booked you will receive SMS or Email about your booking.")
                            .setCancelable(false)
                            .setPositiveButton("View Details", (dialog, which) -> {
                                bookedServiceBottomSheet();
                                dialog.dismiss();
                                binding.tvName.setText(null);
                                binding.tvEmail.setText(null);
                                binding.tvContact.setText(null);
                                binding.tvAddress.setText(null);
                                binding.tvDate.setText(null);
                                binding.timeSlotAutoCompleteTV.setText(null);
                                binding.tvNote.setText(null);
                            })
                            .setNegativeButton("Close", (dialog, which) -> Navigation.findNavController(binding.getRoot()).popBackStack())
                            .show();
                }
            }
        });
    }

    private boolean isValid() {
        if (binding.tvName.getText().toString().isEmpty()) {
            showToast("Enter your name");
            return false;
        } else if (binding.tvContact.getText().toString().isEmpty()) {
            showToast("Enter your contact number");
            return false;
        } else if (binding.tvContact.getText().toString().trim().length() != 10) {
            showToast("Enter correct contact number");
            return false;
        } else if (binding.tvAddress.getText().toString().isEmpty()) {
            showToast("Enter your address");
            return false;
        } else if (binding.tvDate.getText().toString().isEmpty()) {
            showToast("Enter service date");
            return false;
        } else if (binding.timeSlotAutoCompleteTV.getText().toString().isEmpty()) {
            showToast("Select time slot");
            return false;
        } else {
            return true;
        }
    }

    private void showToast(String message) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void bookedServiceBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity());
        BottomSheetBehavior<View> bottomSheetBehavior;
        View layoutView = LayoutInflater.from(requireActivity()).inflate(R.layout.bottomsheet_booked_service_details, null);
        bottomSheetDialog.setContentView(layoutView);
        bottomSheetDialog.setCancelable(false);

        tvServiceType = bottomSheetDialog.findViewById(R.id.tvServiceType);
        tvName = bottomSheetDialog.findViewById(R.id.tvName);
        tvContactNo = bottomSheetDialog.findViewById(R.id.tvContactNo);
        tvDate = bottomSheetDialog.findViewById(R.id.tvDate);
        tvEmail = bottomSheetDialog.findViewById(R.id.tvEmail);
        tvAddress = bottomSheetDialog.findViewById(R.id.tvAddress);
        tvNote = bottomSheetDialog.findViewById(R.id.tvNote);
        imgClose = bottomSheetDialog.findViewById(R.id.imgClose);
        tvTime = bottomSheetDialog.findViewById(R.id.tvTime);
        LinearLayout layout = bottomSheetDialog.findViewById(R.id.bottomSheetParent);

        bottomSheetBehavior = BottomSheetBehavior.from((View) layoutView.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        assert layout != null;
        layout.setMinimumHeight(Resources.getSystem().getDisplayMetrics().heightPixels);

        tvServiceType.setText(service_name);
        tvName.setText(binding.tvName.getText());
        tvContactNo.setText(binding.tvContact.getText());
        tvDate.setText(binding.tvDate.getText());
        tvTime.setText(binding.timeSlotAutoCompleteTV.getText().toString());
        tvEmail.setText(binding.tvEmail.getText());
        tvAddress.setText(binding.tvAddress.getText());
        tvNote.setText(binding.tvNote.getText());

        imgClose.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            Navigation.findNavController(binding.getRoot()).popBackStack();
        });

        bottomSheetDialog.show();
    }

}