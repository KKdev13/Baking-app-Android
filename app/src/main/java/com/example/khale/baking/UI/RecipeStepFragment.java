package com.example.khale.baking.UI;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import static com.example.khale.baking.Finals.*;

import com.example.khale.baking.Model.Recipe;
import com.example.khale.baking.Model.Step;
import com.example.khale.baking.R;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeStepFragment extends Fragment{

    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer simpleExoPlayer;
    private ArrayList<Step> steps;

    private Step step;
    private String videoUrl;
    private int position;
    private long playerPosition;
    public static boolean isPlaying;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //boolean isPlayWhenReady;
        View view = inflater.inflate(R.layout.recipe_step_detail_fragment, container, false);
        TextView textView = (TextView) view.findViewById(R.id.recipe_step_text);

        if(savedInstanceState != null){
            step = savedInstanceState.getParcelable(SELECTED_STEP);
            steps = savedInstanceState.getParcelableArrayList(ALL_STEPS);
            playerPosition = savedInstanceState.getLong("player_position");
            //saving the play state, so that the video is resumed or paused accordingly when the device is rotated
            isPlaying = simpleExoPlayer.getPlayWhenReady();
            savedInstanceState.putBoolean("playerState", isPlaying);
            simpleExoPlayer.setPlayWhenReady(isPlaying);
        }else {
            step = getArguments().getParcelable(SELECTED_STEP);
            steps = getArguments().getParcelableArrayList(ALL_STEPS);



        }

        if(step == null){
            Recipe recipe = getArguments().getParcelable(SELECTED_RECIPE);
            steps = (ArrayList<Step>) recipe.getSteps();
            step = steps.get(0);
        }

        textView.setText(step.getDescription());
        textView.setVisibility(View.VISIBLE);

        simpleExoPlayerView = (SimpleExoPlayerView) view.findViewById(R.id.playerView);
        simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

        videoUrl = step.getVideoURL();
        String imageUrl = step.getThumbnailUrl();

        if(!TextUtils.isEmpty(imageUrl)){
            Uri builtUri = Uri.parse(imageUrl).buildUpon().build();
            ImageView imageView = (ImageView) view.findViewById(R.id.iv_thumbnail);
            imageView.setVisibility(View.VISIBLE);
            Picasso.with(getContext()).load(builtUri).into(imageView);
        }

        if(!TextUtils.isEmpty(videoUrl)){
            initializePlayer(Uri.parse(step.getVideoURL()));
        }else {
            simpleExoPlayer = null;
            simpleExoPlayerView.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
            isPlaying = false;
        }

        ImageButton back = (ImageButton) view.findViewById(R.id.ib_back);
        ImageButton next = (ImageButton) view.findViewById(R.id.ib_next);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = step.getId() -1;
                if(position >= 0){
                    if(simpleExoPlayer != null){
                        simpleExoPlayer.stop();
                    }
                    clickButton(position);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = step.getId() +1;
                if(position < steps.size()){
                    if(simpleExoPlayer != null){
                        simpleExoPlayer.stop();
                    }
                    clickButton(position);
                }
            }
        });
        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(simpleExoPlayer != null){
            playerPosition = simpleExoPlayer.getCurrentPosition();
            releasePlayer();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onResume() {
        super.onResume();
        if(!TextUtils.isEmpty(videoUrl)){
            initializePlayer(Uri.parse(videoUrl));
        }else {
            isPlaying = false;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initializePlayer(Uri mediaUri){
        if(simpleExoPlayer == null){
            //instantiate the player
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            simpleExoPlayerView.setPlayer(simpleExoPlayer);
            simpleExoPlayerView.setVisibility(View.VISIBLE);

            if(!RecipeFragment.isTablet && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
            }

            //prepare the media source
            String userAgent = com.google.android.exoplayer2.util.Util.getUserAgent(getContext(), "Backing");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);

            if(playerPosition != C.TIME_UNSET){
                simpleExoPlayer.seekTo(playerPosition);
            }

            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(true);

            isPlaying = true;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putParcelable(SELECTED_STEP, step);
        currentState.putParcelableArrayList(ALL_STEPS, steps);
        currentState.putLong("player_position", playerPosition);
    }

    private void releasePlayer(){
        if(simpleExoPlayer != null){
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    public void clickButton(Integer i){

        RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        Bundle bundle = new Bundle();
        bundle.putParcelable(SELECTED_STEP, steps.get(i));
        bundle.putParcelableArrayList(ALL_STEPS, steps);
        recipeStepFragment.setArguments(bundle);

        if(RecipeFragment.isTablet && getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container2, recipeStepFragment).addToBackStack(RECIPE_STEP)
                    .commit();
        }else {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, recipeStepFragment).addToBackStack(RECIPE_STEP)
                    .commit();
        }
    }
}
