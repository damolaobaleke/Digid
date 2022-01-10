package com.softroniiks.digid.views.ids.ui.identity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softroniiks.digid.R;
import com.softroniiks.digid.model.DriverLicense;
import com.softroniiks.digid.model.UserAndDriverLicense;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterIdentity extends RecyclerView.Adapter<RecyclerViewAdapterIdentity.IdentityViewHolder> {
    List<DriverLicense> driverLicenses;
    onItemClickListener mListener;

    RecyclerViewAdapterIdentity(List<DriverLicense> driverLicenses) {
        this.driverLicenses = driverLicenses;

    }

    @NonNull
    @Override
    public IdentityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_card_view, parent,false);
        IdentityViewHolder viewHolder = new IdentityViewHolder(view, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IdentityViewHolder holder, int position) {
        DriverLicense driverLicense = driverLicenses.get(position);

        holder.number.setText(driverLicense.getDocumentNumber());
        holder.dateOfBirth.setText(driverLicense.getDob().toString());
        holder.vehicleClass.setText(driverLicense.getVehicleClass());
        holder.expiryDate.setText(driverLicense.getDoe().toString());
        holder.sex.setText(driverLicense.getSex());
        holder.address.setText(driverLicense.getAddress());

        StringBuilder name = new StringBuilder();
        name.append(driverLicense.getFirstName()).append(" ").append(driverLicense.getLastName());
        holder.fullName.setText(name);

        //OPTION 1: Use picasso to load in absolute path image, currently Base64 String. URI only works for local resources in android
        //holder.driverImage.setImageURI(Uri.parse(driverLicense.getDriveImageUri()));

        //OPTION 2: Convert Base64 encoding stored back into Bitmap for display
        holder.driverImage.setImageBitmap(base64ToBitmap(driverLicense.getDriveImageUri()));
    }

    @Override
    public int getItemCount() {
        return driverLicenses.size();
    }

    //Click event
    public interface onItemClickListener {
        void onItemClicked(int position);

        void onDeleteClicked(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.mListener = listener;
    }
    //Click event

    static class IdentityViewHolder extends RecyclerView.ViewHolder {
        TextView number;
        TextView expiryDate;
        TextView vehicleClass;
        TextView dateOfBirth;
        TextView address;
        TextView sex;
        TextView fullName;
        ImageView driverImage;

        public IdentityViewHolder(@NonNull View itemView, onItemClickListener listener) {
            super(itemView);

            number = itemView.findViewById(R.id.documentNumber);
            expiryDate = itemView.findViewById(R.id.expiryDate);
            vehicleClass = itemView.findViewById(R.id.vehicleClass);
            dateOfBirth = itemView.findViewById(R.id.dateOfBirth);
            address = itemView.findViewById(R.id.address);
            sex = itemView.findViewById(R.id.sex);
            fullName = itemView.findViewById(R.id.full_name);
            driverImage = itemView.findViewById(R.id.driverLicenseImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        if (getAdapterPosition() != RecyclerView.NO_POSITION)
                            listener.onItemClicked(getAdapterPosition());
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null) {
                        if (getAdapterPosition() != RecyclerView.NO_POSITION) {
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
