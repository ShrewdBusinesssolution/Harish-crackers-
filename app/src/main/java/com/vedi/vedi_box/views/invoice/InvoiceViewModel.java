package com.vedi.vedi_box.views.invoice;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.vedi.vedi_box.models.Invoice;
import com.vedi.vedi_box.repositories.InvoiceRepo;

import java.util.List;

public class InvoiceViewModel extends ViewModel {
    InvoiceRepo invoiceRepo = new InvoiceRepo();

    public LiveData<List<Invoice>> getInvoiceList(Activity activity, String userID, String orderID) {
        return invoiceRepo.getInvoiceList(activity, userID, orderID);
    }
}