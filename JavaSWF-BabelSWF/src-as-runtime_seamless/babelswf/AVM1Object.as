/**
 * An object that looks as it should from the point of view of translated
 * AVM1 code.  __proto__ is used to chase property lookup.
 */
package babelswf
{
	import flash.utils.Proxy;
	import flash.utils.flash_proxy;
	
	public dynamic class AVM1Object extends Proxy
	{
		private var object:Object;
		
		private var names:Array; // array of object's properties
		private var nameMap:Object;		
		
		/**
		 * @param object the wrapped object
		 */
		public function AVM1Object( object:Object )
		{
		    //trace( "new AVM1Object" );
			if( object == null ) object = {};
			this.object = object;
		}
		
		private function log( msg:String ):void
		{
		    //trace( msg );
		}
		
	    override flash_proxy function callProperty(methodName:*, ... args):* 
	    {
	    	log( "AVM1Object callProperty -> " + methodName );
	    	
            var fn:*;

            if( object.hasOwnProperty( methodName ) ) 
            {
            	fn = object[ methodName ];            	
            } 
            else if( object.hasOwnProperty( "__proto__" ) ) 
            {
                var proto:Object = object[ "__proto__" ]; 
            	
            	if( proto != null )
            	{ 
                    fn = object[ "__proto__" ][ methodName ];
                }
            }             
            
            if((fn != null) && (fn is Function))
            {
                return (fn as Function).apply( this, args );
            }
            else
            {
                if( methodName == "toString" )
                {
                    return "AVM1Object";
                }
            } 
            
            return undefined;            
        }

        override flash_proxy function getProperty(name:*):* 
        {
            log( "AVM1Object getProperty -> " + name );
            
            if( object.hasOwnProperty( name ) ) 
            {
            	return object[ name ];
            } 
            
            if( object.hasOwnProperty( "__proto__" ) ) 
            {
                var proto:Object = object[ "__proto__" ]; 
                
                if( proto != null )
                { 
                    return object[ "__proto__" ][ name ];
                }
            }             
            
            return undefined;
        }

        override flash_proxy function setProperty(name:*, value:*):void 
        {
            log( "AVM1Object setProperty -> " + name + " = " + value );
            
            object[ name ] = value;
        }
	
        override flash_proxy function deleteProperty(name:*):Boolean
        {
            log( "AVM1Object deleteProperty -> " + name );
            
            if( object.hasOwnProperty( name ) ) 
            {
                return delete object[ name ];
            } 
            
            if( object.hasOwnProperty( "__proto__" ) ) 
            {
            	var proto:Object = object[ "__proto__" ]; 
                
                if( proto != null )
                { 
                    return delete proto[ name ];
                }
            }             
        	
        	return false;
        }
        
        override flash_proxy function hasProperty(name:*):Boolean
        {
            log( "AVM1Object hasProperty -> " + name );
            
            if( object.hasOwnProperty( name ) ) return true;
            
            if( object.hasOwnProperty( "__proto__" ) ) 
            {
                var proto:Object = object[ "__proto__" ]; 
                
                if( proto != null )
                { 
                    return object[ "__proto__" ].hasOwnProperty( name );
                }
            }             
            
            return false;
        }
        
        override flash_proxy function nextName(index:int):String
        {
            log( "AVM1Object nextName -> " + index );
            
            return names[ index - 1 ];        	
        }
        
        override flash_proxy function nextNameIndex(index:int):int
        {
            log( "AVM1Object nextNameIndex -> " + index );
            
	        // initial call
	        if( index == 0 ) {
	            names   = [];
	            nameMap = {};
	            for( var name:* in object ) {
	                names.push( name );
	                nameMap[ name ] = true;
	            }
	            
	            if( object.hasOwnProperty( "__proto__" ) ) 
	            {
	                var proto:Object = object[ "__proto__" ]; 
	                
	                if( proto != null )
	                { 
		                for( var pname:* in object ) {
		                	if( nameMap[ pname ] == true ) continue; 
		                    names.push( pname );
		                }
	                }
	            }
	            
	            nameMap = undefined;
	        }
	     
	        if( index < names.length ) {
	            return index + 1;
            } else {
	            return 0;
	        }        	
        }
        
        override flash_proxy function nextValue( index:int ):*
        {
            log( "AVM1Object nextValue -> " + index );
            
            return flash_proxy::getProperty( names[ index - 1 ]);        	
        }
	}
}