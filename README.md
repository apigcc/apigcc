# 🐷 Apigcc - 非侵入的RestDoc文档生成工具

![](https://img.shields.io/badge/Language-Java-yellow.svg)

### 前言
程序员一直以来都有一个烦恼，只想写代码，不想写文档。代码就表达了我的思想和灵魂。

Python提出了一个方案，叫**docstring**，来试图解决这个问题。即编写代码，同时也能写出文档，保持代码和文档的一致。docstring说白了就是一堆代码中的注释。Python的docstring可以通过help函数直接输出一份有格式的文档，本工具的思想与此类似。

### 代码即文档

Apigcc是一个**非侵入**的RestDoc文档生成工具。工具通过分析代码和注释，获取文档信息，生成RestDoc文档。

### 有这样一段代码

```java
/**
 * 欢迎使用Apigcc
 * @index 1
 */
@RestController
public class HelloController {

    /**
     * 示例接口
     * @param name 名称
     * @return
     */
    @RequestMapping("/greeting")
    public HelloDTO greeting(@RequestParam(defaultValue="apigcc") String name) {
        return new HelloDTO("hello "+name);
    }

}
```


### 生成文档效果
![示例](https://apigcc-1252473972.cos.ap-shanghai.myqcloud.com/apigcc-hub-demo.png)

### 使用方式

[Hub](https://github.com/apigcc/apigcc-hub)

[Gradle插件](https://github.com/apigcc/apigcc-gradle-plugin)

[Maven插件](https://github.com/apigcc/apigcc-maven-plugin)