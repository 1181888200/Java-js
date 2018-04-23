package com.wloveo.java.js;

import java.io.File;
import java.io.FileReader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JSTest {

	public static void main(String[] args) throws Exception {
		
		//操作Js首先需要ScriptEngine
		//在jdk中可以用3中方式获得：
			//	mgr.getEngineByExtension(String extension)
			//	mgr.getEngineByMimeType(String mimeType)
			//	mgr.getEngineByName(String shortName)
		
		ScriptEngineManager mgr = new ScriptEngineManager();  
		
		//getEngineByExtension可用参数： JS
		ScriptEngine engineByExt = mgr.getEngineByExtension("js");
		
		// getEngineByMimeType可用参数：
		//		application/javascript
		//		application/ecmascript
		//		text/javascript
		//		text/ecmascript
		ScriptEngine engineByType = mgr.getEngineByMimeType("application/javascript");  
		
		//getEngineByName可用参数：
		//		js
		//		rhino
		//		JavaScript
		//		javascript
		//		ECMAScript//js和javascript是它的扩展
		
		ScriptEngine engineByName = mgr.getEngineByName("JavaScript");
		
//		testUsingJDKClasses(engineByExt);
		
//		testScriptInterface(engineByType);
//		
//		testInvokeScriptMethod(engineByName);
//		
		testScriptVariables(engineByName);
		
		testUsingJSFile(engineByName,"F:/nms/workspace/TestEveryThing/src/expression.js");
	}
	
	/**
	 * 读取某个js文件 执行里面的方法
	 * @param engine
	 * @param jsFileName
	 * @throws Exception
	 */
	private static void testUsingJSFile(ScriptEngine engine,String jsFileName) throws Exception{
		FileReader reader = new FileReader(jsFileName);   // 执行指定脚本   
		engine.eval(reader);  
		Invocable invoke = (Invocable)engine;
		Double c = (Double)invoke.invokeFunction("merge", 2, 3);    
		System.out.println("c = " + c);
	}
	
	
	/**
	 * 演示脚本语言如何使用JDK平台下的类,调用一个JDK平台的Swing窗口
	 *
	 * @param engine ScriptEngine实例
	 * @return void
	 * */	
	@SuppressWarnings("unused")
	private static void testUsingJDKClasses(ScriptEngine engine)	throws Exception {
		// Packages是脚本语言里的一个全局变量,专用于访问JDK的package
		String js = "function doSwing(t){var f=new Packages.javax.swing.JFrame(t);f.setSize(400,300);f.setVisible(true);}";
		engine.eval(js);
		// Invocable 接口: 允许java平台调用脚本程序中的函数或方法
		Invocable inv = (Invocable) engine;
		// invokeFunction()中的第一个参数就是被调用的脚本程序中的函数，第二个参数是传递给被调用函数的参数；
		inv.invokeFunction("doSwing", "Scripting Swing");
	}

	/**
	 * 演示脚本语言如何实现Java的接口,启动线程来运行script提供的方法
	 *
	 * @param engine ScriptEngine实例
	 * @return void
	 * */	
	private static void testScriptInterface(ScriptEngine engine)	throws ScriptException {
		String script = "var obj = new Object(); obj.run = function() { println('run method called'); }";
		engine.eval(script);
		Object obj = engine.get("obj");
		Invocable inv = (Invocable) engine;
		
		Runnable r = inv.getInterface(obj, Runnable.class);
		Thread th = new Thread(r);
		th.start();
	}

	/**
	 * 演示如何在Java中调用脚本语言的方法，通过JDK平台给script的方法中的形参赋值
	 *
	 * @param engine ScriptEngine实例
	 * @return void
	 * */		
	private static void testInvokeScriptMethod(ScriptEngine engine)	throws Exception {
		String script = "function helloFunction(name) { return 'Hello everybody,' + name;}";
		engine.eval(script);
		Invocable inv = (Invocable) engine;
		String res = (String) inv.invokeFunction("helloFunction", "Scripting");
		System.out.println("res:" + res);
	}
	
	/**
	 * 演示如何暴露Java对象为脚本语言的全局变量,同时演示将File实例赋给脚本语言，并通过脚本语言取得它的各种属性值
	 *
	 * @param engine ScriptEngine实例
	 * @return void
	 * */		
	private static void testScriptVariables(ScriptEngine engine)	throws ScriptException {
		File file = new File("F:/nms/workspace/TestEveryThing/src/test.txt");
		engine.put("f", file);
		engine.eval("println('Total Space:'+f.getTotalSpace())");
		engine.eval("println('Path:'+f.getPath())");
	}

}
