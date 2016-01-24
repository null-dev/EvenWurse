package tk.wurst_client.analytics;

/**
 * Project: EvenWurse
 * Created: 19/12/15
 * Author: nulldev
 */

/**
 * Temp analytics manager because I don't want to mess up the original Wurst's analytics
 */
public class DoNothingAnalyticsManagerImpl implements AnalyticsManager {
    @Override
    public boolean shouldTrack() {
        return false;
    }

    @Override
    public void trackPageView(String url, String title) {
    }

    @Override
    public void trackPageViewFromReferrer(String url, String title, String referrerSite, String referrerPage) {
    }

    @Override
    public void trackPageViewFromSearch(String url, String title, String searchSite, String keywords) {
    }

    @Override
    public void trackEvent(String category, String action) {
    }

    @Override
    public void trackEvent(String category, String action, String label) {
    }

    @Override
    public void trackEvent(String category, String action, String label, int value) {
    }

    @Override
    public void makeCustomRequest(AnalyticsRequestData data) {
    }
}
