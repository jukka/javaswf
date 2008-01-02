/**
 * The BabelSWF runtime
 */
package babelswf
{
	public class Runtime
	{
        /** The _global object */
        public static var instance:Global = new Global();

        /** The trace function */
        public static function actionTrace( a:* ):void {
        	trace( "BabelSWF >>> actionTrace" );
        	trace( a );
        }

        /** The extends action */
        public static function actionExtends( subclass:Object, superclass:Object ):void {
            trace( "BabelSWF >>> actionExtends" );
            
            //FIXME
        }

        /** The get-variable action */
        public static function actionGetVariable( name:String ):* {
            trace( "BabelSWF >>> actionGetVariable : " + name );
            
            //FIXME
            
            return undefined;
        }

        /** The set-member action */
        public static function actionSetMember( obj:Object, name:String, value:* ):void {
            trace( "BabelSWF >>> actionSetMember : " + name );
            
            //FIXME
        }

        /** The get-member action */
        public static function actionGetMember( obj:Object, name:String ):* {
            trace( "BabelSWF >>> actionGetMember : " + name );
            
            //FIXME
            
            return undefined;
        }

        /** The stop action */
        public static function actionStop():void {        	
            trace( "BabelSWF >>> actionStop" );

        	//FIXME
        }

        /** The play action */
        public static function actionPlay():void {
            trace( "BabelSWF >>> actionPlay" );

            //FIXME
        }

		public function Runtime()
		{
			super();			
		}
	}
}