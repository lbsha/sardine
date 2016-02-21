#Sardine

> **整个项目未完成，尚处于开发中，敬请期待。。。**

轻量级高性能微服务框架，本项目是 Netty5+Java8 的 [Sinatra](http://www.sinatrarb.com) 实现。

![Sardine](http://dn-lbstatics.qbox.me/sardine/logo.jpg)

> 沙丁鱼，世界重要海洋经济鱼类。硬骨鱼纲鲱形目鲱科，形态延长，侧扁，腹部具棱鳞。小者二寸，大者尺许，密集群息，沿岸洄游。

简介
---------------------------------------------------------------------------------

###超轻量级的Java 微服务框架

- Java8：函数式、Lambda表达式、Stream流式操作
- Netty5：事件驱动、异步高性能、高可靠性
- 极简：全部源码只有100k+，仅依赖Netty

###一行代码启动一个HTTP 服务

```
public static void main(String[] args) {
    get("/", () -> "hello sardine.");
}
```

**搞定!** 现在，打开您的浏览器：

>http://localhost:9527/


##入门
---------------------------------------------------------------------------------

###依赖

```
<dependency>
    <groupId>sardine</groupId>
    <artifactId>sardine-all</artifactId>
    <version>${sardine-version}</version>
</dependency>
```

###启动

```
import static sardine.Sardine.*;

public class SardineServerTest {
    public static void main(String[] args) {
        port(9527);
        get("/", () -> "hello sardine.");
    }
}
```

###Route

Route 由三部分组成：

- 动作：http 动作的一种（GET, POST, HEAD, OPTIONS, PUT, DELETE, TRACE, CONNECT, PATCH）
- 路径：http 请求路径（/home, /books/:id, /books/:author）
- 函数：消费http request，生产http response（() -> {}， (request, response) -> {}）

此外，还可以有：

- 条件：条件函数 （(request) -> {}）
- 类型：即 http accept type


```
get("/books/:author", (request, response) -> {
    return "hello sardine";
});

post("/book", (request, response) -> {
    return "ok";
});

put("/books/:id", (request, response) -> {
    return "updated";
});

delete("/books/:id", (request, response) -> {
    return "deleted";
});
```

// TODO
restful verb 介绍

###Parameters


###Named parameters

###Wildcards parameters

###Query parameters


###条件Condition

条件函数

```
get("/hello", request -> "127.0.0.1".equals(request.ip()), (request, response) -> {
    return "hello sardine.";
});
```



###Request
```
request.host();
request.port();
request.method();
request.path();
request.scheme();
request.contentType();
request.accept();
request.ajax();

request.headers();
request.headers("foo");

request.cookies();
request.cookies("name");

request.params();
request.params("foo");
request.paramsOptional("foo")
request.paramsOrElse("foo")

request.splats()
request.splatsAsList()
request.splatsFirst()
request.splatsLast()

request.queryParams();
request.queryParams("foo");
request.multiQueryParams("foo");

request.body();
request.bodyAsByte();
request.bodyLength();
```

###Response
```
response.status(404);
response.header("foo", "bar");
response.cookie("foo", "bar");
response.contentType("application/json");

response.body();
response.body("are you ok ?");

response.redirect("/login");
response.file("/hello.html");
```

###Cookie

###Halt
```
halt();
halt(403);
halt("a u ok?");
halt(503, "i got u!");
```

###Filter

###Redirect

###Rewrite

###Exception

###Static assets
```
staticFileLocation("/resources");

externalStaticFileLocation("/var/www/resources");
```

###ResponseTransformer
json

###MVC

###Gzip

###日志

###监控


##示例
---------------------------------------------------------------------------------

###1、HelloSardine

最简版：
```
import static sardine.Sardine.*;

public class HelloSardine {
    public static void main(String[] args) {
        get("/", () -> "hello sardine.");
    }
}
```

> Run：http://localhost:9527/

Java8 语法糖版：
```
import static sardine.Sardine.*;

public class HelloSardine {
    public static void main(String[] args) {
        get("/", new HelloSardine()::hello);
    }
    public String hello() { return "hello sardine"; }
}
```

> Run：http://localhost:9527/

```
import static sardine.Sardine.*;

public class HelloSardine {
    public static void main(String[] args) {
        get("/", HelloSardine::hello);
    }
    public static String hello() { return "hello sardine"; }
}
```

> Run：http://localhost:9527/



实例
---------------------------------------------------------------------------------

###Demo工程

###真实案例

不蒜子：<http://service.ibruce.info>


压力测试
---------------------------------------------------------------------------------

ab

参考资料
---------------------------------------------------------------------------------