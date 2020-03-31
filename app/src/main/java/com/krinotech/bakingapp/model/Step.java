package com.krinotech.bakingapp.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "steps")
public class Step {
    @PrimaryKey(autoGenerate = true)
    private long stepId;

    @ForeignKey
            (entity = Recipe.class,
                    parentColumns = "id",
                    childColumns = "recipeId",
                    onDelete = CASCADE)
    private int recipeId;

    private String shortDescription;

    private String description;
    private String videoURL;
    private String thumbnailURL;

    public Step(long stepId, int recipeId, String shortDescription, String description, String videoURL, String thumbnailURL) {
        this.stepId = stepId;
        this.recipeId = recipeId;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
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

    public String getVideoURL() {
        return videoURL;
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
