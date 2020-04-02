package com.krinotech.bakingapp.view.fragment;


import android.app.Dialog;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.krinotech.bakingapp.PresenterLogic;
import com.krinotech.bakingapp.R;
import com.krinotech.bakingapp.databinding.FragmentDetailsBinding;
import com.krinotech.bakingapp.model.Step;
import com.krinotech.bakingapp.view.MainActivity;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {
    public static final String TAG = DetailsFragment.class.getSimpleName();
    private static final String RESUME_WINDOW = "resume window";
    private static final String RESUME_POSITION = "resume position";
    private static final String LANDSCAPE_ORIENTATION = "orientation";
    private static final String LAST_VIDEO = "last video";

    private long resumePosition;
    private int resumeWindow;
    private boolean fullscreen = false;

    private FragmentDetailsBinding fragmentDetailsBinding;
    private List<Step> steps;
    private ExoPlayer exoPlayer;
    private int lastVideo = -1;


    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentDetailsBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_details, container, false);

        if(savedInstanceState != null) {
            lastVideo = savedInstanceState.getInt(LAST_VIDEO);
            resumeWindow = savedInstanceState.getInt(RESUME_WINDOW);
            resumePosition = savedInstanceState.getLong(RESUME_POSITION);
            fullscreen = savedInstanceState.getBoolean(LANDSCAPE_ORIENTATION);
        }

        Bundle args = getArguments();
        String stepsKey = getString(R.string.STEP_EXTRA);

        if(args.containsKey(stepsKey)) {
            String positionKey = getString(R.string.POSITION_EXTRA);
            int position;

            if(lastVideo != -1) {
                position = lastVideo;
            }
            else {
                position = args.getInt(positionKey);
                lastVideo = position;
            }
            steps = args.getParcelableArrayList(stepsKey);
            setViews(position);

            return fragmentDetailsBinding.getRoot();
        }
        else {
            hideVideosAndButtons();
            String text = args.getString(getString(R.string.INGREDIENTS_EXTRA));
            ViewGroup.LayoutParams layoutParams = fragmentDetailsBinding.tvStepDetails.getLayoutParams();
            layoutParams.height = MATCH_PARENT;

            fragmentDetailsBinding.tvStepDetails.setLayoutParams(layoutParams);
            setDetails(text);
            return fragmentDetailsBinding.getRoot();
        }
    }

//    private void initFullscreenDialog() {
//
//        fullScreenDialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
//            public void onBackPressed() {
//                if (fullscreen)
//                    closeFullscreenDialog();
//                super.onBackPressed();
//            }
//        };
//    }
//
//    private void closeFullscreenDialog() {
//        ((ViewGroup) fragmentDetailsBinding.videoViewDetails
//                .getParent())
//                .removeView(fragmentDetailsBinding.videoViewDetails);
//        ((FrameLayout) findViewById(R.id.main_media_frame)).addView(mExoPlayerView);
//        mExoPlayerFullscreen = false;
//        mFullScreenDialog.dismiss();
//        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_fullscreen_expand));
//    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        int orientation = getResources().getConfiguration().orientation;
        boolean orientationLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE;

        outState.putInt(LAST_VIDEO, lastVideo);
        outState.putLong(RESUME_POSITION, exoPlayer.getContentPosition());
        outState.putInt(RESUME_WINDOW, exoPlayer.getCurrentWindowIndex());
        outState.putBoolean(LANDSCAPE_ORIENTATION, orientationLandscape);
    }

    private void setViews(int position) {
        String text = steps.get(position).getDescription();
        String url = steps.get(position).getVideoURL();
        String thumbnailUrl = steps.get(position).getThumbnailURL();

        setDetails(text);

        if(PresenterLogic.prevExists(position)){
            activatePrevBtn(position);
        }
        else {
            hidePrevButton();
        }

        if(PresenterLogic.nextExists(position, steps.size())){
            activateNextBtn(position);
        }
        else {
            hideNextButton();
        }
        if(PresenterLogic.videoExists(url, thumbnailUrl)) {
            Uri chosenUrl = Uri
                    .parse(
                            PresenterLogic.getUri(url, thumbnailUrl)
                    );
            activateVideo(chosenUrl);
        }
        showVideo();
    }

    private void activateVideo(Uri chosenUrl) {
        if(exoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector(getContext());
            LoadControl loadControl = new DefaultLoadControl();

            exoPlayer = new SimpleExoPlayer
                    .Builder(getContext())
                    .setLoadControl(loadControl)
                    .setTrackSelector(trackSelector)
                    .build();

            fragmentDetailsBinding.videoViewDetails.setPlayer(exoPlayer);
            String userAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));

            boolean hasResumePosition = resumeWindow != C.INDEX_UNSET;

            MediaSource mediaSource = new ProgressiveMediaSource.Factory(
                    new DefaultDataSourceFactory(getContext(), userAgent)
            ).createMediaSource(chosenUrl);

            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
            if(hasResumePosition) {
                Log.d(TAG, "activateVideo: ");
                fragmentDetailsBinding
                        .videoViewDetails
                        .getPlayer()
                        .seekTo(resumeWindow, resumePosition);
            }
        }
    }

    private void showVideo() {
        fragmentDetailsBinding.videoViewDetails.setVisibility(View.VISIBLE);
    }

    public void releasePlayer() {
        if(exoPlayer != null) {
            exoPlayer.stop(true);
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    private void activateNextBtn(int position) {
        final int newPosition = position + 1;

        Button nextBtn = fragmentDetailsBinding.btnNextStep;

        nextBtn.setVisibility(View.VISIBLE);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStep(newPosition);
            }
        });
    }

    private void changeStep(int newPosition) {
        releasePlayer();
        lastVideo = newPosition;
        resumeWindow = C.INDEX_UNSET;
        resumePosition = C.INDEX_UNSET;
        setViews(newPosition);
    }

    private void activatePrevBtn(int position) {
        final int newPosition = position - 1;

        Button prevBtn = fragmentDetailsBinding.btnPreviousStep;

        prevBtn.setVisibility(View.VISIBLE);
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStep(newPosition);
            }
        });
    }

    private void hideVideo() {
        fragmentDetailsBinding
                .videoViewDetails
                .setVisibility(View.GONE);
    }

    private void hidePrevButton() {
        fragmentDetailsBinding
                .btnPreviousStep
                .setVisibility(View.GONE);
    }

    private void hideNextButton() {
        fragmentDetailsBinding
                .btnNextStep
                .setVisibility(View.GONE);
    }

    private void setDetails(String text) {
        fragmentDetailsBinding
                .tvStepDetails
                .setText(text);
    }

    private void hideVideosAndButtons() {
        hideVideo();
        hidePrevButton();
        hideNextButton();
    }

}
