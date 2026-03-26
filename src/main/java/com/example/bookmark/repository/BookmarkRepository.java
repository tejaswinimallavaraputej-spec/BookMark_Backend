package com.example.bookmark.repository;

import com.example.bookmark.model.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Page<Bookmark> findAllByOrderByCreatedAtDesc(Pageable pageable);
    
    // For search/filtering
    Page<Bookmark> findByTitleContainingIgnoreCaseOrUrlContainingIgnoreCaseOrderByCreatedAtDesc(
            String title, String url, Pageable pageable);
            
    @org.springframework.data.jpa.repository.Query("SELECT b FROM Bookmark b WHERE b.id = :id AND b.user.id = :userId")
    Optional<Bookmark> findByIdAndUserId(
            @org.springframework.data.repository.query.Param("id") Long id, 
            @org.springframework.data.repository.query.Param("userId") Long userId);
}
