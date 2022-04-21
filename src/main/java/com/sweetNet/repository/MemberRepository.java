package com.sweetNet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sweetNet.model.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

}