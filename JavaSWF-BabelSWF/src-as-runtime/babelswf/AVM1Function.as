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
         * Call the function
         */
        public function avm1_call( args:Array ):*
        {
            if( avm2Function == null ) return undefined;

            trace( "NEED TO RETHINK avm1_call" );   

            var context:AVM1ExecutionContext = 
                   new AVM1ExecutionContext( {}, capturedScope );   
         
            return avm2Function.apply( context, args );
        }

        /**
         * Call the function and a method
         */
        public function avm1_calMethod( object:*, args:Array ):*
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
            
            var context:AVM1ExecutionContext = 
                   new AVM1ExecutionContext( { "this": instance }, capturedScope );   
         
            avm2Function.apply( context, args );
            return instance;
        }
	}
}