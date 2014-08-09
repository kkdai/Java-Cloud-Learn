package com.evanlin.cloud.client.test;

import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

	/**
	 * This is how we turn the VideoSvcApi into an object that
	 * will translate method calls on the VideoSvcApi's interface
	 * methods into HTTP requests on the server. Parameters / return
	 * values are being marshalled to/from JSON.
	 */
	private VideoSvcApi videoService = new RestAdapter.Builder()
			.setEndpoint(TEST_URL)
			.setLogLevel(LogLevel.FULL)
			.build()
			.create(VideoSvcApi.class);

	/**
	 * This test sends a POST request to the VideoServlet to add a new video and
	 * then sends a second GET request to check that the video showed up in the
	 * list of videos.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testVideoAddAndList() throws Exception {
		// Information about the video
		String title = "Programming Cloud Services for Android Handheld Systems";
		String url = "http://coursera.org/some/video";
		long duration = 60 * 10 * 1000; // 10min in milliseconds
		
		//目前時間
		Date date = new Date();
		//設定日期格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String current_date = sdf.format(date);
		
		Video video = new Video(title, url, duration, current_date);
		
		// Send the POST request to the VideoServlet using Retrofit to add the video.
		// Notice how Retrofit provides a nice strongly-typed interface to our
		// HTTP-accessible video service that is much cleaner than muddling around
		// with URL query parameters, etc.
		boolean ok = videoService.addVideo(video);
		assertTrue(ok);
		
		List<Video> videos = videoService.getVideoList();
		assertTrue(videos.contains(video));
	}

}
