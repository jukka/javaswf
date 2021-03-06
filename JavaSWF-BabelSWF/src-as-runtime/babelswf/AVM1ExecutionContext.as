/**
 * Represents the execution context for any translated AVM1 code. 
 */ 
package babelswf
{
	import flash.display.MovieClip;
	
	public class AVM1ExecutionContext
	{
		/** The current AVM2 movieclip */
		private var movieclip:MovieClip;
		
		/** The parent context */
		private var parent:AVM1ExecutionContext;
		
		/** The current activation object */
		private var activation:Object;
		
		/** Debug mode flag */
 		private var debug:Boolean = true;
		
		public function AVM1ExecutionContext( activation :Object,
		                                      parent     :AVM1ExecutionContext,
		                                      movieclip  :MovieClip = null )
		{
			this.activation = activation;
			this.movieclip  = movieclip;
			this.parent     = parent; 
			
            activation["_global"] = AVM1Runtime.global;
		}

        /**
         * Get the "this" value
         */ 
        public function avm1_getThis():*
        {
        	return activation["this"];
        }

        /**
         * Get the "_parent" value
         */ 
        public function avm1_getParent():*
        {
            trace( "NEED TO IMPLEMENT getParent" );
            return activation["_parent"];
        }

        /**
         * Get the "_root" value
         */ 
        public function avm1_getRoot():*
        {
            trace( "NEED TO IMPLEMENT getRoot" );
            return activation["_root"];
        }

        /**
         * Get the "super" value
         */ 
        public function avm1_getSuper():*
        {
        	//TODO - revisit this .. how are super methods called ?
        	return activation["this"]["__proto__"]["__proto__"]["constructor"];
        }

        /**
         * Get the "arguments" value
         */ 
        public function avm1_getArguments():*
        {
            trace( "NEED TO IMPLEMENT getArguments" );
            return undefined;
        }

        /**
         * Get the "_global" value
         */ 
        public function avm1_getGlobal():*
        {
            return AVM1Runtime.global;
        }


        /**
         * Get a variable
         */
        public function avm1_getVar( name:* ):* 
        {
        	//TODO
        	var value:* = avm1_getMember( activation, name );
        	
        	if( value === undefined )
        	{ 
	        	if( parent != null )
	        	{
	        	    value = parent.avm1_getVar( name );	
	        	}
	        	else 
	        	{
	        		value = AVM1Runtime.global[name];
	        	}
	        }
        	
        	//if( debug ) 
        	trace( "avm1_getVar >> " + name + " --> " + value );
        	return value;

	        //FIXME: handle path prefixes on the variable name
        }

        /**
         * Get a variable
         */
        public function avm1_setVar( name:*, value:* ):void
        {
            //TODO
            //if( debug )
            trace( "avm1_setVar >> " + name + " = " + value );
            activation[name] = value;
                    
	        //FIXME: handle path prefixes on the variable names
        }

        /**
         * Delete a variable
         */
        public function avm1_deleteVar( name:* ):Boolean
        {
            //TODO
            if( debug ) trace( "avm1_deleteVar >> " + name );
            
            return false;
        }

        /**
         * Define a local variable
         */
        public function avm1_defineVar( name:* ):void 
        {
            //TODO
            if( debug ) trace( "avm1_defineVar >> " + name );
            activation[name] = null;
        }

        /**
         * Define a local variable with a value
         */
        public function avm1_defineVarValue( name:*, value:* ):void
        {
            //TODO
            if( debug ) trace( "avm1_defineVarValue >> " + name + " = " + value );
            activation[name] = value;
        }
        
        /**
         * Delete a member
         */
        public function avm1_deleteMember( object:*, name:* ):Boolean
        {
            //TODO
            if( debug ) trace( "avm1_deleteMember >> " + name );
            
            return false;
        }
        
        /**
         * Get a member value
         */
        public function avm1_getMember( object:*, name:* ):*
        {
            //TODO
            var value:*;
            
            if( object != null ) {
            	 if( (object as Object).hasOwnProperty( name )) {
            	 	 value = object[name];
            	 } 
            	 else
            	 {
            	 	 value = avm1_getMember( object.__proto__, name );
            	 }
            } 
            
            //if( debug ) 
            trace( "avm1_getMember >> " + object + "." + name + " --> " + value );
            return value;
        }
        
        /**
         * Set a member value
         */
        public function avm1_setMember( object:*, name:*, value:* ):void
        {
            //TODO
            //if( debug ) 
            trace( "avm1_setMember >> " + object + "." + name + " = " + value );
            if( object == null ) return; 
            object[name] = value;
        }
        
        /**
         * Call a function 
         */
        public function avm1_callFunction( args:Array, name:* ):*
        {
        	//TODO
            //if( debug ) 
            trace( "avm1_callFunction >> " + name + " (" + args.length + " args)" );
            
            var fobj:* = avm1_getVar( name );
            if( fobj == null ) return undefined;
             
            var func:AVM1Function = fobj as AVM1Function;
            return func.avm1_call( this, args.reverse() );
        }      
        
        /**
         * Call a method 
         */
        public function avm1_callMethod( args:Array, object:*, name:* ):*
        {
            //TODO
            //if( debug ) 
            trace( "avm1_callMethod >> " + object + "." + name + " (" + args.length + " args)" );

            if( object == null ) return undefined; 
            
            if( name == null ) {
                var anonfunc:AVM1Function = object as AVM1Function;
                return anonfunc.avm1_call( this, args.reverse() );	   
            } 
            
            var fobj:* = avm1_getMember( object, name );
            if( fobj == null ) return undefined;
             
            var func:AVM1Function = fobj as AVM1Function;
            return func.avm1_callMethod( object, args.reverse() );
        }              
        
        /**
         * Start a With block
         */ 
        public function avm1_startWith( object:* ):void
        {
            //TODO
            if( debug ) trace( "avm1_startWith >> " + object );        
        } 

        /**
         * End a With block
         */ 
        public function avm1_endWith():void
        {
            //TODO
            if( debug ) trace( "avm1_endWith << " );        
        } 
        
        /**
         * Implementation of the AVM1 trace instruction
         */
        public function avm1_trace( value:* ):void {
        	trace( "avm1 >> " + value ); 
        }
        
        /**
         * Start playing the current movie/clip
         */
        public function avm1_play():void {
        	if( movieclip != null )
        	{ 
	            if( debug ) trace( "avm1_play" );        
	        	movieclip.play();
	        	return;
	        }
	        
	        if( parent != null ) {
	        	parent.avm1_play();
	        }
        }

        /**
         * Stop playing the current movie/clip
         */
        public function avm1_stop():void {
        	if( movieclip != null ) 
        	{
	            if( debug ) trace( "avm1_stop" );        
	            movieclip.stop();
            	return;
        	} 
        	
        	if( parent != null )
        	{
        		parent.avm1_stop();
        	} 
        }
        
        /**
         * Stop the current drag operation
         */
        public function avm1_endDrag():void {
        	if( movieclip != null ) 
        	{ 
               if( debug ) trace( "avm1_endDrag" );  
        	   movieclip.stopDrag();	
        	   return;
        	}
        	
            if( parent != null )
            {
            	parent.avm1_endDrag();            	
            }                   
        }        
        
        /**
         * Create a new AVM1 function that captures this scope
         */ 
        public function avm1_newFunction( body:Function ):AVM1Function
        {
            //if( debug ) 
            trace( "avm1_newFunction" );        
        	return new AVM1Function( body, this );
        }

        /**
         * Create a new AVM1 function that captures this scope
         */ 
        public function avm1_defineFunction( name:String, body:Function ):void
        {
            //if( debug ) 
            trace( "avm1_defineFunction >> " + name );  
            var func:AVM1Function = new AVM1Function( body, this );
            
            trace( "activation[this]-->" + activation["this"] );
            
            activation["this"][name] = func;
        }
        
        /**
         * Create a new object from a named constructor function 
         */
        public function avm1_newObject( args:Array, name:* ):*
        {
            //TODO
            //if( debug ) 
            trace( "avm1_newObject >> " + name + " (" + args.length + " args)" );
            
            var fobj:* = avm1_getVar( name );
            if( fobj == null ) return undefined;
             
            var func:AVM1Function = fobj as AVM1Function;
            return func.avm1_new( args.reverse() );
        } 

       /**
         * Create a new object from a constructor method 
         */
        public function avm1_newMethod( args:Array, object:*, name:* ):*
        {
            //TODO
            //if( debug ) 
            trace( "avm1_newMethod >> " + object + "." + name + " (" + args.length + " args)" );

            if( object == null ) return undefined; 
            
            if( name == null ) {
                var anonfunc:AVM1Function = object as AVM1Function;
                return anonfunc.avm1_new( args.reverse() );       
            } 
            
            var fobj:* = avm1_getMember( object, name );
            if( fobj == null ) return undefined;
             
            var func:AVM1Function = fobj as AVM1Function;
            return func.avm1_new( args.reverse() );
        }  
        
        /**
         * Create an inheritance relationship between two classes
         */ 
        public function avm1_extends( subclass:AVM1Function, superclass:AVM1Function ):void
        {
            //if( debug ) 
            trace( "avm1_extends >> " + subclass + " extends " + superclass );
            	
            var newProto:Object = AVM1Runtime.global.Object.avm1_new([]);
            newProto["__proto__"  ] = superclass["prototype"];
            newProto["constructor"] = subclass; 	
            subclass.setupPrototype( newProto );
        }
        
        /**
         * Trace out the vars in this context
         */ 
        public function dumpContext():void
        {
            trace( "dumpContext >>>>>>>>>>>>>>>>>>>>>>" );
            for( var n:String in activation )
            {
                trace( "> activation[" + n + "] = " + activation[n] );
            }
            
            if( activation.hasOwnProperty( "__proto__" ))
            {
	            for( var n2:String in activation["__proto__"] )
	            {
	                trace( "> __proto__ [" + n2 + "] = " + activation["__proto__"][n2] );
	            }            	
            }
            	
            trace( "<<<<<<<<<<<<<<<<<<<<<< dumpContext" );  
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