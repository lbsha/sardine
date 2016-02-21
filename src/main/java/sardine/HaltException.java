package sardine;

import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * 沙丁中断异常
 *
 * @auth bruce-sha
 * @date 2015/5/21
 */
public class HaltException extends RuntimeException {

    private int statusCode = HttpResponseStatus.OK.code();
    private String content = HttpResponseStatus.OK.reasonPhrase().toString();

    HaltException() {
    }

    HaltException(int statusCode) {
        this.statusCode = statusCode;
    }

    HaltException(String content) {
        this.content = content;
    }

    HaltException(HttpResponseStatus status) {
        this.statusCode = status.code();
        this.content = status.reasonPhrase().toString();
    }

    HaltException(int statusCode, String content) {
        this.statusCode = statusCode;
        this.content = content;
    }

    public int statusCode() {
        return statusCode;
    }

    public String content() {
        return content == null ? SardineBase.EMPTY : content;
    }

    @Override
    public String toString() {
        return "statusCode: " + statusCode() + ", content: " + content();
    }
}
