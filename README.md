# Restdoc
A rest document generator with parse java source code

### Usage
execute this example:
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

![example](https://github.com/ayz6uem/restdoc/blob/master/example/example.jpg)

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

1. 解决自定义的argumentResolve。 配置忽略某种参数类型
1. 未找到的参数，也要加入到参数列表中
1. 参数是List的情况
1. 参数是map的情况
1. 注释的描述里要处理 {@link }
1. commons-lang3 会产生一个 noSuchMethodError org.apache.commons.lang3.Validate.inclusiveBetween
1. ModelMap忽略 Model忽略 ModelAndView处理

```java
public class DTO{
    private List<InputDTO> list;

    public static class InputDTO {
        private String policyNumber;
    }
}
```

```java
{@link com.ybyc.car.insurance.model.slip.RenewSlipStatus}
```

```java
NoSuchMethodError: org.apache.commons.lang3.Validate.inclusiveBetween
```
 
