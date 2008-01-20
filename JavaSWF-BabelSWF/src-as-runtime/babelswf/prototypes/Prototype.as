/**
 * Base for prototype objects
 */
package babelswf.prototypes
{
	import babelswf.AVM1Function;
	
	public dynamic class Prototype
	{
		public var constructor:AVM1Function = new AVM1Function(null,null);
		
		public function Prototype()
	    {
			constructor.prototype = this;
		}
		
		/**
		 * Add a function to the prototype
		 */
		public function add( name:String, func:Function ):void {
			this[name] = new AVM1Function(func,null);
		}
	}
}