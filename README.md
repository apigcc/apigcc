# 🐷 Apiggs - 代码零侵入的Spring RestDoc 生成工具

![](https://img.shields.io/badge/language-java-yellow.svg)
![](https://img.shields.io/badge/build-processing-green.svg)
[ ![Download](https://api.bintray.com/packages/apiggs/maven/apiggs/images/download.svg) ](https://bintray.com/apiggs/maven/apiggs/_latestVersion)

一个代码零侵入的RestDoc文档生成工具。工具通过分析基于注解的spring mvc代码，分析代码，获取注释，生成RestDoc文档。

### 引入插件

* [apiggs-gradle-plugin](https://github.com/apiggs/apiggs-gradle-plugin) **free**
* [apiggs-maven-plugin](https://github.com/apiggs/apiggs-maven-plugin) **free**
* [apiggs-idea-plugin](https://github.com/apiggs/apiggs-idea-plugin)

> 具体引入方式请参考链接，apiggs-maven-plugin找不到时请使用jcenter仓库

### 准备代码，加入javadoc注释

```java
/**
 * Building a RESTful Web Service
 * 来自spring的官方示例:https://spring.io/guides/gs/rest-service/
 * @index 1
 */
@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    /**
     * Web Endpoint greeting
     * @param name who is this
     * @return
     */
    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }
}
```

### 运行插件

* gradle 运行 task: Tasks/documentation/apiggs
* maven 运行 compile

### Apiggs生成结果
在编译目录下生成 apiggs文件夹，并生成三个文件：
1. <project>.json ，可直接导入postman
1. <project>.adoc，文档源文件
1. <project>.html，源文件渲染结果，效果如下图

![example](https://apiggy-1252473972.cos.ap-shanghai.myqcloud.com/greeting.jpg)

### Plans

#### v1.0 Parse code and generate files
1. postman.json
2. index.adoc
3. render adoc to html
4. find some target test

#### v1.1 Maven and Gradle plugin

#### v1.2 idea plugin

#### v1.x Increase code robustness

#### v2.0 Hammer code and auto translation


### Problems

1. 解决自定义的argumentResolve。 配置忽略某种参数类型 （实现有难度）
1. 多个接口公用一个请求参数，如 add update，update 多一个id，解决方案是 自定义注释 @ignore ？
1. postman脚本
1. 哪些参数必传，哪些非必传

### Attentions

#### commons-lang3

if your project have import
```
compile('org.apache.commons:commons-lang3:3.0')
```
it possible throw this error
```java
java.lang.NoSuchMethodError: org.apache.commons.lang3.Validate.inclusiveBetween(JJJ)V
```
