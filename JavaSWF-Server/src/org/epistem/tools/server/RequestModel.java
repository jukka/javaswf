package org.epistem.tools.server;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.epistem.template.TemplateSet;

/**
 * The model for templates
 * 
 * @author nickmain
 */
public class RequestModel {

	private final TemplateSet         templateSet;
	private final HttpServletRequest  request;
	private final HttpServletResponse response;
	private final Map<String, String> params = new HashMap<String, String>();
	private final ToolsServer         server;
	
	RequestModel( TemplateSet         templateSet, 
			      HttpServletRequest  request,
	              HttpServletResponse response,
	              ToolsServer         server ) {
		this.templateSet = templateSet;
		this.request     = request;
		this.response    = response;
		this.server      = server;
		
    	@SuppressWarnings("unchecked")
    	Map<String, String[]> paramMap = (Map<String, String[]>) request.getParameterMap();
    	
    	for( Map.Entry<String, String[]> entry : paramMap.entrySet()) {
    		params.put( entry.getKey(), entry.getValue()[0] );
    	}
	}
	
	public HttpServletRequest  getRequest () { return request;  }
	public HttpServletResponse getResponse() { return response; }
	
	/** Get the parameter map */
	public Map<String,String> getParams() { return params; }
	
	/** Whether the given parameter exists */
	public boolean hasParameter( String name ) { return params.containsKey( name ); }
	
	public ToolsServer getServer() { return server; }

	/**
	 * Get the root url for this server
	 */
	public String getRootUrl() {
		return "http://" + request.getServerName() + ":" + request.getLocalPort() + "/";
	}

	/**
	 * Set the default value for a session attribute (if the attribute does not
	 * already exist).
	 */
	public void defaultAttribute( String name, Object value ) {
		if( hasAttribute( name ) ) return;
		request.getSession().setAttribute( name, value );
	}
	
	/**
	 * Test whether a given session attribute exists
	 */
	public boolean hasAttribute( String name ) {
		return request.getSession().getAttribute( name ) != null;
	}
	
	/**
	 * Get a session attribute
	 */
	public Object attribute( String name ) {
		Object value = request.getSession().getAttribute( name );
		if( value == null ) return "";
		return value;
	}
	
	/**
	 * Set a session attribute
	 */
	public void setAttribute( String name, Object value ) {
		request.getSession().setAttribute( name, value );
	}
}
