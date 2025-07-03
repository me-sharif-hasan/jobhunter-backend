package com.iishanto.jobhunterbackend.domain.usecase.admin;

import com.iishanto.jobhunterbackend.domain.model.SiteAttributeValidatorModel;

public interface GetRenderedHtmlPageUseCase {
    /**
     * Fetches the rendered HTML content of a given URL.
     *
     * @param url The URL of the page to be rendered.
     * @return The rendered HTML content as a String.
     */
    String getRenderedHtmlPage(String url);
    void getSiteAttributes(SiteAttributeValidatorModel siteAttributes);
}
