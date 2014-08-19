package com.evanlin.cloud.video.client;

import java.util.Collection;
import java.util.List;

import com.evanlin.cloud.video.controller.Video;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface VideoSvcApi {
	
	// The path where we expect the VideoSvc to live
	public static final String VIDEO_SVC_PATH = "/video";	
	public static final String VIDEO_DATA_PATH = VIDEO_SVC_PATH + "/{id}/data";
	public static final String VIDEO_SEARCH_PATH = VIDEO_SVC_PATH + "/find";
	public static final String DATA_PARAMETER = "data";
	public static final String ID_PARAMETER = "id";	
	public static final String NAME_PARAMETER = "name";

	@GET(VIDEO_SVC_PATH)
	public Collection<Video> getVideoList();
	
	@POST(VIDEO_SVC_PATH)
	public Void addVideo(@Body Video v);

	@GET(VIDEO_DATA_PATH)
	public Video getVideoDataByID(@Path(ID_PARAMETER) long id);

	@POST(VIDEO_DATA_PATH)
	public boolean setVideoData(@Path(ID_PARAMETER) long id, @Body Video videoData);

	@GET(VIDEO_SEARCH_PATH)
	public Collection<Video> findByTitle(@Query(NAME_PARAMETER) String title);

}
