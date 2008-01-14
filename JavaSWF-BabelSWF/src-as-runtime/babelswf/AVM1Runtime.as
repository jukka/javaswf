/**
 * Runtime utilities to support translated AVM1 code.
 */
package babelswf
{
	import babelswf.prototypes.PrototypeMovieClip;
	import babelswf.prototypes.PrototypeObject;
	
	public class AVM1Runtime
	{
		private var mainTimelineClass:Class = AVM1MainTimeline;
        private var spriteClass:Class       = AVM1Sprite;
		
		public static var avm1Object:Function = function():* 
		{
			this.__proto__ = babelswf.prototypes.PrototypeObject;
		};
		avm1Object.prototype = babelswf.prototypes.PrototypeObject;
		
        public static var avm1MovieClip:Function = function():* 
        {
            this.__proto__ = babelswf.prototypes.PrototypeMovieClip;
        };
        avm1MovieClip.prototype = babelswf.prototypes.PrototypeMovieClip;
		
		public function AVM1Runtime()
		{
			super();
		}
	}
}