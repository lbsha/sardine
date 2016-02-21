#sardine


跨域
压缩
https

> 本项目未完成，尚处于孵化阶段。

##微服务框架必备的几个要素：

- 配置（容器配置/项目配置）
- 日志
- 监控，告警
- 部署脚本

- 极少依赖guava logback netty 等

## 超级特性
- 自动重启（java self restart），当监控某个指标到达某个参数值

<http://stackoverflow.com/questions/4159802/how-can-i-restart-a-java-application>

<http://java.dzone.com/articles/programmatically-restart-java>


> 沙丁鱼，小而迅速，多而勇猛。


- [撰写合格的REST API](http://mp.weixin.qq.com/s?__biz=MzA3NDM0ODQwMw==&mid=208060670&idx=1&sn=ce67b8896985e8448137052b338093e0)


#sardine

##微服务框架必备的几个要素：

- 配置（容器配置/项目配置）
- 日志
- 监控，告警
- 部署脚本

- 极少依赖guava logback netty 等

## 超级特性
- 自动重启（java self restart），当监控某个指标到达某个参数值

<http://stackoverflow.com/questions/4159802/how-can-i-restart-a-java-application>

<http://java.dzone.com/articles/programmatically-restart-java>


> 沙丁鱼，小而快，集群的力量。


<http://www.sinatrarb.com/intro-zh.html>
sinatra 原版的路由仅有两部分组成，一个http动词 一个url匹配范式  然后就是 代码块

视图 / 模板 模板被假定直接位于./views

markdown模版的支持

路由和过滤器都可以可选的带有条件

enable :sessions 开关 请注意 enable :sessions 实际上保存所有的数据在一个cookie之中。

挂起 halt
让路 pass

一个路由可以让路给下一个路由

缓存控制
要使用HTTP缓存，必须正确地设定消息头。

配置 configure
配置这一部分是一定要有的，而且根据微服务的要求配置必须外部化，外部可修改
通过 settings("foo")
而且可以指定production和test




版本问题
<http://www.lexicalscope.com/blog/2012/03/12/how-are-rest-apis-versioned>
<http://maxenglander.com/2013/04/23/basic-restful-api-versioning-in-jersey.html>

内容协商
<http://badqiu.iteye.com/blog/552806>

Accept header vs Content-Type Header
<http://www.java-allandsundry.com/2012/08/accept-header-vs-content-type-header.html>

RESTful实践总结 - 新浪云存储讨论区
如何设计靠谱的REST API
<http://get.jobdeer.com/353.get>


需要写个指北：restful设计最佳指北
各种各样的最佳指南，理论派学院派都有，这是我的最佳指南，哦，不，指北。

- GET POST等动作的语义
- 版本问题  既然get post通过url都分不清 为啥版本号要分清
- 是否accept指定问题 .json ?format=json
- 返回值200封装还是使用http状态码
- 命名约定 不适用大些 －代替_


doesn't support resume large file downloads (from static files / public directory) #257



http://flask.pocoo.org/
express.js
https://github.com/Unknwon/macaron



HTTPS
http://zjumty.iteye.com/blog/1885356

http://www.importnew.com/16045.html






















