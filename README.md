# Restdoc
A rest document generator with parse java code

example:
```$xslt
RestDoc restDoc = new RestDoc("source").parse();
System.out.println(JsonHelper.toPretty(restDoc.getTree()));
```
you will get a postman v2.1 json file
