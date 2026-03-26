package com.example.bookmark.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BookmarkRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "URL is required")
    private String url;
}
