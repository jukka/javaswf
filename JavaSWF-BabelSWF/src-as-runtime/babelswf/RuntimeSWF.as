/**
 * The MovieClip for loading the Babel runtime
 */
package babelswf
{
	import flash.display.Loader;
	import flash.display.MovieClip;
	import flash.net.URLRequest;
	
	public class RuntimeSWF extends MovieClip
	{
		public var runtime:Class = Runtime;
		
		public function RuntimeSWF()
		{
			super();
			
			trace( "BabelSWF runtime swf loaded" );
			
			var loader:Loader = new Loader();
			addChild( loader );
			loader.load( new URLRequest( "hello-world-trace-avm2.swf" ) );
		}
	}
}