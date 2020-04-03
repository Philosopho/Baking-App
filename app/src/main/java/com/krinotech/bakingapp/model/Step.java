package com.krinotech.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "steps")
public class Step implements Parcelable {
    @PrimaryKey
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

    @Ignore
    protected Step(Parcel in) {
        stepId = in.readLong();
        recipeId = in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(stepId);
        dest.writeInt(recipeId);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

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

    public void setStepsId(long i) {
        this.stepId = i;
    }
}
