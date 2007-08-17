package org.epistem.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Util that uses a Proxy to implement an interface, record all calls and
 * play them back.  The methods of the interface should be void or return
 * an instance of another interface.
 * 
 * When a method returns an interface then a proxy-method-call-recorder is
 * returned and calls to that interface are recorded and played back. 
 * This nesting will happen to any depth.
 * 
 * @author nmain
 */
public class MethodCallRecorder implements InvocationHandler {

    public static class Call {
        public final Method method;
        public final Object[] args;
        private MethodCallRecorder recorder; 
        
        public Call( Method method, Object[] args ) {
            this.method = method;
            this.args   = args;
        }

        /**
         * Get the object to return from the method
         */
        public Object getReturnObject() throws Exception {
            Class retType = method.getReturnType();
            if( retType.isInterface() ) {
                recorder = new MethodCallRecorder();
                return recorder.implementInterface( retType );
            }
            
            return null;
        }
        
        /** Replay the method call */
        public void replay( Object target ) throws Throwable {
            Object result = method.invoke( target, args );
            if( result != null && recorder != null ) {
                recorder.replayAllCalls( result );
            }
        }
    }
    
    /** List of Call instances in order */
    public final List calls = new ArrayList();
    
    public MethodCallRecorder() {}
    
    /**
     * Implement a given interface - calls to the proxy will be recorded.
     * @param interfaceClass the interface to implement
     * @return a proxy that implements the interface
     */
    public final Object implementInterface( Class interfaceClass ) throws Exception {
        return Proxy.newProxyInstance( interfaceClass.getClassLoader(), 
                                       new Class[] { interfaceClass }, 
                                       this );
    }
    
    /**
     * Replay all recorded calls.
     * @param target the instance to invoke against
     * @throws Exception as soon as any call fails or throws up
     */
    public void replayAllCalls( Object target ) throws Throwable {
        for (Iterator i = calls.iterator(); i.hasNext();) {
            Call call = (Call) i.next();
			try{
				call.replay( target );
			} catch( InvocationTargetException ex ) {
				throw ex.getCause();
			}
        }			
    }
    
    /** @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[]) */
    public final Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        
        Call call = recordCall( method, args );
        if( call != null ) {
            calls.add( call );
            return call.getReturnObject();
        }
        
        return null;
    }
    
    /**
     * Override to provide specialized Call objects.
     * @param method the method called
     * @param args the arguments - may be null
     * @return null to skip recording the call
     */
    protected Call recordCall( Method method, Object[] args ) {
        return new Call( method, args );
    }
}
