package com.example.pabji.siftapplication.utils;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ObjectRecognizer {

	private FeatureDetector detector;
	private DescriptorExtractor descriptor;
	private DescriptorMatcher matcher;

	private ArrayList<Mat> trainDescriptors;
	private ArrayList<String> objectNames;

	private MatchingStrategy matchingStrategy = MatchingStrategy.RATIO_TEST;

	private int numMatches;
	private int matchIndex;
	private int[] numMatchesInImage;
	private  boolean listo = false;
	private HashMap<String, List<String>> map;

	public ObjectRecognizer(final Context context, List<String> buildings) {

		detector = FeatureDetector.create(FeatureDetector.ORB);
		descriptor = DescriptorExtractor.create(DescriptorExtractor.ORB);
		matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);
		objectNames = new ArrayList<>();
		trainDescriptors = new ArrayList<>();
		getDataFirebase(context,buildings);
	};

	private void getDataFirebase(final Context context, final List<String> buildings) {
		Firebase mref = new Firebase("https://city-catched.firebaseio.com/descriptors");
		mref.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				listo = false;
				map = new HashMap<>();
				objectNames.clear();
				trainDescriptors.clear();

				for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
					List<String> list = new ArrayList<>();
					if(buildings.contains(postSnapshot.getKey())) {
						for (DataSnapshot postSnapshot2 : postSnapshot.getChildren()) {
							String s = postSnapshot2.getValue(String.class);
							byte[] array = Utilities.decodeImage(s);
							Mat m = new Mat(500, 32, CvType.CV_8UC1);
							m.put(0, 0, array);
							trainDescriptors.add(m);
							objectNames.add(postSnapshot.getKey());
							list.add(postSnapshot.getKey());
						}
						map.put(postSnapshot.getKey(), list);
					}
				}
				matcher.clear();
				matcher.add(trainDescriptors);
				matcher.train();
				listo = true;
			}

			@Override
			public void onCancelled(FirebaseError firebaseError) {

			}
		});


	}

	public Mat getDescriptorsImage(Mat image){
		MatOfKeyPoint keypoints = new MatOfKeyPoint();
		Mat descriptors = new Mat();
		List<MatOfDMatch> matches = new LinkedList<MatOfDMatch>();

		detector.detect(image, keypoints);
		descriptor.compute(image, keypoints, descriptors);
		return descriptors;
	}


	public String recognize(Mat mGray) {
		if(listo){
			MatOfKeyPoint keypoints = new MatOfKeyPoint();
			Mat descriptors = new Mat();
			List<MatOfDMatch> matches = new LinkedList<MatOfDMatch>();

			detector.detect(mGray, keypoints);
			descriptor.compute(mGray, keypoints, descriptors);
			return match(keypoints, descriptors, matches, matchingStrategy);
		}
		return "Initialization...";

	}
	public static final double RATIO_TEST_RATIO = 0.92;
	public static final int RATIO_TEST_MIN_NUM_MATCHES = 32;

	public String match(MatOfKeyPoint keypoints, Mat descriptors,
						List<MatOfDMatch> matches, MatchingStrategy matchingStrategy) {
		return match_ratioTest(descriptors, matches, RATIO_TEST_RATIO,
				RATIO_TEST_MIN_NUM_MATCHES);

	}

	private String match_ratioTest(Mat descriptors, List<MatOfDMatch> matches,
								   double ratio, int minNumMatches) {
		getMatches_ratioTest(descriptors, matches, ratio);
		return getDetectedObjIndex(matches, minNumMatches);
	}

	private void getMatches_ratioTest(Mat descriptors,
									  List<MatOfDMatch> matches, double ratio) {
		LinkedList<MatOfDMatch> knnMatches = new LinkedList<MatOfDMatch>();
		DMatch bestMatch, secondBestMatch;

			matcher.knnMatch(descriptors, knnMatches, 2);

			for (MatOfDMatch matOfDMatch : knnMatches) {
				bestMatch = matOfDMatch.toArray()[0];
				secondBestMatch = matOfDMatch.toArray()[1];
				if (bestMatch.distance / secondBestMatch.distance <= ratio) {
					MatOfDMatch goodMatch = new MatOfDMatch();
					goodMatch.fromArray(new DMatch[]{bestMatch});
					matches.add(goodMatch);
				}
			}
	}

	private String getDetectedObjIndex(List<MatOfDMatch> matches,
									   int minNumMatches) {
		numMatchesInImage = new int[trainDescriptors.size()];
		matchIndex = -1;
		numMatches = 0;

		for (MatOfDMatch matOfDMatch : matches) {
			DMatch[] dMatch = matOfDMatch.toArray();
			boolean[] imagesMatched = new boolean[trainDescriptors.size()];
			for (int i = 0; i < dMatch.length; i++) {
				if (!imagesMatched[dMatch[i].imgIdx]) {
					numMatchesInImage[dMatch[i].imgIdx]++;
					imagesMatched[dMatch[i].imgIdx] = true;
				}
			}
		}
		for (int i = 0; i < numMatchesInImage.length; i++) {
			if (numMatchesInImage[i] > numMatches) {
				matchIndex = i;
				numMatches = numMatchesInImage[i];
			}
		}
		if (numMatches < minNumMatches) {
			return "-";
		} else {
			return objectNames.get(matchIndex);
		}
	}
}


