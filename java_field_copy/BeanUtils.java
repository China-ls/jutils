import java.lang.reflect.Field;

/**
 * Created by hx on 2017/1/3.
 * <br><a href="https://github.com/China-ls">GitHub</a>
 */
public abstract class BeanUtils {

    /**
     * 从多个Object 中拷贝相同属性名称且相同类型字段到dsc对象
     * 默认不忽略空字段，即便source中是null，也不忽略
     *
     * @param dsc  目标对象
     * @param srcs 源对象列表，如果具有相同属性，则第二个覆盖第一个相同字段
     * @param <T>  泛型
     * @return 返回目标对象
     */
    public static <T> T copy(T dsc, Object... srcs) {
        return copy(false, dsc, srcs);
    }

    /**
     * 从多个Object 中拷贝相同属性名称且相同类型字段到dsc对象
     *
     * @param ignoreNull 忽略空属性, true则source字段是null,则不复制到dsc
     * @param dsc        目标对象
     * @param srcs       源对象列表，如果具有相同属性，则第二个覆盖第一个相同字段
     * @param <T>        泛型
     * @return 返回目标对象
     */
    public static <T> T copy(boolean ignoreNull, T dsc, Object... srcs) {
        if (null == dsc) {
            return null;
        }
        Class dscCl = dsc.getClass();
        Field[] fields = dscCl.getDeclaredFields();
        for (Object src : srcs) {
            if (null == src) {
                continue;
            }
            Class srcCl = src.getClass();
            for (Field field : fields) {
                try {
                    Field srcField = srcCl.getDeclaredField(field.getName());
                    if (null == srcField || srcField.getType() != field.getType()) {
                        continue;
                    }
                    //Read Value From Source
                    boolean accessible = srcField.isAccessible();
                    srcField.setAccessible(true);
                    Object value = srcField.get(src);
                    srcField.setAccessible(accessible);
                    if (ignoreNull && null == value) {
                        continue;
                    }
                    // Set Value To Desc
                    accessible = field.isAccessible();
                    field.setAccessible(true);
                    field.set(dsc, value);
                    field.setAccessible(accessible);
                } catch (NoSuchFieldException e) {
                    // ignore
                } catch (IllegalAccessException e) {
                    // ignore
                }
            }
        }
        return dsc;
    }

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

    public static void main(String[] args) {
        Desc emptyDesc = new Desc();
        Src src = new Src();
        src.overwrite = "overwrite from src";
        src.ignore = "this field will be ignored";
        src.whatever = "this field will be ignored";
        // you also can ignore the return value
        emptyDesc = copy(emptyDesc, src);
        System.out.println( "copy to empty Desc:" );
        System.out.println( emptyDesc );

        Desc normalDesc = new Desc();
        normalDesc.overwrite = "the orign value";
        normalDesc.ignore = 1;
        normalDesc = copy(normalDesc, src);
        System.out.println( "copy to not empty Desc:" );
        System.out.println( normalDesc );

        Desc unignoreNullDesc = new Desc();
        unignoreNullDesc.overwrite = "now it is not null.";
        unignoreNullDesc.ignore = 1;
        Src emptysrc = new Src();
        unignoreNullDesc = copy(unignoreNullDesc, emptysrc);
        System.out.println( "unignore null value:" );
        System.out.println( unignoreNullDesc );

        Src sencodsrc = new Src();
        sencodsrc.overwrite = "overwrite from second src";
        sencodsrc.ignore = "this field will be ignored";
        sencodsrc.whatever = "this field will be ignored";
        normalDesc = copy(normalDesc, src, sencodsrc);
        System.out.println( "copy from multy srcs :" );
        System.out.println( normalDesc );
    }

}
