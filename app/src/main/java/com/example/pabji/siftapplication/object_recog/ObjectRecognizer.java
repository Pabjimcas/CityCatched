package com.example.pabji.siftapplication.object_recog;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;

public class ObjectRecognizer {

	private FeatureDetector detector;
	private DescriptorExtractor descriptor;
	private DescriptorMatcher matcher;

	private ArrayList<Mat> trainImages;
	private ArrayList<MatOfKeyPoint> trainKeypoints;
	private ArrayList<Mat> trainDescriptors;
	private ArrayList<String> objectNames;

	private MatchingStrategy matchingStrategy = MatchingStrategy.RATIO_TEST;

	private int numMatches;
	private int matchIndex;
	private int[] numMatchesInImage;
	private  boolean listo = false;
	private HashMap<String, List<String>> map;

	public void updateFirebase(File dir){
		Firebase mref = new Firebase("https://city-catched.firebaseio.com/descriptors");
		ArrayList<File> jpgFiles = Utilities.getJPGFiles(dir);
		trainImages = Utilities.getImageMats(jpgFiles);
		objectNames = Utilities.getFileNames(jpgFiles);

		detector = FeatureDetector.create(FeatureDetector.ORB);
		descriptor = DescriptorExtractor.create(DescriptorExtractor.ORB);
		matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);

		trainKeypoints = new ArrayList<MatOfKeyPoint>();
		trainDescriptors = new ArrayList<Mat>();

		for (int i = 0; i < trainImages.size(); i++) {
			String build = objectNames.get(i);
			trainKeypoints.add(new MatOfKeyPoint());
			detector.detect(trainImages.get(i), trainKeypoints.get(i));
			trainDescriptors.add(new Mat());
			descriptor.compute(trainImages.get(i), trainKeypoints.get(i),
						trainDescriptors.get(i));
			Mat mat = trainDescriptors.get(i);
			byte[] data = new byte[(int) (mat.total() * mat.channels())];
			mat.get(0, 0, data);

			mref.child(build).child(String.valueOf(System.currentTimeMillis())).setValue(Utilities.encodeImage(data));
		}

		matcher.add(trainDescriptors);
		matcher.train();
		dir.delete();
	}

	public ObjectRecognizer(final Context context) {

		detector = FeatureDetector.create(FeatureDetector.ORB);
		descriptor = DescriptorExtractor.create(DescriptorExtractor.ORB);
		matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);
		objectNames = new ArrayList<>();
		trainDescriptors = new ArrayList<>();
		Firebase mref = new Firebase("https://city-catched.firebaseio.com/descriptors");
		Log.d("TAG","Estoy aqui");
		mref.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				listo = false;
				int count = 0;
				map = new HashMap<String,List<String>>();
				objectNames.clear();
				trainDescriptors.clear();


				for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
					List<String> list = new ArrayList<>();
					Log.d("TAG",String.valueOf(postSnapshot.getChildrenCount()));
					for(DataSnapshot postSnapshot2: postSnapshot.getChildren()){
						String s = postSnapshot2.getValue(String.class);
						byte[] array = Utilities.decodeImage(s);
						Mat m = new Mat(500, 32, CvType.CV_8UC1);
						m.put(0, 0, array);
						trainDescriptors.add(m);
						objectNames.add(postSnapshot.getKey());
						list.add(postSnapshot.getKey());
					}
					map.put(postSnapshot.getKey(),list);
				}
				matcher.clear();
				matcher.add(trainDescriptors);
				matcher.train();
				Toast.makeText(context,String.valueOf(matcher.getTrainDescriptors().size()),Toast.LENGTH_SHORT).show();
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

	public void sendFirebase (Mat mat, Location location, int val){
		Firebase mref = new Firebase("https://city-catched.firebaseio.com/");
		MatOfKeyPoint matKeypoints = new MatOfKeyPoint();
		Mat matDescriptor = new Mat();
		detector.detect(mat, matKeypoints);
		descriptor.compute(mat, matKeypoints,matDescriptor);

		byte[] data = new byte[ (int) (matDescriptor.total() * matDescriptor.channels()) ];
		mat.get(0,  0, data);
		//String s = Utilities.encodeImage(data);
		mref.child("descriptors").child(String.valueOf(val)).child(String.valueOf(System.currentTimeMillis())).setValue(Arrays.toString(data));
		/*mref.child(String.valueOf(val)).child("location").child("latitude").setValue(location.getLatitude());
		mref.child(String.valueOf(val)).child("location").child("longitude").setValue(location.getLongitude());*/
		Log.d("FIREBASE","OK");
	}

	public void removeObject(int clickedImgIdx) {
		trainImages.remove(clickedImgIdx);
		objectNames.remove(clickedImgIdx);
		trainKeypoints.remove(clickedImgIdx);
		trainDescriptors.remove(clickedImgIdx);

		matcher.clear();
		matcher.add(trainDescriptors);
		matcher.train();
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

	// Parameters for matching
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

	// adds to the matches list matches that satisfy the ratio test with ratio
	// ratio
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
		Log.d("PTOPI",String.valueOf(matches.size()));
	}

	// uses the list of matches to count the number of matches to each database
	// object. The object with the maximum such number nmax is considered to
	// have been recognized if nmax > minNumMatches.
	// if for a query descriptor there exists multiple matches to train
	// descriptors of the same train image, all such matches are counted as only
	// one match.
	// returns the name of the object detected, or "-" if no object is detected.
	private String getDetectedObjIndex(List<MatOfDMatch> matches,
									   int minNumMatches) {
		numMatchesInImage = new int[trainDescriptors.size()];
		matchIndex = -1;
		numMatches = 0;

		Log.d("PTOP",String.valueOf(matches.size()));

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


