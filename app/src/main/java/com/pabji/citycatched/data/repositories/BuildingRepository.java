package com.pabji.citycatched.data.repositories;

import android.location.Location;

import com.pabji.citycatched.presentation.mvp.models.Building;
import com.pabji.citycatched.presentation.mvp.models.Comment;
import com.pabji.citycatched.presentation.mvp.models.Descriptor;
import com.pabji.citycatched.presentation.mvp.models.MyLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.Observable;

/**
 * Created by Pablo Jiménez Casado on 05/08/2016.
 */

public interface BuildingRepository {

    Observable<Building> getBuilding(String buildingId);

    Observable<Building> getCurrentBuilding();

    //Observable<String> saveBuilding(Building building);

    //Observable<String> deleteBuilding(String buildingId);

    //Observable<List<String>> getBuildingDescriptors(String buildingId);

    Observable<String> addBuildingDescriptor(String contentDescriptor);

    //Observable<String> saveBuildingAsVisited(String userId,String buildingId);

    //Observable<String> deleteBuildingFromVisited(String userId,String buildingId);

    //Observable<String> addComment(String buildingId, Comment comment);

    //Observable<String> updateComment(String buildingId,Comment comment);

    Observable<Descriptor> getNearBuildingDescriptors();

    Observable<List<Comment>> getBuildingComments();

    Observable<MyLocation> getBuildingRange();

    Observable<MyLocation> getBuildingLocation();

    Observable<List<Building>> getNearBuildings(String uid);

    Observable<String> sendComment(String uid,Comment comment);

    Observable<Map<String,List<String>>> getBuildingsFeatures();

    Observable<Building> getNearBuilding(String buildingId);

    Observable<Building> getBuildingVisited(String key,String buildingId);

    Observable<MyLocation> updateMyCurrentLocation(Location location);

    Observable<List<String>> getBuildingPictures();

    Observable<String> addBuildingRange();

    Observable<String> addBuildingUserVisited(String uid);

    Observable<Map<String,List<Building>>> getBuildingsVisited(String uid);
}
