/**
 * The global object
 */
package babelswf
{
	import babelswf.prototypes.PrototypeMovieClip;
	import babelswf.prototypes.PrototypeObject;
	
	public dynamic class AVM1Global
	{
		
		public var _global:AVM1Global;
		
		public var Object    :Function = new PrototypeObject().constructor;
        public var MovieClip :Function = new PrototypeMovieClip().constructor;  
		
		public function AVM1Global()
		{
			_global = this;
		}
	}
}