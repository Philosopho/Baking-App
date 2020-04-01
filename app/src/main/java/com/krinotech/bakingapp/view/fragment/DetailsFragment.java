package com.krinotech.bakingapp.view.fragment;


import android.annotation.NonNull;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.krinotech.bakingapp.PresenterLogic;
import com.krinotech.bakingapp.R;
import com.krinotech.bakingapp.databinding.FragmentDetailsBinding;
import com.krinotech.bakingapp.model.Step;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {
    public static final String TAG = DetailsFragment.class.getSimpleName();
    public FragmentDetailsBinding fragmentDetailsBinding;
    private List<Step> steps;


    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentDetailsBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_details, container, false);

        Bundle args = getArguments();
        String stepsKey = getString(R.string.STEP_EXTRA);

        if(args.containsKey(stepsKey)) {
            String positionKey = getString(R.string.POSITION_EXTRA);

            steps = args.getParcelableArrayList(stepsKey);
            int position = args.getInt(positionKey);

            setViews(position);

            return fragmentDetailsBinding.getRoot();
        }
        else {
            hideVideosAndButtons();
            String text = args.getString(getString(R.string.INGREDIENTS_EXTRA));
            setDetails(text);
            return fragmentDetailsBinding.getRoot();
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
            activateVideo(url, thumbnailUrl);
        }
        else {
            hideVideo();
        }
    }

    private void activateVideo(String url, String thumbnailUrl) {

    }

    private void activateNextBtn(int position) {
        final int newPosition = position + 1;

        Button nextBtn = fragmentDetailsBinding.btnNextStep;

        nextBtn.setVisibility(View.VISIBLE);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setViews(newPosition);
            }
        });
    }

    private void activatePrevBtn(int position) {
        final int newPosition = position - 1;

        Button prevBtn = fragmentDetailsBinding.btnPreviousStep;

        prevBtn.setVisibility(View.VISIBLE);
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setViews(newPosition);
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

    private void setVideo(String url) {

    }

    private void hideVideosAndButtons() {
        hideVideo();
        hidePrevButton();
        hideNextButton();
    }

}
