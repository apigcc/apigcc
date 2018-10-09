# Restdoc
A rest document generator with parse java code

example:
```$xslt
RestDoc restDoc = new RestDoc("source").parse();
System.out.println(JsonHelper.toPretty(restDoc.getTree()));
```
you will get a postman v2.1 json file

1. url继承父类
1. content-Type 优先级，有 requestBody参数的，一定是 json
1. 解决自定义的argumentResolve。 配置忽略某种参数类型
1. 返回类型如ResultData User,ResultData找不到时，列出User
1. 未找到的参数，也要加入到参数列表中
1. asciidoc 表头处理
1. request的头和体合到一起 response的头和体合到一起
1. 解决代码在二方jar包的问题
1. urlencoded 与 raw 共存，raw是null么？
1. 参数是List的情况
1. 参数是map的情况
1. 请求参数循环深入
1. 注释的描述里要处理 {@link }
1. 自定义注解@CHARGENOTIFY
1. commons-lang3 会产生一个 noSuchMethodError org.apache.commons.lang3.Validate.inclusiveBetween
1. RequestMapping 和 @RequestBody 搭配使用，POST
1. EntityVisit解析Entity时，内部类，要加上外部类域;ClassOrInterfaceType getScope() + getName()
1. ModelMap忽略 Model忽略 ModelAndView处理
1. header值不能重复
```java
public class DTO{
    private List<InputDTO> list;

    public static class InputDTO {
        private String policyNumber;
    }
}
```

```
{@link com.ybyc.car.insurance.model.slip.RenewSlipStatus}
```

```
NoSuchMethodError: org.apache.commons.lang3.Validate.inclusiveBetween
```
 