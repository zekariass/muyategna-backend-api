package com.muyategna.backend.common.service;

import com.muyategna.backend.common.entity.Language;
import com.muyategna.backend.location.entity.Country;
import com.muyategna.backend.location.entity.Region;

public interface CacheEvictionService {
    void evictRegionCache(Region region);

    void evictLanguageCache(Language language);

    void evictCountryCache(Country country);
}
