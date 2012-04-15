/**
 * image2unisay — ponysay to unisay pony convertion tool
 *
 * Copyright © 2012  Mattias Andrée (maandree@kth.se)
 *
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
import javax.imageio.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.io.*;


/**
 * The main class of the img2unisay program
 *
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class img2unisay
{
    /**
     * Non-constructor
     */
    private img2unisay()
    {
	assert false : "This class [img2unisay] is not meant to be instansiated.";
    }
    
    
    
    private static final ArrayList<int[]> pre = new ArrayList<int[]>();
    private static int[] prebuf = null;
    private static int preptr = 0;
    
    
    
    /**
     * This is the main entry point of the program
     * 
     * @param  args  Startup arguments, start the program with </code>--help</code> for details
     * 
     * @throws  IOException  On I/O exception
     */
    public static void main(final String... args) throws IOException
    {
	if (args.length != 1)
	{
	    System.out.println("Image to unisay convertion tool");
	    System.out.println();
	    System.out.println("USAGE:  image2unisay SOURCE > TARGET");
	    System.out.println();
	    System.out.println("Source:          Image file");
	    System.out.println("Target (STDOUT): File name for new unisay pony");
	    System.out.println();
	    System.out.println("Known supported input formats:");
	    System.out.println("  ⋅  PNG  (non-animated)");
	    System.out.println("  ⋅  GIF  (first frame)");
	    System.out.println();
	    System.out.println();
	    System.out.println("Copyright (C) 2012  Mattias Andrée <maandree@kth.se>");
	    System.out.println();
	    System.out.println("This program is free software: you can redistribute it and/or modify");
	    System.out.println("it under the terms of the GNU General Public License as published by");
	    System.out.println("the Free Software Foundation, either version 3 of the License, or");
	    System.out.println("(at your option) any later version.");
	    System.out.println();
	    System.out.println("This program is distributed in the hope that it will be useful,");
	    System.out.println("but WITHOUT ANY WARRANTY; without even the implied warranty of");
	    System.out.println("MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the");
	    System.out.println("GNU General Public License for more details.");
	    System.out.println();
	    System.out.println("You should have received a copy of the GNU General Public License");
	    System.out.println("along with this library.  If not, see <http://www.gnu.org/licenses/>.");
	    System.out.println();
	    System.out.println();
	    return;
	}
	
	final PrintStream out = System.out;
	final BufferedImage img = ImageIO.read(new File(args[0]));
	
	int w = img.getWidth();
	int h = img.getHeight();
	
	int maxx = 0;
	int minx = w;
	
	int[][] pony = new int[h + 1][w];
	final int[] emptyset = new int[w];
	for (int x = 0; x < w; x++)
	    emptyset[x] = -1;

	for (int y = 0; y < h; y++)
	{
	    boolean empty = true;
	    for (int x = 0; x < w; x++)
	    {
		final int argb = img.getRGB(x, y);
		int a = (argb >> 24) & 0xFF;
		int r = (argb >> 16) & 0xFF;
		int g = (argb >>  8) & 0xFF;
		int b =  argb        & 0xFF;
		
		if ((0 < a) && (a < 255))
		{
		    r = r * a / 255 + 255 - a;
		    g = g * a / 255 + 255 - a;
		    b = b * a / 255 + 255 - a;
		}
		
		if (a != 0)
		{
		    pony[y][x] = (new Colour((byte)r, (byte)g, (byte)b)).index;
		    empty = false;
		    if (maxx < x)  maxx = x;
		    if (minx > x)  minx = x;
		}
		else
		    pony[y][x] = -1;
	    }
	    if (empty)
		pony[y] = null;
	}
	
	int yoff = 0;
	while (pony[yoff] == null)
	    yoff++;
	
	for (int y = yoff; y < h; y++)
	    pony[y - yoff] = pony[y];
	
	h -= yoff;
	
	while (pony[h - 1] == null)
	    h--;
	
	for (int y = 0; y < h; y++)
	    if (pony[y] == null)
		pony[y] = emptyset;
	
	pony[h] = emptyset;
	h++;
	
	int fore = -1;
	int back = -1;
	
	minx = (minx -= 1) < 0 ? 0 : minx;
	
	int bw = 0;
	String offl = new String();
	for (int x = minx; x <= maxx; x++)
	{
	    if (pony[0][x] >= 0)
		break;
	    if (x - minx > 3)
	    {
		offl += ' ';
		bw++;
	    }
	}
	
	System.out.println("$baloon" + (bw + 3) +  "$\033[0m");
	System.out.println(offl + "$\\$");
	System.out.println(offl + " $\\$");
	System.out.println(offl + "  $\\$");
	
	for (int y = 0; y < h; y += 2)
	{
	    for (int x = minx; x <= maxx; x++)
	    {
		final int upper = pony[y][x];
		final int lower = pony[y + 1][x];
		
		if ((upper < 0) && (lower < 0))
	        {
		    if (fore >= 0)  System.out.print("\033[39m");
		    if (back >= 0)  System.out.print("\033[49m");
		    fore = back = -1;
		    System.out.print(' ');
		}
		else if (upper < 0)
		{
		    if (back >= 0)  System.out.print("\033[49m");
		    back = -1;
		    if (fore != lower)
			System.out.print("\033[38;5;" + (fore = lower) + "m");
		    System.out.print('▄');
		}
		else if (lower < 0)
		{
		    if (back >= 0)  System.out.print("\033[49m");
		    back = -1;
		    if (fore != lower)
			System.out.print("\033[38;5;" + (fore = upper) + "m");
		    System.out.print('▀');
		}
		else if ((back == lower) || (fore == upper))
		{
		    if (fore != upper)  System.out.print("\033[38;5;" + (fore = upper) + "m");
		    if (back != lower)  System.out.print("\033[48;5;" + (back = lower) + "m");
		    System.out.print('▀');
		}
		else
		{
		    if (back != upper)  System.out.print("\033[48;5;" + (back = upper) + "m");
		    if (fore != lower)  System.out.print("\033[38;5;" + (fore = lower) + "m");
		    System.out.print('▄');
		}
	    }
	    fore = back = -1;
	    System.out.println("\033[0m");
	}
    }
    
}


//####################################################################################
//##  The following code is pasted from TWT, but the class is made package private  ##
//####################################################################################


/**
 * TWT — Terminal Window Toolkit, a free pure Java terminal toolkit.
 * Copyright (C) 2011 Mattias Andrée <maandree@kth.se>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


/**
 * Terminal colour class.
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
class Colour
{
    /**
     * Possible colour intensitivities on mixed colours
     */
    public static final int[] COLOUR_INTENSITIVITY = {0, 95, 135, 175, 215, 255};

    /**
     * Possible intensitivities on grey colours, excluding the mixed colours' intensitivities
     */
    public static final int[] GREY_EXTRA_INTENSITIVITY = {8, 18, 28, 38, 48, 58, 68, 78, 88, 98, 108, 118, 128,
                                                          138, 148, 158, 168, 178, 188, 198, 208, 218, 228, 238};

    /**
     * Possible intensitivities on mixed colours, including the mixed colours' intensitivities
     */
    public static final int[] GREY_FULL_INTENSITIVITY = {0, 8, 18, 28, 38, 48, 58, 68, 78, 88, 95, 98, 108, 118, 128, 135,
                                                         138, 148, 158, 168, 175, 178, 188, 198, 208, 218, 215, 228, 238, 255};



    /**
     * <p>Constructor</p>
     * <p>
     *     Selects the colour by index.
     * </p>
     *
     * @param  index  The colour's index [0–255].
     */
    @SuppressWarnings("hiding")
    public Colour(final byte index)
    {
        final int[] I = COLOUR_INTENSITIVITY;

        int i = index;  if (i < 0)  i += 1 << 8;

        if ((this.index = i) < 16)
        {
            this.systemColour = true;
            this.red   = new int[] {  0, 128,   0, 128,   0, 128,   0, 192,
                                    128, 255,   0, 255,   0, 255,   0, 255} [i];
            this.green = new int[] {  0,   0, 128, 128,   0,   0, 128, 192,
                                    128,   0, 255, 255,   0,   0, 255, 255} [i];
            this.blue  = new int[] {  0,   0,   0,   0, 128, 128, 128, 192,
                                    128,   0,   0,   0, 255, 255, 255, 255} [i];
        }
        else
        {
            this.systemColour = false;
            if (i < 232)
            {
                final int j = i - 16, b, g;
                this.blue  = b = j % 6;
                this.green = g = ((j - b) / 6) % 6;
                this.red   = (j - b - g * 6) / (6 * 6);
            }
            else
                this.red = this.green = this.blue = (index - 232) * 10 + 8;
        }
    }

    /**
     * <p>Constructor</p>
     * <p>
     *     Selects the colour the closest the a proper terminal colour.
     * </p>
     *
     * @param  red    The red   intensity [0–255].
     * @param  green  The green intensity [0–255].
     * @param  blue   The blue  intensity [0–255].
     */
    @SuppressWarnings("hiding")
    public Colour(final byte red, final byte green, final byte blue)
    {
        final int[] I = COLOUR_INTENSITIVITY;

        int r = red  ;  if (r < 0)  r += 1 << 8;
        int g = green;  if (g < 0)  g += 1 << 8;
        int b = blue ;  if (b < 0)  b += 1 << 8;

        int d, ð, dr, db, dg; dr = db = dg = 0;

        int ir = -1, ig = -1, ib = -1, ii = -1;

        d = 500; for (int cr : I) if (d > (ð = Math.abs(cr - r))) {d = ð; dr = cr; ir++;} else break;
        d = 500; for (int cg : I) if (d > (ð = Math.abs(cg - g))) {d = ð; dg = cg; ig++;} else break;
        d = 500; for (int cb : I) if (d > (ð = Math.abs(cb - b))) {d = ð; db = cb; ib++;} else break;

        d = (dr - r)*(dr - r) + (dg - g)*(dg - g) + (db - b)*(db - b);

        for (int gr = 8; gr <= 238; gr += 10)
        {
            int ðr = Math.abs(gr - r);
            int ðg = Math.abs(gr - g);
            int ðb = Math.abs(gr - b);

            ð = ðr*ðr + ðg*ðg + ðb*ðb;

            if (d > ð)
            {
                d = ð;
                dr = gr;
                dg = gr;
                db = gr;
                ii = (gr - 8) / 10;
            }
        }
        
        this.systemColour = false;
        this.red   = dr;
        this.green = dg;
        this.blue  = db;
        this.index = ii < 0 ? (16 + (ir * 6 + ig) * 6 + ib) : ii + 232;
    }

    /**
     * <p>Constructor</p>
     * <p>
     *     Selects the colour by index.
     * </p>
     *
     * @param  index  The colour's index [0–255].
     */
    @SuppressWarnings("hiding")
    public Colour(final int index)
    {
        this((byte)index);
    }

    /**
     * <p>Constructor</p>
     * <p>
     *     Selects the colour the closest the a proper terminal colour.
     * </p>
     *
     * @param  red    The red   intensity [0–255].
     * @param  green  The green intensity [0–255].
     * @param  blue   The blue  intensity [0–255].
     */
    @SuppressWarnings("hiding")
    public Colour(final int red, final int green, final int blue)
    {
        this((byte)red, (byte)green, (byte)blue);
    }



    /**
     * The red intensity [0–255].
     */
    public final int red;

    /**
     * The green intensity [0–255].
     */
    public final int green;

    /**
     * The blue intensity [0–255].
     */
    public final int blue;

    /**
     * The colour's index [0–255].
     */
    public final int index;

    /**
     * Whether the colour is a system colour.
     */
    public final boolean systemColour;



    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return this.index;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object o)
    {
        if ((o == null) || !(o instanceof Colour))
            return false;

        return ((Colour)o).index == this.index;
    }



    /**
     * System colour initialisation index counter.
     */
    private static byte sciic = 0;


    /**
     * System colour: black
     */
    public static final Colour SYSTEM_BLACK   = new Colour(sciic++);

    /**
     * System colour: medium red
     */
    public static final Colour SYSTEM_RED     = new Colour(sciic++);

    /**
     * System colour: medium green
     */
    public static final Colour SYSTEM_GREEN   = new Colour(sciic++);

    /**
     * System colour: medium yellow, dark orange or brown
     */
    public static final Colour SYSTEM_YELLOW  = new Colour(sciic++);

    /**
     * System colour: medium blue
     */
    public static final Colour SYSTEM_BLUE    = new Colour(sciic++);

    /**
     * System colour: medium magenta or medium lilac
     */
    public static final Colour SYSTEM_MAGENTA = new Colour(sciic++);

    /**
     * System colour: medium cyan or medium turquoise
     */
    public static final Colour SYSTEM_CYAN    = new Colour(sciic++);

    /**
     * System colour: dark grey
     */
    public static final Colour SYSTEM_GREY    = new Colour(sciic++);

    /**
     * System colour: light grey
     */
    public static final Colour SYSTEM_INTENSIVE_BLACK   = new Colour(sciic++);

    /**
     * System colour: light red
     */
    public static final Colour SYSTEM_INTENSIVE_RED     = new Colour(sciic++);

    /**
     * System colour: light green
     */
    public static final Colour SYSTEM_INTENSIVE_GREEN   = new Colour(sciic++);

    /**
     * System colour: light yellow or medium orange
     */
    public static final Colour SYSTEM_INTENSIVE_YELLOW  = new Colour(sciic++);

    /**
     * System colour: light blue
     */
    public static final Colour SYSTEM_INTENSIVE_BLUE    = new Colour(sciic++);

    /**
     * System colour: light magenta or light lilac
     */
    public static final Colour SYSTEM_INTENSIVE_MAGENTA = new Colour(sciic++);

    /**
     * System colour: light cyan or light turquoise
     */
    public static final Colour SYSTEM_INTENSIVE_CYAN    = new Colour(sciic++);

    /**
     * System colour: white
     */
    public static final Colour SYSTEM_INTENSIVE_GREY    = new Colour(sciic++);


    /**
     * System independent colour: pitch black
     */
    public static final Colour PURE_BLACK   = new Colour(0, 0, 0);

    /**
     * System independent colour: medium red
     */
    public static final Colour PURE_RED     = new Colour(175, 0, 0);

    /**
     * System independent colour: medium green
     */
    public static final Colour PURE_GREEN   = new Colour(0, 175, 0);

    /**
     * System independent colour: medium yellow
     */
    public static final Colour PURE_YELLOW  = new Colour(175, 175, 0);

    /**
     * System independent colour: medium blue
     */
    public static final Colour PURE_BLUE    = new Colour(0, 0, 175);

    /**
     * System independent colour: medium magenta
     */
    public static final Colour PURE_MAGENTA = new Colour(175, 0, 175);

    /**
     * System independent colour: medium cyan
     */
    public static final Colour PURE_CYAN    = new Colour(0, 175, 175);

    /**
     * System independent colour: dark grey
     */
    public static final Colour PURE_GREY    = new Colour(198, 192, 192);

    /**
     * System independent colour: light grey
     */
    public static final Colour PURE_INTENSIVE_BLACK   = new Colour(127, 128, 128);

    /**
     * System independent colour: light red
     */
    public static final Colour PURE_INTENSIVE_RED     = new Colour(255, 0, 0);

    /**
     * System independent colour: light green
     */
    public static final Colour PURE_INTENSIVE_GREEN   = new Colour(0, 255, 0);

    /**
     * System independent colour: light yellow
     */
    public static final Colour PURE_INTENSIVE_YELLOW  = new Colour(255, 255, 0);

    /**
     * System independent colour: light blue
     */
    public static final Colour PURE_INTENSIVE_BLUE    = new Colour(0, 0, 255);

    /**
     * System independent colour: light magenta
     */
    public static final Colour PURE_INTENSIVE_MAGENTA = new Colour(255, 0, 255);

    /**
     * System independent colour: light cyan
     */
    public static final Colour PURE_INTENSIVE_CYAN    = new Colour(0, 255, 255);

    /**
     * System independent colour: pure white
     */
    public static final Colour PURE_INTENSIVE_GREY    = new Colour(0, 255, 255);

}

