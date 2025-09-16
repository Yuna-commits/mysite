package com.bit2025.mysite.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.bit2025.mysite.config.web.FileuploadConfig;
import com.bit2025.mysite.config.web.LocaleConfig;
import com.bit2025.mysite.config.web.SecurityConfig;

@Configuration
@Import({ LocaleConfig.class, FileuploadConfig.class, SecurityConfig.class })
public class WebConfig {

}
