/**
 * Base for prototype objects
 */
package babelswf.prototypes
{
	import babelswf.AVM1Object;
	
	public dynamic class Prototype
	{
		public var constructor:Function;
		
		public function Prototype( superclass:Prototype, cons:Function )
	    {	        
	        if( cons == null ) cons = function():* {};
	        constructor = cons;
			constructor.prototype = new AVM1Object( this );
			
			if( superclass != null )
			{
			    this["__proto__"] = superclass.constructor.prototype;
                this["super"    ] = superclass.constructor;
			} 
		}
		
		/**
		 * Instantiate
		 */
		public function avm1_new():AVM1Object
		{
		    var obj:AVM1Object = new AVM1Object({ "__proto__": constructor.prototype });
		    
		    //trace( "Prototype.avm1_new --> " + obj );
		    
		    return obj;
		}
	}
}