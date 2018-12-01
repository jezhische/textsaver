package com.jezh.textsaver.repository;

import com.jezh.textsaver.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    @Query(value = "SELECT * FROM public.get_all_bookmarks_in_sorted_order(?1)", nativeQuery = true)
    List<Bookmark> getAllInSortedOrder(Long textCommonDataId);
}
