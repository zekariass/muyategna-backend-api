package com.muyategna.backend.system.context;

import com.muyategna.backend.common.dto.language.LanguageDto;

public class LanguageContextHolder {

    private static final ThreadLocal<LanguageDto> context = ThreadLocal.withInitial(() -> null);

    public static LanguageDto getLanguage() {
        return context.get();
    }

    public static void setLanguage(LanguageDto language) {
        context.set(language);
    }

    public static void clear() {
        context.remove();
    }
}
