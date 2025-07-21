package com.jose.twttr_security.controller.dto;

public record FeedItemDTO(long tweetId,
                          String content,
                          String username) {
}
