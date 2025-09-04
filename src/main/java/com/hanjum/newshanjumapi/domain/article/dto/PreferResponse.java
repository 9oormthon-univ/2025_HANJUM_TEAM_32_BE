package com.hanjum.newshanjumapi.domain.article.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PreferResponse {
    private boolean success;
    private String message;
}