package com.jezh.textsaver.repository;

import com.jezh.textsaver.entity.TextCommonData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextCommonDataRepository extends JpaRepository<TextCommonData, Long>{
}
