package com.hanjum.newshanjumapi.domain.article.dto;

import com.hanjum.newshanjumapi.domain.article.entity.PreferenceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PreferRequest {
    
    private String articleLink;
    private PreferenceType preferenceType;
}
