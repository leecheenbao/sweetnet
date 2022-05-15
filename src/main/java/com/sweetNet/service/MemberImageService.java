package com.sweetNet.service;

import com.sweetNet.model.MemberImage;

public interface MemberImageService {

	Iterable<MemberImage> findAll();

	void save(MemberImage contact);

}
