package com.softroniiks.digid.views.ids.ui.card;

import static android.view.View.INVISIBLE;
import static androidx.recyclerview.widget.RecyclerView.NO_POSITION;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.softroniiks.digid.R;
import com.softroniiks.digid.model.Card;

import java.util.List;

public class RecyclerViewAdapterCard extends RecyclerView.Adapter<RecyclerViewAdapterCard.CardViewHolder> {
    List<Card> cards;
    OnItemClickListener mListener;
    Context context;

    public RecyclerViewAdapterCard(List<Card> cards, Context context){
        this.cards = cards;
        this.context = context;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.debit_card, parent, false);
        CardViewHolder viewHolder = new CardViewHolder(view, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Card card = cards.get(position);

        holder.cardNumber.setText(card.getCardNumber());
        holder.fullName.setText(card.getFullName());
        holder.cardType.setImageDrawable(ContextCompat.getDrawable(context, card.getCardIssuerImageId()));
        holder.expDate.setText(card.getExpiryDate());
        holder.cvv.setText(card.getCvv());

        holder.revealCvv.setOnClickListener(v->{
            if(holder.cvv.getVisibility() == INVISIBLE){
                holder.cvv.setVisibility(View.VISIBLE);
            }else{
                holder.cvv.setVisibility(INVISIBLE);
            }
        });

        holder.fullFrontImage.setImageBitmap(base64ToBitmap(card.getCardFrontImageUri()));

    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    //click event and listeners
    interface OnItemClickListener{
        void onItemClicked(int position);
        void onDeleteClicked(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
    //

    static class CardViewHolder extends RecyclerView.ViewHolder{
        TextView cardNumber;
        TextView cvv;
        TextView expDate;
        TextView fullName;
        ImageView fullFrontImage;
        ImageView cardType;
        Button revealCvv;

        public CardViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            cardNumber = itemView.findViewById(R.id.debitCardNumber);
            cvv = itemView.findViewById(R.id.debitCardCvv);
            expDate = itemView.findViewById(R.id.debitCardExpDate);
            fullName = itemView.findViewById(R.id.debitCardFullName);
            cardType = itemView.findViewById(R.id.debitCardIssuerImage);
            fullFrontImage =itemView.findViewById(R.id.debitCardFrontImage);
            revealCvv = itemView.findViewById(R.id.showCVV);

            itemView.setOnClickListener(v->{
                if(listener != null){
                    if(getAdapterPosition() != NO_POSITION){
                        listener.onItemClicked(getAdapterPosition());
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(listener != null){
                        if(getAdapterPosition() != NO_POSITION){
                            listener.onDeleteClicked(getAdapterPosition());
                        }
                    }
                    return true;
                }
            });


        }
    }

    private Bitmap base64ToBitmap(String base64EncodedImage){
        byte[] decodedString = Base64.decode(base64EncodedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
}
