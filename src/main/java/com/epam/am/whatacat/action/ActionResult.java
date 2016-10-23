package com.epam.am.whatacat.action;

public class ActionResult {
    private static final int NO_ERROR = -1;

    private final String view;
    private final boolean isRedirect;
    private final int error;

    private ActionResult(String view, boolean isRedirect, int error) {
        this.view = view;
        this.isRedirect = isRedirect;
        this.error = error;
    }

    public ActionResult(int error) {
        this(null, false, error);
    }

    public ActionResult(String view) {
        this(view, false, NO_ERROR);
    }

    public ActionResult(String view, boolean isRedirect) {
        this(view, isRedirect, NO_ERROR);
    }

    public String getView() {
        return view;
    }

    public boolean isRedirect() {
        return isRedirect;
    }

    public int getError() {
        return error;
    }

    public boolean isError() {
        return error != NO_ERROR;
    }
}
