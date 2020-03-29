package com.krinotech.bakingapp.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Step {
    @PrimaryKey(autoGenerate = true)
    private long stepId;

    private int recipeId;
    private String shortDescription;

    private String description;
    private String videoUrl;
    private String thumbnailURL;

    public Step(long stepId, int recipeId, String shortDescription, String description, String videoUrl, String thumbnailURL) {
        this.stepId = stepId;
        this.recipeId = recipeId;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoUrl = videoUrl;
        this.thumbnailURL = thumbnailURL;
    }

    public long getStepId() {
        return stepId;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }
}
