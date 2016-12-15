package com.pabji.citycatched.data.datasources;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pabji.citycatched.data.repositories.BuildingRepository;
import com.pabji.citycatched.presentation.mvp.models.Building;
import com.pabji.citycatched.presentation.mvp.models.Comment;
import com.pabji.citycatched.presentation.mvp.models.Descriptor;
import com.pabji.citycatched.presentation.mvp.models.MyLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

/**
 * Created by usuario on 2/08/16.
 */
@Singleton
public class FirebaseDatasource implements BuildingRepository {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference refBase = database.getReference();
    DatabaseReference refRoot  = database.getReference("Users");
    DatabaseReference refBuildings = database.getReference("Buildings");
    DatabaseReference refBuildingsDescriptors = database.getReference("BuildingsDescriptors");
    DatabaseReference refBuildingsPictures = database.getReference("BuildingsPictures");
    DatabaseReference refBuildingsComments = database.getReference("BuildingsComments");
    DatabaseReference refBuildingsLocations = database.getReference("BuildingsLocation");
    DatabaseReference refBuildingsArea = database.getReference("BuildingsArea");
    DatabaseReference refBuildingsFeatures = database.getReference("BuildingsFeatures");
    DatabaseReference refComments = database.getReference("Comments");
    DatabaseReference refBuildingsRange = database.getReference("BuildingsRanges");
    DatabaseReference refUsersVisites = database.getReference("UsersVisites");
    DatabaseReference refUsersComments = database.getReference("UsersComments");
    DatabaseReference refRanges = database.getReference("Ranges");

    GeoFire geoFire = new GeoFire(refBuildingsLocations);

    int count = 0;
    int nearBuildingCount = 0;
    List<Comment> commentList = new ArrayList<Comment>();
    private int countRanges;
    Map<String,List<String>> mapFeatures = new HashMap<>();
    private int buildingLocated = 0;
    private boolean isGeoqueryReady = false;
    private int countNearBuildings = 0;
    private List<Building> nearBuildingList = new ArrayList<>();
    private MyLocation lastLocation;
    private MyLocation currentLocation;
    private Building currentBuilding;

    private Context context;
    private HashMap<String, List<Building>> mapVisites = new HashMap<String,List<Building>>();
    private int countVisites = 0;

    @Inject
    public FirebaseDatasource(Context context){
        this.context = context;
    }

    /*@Override
    public Observable<String> saveBuilding(Building building) {
        String buildingKey = refBuildings.push().getKey();
        String descriptorKey = refBuildingsDescriptors.push().getKey();

        Map<String,Object> buildingMap = building.toMap();
        Map<String,Object> childUpdates = new HashMap<>();

        //String descriptorAddress = building.addressList.get(0);

        String contentDescriptor = building.descriptors.get(0);
        Descriptor descriptor = new Descriptor();
        descriptor.content = contentDescriptor;
        descriptor.buildingId = buildingKey;

        buildingMap.put("id",buildingKey);
        buildingMap.put("nearBuildings",building.nearBuildings);

        childUpdates.put("/Buildings/"+buildingKey,buildingMap);
        //childUpdates.put("/Address/"+descriptorAddress+"/"+descriptorKey,descriptorKey);
        //hildUpdates.put("/Descriptors/"+descriptorAddress+"/"+descriptorKey,descriptor.toMap());
        childUpdates.put("/Pictures/"+buildingKey,building.pictures);

        return updateChildren(childUpdates, buildingKey);
    }

    @Override
    public Observable<String> deleteBuilding(String buildingId) {
        Map<String,Object> childUpdates = new HashMap<>();

        childUpdates.put("/Buildings/"+buildingId,null);
        childUpdates.put("/Pictures/"+buildingId,null);

        return updateChildren(childUpdates, buildingId);
    }*/


    @Override
    public Observable<String> addBuildingDescriptor(String contentDescriptor) {
        String descriptorKey = refBuildingsDescriptors.push().getKey();

        Map<String,Object> childUpdates = new HashMap<>();
        childUpdates.put("/BuildingsDescriptors/"+currentBuilding.getId()+"/"+descriptorKey,contentDescriptor);
        return updateChildren(childUpdates, descriptorKey);
    }

    /*@Override
    public Observable<String> addComment(String buildingId, Comment comment) {
        String commentKey = refBuildingsDescriptors.push().getKey();

        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> commentMap = comment.toMap();
        commentMap.put("id", commentKey);

        childUpdates.put("/Comments/" + buildingId + "/" + commentKey, commentMap);

        return updateChildren(childUpdates, commentKey);
    }

    @Override
    public Observable<String> updateComment(String commentId, Comment comment) {
        Map<String,Object> childUpdates = new HashMap<>();

        Map<String,Object> commentMap = comment.toMap();
        /*commentMap.put("id",comment.id);
        childUpdates.put("/Comments/"+buildingId+"/"+comment.id,commentMap);

        return updateChildren(childUpdates, commentId);
    }*/

    /*@Override
    public Observable<String> saveBuildingAsVisited(String userId, String buildingId) {
        Map<String,Object> childUpdates = new HashMap<>();

        childUpdates.put("/Users/"+userId+"/visited/"+buildingId,true);
        return updateChildren(childUpdates, buildingId);
    }

    @Override
    public Observable<String> deleteBuildingFromVisited(String userId, String buildingId) {
        Map<String,Object> childUpdates = new HashMap<>();
        childUpdates.put("/Users/"+userId+"/visited/"+buildingId,null);
        return updateChildren(childUpdates,buildingId);
    }*/

    @Override
    public Observable<Descriptor> getNearBuildingDescriptors() {
        nearBuildingCount = 0;
        return Observable.create(new Observable.OnSubscribe<Descriptor>() {
            @Override
            public void call(final Subscriber<? super Descriptor> subscriber) {

                if(nearBuildingList != null && !nearBuildingList.isEmpty()){
                    final ValueEventListener listener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot child: dataSnapshot.getChildren()){
                                Descriptor descriptor = new Descriptor();
                                descriptor.setContent(child.getValue(String.class));
                                descriptor.setBuildingId(dataSnapshot.getKey());
                                subscriber.onNext(descriptor);
                            }
                            nearBuildingCount++;
                            if(nearBuildingCount == nearBuildingList.size()){
                                subscriber.onCompleted();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            subscriber.onError(new FirebaseException(databaseError.getMessage()));
                        }
                    };

                    for(Building building : nearBuildingList){
                        refBuildingsDescriptors.child(building.getId()).addListenerForSingleValueEvent(listener);
                    }

                    subscriber.add(Subscriptions.create(new Action0() {
                        @Override
                        public void call() {
                            refBuildingsDescriptors.removeEventListener(listener);
                        }
                    }));
                }else{
                    subscriber.onError(new Throwable("Empty list"));
                }
            }
        });
    }

    @Override
    public Observable<List<String>> getBuildingPictures() {
        return Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(final Subscriber<? super List<String>> subscriber) {
                final ValueEventListener listener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<String> pictureList = new ArrayList<String>();
                        for(DataSnapshot child :dataSnapshot.getChildren()){
                            String picture = child.getValue(String.class);
                            if(picture != null) {
                                pictureList.add(picture);
                            }
                        }
                        subscriber.onNext(pictureList);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        subscriber.onError(new FirebaseException(databaseError.getMessage()));
                    }
                };
                refBuildingsPictures.child(currentBuilding.getId()).addListenerForSingleValueEvent(listener);
                subscriber.add(Subscriptions.create(new Action0() {
                    @Override
                    public void call() {
                        refBuildingsPictures.removeEventListener(listener);
                    }
                }));
            }
        });
    }

    @Override
    public Observable<String> addBuildingRange() {
        String rangeKey = Location.convert(currentLocation.getLatitude(),Location.FORMAT_DEGREES);
                rangeKey +=Location.convert(currentLocation.getLongitude(),Location.FORMAT_DEGREES);

        Map<String,Object> childUpdates = new HashMap<>();
        childUpdates.put("/Ranges/"+rangeKey,currentLocation.toMap());
        childUpdates.put("/BuildingsRanges/"+currentBuilding.getId()+"/"+rangeKey,rangeKey);
        return updateChildren(childUpdates, rangeKey);
    }

    @Override
    public Observable<String> addBuildingUserVisited(String uid) {
        String rangeKey = refUsersVisites.push().getKey();

        Map<String,Object> childUpdates = new HashMap<>();
        childUpdates.put("/UsersVisites/"+uid+"/"+currentBuilding.getId(),currentBuilding.getId());
        return updateChildren(childUpdates, rangeKey);
    }

    @Override
    public Observable<Map<String,List<Building>>> getBuildingsVisited(final String uid) {
        countVisites = 0;
        return Observable.create(new Observable.OnSubscribe<Map<String,List<Building>>>() {
            @Override
            public void call(final Subscriber<? super Map<String,List<Building>>> subscriber) {
                if (!mapVisites.isEmpty()) {
                    subscriber.onNext(mapVisites);
                    subscriber.onCompleted();
                } else {
                    final ValueEventListener listener = new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot visitesSnapshot) {
                            for (DataSnapshot child : visitesSnapshot.getChildren()) {
                                refBuildings.child(child.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot buildingSnapshot) {
                                        final Building building = buildingSnapshot.getValue(Building.class);
                                        building.setId(buildingSnapshot.getKey());
                                        building.setVisited(true);

                                        geoFire.getLocation(buildingSnapshot.getKey(), new LocationCallback() {
                                            @Override
                                            public void onLocationResult(String key, GeoLocation location) {

                                                MyLocation buildingLocation = new MyLocation(location);
                                                building.setMyLocation(buildingLocation);
                                                building.setDistance(currentLocation.getDistanceTo(buildingLocation));

                                                String city = buildingLocation.getCity(context);

                                                List<Building> buildingList = mapVisites.containsKey(city)? mapVisites.get(city) : new ArrayList<Building>();
                                                buildingList.add(building);
                                                mapVisites.put(city,buildingList);
                                                countVisites++;
                                                if (visitesSnapshot.getChildrenCount() == countVisites) {
                                                    subscriber.onNext(mapVisites);
                                                    subscriber.onCompleted();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                subscriber.onError(new FirebaseException(databaseError.getMessage()));
                                            }
                                        });

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        subscriber.onError(new FirebaseException(databaseError.getMessage()));
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            subscriber.onError(new FirebaseException(databaseError.getMessage()));
                        }
                    };
                    refUsersVisites.child(uid).addListenerForSingleValueEvent(listener);
                    subscriber.add(Subscriptions.create(new Action0() {
                        @Override
                        public void call() {
                            refUsersVisites.removeEventListener(listener);
                        }
                    }));
                }
            }
        });

    }

    @Override
    public Observable<Building> getBuilding(final String buildingId) {
        commentList.clear();
        return Observable.create(new Observable.OnSubscribe<Building>() {
            @Override
            public void call(final Subscriber<? super Building> subscriber) {
                final ValueEventListener listener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Building building = dataSnapshot.getValue(Building.class);
                        building.setId(dataSnapshot.getKey());
                        refBuildingsPictures.child(buildingId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                List<String> pictureList = new ArrayList<String>();
                                for(DataSnapshot child :dataSnapshot.getChildren()){
                                    String picture = child.getValue(String.class);
                                    if(picture != null) {
                                        pictureList.add(picture);
                                    }
                                }
                                building.setPictures(pictureList);
                                geoFire.getLocation(buildingId, new LocationCallback() {
                                    @Override
                                    public void onLocationResult(String key, GeoLocation location) {
                                        if (location != null) {
                                            building.setMyLocation(new MyLocation(location));
                                        }
                                        currentBuilding = building;
                                        subscriber.onNext(building);
                                        subscriber.onCompleted();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        subscriber.onError(new FirebaseException(databaseError.getMessage()));
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(new FirebaseException(databaseError.getMessage()));
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        subscriber.onError(new FirebaseException(databaseError.getMessage()));
                    }
                };
                refBuildings.child(buildingId).addListenerForSingleValueEvent(listener);
                subscriber.add(Subscriptions.create(new Action0() {
                    @Override
                    public void call() {
                        refBuildings.removeEventListener(listener);
                    }
                }));
            }
        });
    }

    @Override
    public Observable<Building> getCurrentBuilding() {
        return Observable.create(new Observable.OnSubscribe<Building>() {
            @Override
            public void call(final Subscriber<? super Building> subscriber) {
                if (currentBuilding != null) {
                    subscriber.onNext(currentBuilding);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new Throwable("No exist current building"));
                }
            }
        });
    }

    @Override
    public Observable<List<Comment>> getBuildingComments() {
        commentList.clear();
        return Observable.create(new Observable.OnSubscribe<List<Comment>>() {
            @Override
            public void call(final Subscriber<? super List<Comment>> subscriber) {

                final ValueEventListener listener = new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {

                        for(DataSnapshot child: dataSnapshot.getChildren()){
                            refComments.child(child.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot2) {
                                    Comment comment = dataSnapshot2.getValue(Comment.class);
                                    if(comment != null){
                                        commentList.add(comment);
                                    }

                                    if(commentList.size() == dataSnapshot.getChildrenCount()){

                                        Collections.sort(commentList, new Comparator<Comment>() {
                                            @Override
                                            public int compare(Comment o1, Comment o2) {
                                                return o2.date.compareTo(o1.date);
                                            }
                                        });

                                        subscriber.onNext(commentList);
                                        subscriber.onCompleted();
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        subscriber.onError(new FirebaseException(databaseError.getMessage()));
                    }
                };

                refBuildingsComments.child(currentBuilding.getId()).limitToLast(20).addListenerForSingleValueEvent(listener);
                subscriber.add(Subscriptions.create(new Action0() {
                    @Override
                    public void call() {
                        refComments.removeEventListener(listener);
                    }
                }));
            }
        });
    }

    @Override
    public Observable<MyLocation> getBuildingRange() {
        countRanges = 0;
        return Observable.create(new Observable.OnSubscribe<MyLocation>() {
            @Override
            public void call(final Subscriber<? super MyLocation> subscriber) {
                final ValueEventListener listener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(final DataSnapshot child: dataSnapshot.getChildren()){
                            refRanges.child(child.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Double latitude = dataSnapshot.child("latitude").getValue(Double.class);
                                    Double longitude = dataSnapshot.child("longitude").getValue(Double.class);
                                    MyLocation location = new MyLocation(latitude,longitude);
                                    if(location != null){
                                        subscriber.onNext(location);
                                    }

                                    countRanges++;
                                    if(child.getChildrenCount() == countRanges){
                                        subscriber.onCompleted();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    subscriber.onError(new FirebaseException(databaseError.getMessage()));
                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        subscriber.onError(new FirebaseException(databaseError.getMessage()));
                    }
                };
                refBuildingsRange.child(currentBuilding.getId()).addListenerForSingleValueEvent(listener);
                subscriber.add(Subscriptions.create(new Action0() {
                    @Override
                    public void call() {
                        refBuildings.removeEventListener(listener);
                    }
                }));
            }
        });
    }

    @Override
    public Observable<MyLocation> getBuildingLocation() {
        return Observable.create(new Observable.OnSubscribe<MyLocation>() {
            @Override
            public void call(final Subscriber<? super MyLocation> subscriber) {
                if(currentBuilding != null){
                    if(currentBuilding.getMyLocation() != null){
                        subscriber.onNext(currentBuilding.getMyLocation());
                        subscriber.onCompleted();
                    }else{
                        geoFire.getLocation(currentBuilding.getId(), new LocationCallback() {
                            @Override
                            public void onLocationResult(String key, GeoLocation location) {
                                if (location != null) {
                                    subscriber.onNext(new MyLocation(location));
                                    subscriber.onCompleted();
                                } else {
                                    subscriber.onError(new Throwable());
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(new FirebaseException(databaseError.getMessage()));
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public Observable<List<Building>> getNearBuildings(final String uid) {

        isGeoqueryReady = false;
        countNearBuildings = 0;
        nearBuildingCount = 0;
        return Observable.create(new Observable.OnSubscribe<List<Building>>() {
            @Override
            public void call(final Subscriber<? super List<Building>> subscriber) {

                if(lastLocation != null && lastLocation.getDistanceTo(currentLocation) <= 300){
                    subscriber.onNext(nearBuildingList);
                    subscriber.onCompleted();
                }else{
                    nearBuildingList.clear();
                    final GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(currentLocation.getLatitude(),currentLocation.getLongitude()), 0.3);
                    geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                        @Override
                        public void onKeyEntered(final String key, final GeoLocation location) {
                            countNearBuildings++;
                            refBuildings.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    final Building building = dataSnapshot.getValue(Building.class);
                                    building.setId(key);
                                    building.setMyLocation(new MyLocation(location));
                                    building.setDistance(lastLocation.getDistanceTo(building.myLocation));

                                    refUsersVisites.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(DataSnapshot child:dataSnapshot.getChildren()){
                                                if(key.equals(child.getKey())){
                                                    building.setVisited(true);
                                                }
                                            }

                                            nearBuildingList.add(building);
                                            if(isGeoqueryReady && countNearBuildings == nearBuildingList.size()){
                                                subscriber.onNext(nearBuildingList);
                                                subscriber.onCompleted();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onKeyExited(String key) {

                        }

                        @Override
                        public void onKeyMoved(String key, GeoLocation location) {

                        }

                        @Override
                        public void onGeoQueryReady() {
                            isGeoqueryReady = true;
                        }

                        @Override
                        public void onGeoQueryError(DatabaseError error) {

                        }
                    });

                    subscriber.add(Subscriptions.create(new Action0() {
                        @Override
                        public void call() {
                            geoQuery.removeAllListeners();
                        }
                    }));
                }
                lastLocation = currentLocation;
            }
        });
    }

    @Override
    public Observable<String> sendComment(String uid,Comment comment) {
        String commentKey = refComments.push().getKey();
        Map<String,Object> commentMap = comment.toMap();
        Map<String,Object> childUpdates = new HashMap<>();

        Map<String,Object> commentBuildingMap = new HashMap<>();
        commentBuildingMap.put(commentKey,commentKey);
        commentMap.put("id",commentKey);
        childUpdates.put("/Comments/"+commentKey,commentMap);
        childUpdates.put("UsersComments/"+uid+"/"+commentKey,commentKey);
        childUpdates.put("/BuildingsComments/"+currentBuilding.getId()+"/"+commentKey,commentKey);
        return updateChildren(childUpdates,commentKey);
    }

    @Override
    public Observable<Map<String, List<String>>> getBuildingsFeatures() {
        mapFeatures.clear();
        return Observable.create(new Observable.OnSubscribe<Map<String, List<String>>>() {
            @Override
            public void call(final Subscriber<? super Map<String, List<String>>> subscriber) {
                if(nearBuildingList != null && !nearBuildingList.isEmpty()) {
                    final ValueEventListener listener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot featureSnapshot) {
                            List<String> features = new ArrayList<String>();
                            for (DataSnapshot child : featureSnapshot.getChildren()) {
                                features.add(child.getValue(String.class));
                            }
                            for (String key : mapFeatures.keySet()) {
                                List<String> set2 = mapFeatures.get(key);
                                List<String> aux = new ArrayList<String>();
                                aux.addAll(set2);
                                set2.removeAll(features);
                                mapFeatures.put(key, set2);
                                features.removeAll(aux);
                            }
                            if (features.size() > 0) {
                                mapFeatures.put(featureSnapshot.getKey(), features);
                            }
                            if (mapFeatures.size() == nearBuildingList.size()) {
                                subscriber.onNext(mapFeatures);
                                subscriber.onCompleted();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            subscriber.onError(new FirebaseException(databaseError.getMessage()));
                        }
                    };

                    for (Building building : nearBuildingList) {
                        refBuildingsFeatures.child(building.getId()).addListenerForSingleValueEvent(listener);
                    }
                }else{
                    subscriber.onError(new Throwable());
                }
            }
        });
    }

    @Override
    public Observable<Building> getNearBuilding(final String buildingId) {
        commentList.clear();
        return Observable.create(new Observable.OnSubscribe<Building>() {
            @Override
            public void call(final Subscriber<? super Building> subscriber) {

                if (nearBuildingList != null) {
                    for (Building building : nearBuildingList) {
                        if (building.getId() == buildingId) {
                            currentBuilding = building;
                            subscriber.onNext(building);
                            subscriber.onCompleted();
                            break;
                        }
                    }
                } else {
                    subscriber.onError(new Throwable("No nearbuildinglist"));
                }

            }
        });
    }

    @Override
    public Observable<Building> getBuildingVisited(final String key, final String buildingId) {
        return Observable.create(new Observable.OnSubscribe<Building>() {
            @Override
            public void call(final Subscriber<? super Building> subscriber) {

                if (mapVisites != null) {
                    for (Building building : mapVisites.get(key)) {
                        if (building.getId() == buildingId) {
                            currentBuilding = building;
                            subscriber.onNext(building);
                            subscriber.onCompleted();
                            break;
                        }
                    }
                } else {
                    subscriber.onError(new Throwable("No nearbuildinglist"));
                }

            }
        });
    }

    @Override
    public Observable<MyLocation> updateMyCurrentLocation(final Location location) {
        return Observable.create(new Observable.OnSubscribe<MyLocation>() {
            @Override
            public void call(final Subscriber<? super MyLocation> subscriber) {
                if (location != null) {
                    currentLocation = new MyLocation(location);
                    subscriber.onNext(currentLocation);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new Throwable("No Location"));
                }
            }
        });
    }

    private Observable<String> updateChildren(final Map<String, Object> childUpdates, final String key) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                refBase.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        subscriber.onNext(task.isSuccessful()?key:null);
                        subscriber.onCompleted();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        subscriber.onError(e);
                    }
                });
            }
        });

    }
}
