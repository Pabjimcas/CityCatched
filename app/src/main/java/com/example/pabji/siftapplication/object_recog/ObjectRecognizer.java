package com.example.pabji.siftapplication.object_recog;

import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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

	public ObjectRecognizer(File trainDir) {

		/*detector = FeatureDetector.create(FeatureDetector.ORB);
		descriptor = DescriptorExtractor.create(DescriptorExtractor.ORB);
		matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);
		objectNames = new ArrayList<>();
		trainDescriptors = new ArrayList<>();

		Firebase mref = new Firebase("https://city-catched.firebaseio.com/");

		mref.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				int count = 0;
				for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
					String s = postSnapshot.getValue(String.class);
					s = s.substring(1,s.length()-1).replace(" ","");
					String[] e = s.split(",");
					byte[] array = new byte[e.length];
					for(int i = 0; i< e.length;i++){
						array[i] = Byte.valueOf(e[i]);
					}
					//Log.d("PTOP",Arrays.toString(array));
					Mat m = new Mat(500, 32, CvType.CV_8UC1);
					m.put(0, 0, array);
					trainDescriptors.add(m);
					if(count == 0) {
						objectNames.add("Casa de las conchas");
					}else if(count == 1){
						objectNames.add("La clerecia");
					}else{
						objectNames.add("Pantalla");
					}
					count++;


				}
				matcher.add(trainDescriptors);
				matcher.train();
				listo = true;
			}

			@Override
			public void onCancelled(FirebaseError firebaseError) {

			}
		});*/
		Firebase mref = new Firebase("https://city-catched.firebaseio.com/");
		ArrayList<File> jpgFiles = Utilities.getJPGFiles(trainDir);
		trainImages = Utilities.getImageMats(jpgFiles);
		objectNames = Utilities.getFileNames(jpgFiles);

		detector = FeatureDetector.create(FeatureDetector.ORB);
		descriptor = DescriptorExtractor.create(DescriptorExtractor.ORB);
		matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);

		trainKeypoints = new ArrayList<MatOfKeyPoint>();
		trainDescriptors = new ArrayList<Mat>();

		for (int i = 0; i < trainImages.size(); i++) {
			trainKeypoints.add(new MatOfKeyPoint());
			detector.detect(trainImages.get(i), trainKeypoints.get(i));
			trainDescriptors.add(new Mat());
			descriptor.compute(trainImages.get(i), trainKeypoints.get(i),
					trainDescriptors.get(i));
			/*Mat mat = trainDescriptors.get(i);
			byte[] data = new byte[ (int) (mat.total() * mat.channels()) ];
			mat.get(0,  0, data);
			mref.child(String.valueOf(i)).setValue(Arrays.toString(data));*/
		}



		matcher.add(trainDescriptors);
		matcher.train();
		listo = true;




		/*List<Mat> lista = matcher.getTrainDescriptors();

		for(int i = 0; i< lista.size(); i++){
			Mat mat = lista.get(i);
			byte[] data = new byte[ (int) (mat.total() * mat.channels()) ];
			mat.get(0,  0, data);
			mref.child(String.valueOf(i)).setValue(Arrays.toString(data));
			Log.d("PTOP","eNTRA");
		}*/

	}

	public void sendFirebase (Mat mat,int val){
		Firebase mref = new Firebase("https://city-catched.firebaseio.com/");
		MatOfKeyPoint matKeypoints = new MatOfKeyPoint();
		Mat matDescriptor = new Mat();
		detector.detect(mat, matKeypoints);
		trainDescriptors.add(new Mat());
		descriptor.compute(mat, matKeypoints,matDescriptor);
		byte[] data = new byte[ (int) (matDescriptor.total() * matDescriptor.channels()) ];
		mat.get(0,  0, data);
		mref.child(String.valueOf(val)).setValue(Arrays.toString(data));
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
		return "Caca de vaca";

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


