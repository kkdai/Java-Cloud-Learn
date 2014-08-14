package com.evanlin.cloud.client.test;

import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.junit.Test;

import com.evanlin.cloud.video.client.VideoSvcApi;
import com.evanlin.cloud.video.controller.Video;

import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;

/**
 * 
 * This test sends a POST request to the VideoServlet to add a new video and
 * then sends a second GET request to check that the video showed up in the list
 * of videos.
 * 
 * The test requires that the application be running first (see the directions in
 * the README.md file for how to launch the application.
 * 
 * To run this test, right-click on it in Eclipse and select
 * "Run As"->"JUnit Test"
 * 
 * Pay attention to how this test that actually uses HTTP and the test that
 * just directly makes method calls on a VideoSvc object are essentially
 * identical. All that changes is the setup of the videoService variable.
 * Yes, this could be refactored to eliminate code duplication...but the
 * goal was to show how much Retrofit simplifies interaction with our 
 * service!
 * 
 * @author jules
 *
 */
public class VideoSvcClientApiTest {

	private final String TEST_URL = "http://localhost:9000";

	private VideoSvcApi videoService = new RestAdapter.Builder()
			.setEndpoint(TEST_URL)
			.setLogLevel(LogLevel.FULL)
			.build()
			.create(VideoSvcApi.class);

	@Test
	public void testVideoAddAndList() throws Exception {
		// Information about the video
		String title = "Programming Cloud Services for Android Handheld Systems";
		String url = "http://coursera.org/some/video";
		long duration = 60 * 10 * 1000; // 10min in milliseconds
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String current_date = sdf.format(date);
		
		Video video = new Video(0, title, url, duration, current_date);
		Collection<Video> videos = videoService.getVideoList();
		long currentID = videos.size()+1;
		video.setId(currentID);

		boolean ok = videoService.addVideo(video);
		assertTrue(ok);
		Collection<Video> videos2 = videoService.getVideoList();
		assertTrue(videos2.contains(video));
		
		// Testing insert and get by ID.
		Video video2 = videoService.getVideoDataByID(currentID);
		assertTrue(video.getId() == video2.getId());		
	}
	
	@Test
	public void testVideoAddAndList2() throws Exception {
		String title = "[update]Programming Cloud Services for Android Handheld Systems";
		String url = "http://coursera.org/some/video";
		long duration = 30 * 10 * 1000; // 10min in milliseconds
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String current_date = sdf.format(date);		
		Video video_updated = new Video(1, title, url, duration, current_date);
	
		Collection<Video> videos = videoService.getVideoList();
		if (videos.size() == 0) {
			testVideoAddAndList();
		}
		
		//verify updated
		videoService.setVideoData(1, video_updated);			
		Collection<Video> videos_update = videoService.getVideoList();
		assertTrue(videos_update.contains(video_updated));
	}
}
