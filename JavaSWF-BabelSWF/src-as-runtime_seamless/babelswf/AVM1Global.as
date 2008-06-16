/**
 * The global object
 */
package babelswf
{
	import babelswf.prototypes.PrototypeFunction;
	import babelswf.prototypes.PrototypeMovieClip;
	import babelswf.prototypes.PrototypeObject;
	
	public dynamic class AVM1Global
	{
		
		public var _global:AVM1Global;
		
		public var Object    :Function = PrototypeObject.instance.constructor
        public var MovieClip :Function = PrototypeMovieClip.instance.constructor;  
		
		public function AVM1Global()
		{
		    trace( "AVM1Global constructed" );
			_global = this;
			
			this["Function"] = PrototypeFunction.instance.constructor;
		}
		
		public function toString():String
		{
		    return "AVM1Global";    
		}
				      
        public function ASSetPropFlags(... rest ):* {
            trace( "*** Need to implement ASSetPropFlags ***" );
            
            return undefined;
        };
	}
}