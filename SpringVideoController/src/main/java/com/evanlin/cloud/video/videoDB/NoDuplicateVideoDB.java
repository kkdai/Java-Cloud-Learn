package com.evanlin.cloud.video.videoDB;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.evanlin.cloud.video.controller.Video;

public class NoDuplicateVideoDB implements videoDB {

	private Set<Video> videoSet = Collections.newSetFromMap(
	        new ConcurrentHashMap<Video, Boolean>());
	
	@Override
	public boolean addVideo(Video v) {
		videoSet.add(v);
		return true;
	}

	@Override
	public Collection<Video> getVideos() {
		return videoSet;
	}

	@Override
	public Collection<Video> findByNamee(String name) {
		Set<Video> matched =  new HashSet<>();
		for (Video v: videoSet) {
			if (v.getName().contains(name)) {
				matched.add(v);
			}
		}
		return matched;
	}

}
