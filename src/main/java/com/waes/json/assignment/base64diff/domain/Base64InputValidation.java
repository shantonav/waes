package com.waes.json.assignment.base64diff.domain;

import com.waes.json.assignment.base64diff.exception.IllegalStateOfModelException;
import com.waes.json.assignment.base64diff.exception.InConsistentDomainStateAPIException;
import com.waes.json.assignment.base64diff.util.Base64DecoderUtil;
import org.springframework.util.StringUtils;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import static  java.lang.annotation.ElementType.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Constraint(validatedBy = Base64InputValidation.Base64InputValidator.class)
@Target({ METHOD, CONSTRUCTOR, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Base64InputValidation {

    String message() default "Input string must be a valid Base64 encoded String (wish I could underline this) Object and of course not null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    public class Base64InputValidator implements ConstraintValidator<Base64InputValidation,String> {
        @Override
        public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
            if (StringUtils.isEmpty(s))
                return false;
            try {
                Base64DecoderUtil.getDataDecodedFromBase64Representation(s);
            }catch(InConsistentDomainStateAPIException | IllegalStateOfModelException e){
                return false;
            }
            return true;
        }
    }
}
