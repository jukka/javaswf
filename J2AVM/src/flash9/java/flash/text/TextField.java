package flash.text;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class TextField extends flash.display.InteractiveObject   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public TextField(  ) { 
		super(  );
	}




    @SlotId( -1 ) 
    public native void replaceText( int arg1, int arg2, String arg3 );

    @SlotId( -1 ) @Getter
    public native int getMaxScrollH(  );

    @SlotId( -1 ) @Getter
    public native int getNumLines(  );

    @SlotId( -1 ) @Getter
    public native int getScrollH(  );

    @SlotId( -1 ) @Getter
    public native int getCaretIndex(  );

    @SlotId( -1 ) @Getter
    public native int getMaxScrollV(  );

    @SlotId( -1 ) 
    public native flash.display.DisplayObject getImageReference( String arg1 );

    @SlotId( -1 ) @Getter
    public native int getScrollV(  );

    @SlotId( -1 ) @Getter
    public native boolean getBorder(  );

    @SlotId( -1 ) @Getter
    public native String getText(  );

    @SlotId( -1 ) @Getter
    public native boolean getBackground(  );

    @SlotId( -1 ) 
    public native flash.geom.Rectangle getCharBoundaries( int arg1 );

    @SlotId( -1 ) @Setter
    public native void setBorderColor( int arg1 );

    @SlotId( -1 ) @Setter
    public native void setScrollH( int arg1 );

    @SlotId( -1 ) 
    public native int getFirstCharInParagraph( int arg1 );

    @SlotId( -1 ) @Getter
    public native String getType(  );

    @SlotId( -1 ) 
    public native void replaceSelectedText( String arg1 );

    @SlotId( -1 ) 
    public native String getRawText(  );

    @SlotId( -1 ) @Getter
    public native boolean getAlwaysShowSelection(  );

    @SlotId( -1 ) @Getter
    public native double getSharpness(  );

    @SlotId( -1 ) @Getter
    public native int getTextColor(  );

    @SlotId( -1 ) @Setter
    public native void setDefaultTextFormat( flash.text.TextFormat arg1 );

    @SlotId( -1 ) @Getter
    public native boolean getCondenseWhite(  );

    @SlotId( -1 ) @Getter
    public native String getAutoSize(  );

    @SlotId( -1 ) @Setter
    public native void setScrollV( int arg1 );

    @SlotId( -1 ) @Setter
    public native void setBorder( boolean arg1 );

    @SlotId( -1 ) @Getter
    public native flash.text.StyleSheet getStyleSheet(  );

    @SlotId( -1 ) @Setter
    public native void setBackground( boolean arg1 );

    @SlotId( -1 ) @Setter
    public native void setEmbedFonts( boolean arg1 );

    @SlotId( -1 ) @Getter
    public native boolean getDisplayAsPassword(  );

    @SlotId( -1 ) @Getter
    public native String getAntiAliasType(  );

    @SlotId( -1 ) @Setter
    public native void setMultiline( boolean arg1 );

    @SlotId( -1 ) @Getter
    public native int getSelectionEndIndex(  );

    @SlotId( -1 ) @Setter
    public native void setStyleSheet( flash.text.StyleSheet arg1 );

    @SlotId( -1 ) @Setter
    public native void setMouseWheelEnabled( boolean arg1 );

    @SlotId( -1 ) @Getter
    public native String getSelectedText(  );

    @SlotId( -1 ) @Getter
    public native double getThickness(  );

    @SlotId( -1 ) 
    public native int getLineIndexAtPoint( double arg1, double arg2 );

    @SlotId( -1 ) 
    public native void appendText( String arg1 );

    @SlotId( -1 ) @Getter
    public native int getSelectionBeginIndex(  );

    @SlotId( -1 ) @Setter
    public native void setTextColor( int arg1 );

    @SlotId( -1 ) @Setter
    public native void setText( String arg1 );

    @SlotId( -1 ) @Getter
    public native int getBottomScrollV(  );

    @SlotId( -1 ) @Getter
    public native String getHtmlText(  );

    @SlotId( -1 ) @Setter
    public native void setAlwaysShowSelection( boolean arg1 );

    @SlotId( -1 ) @Setter
    public native void setSharpness( double arg1 );

    @SlotId( -1 ) @Getter
    public native boolean getSelectable(  );

    @SlotId( -1 ) 
    public native int getLineIndexOfChar( int arg1 );

    @SlotId( -1 ) @Setter
    public native void setRestrict( String arg1 );

    @SlotId( -1 ) @Setter
    public native void setGridFitType( String arg1 );

    @SlotId( -1 ) 
    public native void setSelection( int arg1, int arg2 );

    @SlotId( -1 ) 
    public native flash.text.TextFormat getTextFormat( int arg1, int arg2 );

    @SlotId( -1 ) 
    public native flash.text.TextFormat getTextFormat( int arg1 );

    @SlotId( -1 ) 
    public native flash.text.TextFormat getTextFormat(  );

    @SlotId( -1 ) 
    public native void setTextFormat( flash.text.TextFormat arg1, int arg2, int arg3 );

    @SlotId( -1 ) 
    public native void setTextFormat( flash.text.TextFormat arg1, int arg2 );

    @SlotId( -1 ) 
    public native void setTextFormat( flash.text.TextFormat arg1 );

    @SlotId( -1 ) @Setter
    public native void setType( String arg1 );

    @SlotId( -1 ) @Getter
    public native int getBorderColor(  );

    @SlotId( -1 ) @Setter
    public native void setCondenseWhite( boolean arg1 );

    @SlotId( -1 ) @Getter
    public native double getTextWidth(  );

    @SlotId( -1 ) 
    public native flash.FlashArray getTextRuns( int arg1, int arg2 );

    @SlotId( -1 ) 
    public native flash.FlashArray getTextRuns( int arg1 );

    @SlotId( -1 ) 
    public native flash.FlashArray getTextRuns(  );

    @SlotId( -1 ) 
    public native int getLineOffset( int arg1 );

    @SlotId( -1 ) @Getter
    public native boolean getWordWrap(  );

    @SlotId( -1 ) @Getter
    public native boolean getUseRichTextClipboard(  );

    @SlotId( -1 ) @Setter
    public native void setAutoSize( String arg1 );

    @SlotId( -1 ) @Getter
    public native flash.text.TextFormat getDefaultTextFormat(  );

    @SlotId( -1 ) @Getter
    public native boolean getMultiline(  );

    @SlotId( -1 ) @Setter
    public native void setUseRichTextClipboard( boolean arg1 );

    @SlotId( -1 ) @Setter
    public native void setBackgroundColor( int arg1 );

    @SlotId( -1 ) @Getter
    public native boolean getEmbedFonts(  );

    @SlotId( -1 ) @Setter
    public native void setSelectable( boolean arg1 );

    @SlotId( -1 ) @Getter
    public native double getTextHeight(  );

    @SlotId( -1 ) 
    public native String getXMLText( int arg1, int arg2 );

    @SlotId( -1 ) 
    public native String getXMLText( int arg1 );

    @SlotId( -1 ) 
    public native String getXMLText(  );

    @SlotId( -1 ) @Setter
    public native void setDisplayAsPassword( boolean arg1 );

    @SlotId( -1 ) 
    public native String getLineText( int arg1 );

    @SlotId( -1 ) @Setter
    public native void setMaxChars( int arg1 );

    @SlotId( -1 ) @Getter
    public native boolean getMouseWheelEnabled(  );

    @SlotId( -1 ) @Getter
    public native String getRestrict(  );

    @SlotId( -1 ) @Getter
    public native String getGridFitType(  );

    @SlotId( -1 ) 
    public native int getParagraphLength( int arg1 );

    @SlotId( -1 ) @Setter
    public native void setAntiAliasType( String arg1 );

    @SlotId( -1 ) @Getter
    public native int getBackgroundColor(  );

    @SlotId( -1 ) 
    public native int getCharIndexAtPoint( double arg1, double arg2 );

    @SlotId( -1 ) @Getter
    public native int getMaxChars(  );

    @SlotId( -1 ) @Getter
    public native int getLength(  );

    @SlotId( -1 ) @Setter
    public native void setThickness( double arg1 );

    @SlotId( -1 ) 
    public native void insertXMLText( int arg1, int arg2, String arg3, boolean arg4 );

    @SlotId( -1 ) 
    public native void insertXMLText( int arg1, int arg2, String arg3 );

    @SlotId( -1 ) @Setter
    public native void setWordWrap( boolean arg1 );

    @SlotId( -1 ) @Setter
    public native void setHtmlText( String arg1 );

    @SlotId( -1 ) 
    public native flash.text.TextLineMetrics getLineMetrics( int arg1 );

    @SlotId( -1 ) 
    public native int getLineLength( int arg1 );

}
