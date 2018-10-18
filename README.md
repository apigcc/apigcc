# Apiggy

![](https://img.shields.io/badge/language-java-yellow.svg)
![](https://img.shields.io/badge/build-processing-green.svg)

A rest document generator with parse the source code

### Usage

execute this example:
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

then we run this code:
```java
String source = System.getProperty("user.dir")+"/src/test/java";
Enviroment env = new Enviroment()
        .source(source)
        .project("user")
        .title("User服务接口文档")
        .description("用户服务文档，使用默认模板");
RestDoc restDoc = new RestDoc(env);
restDoc.parse();
restDoc.build();
```
then we will get files in /build/restdoc/


then user.html look like this:
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
