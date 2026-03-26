package com.example.bookmark.controller;

import com.example.bookmark.dto.BookmarkRequest;
import com.example.bookmark.dto.BookmarkResponse;
import com.example.bookmark.model.User;
import com.example.bookmark.service.BookmarkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/bookmarks")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000", "http://localhost:5174"})
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @GetMapping
    public ResponseEntity<Page<BookmarkResponse>> getAllBookmarks(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(bookmarkService.getAllBookmarks(search, PageRequest.of(page, size)));
    }

    @PostMapping
    public ResponseEntity<BookmarkResponse> createBookmark(
            @Valid @RequestBody BookmarkRequest request,
            @AuthenticationPrincipal User user) {
        BookmarkResponse response = bookmarkService.createBookmark(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookmarkResponse> updateBookmark(
            @PathVariable Long id,
            @Valid @RequestBody BookmarkRequest request,
            @AuthenticationPrincipal User user) {
        BookmarkResponse response = bookmarkService.updateBookmark(id, request, user.getId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteBookmark(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        bookmarkService.deleteBookmark(id, user.getId());
        return ResponseEntity.ok(Map.of("message", "Bookmark deleted successfully"));
    }
}
