package com.softroniiks.digid.views.ids.ui.identity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.microblink.entities.recognizers.Recognizer;
import com.microblink.entities.recognizers.RecognizerBundle;
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdCombinedRecognizer;
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdRecognizer;
import com.microblink.entities.recognizers.blinkid.generic.ProcessingStatus;
import com.microblink.image.Image;
import com.microblink.uisettings.ActivityRunner;
import com.microblink.uisettings.BlinkIdUISettings;
import com.softroniiks.digid.R;
import com.softroniiks.digid.model.DriverLicense;
import com.softroniiks.digid.model.UserAndDriverLicense;
import com.softroniiks.digid.utils.SpacesItemDecoration;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class IdentityFragment extends Fragment {

    private IdentityViewModel identityViewModel;
    private BlinkIdRecognizer blinkIdRecognizer;
    private RecognizerBundle recognizerBundle;
    BlinkIdRecognizer.Result idRecognizerResult;
    private final static String TAG = "Identity Fragment";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String selectedId = "";
    String[] fullName;
    String[] lastName;

    ActivityResultLauncher<Intent> activityResultLauncher;

    FloatingActionButton addIdentity;
    Spinner idSpinner;
    BottomSheetDialog bottomSheetDialog;
    RecyclerView recyclerView;
    RecyclerViewAdapterIdentity recyclerViewAdapterIdentity;
    LinearLayoutManager layoutManager;
    TextView hint;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        identityViewModel = new ViewModelProvider(this).get(IdentityViewModel.class);
        identityViewModel.initializeDb(requireContext());

        //activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::onScanActivityResult);

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_identity, container, false);

        addIdentity = root.findViewById(R.id.fabAddId);
        recyclerView = root.findViewById(R.id.identtityRecyclerView);
        hint = root.findViewById(R.id.text_hint_id);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        blinkIdRecognizer = new BlinkIdRecognizer();
        blinkIdRecognizer.setReturnFaceImage(true);

        recognizerBundle = new RecognizerBundle(blinkIdRecognizer);

        addIdentity.setOnClickListener(v -> showBottomSheet());

        identityViewModel.getUserWithDl().observe(getViewLifecycleOwner(), new Observer<UserAndDriverLicense>() {
            @Override
            public void onChanged(UserAndDriverLicense userAndDriverLicense) {

                if(userAndDriverLicense != null)
                recyclerViewAdapterIdentity = new RecyclerViewAdapterIdentity(userAndDriverLicense.driverLicense);
                layoutManager = new LinearLayoutManager(requireContext());

                if (recyclerViewAdapterIdentity.getItemCount() > 0) {
                    hint.setVisibility(View.INVISIBLE);
                } else {
                    hint.setVisibility(View.VISIBLE);
                }

                recyclerViewAdapterIdentity.setOnItemClickListener(new RecyclerViewAdapterIdentity.onItemClickListener() {
                    @Override
                    public void onItemClicked(int position) {
                        //Toast.makeText(requireContext(), "the position " + position, Toast.LENGTH_SHORT).show();

                        assert userAndDriverLicense != null;
                        DriverLicense driverLicense = userAndDriverLicense.driverLicense.get(position);

                        Toast.makeText(requireContext(), driverLicense.getFirstName().toLowerCase(Locale.ROOT), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onDeleteClicked(int position) {
                        DriverLicense driverLicense = userAndDriverLicense.driverLicense.remove(position);

                        AsyncTask.execute(() -> {
                            identityViewModel.deleteIdentity(driverLicense);
                        });

                        recyclerViewAdapterIdentity.notifyItemRemoved(position);
                    }
                });
                recyclerViewAdapterIdentity.notifyDataSetChanged();

                //Line -Divider
                //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
                recyclerView.addItemDecoration(new SpacesItemDecoration(20));

                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(recyclerViewAdapterIdentity);
                recyclerView.setLayoutManager(layoutManager);

            }
        });

    }

    public void startScanActivity() {
        // Settings for BlinkIdActivity
        BlinkIdUISettings settings = new BlinkIdUISettings(recognizerBundle);

        // Start activity
        ActivityRunner.startActivityForResult(this, 1, settings);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    //public void onScanActivityResult(ActivityResult result) {
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //get result
        if (resultCode == Activity.RESULT_OK) {
            bottomSheetDialog.dismiss();

            if (data != null && selectedId.equals("Drivers License")) {
                if (!data.hasExtra(BlinkIdCombinedRecognizer.VerificationConstants.MrzResult)) {//then drivers license

                    recognizerBundle.loadFromIntent(data);

                    idRecognizerResult = blinkIdRecognizer.getResult();

                    if (idRecognizerResult.getResultState() == Recognizer.Result.State.Valid) {
                        //alert dialog to show it, then save it
                        StringBuilder builder = new StringBuilder();
                        builder.append(idRecognizerResult.getFullName()).append("\n").append(idRecognizerResult.getSex()).append("\n").append(idRecognizerResult.getAddress()).append("\n")
                                .append(idRecognizerResult.getAge()).append("\n").append(idRecognizerResult.getDateOfBirth().getDate()).append(idRecognizerResult.getDateOfExpiry().getDate());

                        Log.i(TAG, builder.toString());

                        ProcessingStatus processingStatus = idRecognizerResult.getProcessingStatus();
                        if (processingStatus.equals(ProcessingStatus.Success)) {
                            Log.i(TAG, "Processing successful");

                            fullName = idRecognizerResult.getFullName().split(" ");

                            //for (String s : fullName) { Log.i(TAG, s); }

                            lastName = fullName[0].split(",");

                            if (idRecognizerResult.getFaceImage() != null) {
                                showDialogue(fullName[1] + "\n" + lastName[0], idRecognizerResult.getSex(), idRecognizerResult.getAge(), idRecognizerResult.getDateOfBirth().getDate()
                                        , idRecognizerResult.getDateOfExpiry().getDate(),
                                        idRecognizerResult.getDocumentNumber(), idRecognizerResult.getAddress(), idRecognizerResult.getDriverLicenseDetailedInfo().getVehicleClass(), idRecognizerResult.getFaceImage());
                            } else {
                                Log.i(TAG, "face image was null");
                            }

                        } else {
                            Log.i(TAG, "Processing unsuccessful");
                        }


                    } else {
                        //Toast
                        Toast.makeText(requireContext(), "Result gotten was invalid", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //scan for visa or passport
                    //TODO: Find unique identifier for visa or passport
                }
            } else if (data != null && selectedId.equals("Passport")) {  //level 1
                if (data.hasExtra("mrzResult")) {
                    recognizerBundle.loadFromIntent(data);

                    idRecognizerResult = blinkIdRecognizer.getResult();

                    if (idRecognizerResult.getResultState().equals(Recognizer.Result.State.Valid)) {

                        //idRecognizerResult.getMrzResult()
                    }

                }
            } else {

            }
        }
    }

    //}

    //Date needs to be stored as JDBC Sql format --. microblink date uses sql date
    public void showDialogue(String fullNamee, String sex, int age, com.microblink.results.date.Date dateOfBirth, com.microblink.results.date.Date dateOfExpiry, String docNum, String addrss, String vehicleClass, Image face) {
        View view = View.inflate(requireContext(), R.layout.custom_card_dialogue, null);

        TextView documentNumber = view.findViewById(R.id.documentNumber);
        TextView gender = view.findViewById(R.id.sex);
        TextView fullName = view.findViewById(R.id.fullName);
        TextView dob = view.findViewById(R.id.dateOfBirth);
        TextView doe = view.findViewById(R.id.expiryDate);
        TextView address = view.findViewById(R.id.address);
        TextView vechicleClass = view.findViewById(R.id.vehicleClass);
        ImageView driverFace = view.findViewById(R.id.driverLicenseImage);

        documentNumber.setText(docNum);
        gender.setText(sex);
        fullName.setText(fullNamee);
        dob.setText(dateOfBirth.toString());
        doe.setText(dateOfExpiry.toString());
        address.setText(addrss);
        vechicleClass.setText(vehicleClass);

        if (face != null) {
            Bitmap image = face.convertToBitmap();
            driverFace.setImageBitmap(image);
        } else {
            Log.i(TAG, "face image was null");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        builder.setTitle("Identity Information").setView(view);
        builder.setPositiveButton("Dismiss", (dialog, which) -> dialog.dismiss()).setNegativeButton("Save", (dialog, which) -> {
            storeData();
        });
        builder.create();
        builder.show();

    }

    private void showBottomSheet() {
        View view = getLayoutInflater().inflate(R.layout.custom_add_id_sheet, null);

        bottomSheetDialog = new BottomSheetDialog(requireContext());

        Button scan = view.findViewById(R.id.scan_button);
        Button close = view.findViewById(R.id.closeBottomSheet);

        idSpinner = view.findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(), R.array.identities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        idSpinner.setAdapter(adapter);

        idSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                long itemId = parent.getSelectedItemPosition();

                switch ((int) itemId) {
                    case 0:
                    case 1:
                    case 2:
                        selectedId = parent.getItemAtPosition(position).toString();
                        Log.i(TAG, "Selected Id:" + selectedId);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i(TAG, "No Item selected");
            }
        });

        scan.setOnClickListener(v -> startScanActivity());
        close.setOnClickListener(v -> bottomSheetDialog.dismiss());

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.create();
        bottomSheetDialog.show();

    }

    public Date convertMicroBlinkDate(com.microblink.results.date.Date date) {
        SimpleDateFormat s1 = new SimpleDateFormat("yyyy-mm-dd HH:mm");
        long longDate = 0;

        //micro-blink date to long, long to sql date
        try {
            Date date1 = (Date) s1.parse(date.toString());
            Log.i(TAG, "The Date before parse:: " + date1);
            assert date1 != null;

            longDate = Long.parseLong(date1.toString());
            Log.i(TAG, "The Date:: " + longDate);
        } catch (ParseException e) {
            System.out.println(e);
        }

        return new Date(longDate);
    }

    //TODO: Fix dob, doi, doe for db store, also store faceimage(think of what to store as BLOB ?) or store in firebase storage
    public void storeData() {
        DriverLicense driverLicense = new DriverLicense(
                convertMicroBlinkDate(idRecognizerResult.getDateOfBirth().getDate()),
                convertMicroBlinkDate(idRecognizerResult.getDateOfExpiry().getDate()),
                convertMicroBlinkDate(idRecognizerResult.getDateOfIssue().getDate()),
                idRecognizerResult.getDocumentNumber(), fullName[1],
                lastName[0],
                idRecognizerResult.getSex(), idRecognizerResult.getDriverLicenseDetailedInfo().getVehicleClass(), idRecognizerResult.getAddress());

        driverLicense.setOwnerId(mAuth.getUid());
        AsyncTask.execute(() -> {
            identityViewModel.addIdentity(driverLicense);
        });

        Toast.makeText(requireContext(), "Saved Successfully!", Toast.LENGTH_SHORT).show();
    }
}