package com.softroniiks.digid.views.ids.ui.invoice;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InvoiceViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public InvoiceViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}