Sardine
----------------------------------------------------------------------------------

[中文文档](README_ZH.md "中文文档")

Lightweight framework for creating small standalone Java applications in a micro service way.

轻量级高性能Java微型服务框架，本项目是 Netty+Java8 的 [Sinatra](http://www.sinatrarb.com) 实现。

![Sardine](http://dn-lbstatics.qbox.me/sardine/logo.jpg)

> 沙丁鱼，世界重要海洋经济鱼类。硬骨鱼纲鲱形目鲱科，形态延长，侧扁，腹部具棱鳞。小者二寸，大者尺许，密集群息，沿岸洄游。


Introduction
==================================================================================

### Lightweight micro service framework with Java8

- Java8: functional, Lambda, Stream
- Netty: 事件驱动, 异步高性能, 高可靠性
- Tiny: 全部源码只有100k+，仅依赖Netty 

### Quick Start

> one line code, one http server。

```java
public static void main(String[] args) {
    get("/", () -> "hello sardine");
}
```

**DONE!**  Now, Run and View:

<http://localhost:9527>


Tutorials
==================================================================================

### Dependency
 
maven

```xml
<dependency>
  <groupId>info.ibruce</groupId>
  <artifactId>sardine</artifactId>
  <version>1.0.0</version>
</dependency>
```

gradle

```

```

**versions**

- sardine-1.0.0：Java8 + Netty5（已发版）
- sardine-2.0.0：Java8 + Netty4（开发中）


### Start

```
import static sardine.Sardine.*;

public class SardineServerTest {
    public static void main(String[] args) {
        port(9527);
        get("/", () -> "hello sardine.");
    }
}
```

### Route

Route 由三部分组成：

- 动作：http 动作的一种，如：`GET`, `POST`, `HEAD`, `OPTIONS`, `PUT`, `DELETE`, `TRACE`, `CONNECT`, `PATCH`
- 路径：http 请求路径，如：`/home`, `/books/:id`, `/books/:author`
- 函数：消费http request 产生 http response，如：`() -> {}`, `request, response) -> {}`

> 根据restful规范，建议开发者将`GET`, `PUT`, `DELETE`, `HEAD` 设计为幂等接口。


此外，还可以使用：

- 条件函数：条件函数，如：`(request) -> {}`, `request -> "127.0.0.1".equals(request.ip())`
- 接收类型：即http 头中的 accept，如：`Accept: text/html,*/*`, `Accept: application/json`


示例：

```java
get("/books/:author", (request, response) -> {
    return "query";
});

post("/book", (request, response) -> {
    return "created";
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


#### 条件Condition

条件函数

```java
get("/hello", request -> "127.0.0.1".equals(request.ip()), (request, response) -> {
    return "hello sardine.";
});
```

### Parameters

参数分为三种类型：

- Named parameters：
- Wildcards parameters：
- Query parameters：


### Named parameters

### Wildcards parameters

### Query parameters



### Request
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

### Response
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


### Cookie

### Halt
```
halt();
halt(403);
halt("a u ok?");
halt(503, "i got u!");
```

### Filter

### Redirect

### Rewrite

### Exception

### Static assets
```
staticFileLocation("/resources");

externalStaticFileLocation("/var/www/resources");
```

### ResponseTransformer
json

### MVC

### Gzip

### 日志

### 监控


## 示例
---------------------------------------------------------------------------------

### 1, HelloSardine

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

### Demo工程

### 真实案例

不蒜子：<http://busuanzi.ibruce.info>


压力测试
---------------------------------------------------------------------------------

ab


参考资料
---------------------------------------------------------------------------------

发版资料
---------------------------------------------------------------------------------

- <https://issues.sonatype.org/browse/OSSRH-23697>
- <https://oss.sonatype.org>
- <http://blog.csdn.net/ssrc0604hx/article/details/51513414>
- <http://www.cnblogs.com/gaoxing/p/4359795.html>
- <http://my.oschina.net/looly/blog/270767>