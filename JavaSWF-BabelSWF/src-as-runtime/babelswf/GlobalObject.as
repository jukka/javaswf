/**
 * The global object.
 */
package babelswf
{
	import flash.utils.Proxy;
	import flash.utils.flash_proxy;

	public dynamic class GlobalObject extends Proxy
	{
		/**
		 * The singleton
		 */
		public static var _global:GlobalObject = new GlobalObject();
		
		/**
		 * The actual values
		 */
		private var values:Object = { _global: _global };
		
		private var enumItems:Array; //supporting enumeration
		
		public function GlobalObject()
		{
			super();
		}
		
	    override flash_proxy function getProperty(name:*):* 
	    {
	    	trace( "babelswf >> get _global." + name );
            return values[name];
        }

        override flash_proxy function setProperty(name:*, value:*):void 
        {
            trace( "babelswf >> set _global." + name + " = " + value );
            values[name] = value;
        }
        
        override flash_proxy function hasProperty(name:*):Boolean 
        {
        	var has:Boolean = values.hasOwnProperty( name );
            trace( "babelswf >> _global hasProperty " + name + " = " + has );
            return has;        	
        }
        
        override flash_proxy function callProperty(methodName:*, ... args):* 
        {
	        switch( methodName.toString()) 
	        {
	            case 'toString':
	                return "[BabelSWF _global]";
	            default:
	                break;
	        }
        	
            trace( "babelswf >> calling _global." + methodName + "( " + args.length + " args )" );
            return values[methodName].apply(values, args);
        }
        
        override flash_proxy function nextValue(index:int):*
        {
            var val:* = values[ enumItems[ index - 1 ]];
            trace( "babelswf >> _global nextValue(" + index + ") = " + val );
            return val;
        }
        
        override flash_proxy function nextNameIndex(index:int):int
        {
        	var nextIndex:int = index + 1;
        	
	        // initial call
	        if (index == 0) 
	        {
	            enumItems = new Array();
	            for( var x:* in values ) 
	            {
	                enumItems.push(x);
	            }
	        }
	        
	        if( nextIndex == enumItems.length ) 
	        {
                nextIndex = 0;
	        } 
          
            trace( "babelswf >> _global nextNameIndex(" + index + ") = " + nextIndex );
            return nextIndex;
        }
        
        override flash_proxy function nextName(index:int):String 
        {
            var name:* = enumItems[ index - 1 ];
            trace( "babelswf >> _global nextName(" + index + ") = " + name );
            return name;
        }
	}
}