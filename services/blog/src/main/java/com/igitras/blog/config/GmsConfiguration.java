package com.igitras.blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * Global method security configuration.
 *
 * @author mason
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class GmsConfiguration {

}
