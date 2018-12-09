package com.jezh.textsaver.repository;

import com.jezh.textsaver.entity.Bookmarks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmarks, Long> {

}