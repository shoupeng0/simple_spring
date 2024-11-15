package bagu.spring.event;

import org.springframework.util.ClassUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author ShouPeng
 * @description
 * @since 2024/11/14
 */
public abstract class AbstractApplicationEventMulticaster implements ApplicationEventMulticaster {

    public final Set<ApplicationListener<?>> applicationListeners = new LinkedHashSet<>();

    /**
     * Add a listener to be notified of all events.
     * @param listener
     */
    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.add(listener);
    }

    /**
     * Remove a listener from the notification list.
     * @param listener
     */
    @Override
    public void removeApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.remove(listener);
    }



    /**
     * 拿到所有匹配的监听器
     */
    @SuppressWarnings("unchecked")
    protected Set<ApplicationListener<ApplicationEvent>> getApplicationListeners(ApplicationEvent event) {
        Set<ApplicationListener<ApplicationEvent>> matchedListeners = new HashSet<>();

        // 根据当前事件找到所有匹配的事件监听器
        for (ApplicationListener<?> listener : applicationListeners) {
            ApplicationListener<ApplicationEvent> typedListener = (ApplicationListener<ApplicationEvent>) listener;
            // 查看监听器监听的事件类型与当前事件类型的类之间的关系，匹配就为 true
            if (supportEvent(typedListener, event)) {
                matchedListeners.add(typedListener);
            }
        }

        return matchedListeners;
    }

    /**
     * 这个方法判断当前监听器是不是监听的当前事件
     */
    private boolean supportEvent(ApplicationListener<ApplicationEvent> listener, ApplicationEvent event) {
        Class<? extends ApplicationListener> listenerClass = listener.getClass();

        // 按照 CglibSubclassingInstantiationStrategy、SimpleInstantiationStrategy 不同的实例化类型，需要判断后获取目标 class
        //Cglib实例化后真正的class类型是父类，普通反射实例化后的真正class类型就是其本身，根据这个拿到目标的class
        Class<?> targetClass = ClassUtils.isCglibProxyClass(listenerClass) ? listenerClass.getSuperclass() : listenerClass;

        //拿到事件监听器的第一个泛型
        Type genericInterface = targetClass.getGenericInterfaces()[0];

        //拿到泛型的实际类型
        Type actualTypeArgument = ((ParameterizedType) genericInterface).getActualTypeArguments()[0];
        //拿到监听器泛型中执行的类名
        String className = actualTypeArgument.getTypeName();
        Class<?> eventClassName;
        try {
            //拿到这个类名对应的全限定名
            eventClassName = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("wrong event class name: " + className);
        }
        // 如果A.isAssignableFrom(B)结果是true，证明B可以转换成为A,也就是A可以由B转换而来。
        // 判断监听器监听的全限定名和这个事件的全限定名之间是否可以相互转换
        return eventClassName.isAssignableFrom(event.getClass());
    }

}
