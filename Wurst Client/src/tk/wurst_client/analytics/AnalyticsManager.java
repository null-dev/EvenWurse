package tk.wurst_client.analytics;

/**
 * Project: EvenWurse
 * Created: 19/12/15
 * Author: nulldev
 */
public interface AnalyticsManager {
    boolean shouldTrack();

    void trackPageView(String url, String title);

    void trackPageViewFromReferrer(String url, String title, String referrerSite, String referrerPage);

    void trackPageViewFromSearch(String url, String title, String searchSite, String keywords);

    void trackEvent(String category, String action);

    void trackEvent(String category, String action, String label);

    void trackEvent(String category, String action, String label, int value);

    void makeCustomRequest(AnalyticsRequestData data);
}
