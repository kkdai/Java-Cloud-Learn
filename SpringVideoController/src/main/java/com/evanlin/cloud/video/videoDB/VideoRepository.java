package com.evanlin.cloud.video.videoDB;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.evanlin.cloud.video.controller.Video;

@Repository
public interface VideoRepository extends CrudRepository<Video, Long>{
	public Collection<Video> findByName(String title);

}
