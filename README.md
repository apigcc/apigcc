# Restdoc
A rest document generator with parse the source code

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

![example](https://github.com/ayz6uem/restdoc/master/example/example.jpg)

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
1. 未找到的参数，也要加入到参数列表中
1. 参数是List的情况
1. 参数是map的情况
1. 注释的描述里要处理 {@link }
1. ModelMap忽略 Model忽略 ModelAndView处理
1. ResponseBody
1. requestMapping 多路径处理
1. requestheader 注解
1. json说明，字段层级
1. get post query string
1. enum 的处理 不应该展开字段
1. 泛型的泛型的泛型...... ResultData<List<ChargingEquipmentModelSimpleInfo>>.
1. 多个接口公用一个请求参数，如 add update，update 多一个id，解决方案是 自定义注释 @ignore ？
1. postman脚本
1. 字段默认值的双引号处理
1. 哪些参数必传，哪些非必传
1. controller 的顺序问题
1. 枚举类型是否列出枚举值
1. 空controller不生成文档

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

 