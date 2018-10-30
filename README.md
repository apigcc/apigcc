# 🐷 Apiggs - 非侵入的RestDoc文档生成工具

![](https://img.shields.io/badge/language-java-yellow.svg)
![](https://img.shields.io/badge/build-processing-green.svg)
[ ![Download](https://api.bintray.com/packages/apiggs/maven/apiggs/images/download.svg) ](https://bintray.com/apiggs/maven/apiggs/_latestVersion)

### 前言
程序员一直以来都有一个烦恼，只想写代码，不想写文档。代码就表达了我的思想和灵魂。

Python提出了一个方案，叫**docstring**，来试图解决这个问题。即编写代码，同时也能写出文档，保持代码和文档的一致。docstring说白了就是一堆代码中的注释。Python的docstring可以通过help函数直接输出一份有格式的文档，本工具的思想与此类似。


### 代码即文档

Apiggs是一个**非侵入**的RestDoc文档生成工具。工具通过分析代码和注释，获取文档信息，生成RestDoc文档。

### 引入插件

[gradle](https://github.com/apiggs/apiggs-gradle-plugin)
```groovy
buildscript {
    dependencies {
        classpath 'com.github.apiggs:apiggs-gradle-plugin:替换为上方版本号'
    }
}

apply plugin: 'com.github.apiggs'
```
[maven](https://github.com/apiggs/apiggs-maven-plugin)
```xml
<plugin>
    <groupId>com.github.apiggs</groupId>
    <artifactId>apiggs-maven-plugin</artifactId>
    <version><!-- 替换为上方版本号 --></version>
    <executions>
        <execution>
            <phase>compile</phase>
            <goals>
                <goal>apiggs</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

### 有这样一段代码

```java
/**
 * 欢迎使用Apiggs
 * @index 1
 */
@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    /**
     * 示例接口
     * @param name 名称
     * @return
     */
    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="apiggs") String name) {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }
}
```

### 运行插件

* gradle 运行 
```
Tasks/documentation/apiggs
```
* maven 运行 
```
compile
```


### 生成文档
在编译目录下生成apiggs文件夹，并生成三个文件：
1. .json文件，可直接导入postman
1. .adoc文件，Asciidoc源文件
1. .html文件，源文件渲染结果，效果如下图

![example](https://apiggy-1252473972.cos.ap-shanghai.myqcloud.com/WX20181025-202423.png)

想了解更多，请查看[Wiki](https://github.com/apiggs/apiggs/wiki)

### Versions

#### 1.5

* 支持字段别名
* 外部依赖jar支持目录
* 修复插件不退出，内容重复创建的Bug

#### 1.3
* 自实现asciidoc构建器，移除对swagger2markup:markup-document-builder的依赖
* 实现自己的日志工具，移除对slf4j的依赖
* 支持@readme
* 支持@code

#### 1.1
* 增加注释Tag @return的支持，只支持类的全限定名
* 插件支持子项目解析

#### 1.0
* 解析spring mvc源代码，构建Restful Api树
* 生成postman v2.1格式的json文件
* 生成Asciidoc文件
* 渲染Asciidoc文件，生成html文件
* 支持泛型的参数
* 自定义注释Tag @index，定义文档顺序

