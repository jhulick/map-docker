package gov.max.microservice.resource.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

/**
 * Provides build info.
 */
@Configuration
@Service
public class BuildInfo {

    @Value("${swagger.title}")
    private String swaggerTitle;

    @Value("${swagger.description}")
    private String swaggerDescription;

    @Value("${swagger.termsOfServiceUrl}")
    private String swaggerTermsOfServiceUrl;

    @Value("${swagger.contact}")
    private String swaggerContact;

    @Value("${swagger.license}")
    private String swaggerLicense;

    @Value("${swagger.licenseUrl}")
    private String swaggerLicenseUrl;

    @Value("${app.version}")
    private String appVersion;

    /**
     * Get application version.
     *
     * @return AppVersion
     */
    public String getVersion() {
        if (appVersion.contains("SNAPSHOT")) {
            return "dev-build";
        }
        return appVersion;
    }

    public String getTitle() {
        return swaggerTitle;
    }

    public String getDescription() {
        return swaggerDescription;
    }

    public String getTermsOfService() {
        return swaggerTermsOfServiceUrl;
    }

    public String getContact() {
        return swaggerContact;
    }

    public String getLicense() {
        return swaggerLicense;
    }

    public String getLicenceUrl() {
        return swaggerLicenseUrl;
    }
}