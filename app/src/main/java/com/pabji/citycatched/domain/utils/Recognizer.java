package com.pabji.citycatched.domain.utils;

import com.pabji.citycatched.data.utils.Utilities;
import com.pabji.citycatched.domain.executor.PostExecutionThread;
import com.pabji.citycatched.domain.executor.ThreadExecutor;
import com.pabji.citycatched.presentation.mvp.models.Descriptor;
import com.pabji.citycatched.presentation.mvp.models.RecognizedObject;

import org.opencv.core.CvType;
import org.opencv.core.DMatch;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Size;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

public class Recognizer {

	private static Recognizer instance = null;

	private FeatureDetector detector;
	private DescriptorExtractor descriptor;
	private DescriptorMatcher matcher;

	private ArrayList<Mat> trainDescriptors;
	private ArrayList<String> buildingIds;

	private static final double RATIO_TEST_RATIO = 0.92;
	private static final int RATIO_TEST_MIN_NUM_MATCHES = 32;
	private ArrayList<Descriptor> nearBuildingDescriptors;

	private Mat descriptorsDetected;
	private ArrayList<Descriptor> posibleBuildings;

	private Subscription subscription = Subscriptions.empty();
	private ArrayList<Descriptor> nearBuildings;

	public Recognizer() {
		detector = FeatureDetector.create(FeatureDetector.ORB);
		descriptor = DescriptorExtractor.create(DescriptorExtractor.ORB);
		matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);
		buildingIds = new ArrayList<>();
		trainDescriptors = new ArrayList<>();
	}

	public static Recognizer getInstance(){
		if(instance == null){
			instance = new Recognizer();
		}
		return instance;
	}

	public void recognize(String path, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,Subscriber<RecognizedObject> subscriber) {
		this.subscription = recognizedObservable(path)
				.subscribeOn(Schedulers.from(threadExecutor))
				.observeOn(postExecutionThread.getScheduler())
				.subscribe(subscriber);
	}

	public void recognize(Mat mat, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,Subscriber<String> subscriber) {
		this.subscription = recognizedFrameObservable(mat)
				.subscribeOn(Schedulers.from(threadExecutor))
				.observeOn(postExecutionThread.getScheduler())
				.subscribe(subscriber);
	}

	private Observable recognizedFrameObservable(final Mat mGray) {

		return Observable.create(new Observable.OnSubscribe<String>() {
			@Override
			public void call(Subscriber<? super String> subscriber) {
				try {
					MatOfKeyPoint keypoints = new MatOfKeyPoint();
					Mat descriptors = new Mat();
					List<MatOfDMatch> matches = new LinkedList<MatOfDMatch>();

					detector.detect(mGray, keypoints);
					descriptor.compute(mGray, keypoints, descriptors);

					getMatches_ratioTest(descriptors, matches, RATIO_TEST_RATIO);
					Descriptor descriptorRecognized = getDetectedObjIndex(matches,
							RATIO_TEST_MIN_NUM_MATCHES);

					if(descriptorRecognized != null){
						subscriber.onNext(descriptorRecognized.buildingId);
					}else{
						subscriber.onNext(null);
					}
					subscriber.onCompleted();
				}catch (Exception e){
					subscriber.onError(e);
				}
			}
		});
	}

	public String getDescriptorDetected(){
		byte[] data = new byte[(int) (descriptorsDetected.total() * descriptorsDetected.channels())];
		descriptorsDetected.get(0, 0, data);
		return Utilities.encodeImage(data);
	}

	public Mat getDescriptorsImage(String path){

		Mat fullSizeTrainImg = Imgcodecs.imread(path);
		Mat resizedTrainImg = new Mat();
		Imgproc.resize(fullSizeTrainImg, resizedTrainImg, new Size(640, 480), 0, 0, Imgproc.INTER_CUBIC);

		MatOfKeyPoint keypoints = new MatOfKeyPoint();
		descriptorsDetected = new Mat();
		detector.detect(resizedTrainImg, keypoints);
		descriptor.compute(resizedTrainImg, keypoints, descriptorsDetected);
		return descriptorsDetected;
	}

	public Observable<RecognizedObject> recognizedObservable(final String path){
		return Observable.create(new Observable.OnSubscribe<RecognizedObject>() {
			@Override
			public void call(Subscriber<? super RecognizedObject> subscriber) {
				try {
					List<MatOfDMatch> matches = new LinkedList<>();
					Mat descriptors = getDescriptorsImage(path);
					getMatches_ratioTest(descriptors, matches, RATIO_TEST_RATIO);
					Descriptor descriptorRecognized = getDetectedObjIndex(matches,
							RATIO_TEST_MIN_NUM_MATCHES);
					RecognizedObject recognizedObject = new RecognizedObject(descriptorRecognized,
							getDescriptorDetected());

					subscriber.onNext(recognizedObject);
					subscriber.onCompleted();
				}catch (Exception e){
					subscriber.onError(e);
				}
			}
		});
	}

	private void getMatches_ratioTest(Mat descriptors,
									  List<MatOfDMatch> matches, double ratio) {
		LinkedList<MatOfDMatch> knnMatches = new LinkedList<>();
		DMatch bestMatch, secondBestMatch;

			matcher.knnMatch(descriptors, knnMatches, 2);

			for (MatOfDMatch matOfDMatch : knnMatches) {
				bestMatch = matOfDMatch.toArray()[0];
				secondBestMatch = matOfDMatch.toArray()[1];
				if (bestMatch.distance / secondBestMatch.distance <= ratio) {
					MatOfDMatch goodMatch = new MatOfDMatch();
					goodMatch.fromArray(bestMatch);
					matches.add(goodMatch);
				}
			}
	}

	private Descriptor getDetectedObjIndex(List<MatOfDMatch> matches,
													int minNumMatches) {
		int[] numMatchesInImage = new int[trainDescriptors.size()];
		int numMatches = 0;
		posibleBuildings = new ArrayList<>();
		Descriptor bestDescriptor = null;

		for (MatOfDMatch matOfDMatch : matches) {
			DMatch[] dMatch = matOfDMatch.toArray();
			boolean[] imagesMatched = new boolean[trainDescriptors.size()];
			for (DMatch aDMatch : dMatch) {
				if (!imagesMatched[aDMatch.imgIdx]) {
					numMatchesInImage[aDMatch.imgIdx]++;
					imagesMatched[aDMatch.imgIdx] = true;
				}
			}
		}
		for (int i = 0; i < numMatchesInImage.length; i++) {
			int m = numMatchesInImage[i];
			if (numMatchesInImage[i] > numMatches) {
				numMatches = numMatchesInImage[i];

				bestDescriptor = nearBuildingDescriptors.get(i);

				if(!buildingIds.contains(bestDescriptor.buildingId)) {
					posibleBuildings.add(bestDescriptor);
					buildingIds.add(bestDescriptor.buildingId);
				}
			}
		}
		if (numMatches < minNumMatches) {
			return null;
		} else {
			return bestDescriptor;
		}
	}

	public void addDescriptors(List<Descriptor> descriptors) throws UnsupportedEncodingException {
		this.nearBuildingDescriptors = (ArrayList<Descriptor>) descriptors;
		trainDescriptors.clear();
		buildingIds.clear();
		for(Descriptor descriptor: descriptors){
			byte[] array = Utilities.decodeImage(descriptor.content);
			Mat m = new Mat(500, 32, CvType.CV_8UC1);
			m.put(0, 0, array);
			trainDescriptors.add(m);
		}

		matcher.clear();
		matcher.add(trainDescriptors);
		matcher.train();

	}

	public ArrayList<Descriptor> getPossibleBuildingsDescriptors() {
		return posibleBuildings;
	}
}


