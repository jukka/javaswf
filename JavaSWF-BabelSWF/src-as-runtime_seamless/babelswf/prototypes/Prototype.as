/**
 * Base for prototype objects
 */
package babelswf.prototypes
{
	import babelswf.AVM1Object;
	
	public dynamic class Prototype
	{
		public var constructor:Function = function():* {};
		
		public function Prototype()
	    {	        
			constructor.prototype = new AVM1Object( this );
		}
	}
}