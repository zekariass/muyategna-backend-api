package com.muyategna.backend.system.context;

import com.muyategna.backend.location.dto.country.CountryDto;

public class CountryContextHolder {

    private static final ThreadLocal<CountryDto> context = ThreadLocal.withInitial(() -> null);

    public static CountryDto getCountry() {
        return context.get();
    }

    public static void setCountry(CountryDto country) {
        context.set(country);
    }

    public static void clear() {
        context.remove();
    }

}
