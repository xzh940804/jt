package com.jt.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

//@Component		//将类交给spring容器管理
@Aspect
public class DemoAOP {
	
	//切入点+通知   com.jt.service全部类全部方法 任意参数,任意返回值
	@Pointcut("execution(* com.jt.service..*.*(..))")
	public void pointCut() {
		System.out.println("太TM随意!!!");
	}
	
	/**
	 * 四大通知: before/afterReturning/afterThrowing/after
	 * 主要记录程序的执行状态 一般不加返回值. 都是void
	 *	afterReturning除外.
	 *  参数中只能添加 JoinPoint
	 *
	 *around通知最为强大 可以控制目标方法是否执行.
	 *	参数中必须添加ProceedingJoinPoint
	 */
	
	/**
	 * 获取目标对象的name
	 */
	@Before("pointCut()")
	public void before(JoinPoint joinPoint) {
		String targetName = 
				joinPoint.getSignature().getDeclaringTypeName();
		System.out.println("我是一个before通知:"+targetName);
	}
	
	//目标方法执行之后
	@AfterReturning(pointcut = "pointCut()",returning = "obj")
	public Object afterReturn(Object obj) {
		System.out.println(obj);
		System.out.println("我是一个傻傻的后置通知!!!!");
		return obj;
	}
	
	//异常通知和后置通知是互斥的
	@AfterThrowing(pointcut = "pointCut()",throwing = "thro")
	public void afterThrow(Exception thro) {
		System.out.println("我是一个异常通知!!!!!");
		System.out.println(thro.getMessage());
	}
	
	
	@Around("pointCut()")
	public Object around(ProceedingJoinPoint joinPoint) {
		
		Object obj = null;
		try {
			Long startTime = System.currentTimeMillis();
			System.out.println("环绕通知执行前");
			obj = joinPoint.proceed();
			Long endTime = System.currentTimeMillis();
			System.out.println("方式执行时间:"+(endTime - startTime));
		} catch (Throwable e) {
			e.printStackTrace();
			
		}	
		return obj;
	}
}
