package com.muyategna.backend.location.projection;


/**
 * Projection interface for CountryTranslationRepository entity.
 *
 * <p>
 * The purpose of this interface is to provide a way to select specific fields from the CountryTranslationRepository
 * entity while querying the database. This helps to reduce the amount of data loaded from the database and
 * improve the performance of the application.
 * </p>
 */
public interface CountryTranslationView {
    Long getCountryId();

    String getName();

    String getContinent();

    String getDescription();
}
