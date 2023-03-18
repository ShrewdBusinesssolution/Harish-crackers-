package com.vedi.vedi_box.views.accoount_info;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.vedi.vedi_box.models.Bank;
import com.vedi.vedi_box.repositories.AccountRepo;

import java.util.List;

public class AccountInfoViewModel extends ViewModel {

    AccountRepo accountRepo = new AccountRepo();

    public LiveData<List<Bank>> getBank() {
        return accountRepo.getBank();
    }
}