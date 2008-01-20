/**
 * Represents an AVM1 function object
 */
package babelswf
{
	public dynamic class AVM1Function
	{
		/**
		 * The AVM2 function closure
		 */
		private var avm2Function:Function;
		
		/**
		 * The scope captured by the function closure 
		 */
		private var capturedScope:AVM1ExecutionContext;
		
		public function AVM1Function( avm2Function:Function, capturedScope:AVM1ExecutionContext )
		{
			this.avm2Function  = avm2Function;
			this.capturedScope = capturedScope;
			this.prototype     = { constructor: this };
		}
        
        /**
         * Set the prototype to the given object and return this function
         */ 
        public function setupPrototype( proto:Object ):AVM1Function 
        {
        	this.prototype = proto;
        	return this;
        } 
        
        /**
         * Create a delegate function closure for calling this function
         */ 
        public function delegate():Function
        {
        	var avm1func:AVM1Function = this;
        	
        	var delfunc:Function = function():*
        	{
        		trace( "AVM1Function >> incoming call via delegate" );
        		return avm1func.avm1_callMethod( this, arguments );
        	}
        	
        	delfunc["avm1function"] = this;
        	return delfunc;
        }
        
        /**
         * Call the function
         */
        public function avm1_call( callingContext:AVM1ExecutionContext, args:Array ):*
        {
            if( avm2Function == null ) return undefined;

            trace( "NEED TO RETHINK avm1_call" );   

            var context:AVM1ExecutionContext = 
                   new AVM1ExecutionContext( { "this":callingContext.avm1_getThis() }, capturedScope );   
         
            return avm2Function.apply( context, args );
        }

        /**
         * Call the function and a method
         */
        public function avm1_callMethod( object:*, args:Array ):*
        {
            if( avm2Function == null ) return undefined;

            trace( "NEED TO RETHINK avm1_callMethod" );   

            var context:AVM1ExecutionContext = 
                   new AVM1ExecutionContext( { "this": object }, capturedScope );   
         
            return avm2Function.apply( context, args );
        }
        
        /**
         * Call the function as a constructor
         */ 
        public function avm1_new( args:Array ):Object
        {
        	if( this.prototype !== undefined ) this.prototype["constructor"] = this; 
        	var instance:Object = { __proto__: this.prototype };
        	
            trace( "NEED TO RETHINK avm1_new" );
            
            if( avm2Function == null ) return instance;            
            
            var context:AVM1ExecutionContext = 
                   new AVM1ExecutionContext( { "this": instance }, capturedScope );   
         
            avm2Function.apply( context, args );
            return instance;
        }
	}
}