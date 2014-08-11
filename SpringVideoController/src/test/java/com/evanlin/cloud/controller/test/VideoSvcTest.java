package com.evanlin.cloud.controller.test;

import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.evanlin.cloud.video.controller.Video;
import com.evanlin.cloud.video.controller.VideoSvc;

/**
 * 
 * This test constructs a VideoSvc object, adds a Video to it, and then
 * checks that the Video is returned when getVideoList() is called.
 * 
 * 
 * To run this test, right-click on it in Eclipse and select
 * "Run As"->"JUnit Test"
 * 
 * Pay attention to how the test that actually uses HTTP and this test that
 * just directly makes method calls on a VideoSvc object are essentially
 * identical. All that changes is the setup of the videoService variable.
 * Yes, this could be refactored to eliminate code duplication...but the
 * goal was to show how much Retrofit simplifies interaction with our 
 * service!
 * 
 * @author jules
 *
 */
public class VideoSvcTest {
	
	private VideoSvc videoService = new VideoSvc();

	/**
	 * This test creates a Video, adds the Video to the VideoSvc, and then
	 * checks that the Video is included in the list when getVideoList() is
	 * called.
	 * 
	 * @throws Exception
	 */
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

		// Test the servlet directly, without going through the network.
		boolean ok = videoService.addVideo(video);
		assertTrue(ok);
		
		List<Video> videos = videoService.getVideoList();
		assertTrue(videos.contains(video));
		
		Video video2 = videoService.getVideoDataByID(1);
		assertTrue(video == video2);
	}

}
