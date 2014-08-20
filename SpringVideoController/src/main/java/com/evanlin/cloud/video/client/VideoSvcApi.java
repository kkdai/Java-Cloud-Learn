package com.evanlin.cloud.video.client;

import java.util.Collection;
import java.util.List;

import com.evanlin.cloud.video.controller.Video;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.PATCH;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface VideoSvcApi {
	
	// The path where we expect the VideoSvc to live
	public static final String VIDEO_SVC_PATH = "/video";	
	public static final String VIDEO_PATCH_PATH = VIDEO_SVC_PATH + "/{id}";
	public static final String VIDEO_DATA_PATH = VIDEO_SVC_PATH + "/{id}/data";
	public static final String VIDEO_SEARCH_PATH = VIDEO_SVC_PATH + "/find";
	public static final String DATA_PARAMETER = "data";
	public static final String ID_PARAMETER = "id";	
	public static final String TITLE_PARAMETER = "title";
	// The path to search videos by title
	public static final String VIDEO_TITLE_SEARCH_PATH = VIDEO_SVC_PATH + "/search/findByName";
	public static final String VIDEO_ID_SEARCH_PATH = VIDEO_SVC_PATH + "/search/findById";

	@GET(VIDEO_SVC_PATH)
	public Collection<Video> getVideoList();
	
	@POST(VIDEO_SVC_PATH)
	public Void addVideo(@Body Video v);

	@GET(VIDEO_ID_SEARCH_PATH)
	public Collection<Video> getVideoDataByID(@Query(ID_PARAMETER) long id);

	@GET(VIDEO_TITLE_SEARCH_PATH)
	public Collection<Video> findByTitle(@Query(TITLE_PARAMETER) String title);

	@PATCH(VIDEO_PATCH_PATH)
	public Void setVideoData(@Path(ID_PARAMETER) long id, @Body Video videoData);
}
