package com.evanlin.cloud.client.test;

import static org.junit.Assert.*;

import java.util.Collection;

import org.apache.http.client.HttpClient;
import org.apache.http.HttpStatus;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.hsqldb.error.Error;
import org.junit.Test;
import org.junit.internal.runners.statements.Fail;

import com.evanlin.cloud.video.client.VideoSvcApi;
import com.evanlin.cloud.video.controller.Video;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;


public class VideoSvcClientApiTest {

	private class ErrorRecorder implements ErrorHandler {

		private RetrofitError error;

		@Override
		public Throwable handleError(RetrofitError cause) {
			error = cause;
			return error.getCause();
		}

		public RetrofitError getError() {
			return error;
		}
	}
	
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
		videoService.login("kkdai", "1234");
		videoService.addVideo(video);
		Collection<Video> videos = videoService.getVideoList();
		assertTrue(videos.contains(video));
		
		// Testing insert and get by ID.
		Collection<Video> videoLast = videoService.getVideoDataByID(videos.size());
		assertTrue(videoLast.size()==1);		
	}
	
	@Test
	public void testAccountRoles() throws Exception {
		Video video = TestData.randomVideo();
		videoService.login("test", "1234");
		videoService.addVideo(video);
		Collection<Video> videos = videoService.getVideoList();
		assertTrue(videos.contains(video));
		
		// Testing insert and get by ID.
		try {
			Collection<Video> videoLast = videoService.getVideoDataByID(videos.size());
			fail("Not go here!! Because the roles is not match");
		} catch(Exception e) {
			//Work well because it will failed 
		}
	}
	
	@Test
	public void testVideoAddAndList2() throws Exception {
		//Video video_updated = TestData.randomVideo();
		videoService.login("kkdai", "1234");	
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
		videoService.login("kkdai", "1234");
		Video video = TestData.randomVideo();		
		videoService.addVideo(video);
		Collection<Video> nameMatchedVideos = videoService.findByTitle(video.getName());
		assertTrue(nameMatchedVideos.contains(video));
	}

	
	@Test
	public void testAddVideoWithoutLogin() throws Exception {
		ErrorRecorder error_ret = new ErrorRecorder();

		VideoSvcApi videoUnloginService = new RestAdapter.Builder()
		.setClient(new ApacheClient(createUnsafeClient()))
		.setEndpoint(TEST_URL)
		.setLogLevel(LogLevel.FULL)
		.setErrorHandler(error_ret)
		.build()
		.create(VideoSvcApi.class);
		
		try {
			Video v = TestData.randomVideo();
			videoUnloginService.addVideo(v);
			fail("Yikes, the security setup is horribly broken and didn't require the user to login!!");
		}
		catch (Exception e) {
			assertEquals(HttpStatus.SC_MOVED_TEMPORARILY, error_ret.getError().getResponse().getStatus());
			
			// Fail and try to login
			videoService.login("kkdai", "1234");
			Video video = TestData.randomVideo();		
			videoService.addVideo(video);
			Collection<Video> nameMatchedVideos = videoService.findByTitle(video.getName());
			assertTrue(nameMatchedVideos.contains(video));
		}
	}
	
	@Test
	public void testLogout() throws Exception {
		videoService.login("kkdai", "1234");
		videoService.addVideo(TestData.randomVideo());
		videoService.logout();
		try {
			videoService.addVideo(TestData.randomVideo());
			fail("Not go here!!");
		} catch (Exception e) {
			//Logout worked.
		}		
	}
}
