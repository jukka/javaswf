/****************************************************************
 * Copyright (c) 2001, David N. Main, All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the 
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain the above 
 * copyright notice, this list of conditions and the following 
 * disclaimer. 
 * 
 * 2. Redistributions in binary form must reproduce the above 
 * copyright notice, this list of conditions and the following 
 * disclaimer in the documentation and/or other materials 
 * provided with the distribution.
 * 
 * 3. The name of the author may not be used to endorse or 
 * promote products derived from this software without specific 
 * prior written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A 
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE 
 * AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ****************************************************************/
package com.anotherbigidea.flash.structs;

import java.io.*;

import org.epistem.io.OutStream;

import com.anotherbigidea.flash.SWFConstants;

public class FillStyle implements Style
{
    protected int      fillType;
    protected Color    color;
    protected Matrix   matrix;
    protected int[]    ratios;  //for gradient fill
    protected Color[]  colors;  //for gradient fill
    protected int      bitmapId;
    
    public int     getType()           { return fillType; }
    public Color   getSolidColor()     { return color; }
    public Matrix  getMatrix()         { return matrix; }
    public int     getImageId()        { return bitmapId; }
    public int[]   getGradientRatios() { return ratios; }
    public Color[] getGradientColors() { return colors; }
    
    /**
     * Solid color fill (alpha depends on the TagDefineShapeX tag used)
     */
    public FillStyle( Color solidColor )
    {
        fillType = SWFConstants.FILL_SOLID;
        color = solidColor;
    }

    /**
     * Linear/Radial Gradient Fill
     */
    public FillStyle( Matrix matrix, int[] ratios, 
                      Color[] colors, boolean radial )
    {
        this.matrix = matrix;
        this.ratios = ratios;
        this.colors = colors;
                
        fillType = radial ? SWFConstants.FILL_RADIAL_GRADIENT : 
                            SWFConstants.FILL_LINEAR_GRADIENT;
    }
        
    /**
     * Bitmap fill
     */
    public FillStyle( int bitmapId, Matrix matrix, boolean clipped, boolean smoothed )
    {
        this.matrix   = matrix;
        this.bitmapId = bitmapId;
                
        fillType = clipped ? SWFConstants.FILL_CLIPPED_BITMAP : 
                             SWFConstants.FILL_TILED_BITMAP;
        
        if     ( (!clipped) && (!smoothed) ) fillType = SWFConstants.FILL_NONSMOOTHED_TILED_BITMAP;
        else if( (!clipped) &&   smoothed  ) fillType = SWFConstants.FILL_TILED_BITMAP;
        else if(   clipped  && (!smoothed) ) fillType = SWFConstants.FILL_NONSMOOTHED_CLIPPED_BITMAP;
        else if(   clipped  &&   smoothed  ) fillType = SWFConstants.FILL_CLIPPED_BITMAP;
    }
            
    public void write( OutStream out, boolean hasAlpha ) throws IOException
    {                
        out.writeUI8( fillType );
        
        if( fillType == SWFConstants.FILL_SOLID )
        {
            if( hasAlpha ) color.writeWithAlpha( out );
            else           color.writeRGB( out );
        }
        else if( fillType == SWFConstants.FILL_LINEAR_GRADIENT 
              || fillType == SWFConstants.FILL_RADIAL_GRADIENT )
        {
            matrix.write( out );
                    
            int numRatios = ratios.length;
                           
            out.writeUI8( numRatios );
            
            for( int i = 0; i < numRatios; i++ )
            {
                if( colors[i] == null ) continue;
                
                out.writeUI8( ratios[i] );
                        
                if( hasAlpha ) colors[i].writeWithAlpha( out );
                else           colors[i].writeRGB( out );
            }    
        }
        else if( fillType == SWFConstants.FILL_TILED_BITMAP 
              || fillType == SWFConstants.FILL_CLIPPED_BITMAP )
        {
            out.writeUI16( bitmapId );
            matrix.write( out );
        }
    }      
    
        
    public static void writeMorphFillStyle( OutStream out,
                                            FillStyle startStyle, 
                                            FillStyle endStyle )
        throws IOException
    {
        int fillType = startStyle.fillType;
                
        out.writeUI8( fillType );
        
        if( fillType == SWFConstants.FILL_SOLID )
        {
            startStyle.color.writeWithAlpha( out );
            endStyle  .color.writeWithAlpha( out );                
        }
        else if( fillType == SWFConstants.FILL_LINEAR_GRADIENT 
              || fillType == SWFConstants.FILL_RADIAL_GRADIENT )
        {
            startStyle.matrix.write( out );
            endStyle  .matrix.write( out );

            int numRatios = startStyle.ratios.length;
            out.writeUI8( startStyle.ratios.length );
                            
            for( int i = 0; i < numRatios; i++ )
            {
                if( startStyle.colors[i] == null ||
                    endStyle.colors[i]   == null )  continue;
                
                out.writeUI8( startStyle.ratios[i] );
                startStyle.colors[i].writeWithAlpha( out );
                    
                out.writeUI8( endStyle.ratios[i] );
                endStyle.colors[i].writeWithAlpha( out );
            }            
        }
        else if( fillType == SWFConstants.FILL_TILED_BITMAP 
              || fillType == SWFConstants.FILL_CLIPPED_BITMAP )
        {
            int bitmapId = startStyle.bitmapId;
                             
            out.writeUI16( bitmapId );
                             
            startStyle.matrix.write( out );
            endStyle  .matrix.write( out );
        }                                        
    }    
}