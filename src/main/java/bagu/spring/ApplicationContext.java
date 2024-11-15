package bagu.spring;

import bagu.spring.anno.Autowired;
import bagu.spring.anno.Component;
import bagu.spring.anno.ComponentScan;
import bagu.spring.anno.EventListener;
import bagu.spring.anno.Scope;
import bagu.spring.aop.AspectAutoProxyPostProcessor;
import bagu.spring.aop.AspectProcessor;
import bagu.spring.aware.BeanNameAware;
import bagu.spring.event.ApplicationEventMulticaster;
import bagu.spring.event.ApplicationListener;
import bagu.spring.interfaces.*;


import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author ShouPeng
 * @description
 * @since 2024/11/12
 */
public class ApplicationContext {

    private Class<?> config;

    /**
     * beanName -> BeanDefinition
     */
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    /**
     * Cache of singleton factories: bean name to ObjectFactory.
     */
//    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);

    /**
     * Cache of early singleton objects: bean name to bean instance.
     */
    private final Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>(16);

    /**
     * 单例池： beanName -> beanObj
     */
    public final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

    /**
     * Cache of singleton factories: bean name to ObjectFactory.
     */
    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);

    private final List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    /**
     * Disposable bean instances: bean name to disposable instance.
     */
    private final Map<String, Object> disposableBeans = new LinkedHashMap<>(16);


    public ApplicationContext(Class<?> config) {
        this.config = config;

        scan(config);

        registerAspect();

        registerSmartInstantiationAwareBeanPostProcessor();

        registerBeanPostProcessors();

        for (String s : beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(s);
            if (beanDefinition.isSingleton()) {
                Object o = creatBean(beanDefinition);
                singletonObjects.put(s, o);
            }
        }
        System.out.println("==================bean created===================");
        registerListeners();
    }

    private void scan(Class<?> config) {
        ComponentScan componentScan = config.getDeclaredAnnotation(ComponentScan.class);
        String path = componentScan.value();
        System.out.println(path);

        System.out.println("扫描>>>>>>>>>>>>>>>>>>>>>>>>");
        path = path.replace(".", "/");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(path);
        File file = new File(resource.getFile());

        scanAllBeanDefinition(classLoader, file);
        System.out.println("扫描>>>>>>>>>>>>>>>>>>>>>>>>end");
    }

    private void scanAllBeanDefinition(ClassLoader classLoader, File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                if (f.isDirectory()){
                    scanAllBeanDefinition(classLoader,f);
                }
                String fileName = f.getAbsolutePath();
                if (fileName.endsWith(".class")) {
                    String className = fileName.substring(fileName.indexOf("bagu"), fileName.indexOf(".class"));
                    className = className.replace(File.separator, ".");
                    try {
                        Class<?> cls = classLoader.loadClass(className);
                        // 当前 class 是个 Bean 对象
                        if (cls.isAnnotationPresent(Component.class)) {
                            Component componentAnno = cls.getAnnotation(Component.class);
                            String beanName = componentAnno.value();
                            // 生成默认的 beanName
                            if ("".equals(beanName)) {
                                beanName = Introspector.decapitalize(cls.getSimpleName());
                            }

                            BeanDefinition beanDefinition = new BeanDefinition();
                            beanDefinition.setType(cls);
                            if (cls.isAnnotationPresent(Scope.class)) {
                                Scope scopeAnnotation = cls.getAnnotation(Scope.class);
                                beanDefinition.setScope(scopeAnnotation.value());
                            } else {
                                beanDefinition.setScope("singleton");
                            }
                            beanDefinitionMap.put(beanName, beanDefinition);
                        }
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    /*
       先将 beanDefinition 扫描出来再创建实例，而不是边扫描边创建
       是因为在 createBean 时，要进行依赖注入，需要看看有没有提供某个类的依赖
       所以要先扫描后创建
    */
    public Object getBean(String beanName) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new NullPointerException(beanName + " is not find");
        } else {
            // 单例
            if (beanDefinition.isSingleton()) {
                Object singletonObject = singletonObjects.get(beanName);
//                System.out.println("一级==========" + singletonObject);
                // 没有某个 bean， create
                if (singletonObject == null) {
                    //二级缓存
                    singletonObject = earlySingletonObjects.get(beanName);
//                    System.out.println("二级==========" + singletonObject);
                    if (singletonObject == null) {
                        ObjectFactory<?> objectFactory = singletonFactories.get(beanName);
                        if (objectFactory != null) {
                            singletonObject = objectFactory.getObject();
                            this.earlySingletonObjects.put(beanName, singletonObject);
                            this.singletonFactories.remove(beanName);
                        }else {
                            singletonObject = creatBean(beanDefinition);
                            this.singletonObjects.put(beanName, singletonObject);
                            this.earlySingletonObjects.remove(beanName);
                            this.singletonFactories.remove(beanName);
                        }
                    }

                }

                return singletonObject;
            } else {
                return creatBean(beanDefinition);
            }
        }
    }

    /**
     * 创建bean
     * @param beanDefinition
     * @return
     */
    public Object creatBean(BeanDefinition beanDefinition) {
        String beanName = beanDefinition.getType().getSimpleName();
        beanName = beanName.toLowerCase().charAt(0) + beanName.substring(1);
        Object o1 = singletonObjects.get(beanName);
        Object o2 = earlySingletonObjects.get(beanName);
        if (o1 != null){
            return o1;
        }
        if ( o2 != null){
            return o2;
        }
        Class cls = beanDefinition.getType();
        try {
            Object o = cls.getDeclaredConstructor().newInstance();

            Object t = o;
            String finalBeanName = beanName;
            singletonFactories.put(beanName, new ObjectFactory<Object>() {
                @Override
                public Object getObject() throws RuntimeException {
                    Object exposedObject = t;
                    for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                        if (beanPostProcessor instanceof SmartInstantiationAwareBeanPostProcessor) {
                            exposedObject = ((SmartInstantiationAwareBeanPostProcessor) beanPostProcessor).getEarlyBeanReference(exposedObject, finalBeanName);
                        }
                    }
                    return exposedObject;
                }
            });
            Object exposedObject = o;
            DI(cls, o);
            exposedObject = initializeBean(beanDefinition, o);

            registerDisposableBeanIfNecessary(beanName,o,beanDefinition);

            return exposedObject;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void DI(Class cls, Object o) throws IllegalAccessException {
        for (Field declaredField : cls.getDeclaredFields()) {
            if (declaredField.isAnnotationPresent(Autowired.class)) {
                Object bean = getBean(declaredField.getName());
                System.out.println("依赖注入>>>>>>>>>>>>>>>>>>>>>>>> byName  " + declaredField.getName());
                declaredField.setAccessible(true);
                declaredField.set(o, bean);
            }
        }
    }

    /**
     * 初始化阶段，包含：Aware回调、初始化前、初始化、初始化后
     * @param beanDefinition
     * @param bean
     * @return
     */
    private Object initializeBean(BeanDefinition beanDefinition, Object bean) {
        String beanName = beanDefinition.getType().getSimpleName();
        beanName = beanName.toLowerCase().charAt(0) + beanName.substring(1);
        // 各种 Aware 回调
        if (bean instanceof BeanNameAware) {
            BeanNameAware bean1 = (BeanNameAware) (bean);
            bean1.setBeanName(beanName);
        }

        // 初始化前
        // TODO  BeanPostProcessor 解析 @PostConstruct 执行初始化方法
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
            bean = beanPostProcessor.postProcessBeforeInitialization(bean, beanName);
        }

        // 初始化
        if (bean instanceof InitializingBean) {
            ((InitializingBean) (bean)).afterPropertiesSet();
        }

        // TODO 执行 @Bean(initMethod = “myInit”) 指定的初始化方法（将初始化方法记录在 BeanDefinition 中）

        // 初始化后，由 AnnotationAwareAspectJAutoProxyCreator 创建 aop 代理
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
            bean = beanPostProcessor.postProcessAfterInitialization(bean, beanName);
        }

        // 如果有 aop 的话，这里的 bean 返回的是 aop 后的一个代理对象
        return bean;
    }

    /**
     * 注册 BeanPostProcessor
     */
    private void registerBeanPostProcessors() {
        /*
          1. 从 beanDefinitionMap 中找出所有的 BeanPostProcessor
          2. 创建 BeanPostProcessor 放入容器
          3. 将创建的 BeanPostProcessor 注册到 beanPostProcessorList

          这里的写法：先注册的 BeanPostProcessor 会对后创建的 BeanPostProcessor 进行拦截处理，
          BeanPostProcessor 的创建走 bean 的生命周期流程
         */
        beanDefinitionMap.entrySet()
                .stream()
                .filter((entry) -> BeanPostProcessor.class.isAssignableFrom(entry.getValue().getType()))
                .forEach((entry) -> {
                    BeanPostProcessor beanPostProcessor = (BeanPostProcessor) getBean(entry.getKey());
                    this.beanPostProcessorList.add(beanPostProcessor);
                });
    }

    private void registerSmartInstantiationAwareBeanPostProcessor(){
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setScope("singleton");
        beanDefinition.setType(AspectAutoProxyPostProcessor.class);
        beanDefinitionMap.put("aspectAutoProxyPostProcessor", beanDefinition);
    }

    private void registerAspect() {
        AspectProcessor.getAspects(this);
    }

    private ApplicationEventMulticaster getDefaultApplicationEventMulticaster() {
        for (Object value : singletonObjects.values()) {
            if (value instanceof ApplicationEventMulticaster){
                return (ApplicationEventMulticaster) value;
            }
        }
        return null;
    }

    private void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition) {
        if (beanDefinition.isSingleton() && DisposableBeanAdapter.hasDestroyMethod(bean, beanDefinition)) {
            this.disposableBeans.put(beanName, new DisposableBeanAdapter(bean, beanName, beanDefinition));
        }
    }

    private void registerListeners() {
        System.out.println("start register listeners");
        ApplicationEventMulticaster eventMulticaster = getDefaultApplicationEventMulticaster();
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            BeanDefinition beanDefinition = entry.getValue();
            if (beanDefinition.getType().isAnnotationPresent(EventListener.class)){
                eventMulticaster.addApplicationListener((ApplicationListener<?>) getBean(entry.getKey()));
                System.out.println("scan listener:" + getBean(entry.getKey()));
            }
        }
    }

    public List<Class<?>> getAllBeanClass() {
        return beanDefinitionMap.values()
                .stream()
                .map((Function<BeanDefinition, Class<?>>) BeanDefinition::getType)
                .collect(Collectors.toList());
    }

    public void close() {
        System.out.println("==========================");
        System.out.println("shut down spring application context");
        destroySingletons();
    }

    private void destroySingletons() {
        synchronized (this.disposableBeans) {
            Iterator<Map.Entry<String, Object>> it = disposableBeans.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = it.next();
                String beanName = entry.getKey();
                DisposableBean bean = (DisposableBean) entry.getValue();
                try {
                    bean.destroy();
                } catch (Exception e) {
                    System.out.println("Destruction of bean with name '" + beanName + "' threw an exception：" + e);
                }
                it.remove();
            }
        }

        // Clear all cached singleton instances in this registry.
        this.singletonObjects.clear();
        this.earlySingletonObjects.clear();
        this.singletonFactories.clear();
    }

}
