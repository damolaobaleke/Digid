package com.softroniiks.digid.views.ids.ui.card;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.softroniiks.digid.R;


public class CardFragment extends Fragment {
    private CardViewModel cardViewModel;
    private FloatingActionButton addCardFab;

    //Recognizers


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cardViewModel = new ViewModelProvider(this).get(CardViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  =  inflater.inflate(R.layout.fragment_card, container, false);

        addCardFab = view.findViewById(R.id.fabAddCard);

        //observer -update UI



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }

    public void addACard(){

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}