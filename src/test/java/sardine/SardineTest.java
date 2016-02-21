package sardine;

//import org.json.JSONObject;

import static sardine.Sardine.externalStaticFileLocation;
import static sardine.Sardine.get;
import static sardine.SardineBase.notFound;
import static sardine.SardineBase.staticFileLocation;
//import static sardine.Sardine.get2;

/**
 * @auth bruce-sha
 * @date 2015/5/21
 */
public class SardineTest {

    public static class Person {
        String name;
        int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    public static String hello1() {
        return "hello ";
    }

    public static void main(String[] args) {

//        BEFORE((request, response) -> {
//
//        });

        staticFileLocation("");
        externalStaticFileLocation("D:\\buru\\gitosc\\sardine\\src\\main\\resources\\hehe");

        get("/", () -> "hello");

        get("/status/ping", (request, response) -> {
//            response.header("Content-Type","text/plain");
//            response.headerInt("Content-Length", 2);
//            response.headerObject("Date", new Date());
//            response.header(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);

            return "ok";
        });

        get("/hello", request -> "127.0.0.1".equals(request.ip()), (request, response) -> {
            return "hello sardine.";
        });

        get("/ip", (request) -> request.headers("ok") != null, (request, response) -> {

            return request.ip();
        });


        get("/ip0", (request, response) -> {

            return request.ip();
        });


        get("/trans1", (request, response) -> {

            return 1;
        }, new ResponseTransformer<Integer>() {
            @Override
            public String render(Integer model) throws Exception {
                return null;
            }
        });


//        get("/trans", "*/*", (request, response) -> {
//            response.contentType("text/plain");
//            return new Person("buru", 10086);
//        }, (model) -> new JSONObject(model).toString() + System.currentTimeMillis());


        get("/ck", (request, response) -> {

            response.cookie("a", "1");
            response.cookie("a", "2");

            request.cookies();

            return "hello";
        });
        notFound("xxxx");
        get("/req", (request, response) -> {

            response.contentType("buru");
            response.contentType("application/json");
//            request.uri();
//            request.headers();
//            request.queryString();

            return "buru";
        });

//        get("/", SardineTest::hello1);
//        get("/", new SardineTest()::hello2);

//
//        get("/hello/:name", (request, response) -> "/hello/:name " + request.params("name"));
//        get("/hello/*", (request, response) -> "/hello/* " + request.splats());
//
//        singleton("/tryHello", request -> request.host().contains("localhost"), (request, response) -> {
//            return "hello " + request.host();
//        });
//
//
//        singleton("/hello/*/to/*", (request, response) -> "/hello/*/to/* " + Arrays.asList(request.splats()));

//        singleton("/hello", (request, response) -> "/hello " + request.toString());
//        singleton("/hello/*", (request, response) -> "/hello/* " + Arrays.asList(request.splats()));

//        get("/hello/*/to/", (request, response) -> "/hello/*/to/ " + request.toString());
//        get("/hello/*/to", (request, response) -> "/hello/*/to " + request.toString());
//        get("/hello/*/to/*", (request, response) -> "/hello/*/to/* " + Arrays.asList(request.splats()));

//        after((request, response) -> response.body(response.body() + " after"));
    }

    public String hello2() {
        return "hello ";
    }

}
