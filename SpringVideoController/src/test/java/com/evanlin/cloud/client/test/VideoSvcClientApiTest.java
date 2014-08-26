package com.evanlin.cloud.client.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.UUID;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

import retrofit.ErrorHandler;
import retrofit.RestAdapter.LogLevel;
import retrofit.RetrofitError;
import retrofit.client.ApacheClient;

import com.evanlin.cloud.video.client.SecuredRestBuilder;
import com.evanlin.cloud.video.client.VideoSvcApi;
import com.evanlin.cloud.video.controller.Video;


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
	private final String USERNAME = "kkdai";
	private final String PASSWORD = "1234";
	private final String CLIENT_ID = "mobile";
	private final String READ_ONLY_CLIENT_ID = "mobileReader";

	private VideoSvcApi videoService = new SecuredRestBuilder()
	.setLoginEndpoint(TEST_URL + VideoSvcApi.TOKEN_PATH)
	.setUsername(USERNAME)
	.setPassword(PASSWORD)
	.setClientId(CLIENT_ID)
	.setClient(new ApacheClient(createUnsafeClient()))
	.setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL).build()
	.create(VideoSvcApi.class);

	private VideoSvcApi invalidClientVideoService = new SecuredRestBuilder()
	.setLoginEndpoint(TEST_URL + VideoSvcApi.TOKEN_PATH)
	.setUsername(UUID.randomUUID().toString())
	.setPassword(UUID.randomUUID().toString())
	.setClientId(UUID.randomUUID().toString())
	.setClient(new ApacheClient(createUnsafeClient()))
	.setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL).build()
	.create(VideoSvcApi.class);

	private VideoSvcApi userClientVideoService = new SecuredRestBuilder()
	.setLoginEndpoint(TEST_URL + VideoSvcApi.TOKEN_PATH)
	.setUsername("test")
	.setPassword("1234")
	.setClientId(CLIENT_ID)
	.setClient(new ApacheClient(createUnsafeClient()))
	.setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL).build()
	.create(VideoSvcApi.class);

	private Video video = TestData.randomVideo();
	
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
	public void testSecuredVideoAddAndList() throws Exception {
		// Add the video
		videoService.addVideo(video);

		// We should get back the video that we added above
		Collection<Video> videos = videoService.getVideoList();
		assertTrue(videos.contains(video));
	}

	@Test
	public void testAccessDeniedWithIncorrectCredentials() throws Exception {

		try {
			// Add the video
			invalidClientVideoService.addVideo(video);

			fail("The server should have prevented the client from adding a video"
					+ " because it presented invalid client/user credentials");
		} catch (RetrofitError e) {
			assert (e.getCause() instanceof RuntimeException);
		}
	}

	@Test
	public void testVideoAddAndList() throws Exception {
		videoService.addVideo(video);
		Collection<Video> videos = videoService.getVideoList();
		assertTrue(videos.contains(video));
		
		// Testing insert and get by ID.
		Collection<Video> videoLast = videoService.getVideoDataByID(videos.size());
		assertTrue(videoLast.size()==1);		
	}
	
	@Test
	public void testAccountRoles() throws Exception {
		videoService.addVideo(video);
		Collection<Video> videos = userClientVideoService.getVideoList();
		assertTrue(videos.contains(video));
		
		// Testing insert and get by ID.
		try {
			Collection<Video> videoLast = userClientVideoService.getVideoDataByID(videos.size());
			fail("Not go here!! Because the roles is not match");
		} catch(Exception e) {
			//Work well because it will failed 
		}
	}
	
	@Test
	public void testVideoAddAndList2() throws Exception {
		//Video video_updated = TestData.randomVideo();
		Collection<Video> videos = videoService.getVideoList();
		if (videos.size() == 0) {
			testVideoAddAndList();
		}
	}
}
