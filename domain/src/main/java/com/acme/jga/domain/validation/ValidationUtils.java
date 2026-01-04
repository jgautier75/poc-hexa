package com.acme.jga.domain.validation;

import com.acme.jga.domain.i18n.BundleFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {
    private static final Pattern EMAIL_REGEX = Pattern.compile("^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$");
    private static final Pattern PHONE_REGEX = Pattern.compile("^(?:\\+)?(?:[ \\-0-9])+$");

    public static boolean isNotNull(Object obj) {
        return obj != null;
    }

    public static boolean isNotEmpty(String obj) {
        return !"".equals(obj);
    }

    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        Matcher m = EMAIL_REGEX.matcher(email);
        return m.matches();
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return true;
        }
        Matcher m = PHONE_REGEX.matcher(phone);
        return m.matches();
    }

    public static boolean validateListNotEmpty(ValidationResult validationResult, String fieldName, List<?> list) {
        if (list == null || list.isEmpty()) {
            validationResult.setSuccess(false);
            validationResult.addError(ValidationError.builder()
                    .fieldName(fieldName)
                    .validationRule(ValidationRule.LIST_NOT_NULL_NOT_EMPTY.name())
                    .message(buildNonNullMessage(fieldName))
                    .build());
        }
        return validationResult.isSuccess();
    }

    public static boolean validateNotNull(ValidationResult validationResult, String fieldName, Object fieldValue) {
        if (!isNotNull(fieldValue)) {
            validationResult.setSuccess(false);
            validationResult.addError(ValidationError.builder()
                    .fieldName(fieldName)
                    .validationRule(ValidationRule.NOT_NULL.name())
                    .message(buildNonNullMessage(fieldName))
                    .build());
        }
        return validationResult.isSuccess();
    }

    public static boolean validateNotNullNonEmpty(ValidationResult validationResult, String fieldName, String fieldValue) {
        if (!isNotNull(fieldValue)) {
            validationResult.setSuccess(false);
            validationResult.addError(ValidationError.builder()
                    .fieldName(fieldName)
                    .validationRule(ValidationRule.NOT_NULL.name())
                    .message(buildNonNullMessage(fieldName))
                    .build());
            return false;
        } else {
            if (!isNotEmpty(fieldValue)) {
                validationResult.setSuccess(false);
                validationResult.addError(ValidationError.builder()
                        .fieldName(fieldName)
                        .validationRule(ValidationRule.NOT_EMPTY.name())
                        .message(buildNonNullMessage(fieldName))
                        .build());
                return false;
            }
        }
        return true;
    }

    public static boolean validateNotNullNonEmpty(ValidationResult validationResult, String fieldName, Long fieldValue) {
        if (!isNotNull(fieldValue)) {
            validationResult.setSuccess(false);
            validationResult.addError(ValidationError.builder()
                    .fieldName(fieldName)
                    .validationRule(ValidationRule.NOT_NULL.name())
                    .message(buildNonNullMessage(fieldName))
                    .build());
        } else {
            if (fieldValue == 0) {
                validationResult.setSuccess(false);
                validationResult.addError(ValidationError.builder()
                        .fieldName(fieldName)
                        .validationRule(ValidationRule.NOT_EMPTY.name())
                        .message(buildNonNullMessage(fieldName))
                        .build());
            }
        }
        return validationResult.isSuccess();
    }

    public static boolean validatePayLoad(ValidationResult validationResult, String fieldName, Object fieldValue) {
        if (!isNotNull(fieldValue)) {
            validationResult.setSuccess(false);
            validationResult.addError(ValidationError.builder()
                    .fieldName(fieldName)
                    .validationRule(ValidationRule.PAYLOAD.name())
                    .message(buildNonNullMessage(fieldName))
                    .build());
        }
        return validationResult.isSuccess();
    }

    public static boolean validateTextLength(ValidationResult validationResult, String fieldName, String txt, int min,
                                             int max) {
        if (isNotNull(txt) && isNotEmpty(txt) && !isValidTextLength(txt, min, max)) {
            validationResult.setSuccess(false);
            validationResult.addError(ValidationError.builder()
                    .fieldName(fieldName)
                    .validationRule(ValidationRule.LENGTH.name())
                    .message(buildInvalidTextLength(fieldName, min, max))
                    .build());
        }
        return validationResult.isSuccess();
    }

    public static String buildNonNullMessage(String field) {
        return BundleFactory.getMessage("validation_field_nonnull", new Object[]{field});
    }

    public static String buildNonEmpty(String field) {
        return BundleFactory.getMessage("validation_field_nonempty", new Object[]{field});
    }

    public static String buildInvalidEmail(String field, String pattern) {
        return BundleFactory.getMessage("validation_field_email", new Object[]{field, pattern});
    }

    public static String buildInvalidPhone(String field, String pattern) {
        return BundleFactory.getMessage("validation_field_phone", new Object[]{field, pattern});
    }

    public static String getMessage(String msgKey, Object[] params) {
        return BundleFactory.getMessage(msgKey, params);
    }

    public static String buildInvalidTextLength(String fieldName, int min, int max) {
        return BundleFactory.getMessage("validation_text_length", new Object[]{fieldName, min, max});
    }

    public static boolean isValidTextLength(String txt, int min, int max) {
        if (txt == null || txt.isEmpty()) {
            return true;
        } else {
            return txt.length() >= min && txt.length() <= max;
        }
    }

    public static String getMessage(String key) {
        return BundleFactory.getMessage(key, new Object[]{});
    }

    public static boolean validateCountry(ValidationResult validationResult, String fieldName, String fieldValue) {
        String[] isoCountries = Locale.getISOCountries();
        Optional<String> countryOpt = Arrays.stream(isoCountries).filter(isoCountry -> isoCountry.equalsIgnoreCase(fieldValue)).findFirst();
        if (countryOpt.isEmpty()) {
            String message = BundleFactory.getMessage("validation_country", new Object[]{fieldName});
            validationResult.setSuccess(false);
            validationResult.addError(new ValidationError(fieldName, fieldValue, ValidationRule.COUNTRY_ISO.name(), message));
        }
        return validationResult.isSuccess();
    }

}
