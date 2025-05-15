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

package org.eclipse.ecsp.devicemessage.mqtt;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Configuration class for web-related configurations.
 */
@Configuration("myConfig")
@ImportAutoConfiguration(RestTemplateAutoConfiguration.class)
public class WebConfig implements WebMvcConfigurer {

    @Value("${authentication.check}")
    private boolean authCheck;

    @Value("${cors.origin.allow}")
    private String corsOriginAllow;


    /**
     * Adds interceptors to the registry.
     * If the authentication check is enabled, it adds a security interceptor and a device association interceptor.
     *
     *
     * @param registry the InterceptorRegistry to which the interceptors will be added
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (authCheck) {
            registry.addInterceptor(createSecurityInterceptor());
            registry.addInterceptor(createDeviceAssociationInterceptor());

        }

        //super.addInterceptors(registry);
    }

    /**
     * Creates a security interceptor for handling authentication in the device message API.
     *
     * @return The created security interceptor.
     */
    @Bean
    public HandlerInterceptor createSecurityInterceptor() {
        return new DeviceMessageAuthHandlerInterceptor();
    }


    /**
     * Creates a new instance of RestTemplate.
     *
     * @param builder the RestTemplateBuilder used to build the RestTemplate
     * @return a new instance of RestTemplate
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }


    /**
     * Returns a WebMvcConfigurer bean that configures Cross-Origin Resource Sharing (CORS) for the application.
     * If the 'corsOriginAllow' property is not blank, it adds a mapping for the specified origin.
     * Otherwise, it adds a mapping for all origins.
     *
     * @return the WebMvcConfigurer bean for configuring CORS
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                if (!StringUtils.isBlank(corsOriginAllow)) {
                    registry.addMapping(corsOriginAllow.trim());
                } else {
                    registry.addMapping("*");
                }
            }
        };
    }

    /**
     * Creates a new instance of the DeviceAssociationHandlerInterceptor.
     * This interceptor is responsible for handling device association related operations.
     *
     * @return The DeviceAssociationHandlerInterceptor instance.
     */
    @Bean
    public DeviceAssociationHandlerInterceptor createDeviceAssociationInterceptor() {
        return new DeviceAssociationHandlerInterceptor();
    }
}