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
package com.anotherbigidea.flash.interfaces;

import java.io.*;
import com.anotherbigidea.flash.structs.*;

/**
 * Interface for passing shape style information in addition to the basic
 * vectors.
 */
public interface SWFShape extends SWFVectors
{
    public void setFillStyle0( int styleIndex ) throws IOException;
    
    public void setFillStyle1( int styleIndex ) throws IOException;
    
    public void setLineStyle( int styleIndex ) throws IOException;

    /**
     * Solid color fill
     */
    public void defineFillStyle( Color color ) throws IOException;

    /**
     * Gradient fill - linear or radial.
     * @param colors may have null elements - these (and the corresponding ratio) 
     *               should be ignored
     */
    public void defineFillStyle( Matrix matrix, int[] ratios,
                                 Color[] colors, boolean radial )
        throws IOException;

    /**
     * Bitmap fill - tiled or clipped
     */
    public void defineFillStyle( int bitmapId, Matrix matrix, boolean clipped, boolean smoothed )
        throws IOException;
    
    public void defineLineStyle( int width, Color color ) throws IOException;
}
