package pe.edu.utp.backendferreweb.util.validation.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pe.edu.utp.backendferreweb.util.validation.annotation.NotBlankIfNotNull;

public class NotBlankIfNotNullValidator implements ConstraintValidator<NotBlankIfNotNull, String> {

    @Override
    public void initialize(NotBlankIfNotNull constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return !value.trim().isEmpty();
    }
}