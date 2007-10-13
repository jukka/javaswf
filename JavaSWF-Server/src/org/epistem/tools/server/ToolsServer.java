package org.epistem.tools.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.epistem.template.Template;
import org.epistem.template.TemplateSet;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.HashSessionManager;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.servlet.SessionHandler;

/**
 * A simple HTTP server to host various tools
 * 
 * @author nickmain
 */
public class ToolsServer {

	private final TemplateSet templates = new TemplateSet( ToolsServer.class );
	
	private Server server;
	
	public ToolsServer( int port )  throws Exception {
		templates.bypassCache( true );
		
		server = new Server( port );
		Context context = new Context(server,"/",Context.SESSIONS);
		context.addServlet( new ServletHolder(new ToolServlet()), "/*");
		server.start();
		server.join();
	}
	
	/**
	 * Stop the server
	 */
	public void stop() throws Exception { 
		server.stop(); 
	}
	
	public static void main( String[] args ) throws Exception {
		new ToolsServer( 8910 );
	}
	
	private class ToolServlet extends HttpServlet {

		/** @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse) */
		@Override
		protected void service( HttpServletRequest  request,
				                HttpServletResponse response ) 
			throws ServletException, IOException {
			
			String target = request.getPathInfo();
	    	target = target.substring( 1 ); //strip the leading slash
	    	Template template;
	    	
	    	try {
	    		
	    		template = templates.get( target );
	    		
	    	} catch( Exception ex ) {
		        response.setContentType("text/plain");
		        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		        response.getWriter().println("NOT FOUND");
		        ((Request)request).setHandled(true);
		        return;
	    	}

	    	RequestModel model = new RequestModel( templates, 
	    			                               request, response, 
	    			                               ToolsServer.this );
	    	
	    	try {
		        
	    		StringWriter sw = new StringWriter();
	    		template.process( sw, model );
	    		
	    		response.setContentType("text/html");
	    		response.setStatus( HttpServletResponse.SC_OK );
	    		response.getWriter().write( sw.toString() );
	    		
	    	} catch( Exception ex ) {		    		
		        response.setContentType("text/plain");
		        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

		        PrintWriter pw = new PrintWriter( response.getWriter() );
	    		ex.printStackTrace( pw );
	    		pw.flush();
	    	}
		}
	}
}