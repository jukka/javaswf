/**
 * Runtime utilities to support translated AVM1 code.
 */
package babelswf
{
    import babelswf.prototypes.PrototypeFunction;
    import babelswf.prototypes.PrototypeObject;
    
	public class AVM1Runtime
	{
		private var mainTimelineClass:Class = AVM1MainTimeline;
        private var spriteClass:Class       = AVM1Sprite;
		
		public static var avm1_global:AVM1Global = new AVM1Global();

        /**
         * Stack of movie clips to maintain the current context
         */ 
        private static var movieClipStack:Array = [];
					
		public function AVM1Runtime()
		{
			super();
		}
		
        private static function log( msg:String ):void
        {
            //trace( msg );
        }
		
		/**
		 * Make a new generic object
		 */
		public static function newObject():AVM1Object
		{
		    var obj:AVM1Object = PrototypeObject.instance.avm1_new();
		    //trace( "AVM1Runtime >>> newObject " + obj );
		    return obj;
		}
		
		/**
		 * Enter the context of a movie clip
		 */
		public static function pushMC( clip:AVM1MovieClip ):void
		{
			if( clip is AVM1MainTimeline )
			{
				avm1_global["_root"] = clip;
			} 
			
			avm1_global["_parent"] = clip.avm1_getParent();  			
			movieClipStack.unshift( clip );
		}

        /**
         * Exit the current mc context
         */
        public static function popMC():void
        {
            movieClipStack.shift(); 

        	if( movieClipStack.length > 0 ) 
        	{
                avm1_global["_parent"] = (movieClipStack[0] as AVM1MovieClip).avm1_getParent();    
        	} 
        	else 
        	{
	            avm1_global["_parent"] = undefined;        		
        	}
        } 
		
		/**
		 * The AVM1 trace operation
		 */
		public static function avm1_trace( msg:* ):void
		{
			trace( "AVM1---> " +  msg );
		}
		
        /**
         * The AVM1 play operation
         */
        public static function avm1_play():void
        {
            log( "AVM1Runtime >>> play" );
            (movieClipStack[0] as AVM1MovieClip).play();
        }

        /**
         * The AVM1 stop operation
         */
        public static function avm1_stop():void
        {
            log( "AVM1Runtime >>> stop" );
            (movieClipStack[0] as AVM1MovieClip).stop();
        }
		
		/**
		 * The AVM1 set-member operation
		 */
		public static function avm1_setMember( object:*, name:*, value:* ):void
		{
            log( "AVM1Runtime >>> set " + object + "[ " + name + " ] = " + value );
            if( object == null ) return;
            object[ name ] = value; 
		}

        /**
         * The AVM1 get-member operation
         */
        public static function avm1_getMember( object:*, name:* ):*
        {
        	if( object == null ) {
                log( "AVM1Runtime >>> get " + object + "[ " + name + " ] = undefined" );
        	    return undefined;
        	}

            var a:* = object[name];
            log( "AVM1Runtime >>> get " + object + "[ " + name + " ] = " + a );
        	return a; 
        }

        /**
         * The AVM1 delete-member operation
         */
        public static function avm1_delMember( object:*, name:* ):Boolean
        {
            log( "AVM1Runtime >>> delete " + object + "[ " + name + " ]" );
            if( object == null ) return false;
            return delete object[ name ]; 
        }
		
		/**
		 * Call a function
		 */
		public static function avm1_callFunction( args:Array, object:*, func:*, name:* ):*
		{
            log( "AVM1Runtime >>> call function " + object + "." + name + "( " + args.length + " args )" );
			if( func == null ) 
			{
			    log( "*** FUNCTION IS NULL ***" );
			    return undefined;
			}
			
			var f:Function = func;
			
			return f.apply( object, args.reverse() ); 
		} 
		
        /**
         * Call a method
         */
        public static function avm1_callMethod( args:Array, object:*, name:* ):*
        {
            log( "AVM1Runtime >>> call method " + object + "." + name + "( " + args.length + " args )" );
            if( object == null ) {
                log( "*** FUNCTION Object IS NULL ***" );
                return undefined;
            }
            
            var func:Function;
            var thisObj:*;
            
            if( name == null || (name as String).length == 0 ) 
            {
            	func = object;
            }
            else 
            {
            	func    = object[ name ];
            	thisObj = object;
            }
            
            if( func == null ) 
            {
                trace( "*** FUNCTION IS NULL ***" );
                return undefined;
            }
            
            return func.apply( thisObj, args.reverse() ); 
        } 
		
		/**
		 * Create a new activation object for a function
		 */
		public static function avm1_newActivation( thisObj:Object ):Object
		{
            log( "AVM1Runtime >>> newActivation this=" + thisObj );
            
            if( thisObj is AVM1MovieClip ) 
            {
                thisObj = (thisObj as AVM1MovieClip).avm1_proxy;
            } 
            return new AVM1Object( { "this" : thisObj, "super":thisObj["super"] } );			
		}
		
		/**
		 * The AVM1 end-drag operation
		 */
		public static function avm1_endDrag():void 
		{
            log( "AVM1Runtime >>> endDrag" );
			(movieClipStack[0] as AVM1MovieClip).stopDrag();
		}
		
        /**
         * The AVM1 start-drag operation
         */
        public static function avm1_startDrag():void 
        {
            log( "AVM1Runtime >>> startDrag" );
            (movieClipStack[0] as AVM1MovieClip).startDrag();
        }		
		
		/**
		 * Set up a new function
		 */
		public static function avm1_newFunction( f:Function ):Function 
		{
            log( "AVM1Runtime >>> newFunction" );
		    
		    f.prototype = AVM1Runtime.newObject();
		    f.prototype["constructor"] = f;
            f.prototype["__constructor__"] = f;
            f["__proto__"] = PrototypeFunction.instance.constructor.prototype;
		    
		    return f;
		}
		
		/**
		 * The AVM1 extends operation
		 */
		public static function avm1_extends( clazz:Function, superclass:Function ):void
		{
            log( "AVM1Runtime >>> extends" );
		    clazz.prototype["__proto__"] = superclass.prototype;
		    clazz.prototype["super"] = superclass;		    
		}
		
		/**
		 * The new-object constructor operation
		 */
		public static function avm1_newObject( args:Array, object:*, func:*, name:* ):Object
		{
            log( "AVM1Runtime >>> new(func) " + object + "." + name + "( " + args.length + " args )" );
            if( func == null ) return undefined;
            
            var f:Function = func;
            
            var newObj:Object = new AVM1Object({ "__proto__": f.prototype });
            f.apply( newObj, args.reverse() ); 
            return newObj;
		}
		

        /**
         * The new-method constructor operation
         */
        public static function avm1_newMethod( args:Array, object:*, name:* ):*
        {
            log( "AVM1Runtime >>> new(method) " + object + "." + name + "( " + args.length + " args )" );
            if( object == null ) return undefined;
            
            var func:Function;
            
            if( name == null || (name as String).length == 0 ) 
            {
                func = object;
            }
            else 
            {
                func = object[ name ];
            }
            
            if( func == null ) return undefined;
            
            var newObj:Object = new AVM1Object({ "__proto__": func.prototype });
            func.apply( newObj, args.reverse() ); 
            return newObj;
        } 		

	}
}