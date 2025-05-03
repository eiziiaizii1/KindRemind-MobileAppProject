package com.example.kindremind_mobileappproject.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kindremind_mobileappproject.R;

/**
 * Activity for adding custom deeds
 * This is a placeholder implementation
 */
public class AddDeedActivity extends AppCompatActivity {

    private EditText deedTextInput;
    private Spinner categorySpinner;
    private Button saveButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_deed);

        // Find views
        deedTextInput = findViewById(R.id.deed_text_input);
        categorySpinner = findViewById(R.id.category_spinner);
        saveButton = findViewById(R.id.btn_save);
        cancelButton = findViewById(R.id.btn_cancel);

        // Setup spinner
        setupCategorySpinner();

        // Setup buttons
        saveButton.setOnClickListener(v -> saveDeed());
        cancelButton.setOnClickListener(v -> finish());
    }

    private void setupCategorySpinner() {
        // Sample categories - in a real app, these would come from a repository
        String[] categories = {"environment", "empathy", "community", "health"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }

    private void saveDeed() {
        String deedText = deedTextInput.getText().toString().trim();

        if (deedText.isEmpty()) {
            deedTextInput.setError("Please enter deed text");
            return;
        }

        String category = categorySpinner.getSelectedItem().toString();

        // TODO: Save the custom deed to the database
        // In a real app, this would use a repository:
        // deedRepository.saveCustomDeed(deedText, category);

        Toast.makeText(this, "Custom deed saved", Toast.LENGTH_SHORT).show();
        finish();
    }
}