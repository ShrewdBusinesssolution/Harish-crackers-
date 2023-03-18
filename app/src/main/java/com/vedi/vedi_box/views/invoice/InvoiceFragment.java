package com.vedi.vedi_box.views.invoice;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vedi.vedi_box.R;
import com.vedi.vedi_box.adapters.InvoiceListAdapter;
import com.vedi.vedi_box.databinding.ActivityInvoiceBinding;
import com.vedi.vedi_box.listeners.BackPressListener;
import com.vedi.vedi_box.models.Invoice;
import com.vedi.vedi_box.utilities.Constants;
import com.vedi.vedi_box.utilities.PreferManager;
import com.vedi.vedi_box.views.track_order.TrackFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InvoiceFragment extends Fragment implements BackPressListener {

    public static BackPressListener listener;
    ActivityInvoiceBinding binding;
    private PreferManager preferManager;
    private MediaPlayer mp;
    private InvoiceListAdapter invoiceListAdapter;
    private InvoiceViewModel invoiceViewModel;
    private String userID, fullName, orderID, DeliveryCost;

    public static InvoiceFragment newInstance() {
        return new InvoiceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ActivityInvoiceBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Activity activity = getActivity();
        if (activity != null) {
            preferManager = new PreferManager(activity);
            mp = MediaPlayer.create(requireActivity(), R.raw.single_shot);
            userID = preferManager.getString(Constants.KEY_USER_ID);
            fullName = preferManager.getString(Constants.KEY_USER_FULL_NAME);
            orderID = preferManager.getString(Constants.KEY_PRODUCT_ID);
            DeliveryCost = preferManager.getString(Constants.KEY_DELIVERY_COST);


            invoiceViewModel = new ViewModelProvider(requireActivity()).get(InvoiceViewModel.class);

            binding.invoiceDetailsBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mp.start();
                    onBackPressed();
                }
            });


            binding.orderInvoiceRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false));
            binding.orderInvoiceRecyclerView.setHasFixedSize(true);


            invoiceViewModel.getInvoiceList(activity, userID, orderID).observe(getViewLifecycleOwner(), new Observer<List<Invoice>>() {
                @Override
                public void onChanged(List<Invoice> invoices) {
                    for (Invoice invoice : invoices) {

                        Invoice invoiceDetails = new Invoice(invoice.getOrder_id(), invoice.getOrder_date(), invoice.getProduct_name(), invoice.getProduct_quantity(), invoice.getProduct_pieces(), "₹ " + invoice.getProduct_rate(), "₹ " + invoice.getTotal_price(), invoice.getPayment_method(), invoice.getHouse_or_flat() + " " + invoice.getArea_street() + ",\n" + invoice.getTaluk() + ", " + invoice.getCity() + ",\n" + invoice.getState() + ",\n" + invoice.getPincode() + ".");
                        binding.setInvoice(invoiceDetails);

                        binding.orderInvoiceFullname.setText(fullName);
                        binding.orderInvoiceDeliveryCost.setText("₹ " + DeliveryCost);

                        String total = invoice.getTotal_price();
                        total = total.replaceAll("[^0-9]", "");



                        binding.orderInvoiceTotalcost.setText("₹ " + total);
                    }

                    invoiceListAdapter = new InvoiceListAdapter(requireActivity(), invoices);
                    invoiceListAdapter.notifyDataSetChanged();
                    binding.orderInvoiceRecyclerView.setAdapter(invoiceListAdapter);
                }
            });


        }
    }

    @Override
    public void onPause() {
        listener = null;
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        listener = this;
    }

    @Override
    public void onBackPressed() {
        listener = null;
        requireActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.fragment_container, new TrackFragment()).commit();

    }
}