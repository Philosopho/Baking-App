package com.krinotech.bakingapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.krinotech.bakingapp.R;
import com.krinotech.bakingapp.databinding.ActivityIngredientsWidgetBinding;

public class IngredientsWidgetActivity extends AppCompatActivity {
    public static final String TAG = IngredientsWidgetActivity.class.getSimpleName();
    private ActivityIngredientsWidgetBinding activityIngredientsWidgetBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityIngredientsWidgetBinding = DataBindingUtil
                .setContentView(this, R.layout.activity_ingredients_widget);

        launchMainActivity();

        finish();
    }

    private void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(getString(R.string.INGREDIENTS_WIDGET_EXTRA), true);

        startActivity(intent);
    }
}
