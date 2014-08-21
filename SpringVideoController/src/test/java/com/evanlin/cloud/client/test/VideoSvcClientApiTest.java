package com.evanlin.cloud.client.test;

import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

import com.evanlin.cloud.video.client.VideoSvcApi;
import com.evanlin.cloud.video.controller.Video;

import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;

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

	private final String TEST_URL = "https://localhost:8443";

	private VideoSvcApi videoService = new RestAdapter.Builder()
			.setClient(new ApacheClient(createUnsafeClient()))
			.setEndpoint(TEST_URL)
			.setLogLevel(LogLevel.FULL)
			.build()
			.create(VideoSvcApi.class);

	
	public static HttpClient createUnsafeClient() {
		try {
			SSLContextBuilder builder = new SSLContextBuilder();
			builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
					builder.build());
			CloseableHttpClient httpclient = HttpClients.custom()
					.setSSLSocketFactory(sslsf).build();

			return httpclient;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Test
	public void testVideoAddAndList() throws Exception {
		Video video = TestData.randomVideo();
		videoService.addVideo(video);
		Collection<Video> videos = videoService.getVideoList();
		assertTrue(videos.contains(video));
		
		// Testing insert and get by ID.
		Collection<Video> videoLast = videoService.getVideoDataByID(videos.size());
		assertTrue(videoLast.size()==1);		
	}
	
	@Test
	public void testVideoAddAndList2() throws Exception {
		//Video video_updated = TestData.randomVideo();
	
		Collection<Video> videos = videoService.getVideoList();
		if (videos.size() == 0) {
			testVideoAddAndList();
		}
		
		//verify updated latest one item
		//Collection<Video> videos2 = videoService.getVideoList();
		//long currentID = videos2.size();
		//videoService.setVideoData(currentID, video_updated);			
		//Collection<Video> videos_update = videoService.getVideoList();
		//assertTrue(videos_update.contains(video_updated));
	}

	@Test
	public void testSearchVideoByTitle() throws Exception {
		Video video = TestData.randomVideo();		
		videoService.addVideo(video);
		Collection<Video> nameMatchedVideos = videoService.findByTitle(video.getName());
		assertTrue(nameMatchedVideos.contains(video));
	}
}
