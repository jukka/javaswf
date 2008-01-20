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
		
		public var Object    :AVM1Function = new PrototypeObject().constructor;
        public var MovieClip :AVM1Function = new PrototypeMovieClip().constructor;  
		
		public function AVM1Global()
		{
			_global = this;
		}

	}
}