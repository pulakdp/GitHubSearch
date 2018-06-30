package io.github.pulakdp.githubsearch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bumptech.glide.Glide;

import uk.co.senab.photoview.PhotoView;

/**
 * Author: PulakDebasish
 */

public class FullScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreen_view);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_IMMERSIVE);

        PhotoView photoView = findViewById(R.id.photo_view);
        String photoUrl = getIntent().getStringExtra("url");
        if (!photoUrl.isEmpty()) {
            Glide.with(this)
                    .load(photoUrl)
                    .into(photoView);
        }
    }
}
