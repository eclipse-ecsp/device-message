/********************************************************************************
 * *******************************************************************************
 *  * Copyright (c) 2023-24 Harman International
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *  http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *
 *  * SPDX-License-Identifier: Apache-2.0
 *  *******************************************************************************
 *******************************************************************************/

package org.eclipse.ecsp.devicemessage.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.Pattern;
import org.eclipse.ecsp.devicemessage.util.MessageConstants;
import org.hibernate.validator.constraints.NotBlank;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * Specifies that the annotated element represents an API version 1.
 * This annotation can be used on methods, fields, constructors, and parameters.
 * It is used in conjunction with validation annotations like @NotBlank and @Pattern
 * to enforce validation rules specific to API version 1.
 * The default message for validation failures is defined in the MessageConstants class.
 * Additional validation groups and payload can be specified if needed.
 */
@Target({METHOD, FIELD, CONSTRUCTOR, PARAMETER})
// specifies where this validation can be used (Field, Method, Parameter etc)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@ReportAsSingleViolation
@NotBlank
@Pattern(regexp = "^v1$")

public @interface ApiVersion1 {
   
    /**
     * Gets the message associated with the API version.
     * The default value is {@link MessageConstants#UNSUPPORTED_API_VERSION_MSG}.
     *
     * @return the message associated with the API version
     */
    String message() default MessageConstants.UNSUPPORTED_API_VERSION_MSG;

    /**
     * Specifies the validation groups that should be applied when validating the annotated element.
     * By default, no groups are specified.
     *
     * @return the validation groups
     */
    Class<?>[] groups() default {};

    /**
     * Returns the payload types associated with this API version.
     *
     * @return an array of payload types
     */
    Class<? extends Payload>[] payload() default {};
}