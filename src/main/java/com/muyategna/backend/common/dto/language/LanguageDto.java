package com.muyategna.backend.common.dto.language;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LanguageDto {
    private long id;
    private String name;
    private String locale;
    private boolean isActive;
    private boolean isDefault;
    private boolean isGlobal;
    private Long countryId;
    private String nativeName;
    private String direction;
    private String flagEmoji;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
