# simple_spring

spring框架的简单实现

实现了：

- IOC
- AOP
- 事件

## IOC

### Bean 生命周期

Scan 组件扫描、Bean 生命周期`（创建、依赖注入、初始化、销毁）`， 包括：Bean后处理器 ==BeanPostProcessor==的调用、各种 Aware 回调。

@Autowired 通过 byName 查找依赖。

such as

```java
@Autowired
public T testService;
```

byName --->>> testService

### 循环依赖

- 使用三级缓存解决循环依赖问题。

## AOP

完成了 5 种通知类型（@Before、@AfterReturning、@After、@AfterThrowing、@Around）的解析，对符合切点的目标对象进行代理增强。 应用在目标方法上的多个通知会链式调用执行，且实现了通知的调用顺序控制-对切面排序。

切点表达式的解析只简单判断了是否和当前bean的beanName一致。

通过 Bean 后处理器 ==AspectAutoProxyPostProcessor== 对符合切点的目标对象进行代理增强。 如果发生了循环依赖要在依赖注入阶段提前创建代理，此 Bean 后处理器使用缓存避免了代理对象的重复创建。

参考 Spring 源码实现，大部分类名都和 Spring 中的类名一致， 简单起见，和 Spring 的接口设计略有不同。

## 事件

为了实现事件机制，将其分成了几步：

1. 定义事件时继承`ApplicationContextEvent`类，其终极父类是`EventObject`
2. 事件监听器实现了`ApplicationListener`接口，其终极父类是`EventListener`
3. 事件发布者最终都是调用`ApplicationEventPublisher`接口中的`publishEvent`发布事件，为了能够调用`publishEvent`方法，需要实现这个接口，相当于注入
4. 为了事件发布之后**对应**的监听器能够被触发，所有的监听器被保存到了一个**广播器**的容器中，并且事件发布时调用的`publishEvent`方法内部调用的是广播器的`multicastEvent`方法，内部根据事件类型匹配到保存的监听器，从而进行触发，这个广播器可以说衔接了事件的发布和监听器的触发，是一个**核心组件**

## 测试

Main类即为测试类