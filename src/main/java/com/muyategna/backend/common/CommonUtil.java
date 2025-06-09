package com.muyategna.backend.common;

import java.util.Arrays;

public final class CommonUtil {

    /**
     * Checks if the provided sort field is valid for the given entity class.
     *
     * @param entityClass the class of the entity to check against
     * @param sortField   the name of the field to check
     * @return true if the sort field is invalid, false otherwise
     */
    public static boolean isSortFieldInvalid(Class<?> entityClass, String sortField) {
        return Arrays.stream(entityClass.getDeclaredFields())
                .noneMatch(field -> field.getName().equals(sortField));
    }
}
