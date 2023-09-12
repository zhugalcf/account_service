package faang.school.accountservice.annotations;

import faang.school.accountservice.validator.ValidOwnerValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidOwnerValidator.class})
public @interface ValidOwner {
    String message() default "Invalid owner";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
