/**
 * ponyeditor — pony editor tool for unisay
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
package se.kth.maandree.utilunisay;

import java.util.*;
import java.io.*;


/**
 * The main class of the ponyeditor program
 *
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class ponyeditor
{
    /**
     * Non-constructor
     */
    private ponyeditor()
    {
	assert false : "This class [ponyeditor] is not meant to be instansiated.";
    }
    
    
    
    /**
     * This is the main entry point of the program
     * 
     * @param  args  Startup arguments, start the program with </code>--help</code> for details
     * 
     * @throws  IOException  On I/O exception
     */
    public static void main(final String... args) throws IOException
    {
	if (args.length != 2)
	{
	    System.out.println("Pony editor tool");
	    System.out.println();
	    System.out.println("USAGE:  ponyeditor SOURCE TARGET");
	    System.out.println();
	    System.out.println("Source: Original pony");
	    System.out.println("Target: Where the new pony should be stored");
	    System.out.println();
	    System.out.println("KEYBOARD COMMANDS:");
	    System.out.println();
	    System.out.println("        \\    Override with \\ directional baloon link");
	    System.out.println( "        /    Override with / directional baloon link");
	    System.out.println( "    space    Override with colour");
	    System.out.println( "    enter    Insert new row");
	    System.out.println( "   delete    Delete row");
	    System.out.println( "       ^H    Delete cell");
	    System.out.println( "        +    Insert new cell");
	    System.out.println( "        b    Insert baloon");
	    System.out.println( "        e    Remove one row from the baloon");
	    System.out.println( "        d    Add one row to the baloon");
	    System.out.println( "        a    Move left edge to one step to the left");
	    System.out.println( "        s    Move left edge to one step to the right");
	    System.out.println( "        f    Move right edge to one step to the left");
	    System.out.println( "        g    Move right edge to one step to the right");
	    System.out.println( "        o    Save the pony");
	    System.out.println( "        q    Save the pony and quit");
	    System.out.println( "        p    Preview pony (and exit preview)");
	    System.out.println( "     left    Move the cursor one step to the left");
	    System.out.println( "    right    Move the cursor one step to the right");
	    System.out.println( "       up    Move the cursor one step up");
	    System.out.println( "     down    Move the cursor one step down");
	    System.out.println( "   S-left    Move the view one step to the left");
	    System.out.println( "  S-right    Move the view one step to the right");
	    System.out.println( "     S-up    Move the view one step up");
	    System.out.println( "   S-down    Move the view one step down");
	    System.out.println( "        0    Reset view");
	    System.out.println();
	    System.out.println("     Note that a baloon link has double height, and the cursor");
	    System.out.println("     that is not on a link marks the upper part");
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

    }
    
}
