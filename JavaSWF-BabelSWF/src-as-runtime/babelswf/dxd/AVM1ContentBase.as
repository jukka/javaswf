/**
 * Bridge between AVM1 and AVM2 ContentBases
 */
package babelswf.dxd
{
	import com.disney.gem.panels.ContentBase;
	import flash.display.MovieClip;
	import com.disney.gem.panels.Panel;

	public class AVM1ContentBase extends ContentBase
	{
		
		
		public function AVM1ContentBase( pContentClip:MovieClip, pOwner:Panel, pConfig:Object )
		{
			super(pContentClip, pOwner, pConfig);
		}		
	}
}