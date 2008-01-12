/**
 * Represents the execution context for any translated AVM1 code. 
 */ 
package babelswf
{
	public class AVM1ExecutionContext
	{
		public function AVM1ExecutionContext()
		{
			super();
		}

        /**
         * Get a variable
         */
        public function avm1_getVar( name:* ):* 
        {
        	//TODO
        	var value:* = undefined;
        	trace( "avm1_getVar >> " + name + " --> " + value );
        	return value;
        }

        /**
         * Get a variable
         */
        public function avm1_setVar( name:*, value:* ):void
        {
            //TODO
            trace( "avm1_setVar >> " + name + " = " + value );
        }

        /**
         * Delete a variable
         */
        public function avm1_deleteVar( name:* ):Boolean
        {
            //TODO
            trace( "avm1_deleteVar >> " + name );
            
            return false;
        }

        /**
         * Define a local variable
         */
        public function avm1_defineVar( name:* ):void 
        {
            //TODO
            var value:* = undefined;
            trace( "avm1_defineVar >> " + name );
        }

        /**
         * Define a local variable with a value
         */
        public function avm1_defineVarValue( name:*, value:* ):void
        {
            //TODO
            trace( "avm1_defineVarValue >> " + name + " = " + value );
        }
        
        /**
         * Delete a member
         */
        public function avm1_deleteMember( object:*, name:* ):Boolean
        {
            //TODO
            trace( "avm1_deleteMember >> " + name );
            
            return false;
        }
        
        /**
         * Get a member value
         */
        public function avm1_getMember( object:*, name:* ):*
        {
            //TODO
            var value:* = undefined;
            trace( "avm1_getMember >> " + object + "." + name + " --> " + value );
            return value;
        }
        
        /**
         * Set a member value
         */
        public function avm1_setMember( object:*, name:*, value:* ):void
        {
            //TODO
            trace( "avm1_setMember >> " + object + "." + name + " = " + value );
        }
        
        /**
         * Call a function 
         */
        public function avm1_callFunction( args:Array, name:* ):*
        {
        	//TODO
            trace( "avm1_callFunction >> " + name + " (" + args.length + " args)" );
            return undefined;     
        }      
        
        /**
         * Call a method 
         */
        public function avm1_callMethod( args:Array, object:*, name:* ):*
        {
            //TODO
            trace( "avm1_callMethod >> " + object + "." + name + " (" + args.length + " args)" );
            return undefined;     
        }              
        
        /**
         * Start a With block
         */ 
        public function avm1_startWith( object:* ):void
        {
            //TODO
            trace( "avm1_startWith >> " + object );        
        } 

        /**
         * End a With block
         */ 
        public function avm1_endWith():void
        {
            //TODO
            trace( "avm1_endWith << " );        
        } 
        
        /**
         * Implementation of the AVM1 trace instruction
         */
        public function avm1_trace( value:* ):void {
        	trace( "avm1 >> " + value ); 
        }
        
        /**
         * Dump an object
         */
        public function avm1_dump( value:* ):void {
        	trace( "avm1_dump ------------->> " + value );
        	
        	if( value === undefined ) return; 
        	
        	for( var name:* in value )
        	{
                trace( name + " = " + value[name] );
            }
             
            trace( "-------------<< avm1_dump" );
        }        
	}
}