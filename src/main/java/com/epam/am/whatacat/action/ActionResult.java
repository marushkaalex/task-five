package com.epam.am.whatacat.action;

public class ActionResult {
    private final String view;
    private final boolean isRedirect;

    public ActionResult(String view, boolean isRedirect) {
        this.view = view;
        this.isRedirect = isRedirect;
    }

    public ActionResult(String view) {
        this(view, false);
    }

    public String getView() {
        return view;
    }

    public boolean isRedirect() {
        return isRedirect;
    }
}
