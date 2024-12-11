package pe.edu.utp.backendferreweb.util.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import pe.edu.utp.backendferreweb.util.validation.constraint.NotBlankIfNotNullValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NotBlankIfNotNullValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotBlankIfNotNull {

    String message() default "El campo no puede estar en blanco";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}