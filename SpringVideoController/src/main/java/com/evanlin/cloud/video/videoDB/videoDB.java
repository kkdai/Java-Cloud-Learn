package com.evanlin.cloud.video.videoDB;

import java.util.Collection;

import com.evanlin.cloud.video.controller.Video;

public interface videoDB {
	public boolean addVideo(Video v);
	public Collection<Video> getVideos();
	public Collection<Video> findByNamee(String name);
}
