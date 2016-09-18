import com.epam.am.whatacat.model.Post;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

public class Runner {
    public static void main(String[] args) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        Post post = new Post();
        BeanInfo beanInfo = Introspector.getBeanInfo(Post.class);
        System.out.println(post);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor test : propertyDescriptors) {
            System.out.println(test.getName());
            if (test.getName().equals("title")) {
                test.getWriteMethod().invoke(post, "test");
            }
        }
        System.out.println(post);
    }
}
