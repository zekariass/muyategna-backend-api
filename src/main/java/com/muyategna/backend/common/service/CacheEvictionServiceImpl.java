package com.muyategna.backend.common.service;

import com.muyategna.backend.common.CommonCacheKeyUtil;
import com.muyategna.backend.common.Constants;
import com.muyategna.backend.common.entity.Language;
import com.muyategna.backend.common.repository.LanguageRepository;
import com.muyategna.backend.location.LocationCacheKeyUtil;
import com.muyategna.backend.location.entity.Country;
import com.muyategna.backend.location.entity.Region;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CacheEvictionServiceImpl implements CacheEvictionService {

    public final LanguageRepository languageRepository;
    private final CacheManager cacheManager;

    @Autowired
    public CacheEvictionServiceImpl(CacheManager cacheManager, LanguageRepository languageRepository) {
        this.cacheManager = cacheManager;
        this.languageRepository = languageRepository;
    }

    @Override
    public void evictRegionCache(Region region) {
        if (region == null) {
            return;
        }

        log.info("Evicting region with regionId={} from cache", region.getId());

        Cache cache = cacheManager.getCache(Constants.CACHE_LOCATION);
        if (cache == null) {
            return;
        }

        List<Language> languages = languageRepository.findAll();
        if (languages.isEmpty()) {
            return;
        }
        for (Language language : languages) {
            String key1 = LocationCacheKeyUtil.regionByIdAndLanguageKey(region.getId(), language.getId());
            String key2 = LocationCacheKeyUtil.regionsByCountryAndLanguageKey(region.getCountry().getId(), language.getId());
            cache.evict(key1);
            cache.evict(key2);
        }

        log.info("Evicted region with regionId={} from cache", region.getId());
    }


    @Override
    public void evictLanguageCache(Language language) {
        if (language == null) {
            return;
        }

        log.info("Evicting language with languageId={} from cache", language.getId());

        Cache cache = cacheManager.getCache(Constants.CACHE_COMMON);
        if (cache == null) {
            return;
        }

        String key1 = CommonCacheKeyUtil.languageByLocaleKey(language.getLocale());
        String key2 = CommonCacheKeyUtil.languagesByCountryKey(language.getCountry().getId());
        String key3 = CommonCacheKeyUtil.defaultLanguageForCountryKey(language.getCountry().getId());
        cache.evict(key1);
        cache.evict(key2);
        cache.evict(key3);

        log.info("Evicted language with languageId={} from cache", language.getId());
    }

    @Override
    public void evictCountryCache(Country country) {
        if (country == null) {
            return;
        }

        log.info("Evicting country with countryId={} from cache", country.getId());

        Cache cache = cacheManager.getCache(Constants.CACHE_LOCATION);
        if (cache == null) {
            return;
        }

        List<Language> languages = languageRepository.findAll();
        if (languages.isEmpty()) {
            return;
        }
        for (Language language : languages) {
            String key1 = LocationCacheKeyUtil.countryByIdAndLanguageKey(country.getId(), language.getId());
            String key2 = LocationCacheKeyUtil.countriesByLanguageKey(language.getId());
            cache.evict(key1);
            cache.evict(key2);
        }

        String key3 = LocationCacheKeyUtil.countryByIsoCode2Key(country.getCountryIsoCode2());
        String key4 = LocationCacheKeyUtil.countryByIdKey(country.getId());
        cache.evict(key3);
        cache.evict(key4);

        log.info("Evicted country with countryId={} from cache", country.getId());
    }


}
