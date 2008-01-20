/**
 * Test harness to allow the runtime to be loaded as a SWF and pull in a
 * translated AVM1 swf
 */
package babelswf
{
	import flash.display.Loader;
	import flash.display.MovieClip;
	import flash.events.Event;
	import flash.events.TimerEvent;
	import flash.net.URLRequest;
	import flash.utils.Timer;
	
	public class RuntimeMovieClip extends MovieClip
	{
		private static var runtime:Class = AVM1Runtime;
		
        private var urls:Array = [ 
            "../generated-as3/dxd_minimal_code_as3.swf",
            "../generated-as3/dxd_minimal_as3.swf"
        ];

        private var loaders:Array = [];

        private var timer:Timer = new Timer( 1000, 1 );

        private function onLoaded(e:Event):void
        {
            trace( urls[0] + " loaded" );
            urls.shift();
            
            if( urls.length > 0 ) 
            {
                loadNext();
            } 
        }

        private function loadNext():void 
        {
            var loader:Loader = new Loader();
            addChild( loader );
            loaders.push( loader );         
            loader.loaderInfo.addEventListener(Event.INIT, onLoaded);
            
            trace( "About to load " + urls[0] );
            loader.load( new URLRequest( urls[0] ));
        }

		
		public function RuntimeMovieClip()
	    {
	    	trace( "" );
            trace( "" );
            trace( "" );
            trace( "" );
            trace( "" );
            trace( "" );
	    	trace( "*********************** RuntimeMovieClip ***********************" );
		    //loader.load( new URLRequest( "../generated-as3/Test_mtasc.swf" ) );
            //loader.load( new URLRequest( "../generated-as3/testfla.swf" ) );

            loadNext();

          
            timer.addEventListener(TimerEvent.TIMER, function():void {
            	trace( "TIMER TIMER TIMER TIMER TIMER TIMER" );
            	
            	var loader:Loader = loaders[1];
            	trace( "MovieClip = " + loader.content );
            	
            	var func:AVM1Function = loader.content["init"]; //maintimeline.avm1Object["init"];
            	func.avm1_callMethod( loader.content, [ "this-is-the-owner", "this-is-the-config" ] );
            });
            timer.start();
		}
	}
}