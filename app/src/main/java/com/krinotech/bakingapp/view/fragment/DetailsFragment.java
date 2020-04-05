package com.krinotech.bakingapp.view.fragment;


import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.krinotech.bakingapp.PresenterLogic;
import com.krinotech.bakingapp.R;
import com.krinotech.bakingapp.databinding.FragmentDetailsBinding;
import com.krinotech.bakingapp.model.Step;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {
    public  static final String TAG = DetailsFragment.class.getSimpleName();
    private static final String RESUME_WINDOW = "resume window";
    private static final String RESUME_POSITION = "resume position";
    private static final String LAST_VIDEO = "last video";

    private long resumePosition;
    private int resumeWindow;
    private int lastVideo = -1;
    private int shortAnimationDefault;


    private FragmentDetailsBinding fragmentDetailsBinding;
    private List<Step> steps;
    private ExoPlayer exoPlayer;


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

        shortAnimationDefault = getResources()
                .getInteger(android.R.integer.config_shortAnimTime);
        if(exoPlayer != null) {
            Log.d(TAG, "onCreateView: Not null");
        }
        if(savedInstanceState != null) {
            lastVideo = savedInstanceState.getInt(LAST_VIDEO);
            resumeWindow = savedInstanceState.getInt(RESUME_WINDOW);
            resumePosition = savedInstanceState.getLong(RESUME_POSITION);
        }

        Bundle args = getArguments();

        assert args != null;

        String stepsKey = getString(R.string.STEP_EXTRA);
        String recipeName = args.getString(RecipeDetailsFragment.RECIPE_NAME);

        assert getActivity() != null;

        getActivity().setTitle(recipeName);

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

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if(exoPlayer != null) {
                setFullScreen();
            }
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if(exoPlayer != null) {
                exitFullScreen();
            }
        }
    }

    private void setFullScreen() {
        hideButtonLayoutAndDetails();
        getSupportActionBar().hide();
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) fragmentDetailsBinding.videoViewDetails.getLayoutParams();
        marginLayoutParams.setMargins(0, 0, 0, 0);
        fragmentDetailsBinding.videoViewDetails.setLayoutParams(marginLayoutParams);
    }

    private void exitFullScreen() {
        showButtonLayoutAndDetails();

        getSupportActionBar().show();

        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) fragmentDetailsBinding.videoViewDetails.getLayoutParams();

        float horizontal_dps = getResources().getDimension(R.dimen.margin_standard_size_horizontal);
        float vertical_dps = getResources().getDimension(R.dimen.margin_standard_size_vertical);
        int horizontal_pixels = convertToPixels(horizontal_dps);
        int vertical_pixels = convertToPixels(vertical_dps);

        marginLayoutParams.setMargins(horizontal_pixels, vertical_pixels, horizontal_pixels, vertical_pixels);

        fragmentDetailsBinding.videoViewDetails.setLayoutParams(marginLayoutParams);
    }

    private int convertToPixels(float dpValue) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,dpValue,
                getResources().getDisplayMetrics()
        );
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if(exoPlayer != null) {
            outState.putInt(LAST_VIDEO, lastVideo);
            outState.putLong(RESUME_POSITION, exoPlayer.getContentPosition());
            outState.putInt(RESUME_WINDOW, exoPlayer.getCurrentWindowIndex());
        }
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
            if(getResources()
                    .getConfiguration()
                    .orientation == Configuration.ORIENTATION_LANDSCAPE) {
                setFullScreen();
            }
        }
        showVideo();
    }

    private void activateVideo(Uri chosenUrl) {
        String userAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));

        MediaSource mediaSource = new ProgressiveMediaSource.Factory(
                new DefaultDataSourceFactory(getContext(), userAgent)
        ).createMediaSource(chosenUrl);

        if(exoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector(getContext());
            LoadControl loadControl = new DefaultLoadControl();

            exoPlayer = new SimpleExoPlayer
                    .Builder(getContext())
                    .setLoadControl(loadControl)
                    .setTrackSelector(trackSelector)
                    .build();

            fragmentDetailsBinding.videoViewDetails.setPlayer(exoPlayer);
        }

        exoPlayer.prepare(mediaSource);
        seekToPosition();
        exoPlayer.setPlayWhenReady(true);
    }

    private void seekToPosition() {
        boolean hasResumePosition = resumeWindow != C.INDEX_UNSET;
        if(hasResumePosition) {
            fragmentDetailsBinding
                    .videoViewDetails
                    .getPlayer()
                    .seekTo(resumeWindow, resumePosition);
        }
    }

    private void showVideo() {
        fragmentDetailsBinding.videoViewDetails.setVisibility(View.VISIBLE);
    }

    private void releasePlayer() {
        if(exoPlayer != null) {
            exoPlayer.stop(true);
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    private void activateNextBtn(int position) {
        final int newPosition = position + 1;

        Button nextBtn = fragmentDetailsBinding.btnNextStep;

        nextBtn.setVisibility(View.VISIBLE);
        nextBtn.setOnClickListener(v -> changeStep(newPosition));
    }

    private void changeStep(int newPosition) {
        lastVideo = newPosition;
        resumeWindow = C.INDEX_UNSET;
        resumePosition = C.INDEX_UNSET;
        setViews(newPosition);
    }

    private void activatePrevBtn(int position) {
        final int newPosition = position - 1;

        Button prevBtn = fragmentDetailsBinding.btnPreviousStep;

        prevBtn.setVisibility(View.VISIBLE);
        prevBtn.setOnClickListener(v -> changeStep(newPosition));
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

    private void hideButtonLayoutAndDetails() {
        fragmentDetailsBinding.linearLayoutTwoDetails.setVisibility(View.GONE);
        fragmentDetailsBinding.tvStepDetails.setVisibility(View.GONE);
    }

    private void showButtonLayoutAndDetails() {
        fragmentDetailsBinding.linearLayoutTwoDetails.setVisibility(View.VISIBLE);
        fragmentDetailsBinding.tvStepDetails.setVisibility(View.VISIBLE);
    }


    private ActionBar getSupportActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }
}
