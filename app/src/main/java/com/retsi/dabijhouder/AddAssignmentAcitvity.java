package com.retsi.dabijhouder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddAssignmentAcitvity extends AppCompatActivity {

    private RadioGroup opdrachtOpties;
    private EditText edtOpdrachtNaam;
    private DatePicker opdrachtDatum;
    private EditText editOpdrachtOmschrijving;
    private Spinner spinnerVakken;
    private Button bewaarOpdracht, kiesDatum, btnok;
    private TextView tv_gekozen_datum;
    private RadioButton rbnHuiswerk, rbnEindopdracht, rbnToets, rbnOverig;

    DatabaseHelper myDb;

    private String typeOpdracht, datum = "", titel, beschrijving, vaknaam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assignment_acitvity);
        typeOpdracht = getString(R.string.Huiswerk_key);

        myDb = new DatabaseHelper(this);
        initViews();
        SetupRadioButtons();
        setupSpinner();
        kiesDatum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KiesDatum();
            }
        });

        AddData();
    }

    public void KiesDatum() {
        opdrachtDatum.setVisibility(View.VISIBLE);
        opdrachtOpties.setVisibility(View.GONE);
        editOpdrachtOmschrijving.setVisibility(View.GONE);
        edtOpdrachtNaam.setVisibility(View.GONE);
        spinnerVakken.setVisibility(View.GONE);
        bewaarOpdracht.setVisibility(View.GONE);
        kiesDatum.setVisibility(View.GONE);
        btnok.setVisibility(View.VISIBLE);

        DatePicker.OnDateChangedListener onDateChangedListener = new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
//                Date date = new Date(i, i1, i2);
//                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//                datum = sdf.format(date);
//                System.out.println(date);
//                System.out.println("Jaar van gekozen: " + i);
//                System.out.println("Maand van gekozen: " + i1);
//                System.out.println("Dag van gekozen: " + i2);

                String dag = String.valueOf(i2);
                String maand = String.valueOf(i1 + 1);
                String jaar = String.valueOf(i);

                if (dag.length() == 1) dag = "0"+dag;
                if (maand.length() == 1) maand = "0"+maand;

                datum = dag + "-" + maand + "-" + jaar;

                tv_gekozen_datum.setTextColor(getResources().getColor(R.color.grey));
                tv_gekozen_datum.setText(datum);
            }
        };

        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opdrachtDatum.setVisibility(View.GONE);
                opdrachtOpties.setVisibility(View.VISIBLE);
                editOpdrachtOmschrijving.setVisibility(View.VISIBLE);
                edtOpdrachtNaam.setVisibility(View.VISIBLE);
                spinnerVakken.setVisibility(View.VISIBLE);
                bewaarOpdracht.setVisibility(View.VISIBLE);
                kiesDatum.setVisibility(View.VISIBLE);
                btnok.setVisibility(View.GONE);
            }
        });

        opdrachtDatum.init(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance()
                .get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                onDateChangedListener);


    }



    public void AddData() {
        bewaarOpdracht.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vaknaam = spinnerVakken.getSelectedItem().toString();
                titel = edtOpdrachtNaam.getText().toString();
                if (titel.equals("")){
                    edtOpdrachtNaam.setError(getString(R.string.error));
                } else if (datum.equals("")){
                    tv_gekozen_datum.setText(getString(R.string.datum_error));
                    tv_gekozen_datum.setTextColor(getResources().getColor(R.color.red));
                }
                else {
                    beschrijving = editOpdrachtOmschrijving.getText().toString();

                    boolean isInserted = myDb.insertData(typeOpdracht, vaknaam, titel, datum, beschrijving);

                    if (isInserted) {
                        Intent intent = new Intent(AddAssignmentAcitvity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void SetupRadioButtons() {
        rbnEindopdracht.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbnHuiswerk.setChecked(false);
                rbnOverig.setChecked(false);
                rbnToets.setChecked(false);
                typeOpdracht = getString(R.string.Eindopdracht_key);
            }
        });
        rbnHuiswerk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbnEindopdracht.setChecked(false);
                rbnOverig.setChecked(false);
                rbnToets.setChecked(false);
                typeOpdracht = getString(R.string.Huiswerk_key);
                Toast.makeText(AddAssignmentAcitvity.this, typeOpdracht, Toast.LENGTH_SHORT).show();
            }
        });
        rbnToets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbnHuiswerk.setChecked(false);
                rbnOverig.setChecked(false);
                rbnEindopdracht.setChecked(false);
                typeOpdracht = getString(R.string.Toets_key);
            }
        });
        rbnOverig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbnHuiswerk.setChecked(false);
                rbnEindopdracht.setChecked(false);
                rbnToets.setChecked(false);
                typeOpdracht = getString(R.string.overig_key);
                Toast.makeText(AddAssignmentAcitvity.this, typeOpdracht, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        opdrachtOpties = findViewById(R.id.radioGroupTypeAssignment);
        edtOpdrachtNaam = findViewById(R.id.edtTxtOpdrachtNaam);
        opdrachtDatum = findViewById(R.id.datePicker);
        editOpdrachtOmschrijving = findViewById(R.id.edtTxtBeschrijving);
        spinnerVakken = findViewById(R.id.spinnerVakken);
        bewaarOpdracht = findViewById(R.id.btnMakeAssignment);
        kiesDatum = findViewById(R.id.btn_kies_datum);
        tv_gekozen_datum = findViewById(R.id.tv_gekozen_datum);
        btnok = findViewById(R.id.btn_ok_datum);

        rbnEindopdracht = findViewById(R.id.rbnEindopdracht);
        rbnHuiswerk = findViewById(R.id.rbnHuiswerk);
        rbnToets = findViewById(R.id.rbnToets);
        rbnOverig = findViewById(R.id.rbnOverig);
    }

    private void setupSpinner() {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, myDb.getVakkenNamen());
        spinnerVakken.setAdapter(spinnerAdapter);
    }
}