## 对象拷贝
从多个对象中拷贝属性字段到目标对象

* 仅拷贝本类字段

* 不拷贝父类字段

方法签名：

* public static <T> T copy(T dsc, Object... srcs)
* public static <T> T copy(boolean ignoreNull, T dsc, Object... srcs)

示例：

```java
    static class Desc {
        String overwrite;
        int ignore;

        @Override
        public String toString() {
            return "[overwrite='" + overwrite + "', ignore='" + ignore + "']";
        }
    }

    static class Src {
        String overwrite;
        String ignore;
        String whatever;

        @Override
        public String toString() {
            return "[overwrite='" + overwrite + "', ignore='" + ignore + "', whatever='" + whatever + "']'";
        }
    }
```
简单使用：
```
    Desc emptyDesc = new Desc();
    Src src = new Src();
    src.overwrite = "overwrite from src";
    src.ignore = "this field will be ignored";
    src.whatever = "this field will be ignored";
    // 你也可以忽略这个返回值
    emptyDesc = copy(emptyDesc, src);
    System.out.println( "copy to empty Desc:" );
    System.out.println( emptyDesc );
```
输出结果：
```
copy to empty Desc:
[overwrite='overwrite from src', ignore='0']
```

当目标对象不为空时：
```
    Desc normalDesc = new Desc();
    normalDesc.overwrite = "the orign value";
    normalDesc.ignore = 1;
    normalDesc = copy(normalDesc, src);
    System.out.println( "copy to not empty Desc:" );
    System.out.println( normalDesc );
```
输出结果：
```
copy to not empty Desc:
[overwrite='overwrite from src', ignore='1']
```


当不忽略null字段时：
```
    Desc unignoreNullDesc = new Desc();
    unignoreNullDesc.overwrite = "now it is not null.";
    unignoreNullDesc.ignore = 1;
    Src emptysrc = new Src();
    unignoreNullDesc = copy(unignoreNullDesc, emptysrc);
    System.out.println( "unignore null value:" );
    System.out.println( unignoreNullDesc );
```
输出结果：
```
unignore null value:
[overwrite='null', ignore='1']
```

从多个源对象拷贝：
```
    Src sencodsrc = new Src();
    sencodsrc.overwrite = "overwrite from second src";
    sencodsrc.ignore = "this field will be ignored";
    sencodsrc.whatever = "this field will be ignored";
    normalDesc = copy(normalDesc, src, sencodsrc);
    System.out.println( "copy from multy srcs :" );
    System.out.println( normalDesc );
```
输出结果：
```
copy from multy srcs :
[overwrite='overwrite from second src', ignore='1']
```
