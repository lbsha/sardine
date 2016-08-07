package sardine.route;

public class RoutePathEntry {
    String requestPart;
    String routePart;

    RoutePathEntry(String requestPart, String routePart) {
        this.requestPart = requestPart;
        this.routePart = routePart;
    }

    public String requestPart() {
        return requestPart;
    }

    public String routePart() {
        return routePart;
    }

    public boolean isParam() {
        return routePart().startsWith(":");
    }

    public boolean isSplat() {
        return routePart().equals("*");
    }

    public boolean notNull() {
        return requestPart() != null && routePart() != null;
    }
}