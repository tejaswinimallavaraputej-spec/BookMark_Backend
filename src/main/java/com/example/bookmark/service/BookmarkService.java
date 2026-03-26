package com.example.bookmark.service;

import com.example.bookmark.dto.BookmarkRequest;
import com.example.bookmark.dto.BookmarkResponse;
import com.example.bookmark.model.Bookmark;
import com.example.bookmark.model.User;
import com.example.bookmark.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    public BookmarkResponse createBookmark(BookmarkRequest request, User user) {
        log.info("Creating new bookmark for user: {}", user.getEmail());
        Bookmark bookmark = new Bookmark();
        bookmark.setTitle(request.getTitle());
        bookmark.setUrl(request.getUrl());
        bookmark.setUser(user);

        Bookmark saved = bookmarkRepository.save(bookmark);
        return mapToResponse(saved);
    }

    public Page<BookmarkResponse> getAllBookmarks(String search, Pageable pageable) {
        log.info("Fetching bookmarks with search: '{}', page: {}", search, pageable.getPageNumber());
        Page<Bookmark> bookmarkPage;
        
        if (search != null && !search.trim().isEmpty()) {
            bookmarkPage = bookmarkRepository.findByTitleContainingIgnoreCaseOrUrlContainingIgnoreCaseOrderByCreatedAtDesc(
                    search.trim(), search.trim(), pageable);
        } else {
            bookmarkPage = bookmarkRepository.findAllByOrderByCreatedAtDesc(pageable);
        }
        
        return bookmarkPage.map(this::mapToResponse);
    }

    public BookmarkResponse updateBookmark(Long bookmarkId, BookmarkRequest request, Long userId) {
        log.info("Updating bookmark id: {} for user id: {}", bookmarkId, userId);
        Bookmark bookmark = bookmarkRepository.findByIdAndUserId(bookmarkId, userId)
                .orElseThrow(() -> new RuntimeException("Bookmark not found or you don't have permission to update it"));
        
        bookmark.setTitle(request.getTitle());
        bookmark.setUrl(request.getUrl());
        Bookmark updated = bookmarkRepository.save(bookmark);
        return mapToResponse(updated);
    }

    public void deleteBookmark(Long bookmarkId, Long userId) {
        log.info("Deleting bookmark id: {} for user id: {}", bookmarkId, userId);
        Bookmark bookmark = bookmarkRepository.findByIdAndUserId(bookmarkId, userId)
                .orElseThrow(() -> new RuntimeException("Bookmark not found or you don't have permission to delete it"));
        bookmarkRepository.delete(bookmark);
    }

    private BookmarkResponse mapToResponse(Bookmark bookmark) {
        BookmarkResponse response = new BookmarkResponse();
        response.setId(bookmark.getId());
        response.setTitle(bookmark.getTitle());
        response.setUrl(bookmark.getUrl());
        response.setCreatedBy(bookmark.getUser().getName());
        response.setUserId(bookmark.getUser().getId());
        response.setCreatedAt(bookmark.getCreatedAt());
        return response;
    }
}
