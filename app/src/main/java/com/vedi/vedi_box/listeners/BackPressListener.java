package com.vedi.vedi_box.listeners;

public interface BackPressListener {
    /**
     * If you return true the back press will not be taken into account, otherwise the activity will act naturally
     *
     * @return true if your processing has priority if not false
     */
    void onBackPressed();
}
