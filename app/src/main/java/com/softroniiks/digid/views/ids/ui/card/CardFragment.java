package com.softroniiks.digid.views.ids.ui.card;

import static android.view.View.VISIBLE;

import android.app.AlertDialog;
import android.app.AsyncNotedAppOp;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.microblink.blinkcard.entities.recognizers.blinkcard.BlinkCardProcessingStatus;
import com.microblink.blinkcard.entities.recognizers.blinkcard.BlinkCardRecognizer;
import com.microblink.blinkcard.entities.recognizers.blinkcard.Issuer;
import com.microblink.blinkcard.image.Image;
import com.microblink.blinkcard.uisettings.BlinkCardUISettings;
import com.softroniiks.digid.R;
import com.softroniiks.digid.model.Card;
import com.softroniiks.digid.model.UserAndCard;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class CardFragment extends Fragment implements View.OnClickListener {
    private CardViewModel cardViewModel;
    private FloatingActionButton addCardFab;
    TextView revealCvvButton;
    TextView cvc, textHintCard;
    ImageView imageCreditCard;
    RecyclerView cardRecyclerView;
    RecyclerViewAdapterCard recyclerViewAdapterCard;

    Boolean isCvvRevealed;
    int cardIssImage;

    //Recognizers
    private BlinkCardRecognizer blinkCardRecognizer;
    private com.microblink.blinkcard.entities.recognizers.RecognizerBundle cardRecognizerBundle;

    Card card;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    private static final int BLINK_CARD_REQUEST_CODE = 3;
    private static String TAG = "CardFrag";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cardViewModel = new ViewModelProvider(this).get(CardViewModel.class);
        cardViewModel.initializeDb(requireContext());
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);

        addCardFab = view.findViewById(R.id.fabAddCard);
        cardRecyclerView = view.findViewById(R.id.cardRecyclerView);
        textHintCard = view.findViewById(R.id.text_hint_card);
        imageCreditCard = view.findViewById(R.id.imageCreditCard);

        blinkCardRecognizer = new BlinkCardRecognizer();
        blinkCardRecognizer.setReturnFullDocumentImage(true);
        blinkCardRecognizer.setExtractIban(true);

        cardRecognizerBundle = new com.microblink.blinkcard.entities.recognizers.RecognizerBundle(blinkCardRecognizer);

        //observer -update UI
        cardViewModel.getUserCards().observe(getViewLifecycleOwner(), new Observer<UserAndCard>() {
            @Override
            public void onChanged(UserAndCard userAndCard) {
                if (userAndCard != null) {
                    recyclerViewAdapterCard = new RecyclerViewAdapterCard(userAndCard.cards, requireContext());

                    if (recyclerViewAdapterCard.getItemCount() > 0) {
                        textHintCard.setVisibility(View.INVISIBLE);
                        imageCreditCard.setVisibility(View.INVISIBLE);
                    } else {
                        textHintCard.setVisibility(VISIBLE);
                        imageCreditCard.setVisibility(VISIBLE);
                    }

                    recyclerViewAdapterCard.notifyDataSetChanged();

                    recyclerViewAdapterCard.setOnItemClickListener(new RecyclerViewAdapterCard.OnItemClickListener() {
                        @Override
                        public void onItemClicked(int position) {
                            Card card = userAndCard.cards.get(position);

                            showCardPopUp(base64ToBitmap(card.getCardFrontImageUri()));
                        }

                        @Override
                        public void onDeleteClicked(int position) {
                            Card card = userAndCard.cards.get(position);

                            AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).create();

                            alertDialog.setTitle("DELETE CARD");
                            alertDialog.setMessage("Are you sure you want to delete your card ?");
                            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    AsyncTask.execute(() -> {
                                        cardViewModel.deleteCard(card);
                                    });

                                    recyclerViewAdapterCard.notifyItemRemoved(position);
                                }
                            });
                            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            alertDialog.show();

                        }
                    });

                    cardRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                    cardRecyclerView.setHasFixedSize(true);
                    cardRecyclerView.setAdapter(recyclerViewAdapterCard);


                } else {
                    recyclerViewAdapterCard = new RecyclerViewAdapterCard(null, requireContext());
                }
            }
        });

        return view;
    }

    public void startScanning() {
        BlinkCardUISettings cardUISettings = new BlinkCardUISettings(cardRecognizerBundle);

        com.microblink.blinkcard.uisettings.ActivityRunner.startActivityForResult(this, BLINK_CARD_REQUEST_CODE, cardUISettings);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addCardFab.setOnClickListener(v -> startScanning());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // load the data into all recognizers bundled within your RecognizerBundle
        try {
            cardRecognizerBundle.loadFromIntent(data);
        } catch (NullPointerException e) {
            Toast.makeText(requireContext(), "Card not recognized, try again", Toast.LENGTH_SHORT).show();
        }

        // you can get the result by invoking getResult on recognizer
        BlinkCardRecognizer.Result result = blinkCardRecognizer.getResult();

        if (result.getResultState() == com.microblink.blinkcard.entities.recognizers.Recognizer.Result.State.Valid) {

            BlinkCardProcessingStatus processingStatus = result.getProcessingStatus();

            if (processingStatus == BlinkCardProcessingStatus.Success) {

                StringBuilder cardInfo = new StringBuilder();
                cardInfo.append(result.getCardNumber()).append("\n").append(result.getCvv()).append("\n").append(result.getExpiryDate()).append("\n")
                        .append(result.getOwner()).append(result.getIban()).append(result.getIssuer());

                String[] expDate = result.getExpiryDate().toString().split(" ");
                Log.i(TAG, "The card info==>\n" + cardInfo.toString() + " " + expDate[expDate.length - 1]);

                //show dialogue with card
                assert result.getIssuer() != null;
                setCardType(result.getIssuer());


                showCardDialogue(result.getCardNumber(), result.getCvv(), result.getOwner(), expDate[expDate.length - 1], cardIssImage, result.getFirstSideFullDocumentImage());

                card = new Card(result.getCardNumber(), expDate[expDate.length - 1], result.getCvv(), result.getOwner(), result.getIssuer().toString(), cardIssImage,
                        bitmapToString(result.getFirstSideFullDocumentImage().convertToBitmap()));
                card.setOwnerId(mAuth.getUid());
            }

        }
    }

    private void setCardType(Issuer issuer) {
        switch (issuer) {
            case Visa:
                cardIssImage = R.drawable.mb_ic_visa;
                break;
            case Mastercard:
                cardIssImage = R.drawable.mb_ic_mastercard;
                break;
            case Verve:
                cardIssImage = R.drawable.mb_ic_default_card;
            case Maestro:
                cardIssImage = R.drawable.mb_ic_maestro;
            case AmericanExpress:
                cardIssImage = R.drawable.mb_ic_jcb;
            case DiscoverCard:
                cardIssImage = R.drawable.mb_ic_discover;
            default:
                cardIssImage = R.drawable.mb_ic_default_card;
        }
    }

    private void showCardDialogue(String cardNumber, String cvv, String fullName, String expDate, int issuer, Image cardFrImage) {
        View view = getLayoutInflater().inflate(R.layout.custom_credit_card_view, null);

        TextView number = view.findViewById(R.id.cardNumber);
        cvc = view.findViewById(R.id.debitCardCvv);
        TextView name = view.findViewById(R.id.cardFullName);
        TextView expiryDate = view.findViewById(R.id.debitCardExpDate);
        ImageView cardIssuer = view.findViewById(R.id.cardIssuerImage);
        ImageView cardFrontImage = view.findViewById(R.id.cardFrontImage);
        revealCvvButton = view.findViewById(R.id.revealCvvText);

        number.setText(cardNumber);
        cvc.setText(cvv);
        name.setText(fullName);
        expiryDate.setText(expDate);
        cardIssuer.setImageDrawable(ContextCompat.getDrawable(requireContext(), issuer));

        if (cardFrImage != null) {
            cardFrontImage.setImageBitmap(cardFrImage.convertToBitmap());
        } else {
            Log.i(TAG, "Card front image was null");
        }

        revealCvvButton.setOnClickListener(this);

        new AlertDialog.Builder(requireContext()).setView(view).setPositiveButton("Save", (dialog, which) -> addACard()).create().show();
    }

    @Override
    public void onClick(View v) {

        if (cvc.getVisibility() == View.INVISIBLE) {
            cvc.setVisibility(VISIBLE);
        } else {
            cvc.setVisibility(View.INVISIBLE);
        }
    }


    public void addACard() {
        AsyncTask.execute(() -> {
            cardViewModel.addCard(card);
        });
    }

    private void showCardPopUp(Bitmap cardFrontImage) {
        View view = getLayoutInflater().inflate(R.layout.pop_up_card, null);

        ImageView cardImage = view.findViewById(R.id.fullFrontCardImage);

        cardImage.setImageBitmap(cardFrontImage);
        cardImage.setVisibility(VISIBLE);

        new AlertDialog.Builder(requireContext()).setView(view).create().show();
    }

    private String bitmapToString(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bytes = stream.toByteArray();// Convert to byte array
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } else {
            return "";
        }
    }

    private Bitmap base64ToBitmap(String base64EncodedImage) {
        byte[] decodedString = Base64.decode(base64EncodedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
}