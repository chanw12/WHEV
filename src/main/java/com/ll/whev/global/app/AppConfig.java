package com.ll.whev.global.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class AppConfig {
    @Getter
    public static String jwtSecretKey;

    @Value("${custom.jwt.secretKey}")
    public void setJwtSecretKey(String jwtSecretKey) {
        this.jwtSecretKey = jwtSecretKey;
    }

    private static String activeProfile;

    @Value("${spring.profiles.active}")
    public void setActiveProfile(String activeProfile) {
        this.activeProfile = activeProfile;
    }

    public static boolean isProd() {
        return activeProfile.equals("prod");
    }

    @Getter
    private static long accessTokenExpirationSec;

    @Value("${custom.accessToken.expirationSec}")
    public void setJwtSecretKey(long accessTokenExpirationSec) {
        this.accessTokenExpirationSec = accessTokenExpirationSec;
    }

    @Getter
    private static String siteFrontUrl;

    @Value("${custom.site.frontUrl}")
    public void setSiteFrontUrl(String siteFrontUrl) {
        this.siteFrontUrl = siteFrontUrl;
    }

    @Getter
    private static String siteBackUrl;

    @Value("${custom.site.backUrl}")
    public void setSiteBackUrl(String siteBackUrl) {
        this.siteBackUrl = siteBackUrl;
    }

    @Getter
    private static String siteCookieDomain;

    @Value("${custom.site.cookieDomain}")
    public void setSiteCookieDomain(String siteCookieDomain) {
        this.siteCookieDomain = siteCookieDomain;
    }

    private static String resourcesStaticDirPath;

    public static String getResourcesStaticDirPath() {
        if (resourcesStaticDirPath == null) {
            ClassPathResource resource = new ClassPathResource("static/");
            try {
                resourcesStaticDirPath = resource.getFile().getAbsolutePath();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return resourcesStaticDirPath;
    }

    @Getter
    public static String tempDirPath;

    @Value("${custom.temp.dirPath}")
    public void setTempDirPath(String tempDirPath) {
        this.tempDirPath = tempDirPath;
    }

    @Getter
    public static String genFileDirPath;

    @Value("${custom.genFile.dirPath}")
    public void setGenFileDirPath(String genFileDirPath) {
        this.genFileDirPath = genFileDirPath;
    }

    @Getter
    public static String siteName;

    @Value("${custom.site.name}")
    public void setSiteName(String name) {
        this.siteName = name;
    }

    @Getter
    public static ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Getter
    public static int basePageSize = 24;

    @Getter
    public static int SummaryPassScore = 70;

    @Getter
    public static int videoMinNum = 5;

    public static final String ALPHANUMERIC = "0123456789abcdefghjklmnopqrstuvwxyz";
    public static final int ALPHANUMERIC_LENGTH = 6;

    @Getter
    private static String testClientApiKey;

    @Value("${payment.toss.test_client_api_key}")
    public void setTestClientApiKey(String testClientApiKey) {
        this.testClientApiKey = testClientApiKey;
    }

    @Getter
    private static String testSecretKey;

    @Value("${payment.toss.test_secret_key}")
    public void setTestSecretKey(String testSecretKey) {
        this.testSecretKey = testSecretKey;
    }


    @Getter
    private static String successUrl;

    @Value("${payment.toss.success_url}")
    public void setDefaultSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    @Getter
    private static String failUrl;

    @Value("${payment.toss.fail_url}")
    public void setDefaultFailUrl(String failUrl) {
        this.failUrl = failUrl;
    }

    public static final String URL = "https://api.tosspayments.com/v1/payments/";


    @Getter
    private static String accessKeyId;
    @Getter
    private static String secretAccessKey;
    @Getter
    private static String region;

    @Value("${aws.accessKeyId}")
    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }
    @Value("${aws.secretAccessKey}")
    public void setSecretAccessKey(String secretAccessKey) {
        this.secretAccessKey = secretAccessKey;
    }

    @Value("${aws.region}")
    public void setRegion(String region) {
        this.region = region;
    }

    @Getter
    private static String googleAPIkey;

    @Value("${google.apiKey}")
    public void setGoogleAPIkey(String googleAPIkey) {
        this.googleAPIkey = googleAPIkey;
    }
}
