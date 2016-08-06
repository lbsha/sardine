package sardine.utils;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;

/**
 * @author bruce_sha
 *   2015/6/11
 */
public final class Mimes {

    static final MimetypesFileTypeMap mimeTypesMapping = new MimetypesFileTypeMap();

    static {
        mimeTypesMapping.addMimeTypes("application/javascript js javascript");
        mimeTypesMapping.addMimeTypes("image/x-icon ico");
        mimeTypesMapping.addMimeTypes("text/css css");
        mimeTypesMapping.addMimeTypes("image/png png");
        mimeTypesMapping.addMimeTypes("image/bmp bmp");
        mimeTypesMapping.addMimeTypes("text/xml xml");
    }

    public static String contentType(final String fileNameWithPath) {
        return mimeTypesMapping.getContentType(fileNameWithPath);
    }

    public static String contentType(final File file) {
        return mimeTypesMapping.getContentType(file);
    }


//    private String fileExtName;
//    private String mimeType;
//
//
//    private Mimes(final String fileExtName, final String mimeType) {
//        this.fileExtName = fileExtName;
//        this.mimeType = mimeType;
//    }
//
//    public static String mimeTypeByPath(String path) {
//        return mimeTypeByFileName(path);
//    }
//
//    public static String mimeTypeByFileName(String fileName) {
//        String fileExtName = fileName.substring(fileName.lastIndexOf('.'));
//        return mimeTypeByExtName(fileExtName);
//    }
//
//    public static String mimeTypeByExtName(String fileExtName) {
//        String fxn = fileExtName.toLowerCase();
//
//        if (mapping.containsKey(fxn))
//            return mapping.get(fxn);
//        else
//            return "text/plain";
//    }
//
//    private static final Map<String, String> mapping = new HashMap<>();
//
//    //        C:/Users/rd_bruce_sha/.m2/repository/org/eclipse/jetty/jetty-http/9.0.2.v20130417/jetty-http-9.0.2.v20130417.jar!/org/eclipse/jetty/http/mime.properties
////    https://gist.githubusercontent.com/laurilehmijoki/4483113/raw/3f429390e737e2f1346fd71fe7798d63226dd96c/mime.properties#
//    static {
//        mapping.put(".js", "application/javascript");
//        mapping.put(".htm", "text/html");
//        mapping.put(".html", "text/html");
//        mapping.put(".css", "text/css");
//        mapping.put(".jpeg", "image/jpeg");
//        mapping.put(".png", "image/png");
//        mapping.put(".jpg", "image/jpeg");
//        mapping.put(".gif", "image/gif");
//        mapping.put(".bmp", "image/bmp");
//        mapping.put(".ico", "image/x-icon");
//        mapping.put(".xml", "text/xml");
//        mapping.put(".*", "application/octet-stream");
//    }
//
//    public static void main(String[] args) {
//        System.out.println(mimeTypeByPath("/test/abc.js"));
//    }
}
