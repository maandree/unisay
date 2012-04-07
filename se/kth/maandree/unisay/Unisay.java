/**
 * Unisay — cowsay+ponysay rewritten in Java, with added features and full Unicode(!) support
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
package se.kth.maandree.unisay;

import java.util.*;
import java.io.*;


/**
 * The main class of the unisay program
 *
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class Unisay
{
    /**
     * Non-constructor
     */
    private Unisay()
    {
	assert false : "This class [Unisay] is not meant to be instansiated.";
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
	boolean help = false, anyarg = false;
	boolean random = false, format = false, dash = false;
	boolean say = false, icp = false, fcp = false, ocp = false;
	for (final String arg : args)
	    if (Util.equalsAny(arg, "--help", "-h"))
		help = true;
	    else if (Util.equalsAny(arg, "--"))
		anyarg = dash = true;
	    else if (Util.equalsAny(arg, "--random", "-r"))
		anyarg = random = true;
	    else if (Util.equalsAny(arg, "--format", "-p"))
		anyarg = format = true;
	    else if (Util.equalsAny(arg, "--say", "-s"))
		anyarg = say = true;
	    else if (Util.equalsAny(arg, "--in-encoding", "--icp", "--ie", "-i"))
		anyarg = icp = true;
	    else if (Util.equalsAny(arg, "--file-encoding", "--fcp", "--fe", "-f"))
		anyarg = fcp = true;
	    else if (Util.equalsAny(arg, "--out-encoding", "--ocp", "--oe", "-o"))
		anyarg = ocp = true;
	    
	final boolean allargs = !anyarg;
	
	if (help)
	{
	    System.out.println("unisay is a message and fortune displaying program inspired heavily by");
	    System.out.println("cowsay, and comes with adapted images from cowsay and ponysay; the later");
	    System.out.println("uses the former.  The most importent thing about unisay, beside that it");
	    System.out.println("comes with the 'My Little Ponies' from ponysay is that it supports UCS");
	    System.out.println("(\"Unicode\") fully in all aspects (except you may encounter some problems");
	    System.out.println("with high-plane characters if used on startup arguments or file names)");
	    System.out.println("trough UTF-8 and other UTF, which was an issue with cowsay and ponysay;");
	    System.out.println("but it also includes some sugar.");
	    System.out.println();
	    System.out.println();
	    System.out.println("USAGE:");
	    System.out.print("\n\n");
	    System.out.println("  unisay [-pifos <FILE> <CP> <CP> <CP> <TEXT>] (-r | [--] <FILES...>)");
	    System.out.println();
	    System.out.println("  -p <FILE>, -i <CP>, -f <CP>, -o <CP> and -s <TEXT>");
	    System.out.println("  are mutally independent and may be included as you see fit.");
	    System.out.println();
	    System.out.println("  <FILES...> are whitespace separated files with ponys (or whatever),");
	    System.out.println("  that are used printed in the output.");
	    System.out.print("\n\n");
	    System.out.println("OPTIONS:");
		
	    if (dash || allargs)
	    {
		System.out.print("\n\n");
		System.out.println("  --            Disables parsing any following argument as an option.");
	    }
	    if (help || allargs)
	    {
		System.out.print("\n\n");
		System.out.println("  --help");
		System.out.println("  -h            Prints a this help message, you get less (than all),");
		System.out.println("                options included in the message by adding the options");
		System.out.println("                you want information about.");
	    }
	    if (format || allargs)
	    {
		System.out.print("\n\n");
		System.out.println("  --format <FILE>");
		System.out.println("  -p <FILE>     Specify the file you want to use thay provides an");
		System.out.println("                alternative style for the baloon.");
		System.out.println("                You may add this option multiple times if you");
		System.out.println("                want one to be picked randomly.");
	    }
	    if (random || allargs)
	    {
		System.out.print("\n\n");
		System.out.println("  --random");
		System.out.println("  -r");
		System.out.println("                Use a random, instead of the user's default, pony.");
		System.out.println("                This options is implied by specifying pony files.");
	    }
	    if (say || allargs)
	    {
		System.out.print("\n\n");
		System.out.println("  --say <TEXT>");
		System.out.println("  -s <TEXT>     Specifies what the pony should say or think.");
		System.out.println("                You may instead feed the text to stdin.  You");
		System.out.println("                may (if your system allows) install fortune");
		System.out.println("                (or fortune-mod) and, for a random qoute, run");
		System.out.println("                    unisay --say `fortune`");
		System.out.println("                    unisay --say $(fortune)");
		System.out.println("                    fortune | unisay");
		System.out.println("                You may add this option multiple times if you");
		System.out.println("                want one to be picked randomly.");
	    }
	    if (icp || allargs)
	    {
		System.out.print("\n\n");
		System.out.println("  --in-encoding <CP>");
		System.out.println("  --icp <CP>");
		System.out.println("  --ie <CP>");
		System.out.println("  -i <CP>      Specifies the encoding for the input from stdin.");
		System.out.println("               UTF-8 is default.");
		System.out.println("               <<NOT IMPLEMENTED>>");
	    }
	    if (fcp || allargs)
	    {
		System.out.print("\n\n");
		System.out.println("  --file-encoding <CP>");
		System.out.println("  --fcp <CP>");
		System.out.println("  --fe <CP>");
		System.out.println("  -f <CP>      Specifies the encoding of files.");
		System.out.println("               UTF-8 is default.");
		System.out.println("               <<NOT IMPLEMENTED>>");
	    }
	    if (ocp || allargs)
	    {
		System.out.print("\n\n");
		System.out.println("  --out-encoding <CP>");
		System.out.println("  --ocp <CP>");
		System.out.println("  --oe <CP>");
		System.out.println("  -o <CP>      Specifies the encoding of the output from Unisay.");
		System.out.println("               UTF-8 is default.");
		System.out.println("               <<NOT IMPLEMENTED>>");
	    }
	    
	    
	    if (format || allargs)
	    {
		System.out.print("\n\n");
		System.out.println("BALOON STYLE FILE FORMAT:");
		System.out.print("\n\n");
		System.out.println("  Baloon style files must include ten lines, each starting");
		System.out.println("  start (uniquely) with one of the following exact beginnings:");
		System.out.println("      nw:       The upper left corner of the baloon");
		System.out.println("      n:        The upper edge of the baloon");
		System.out.println("      ne:       The upper right corner of the baloon");
		System.out.println("      e:        The right edge of the baloon");
		System.out.println("      se:       The lower right corner of the baloon");
		System.out.println("      s:        The lower edge of the baloon");
		System.out.println("      sw:       The lower left corner of the baloon");
		System.out.println("      w:        The left edge of the baloon");
		System.out.println("      \\:        Link line between the baloon and the pony. (\\ direction)");
		System.out.println("      /:        Link line between the baloon and the pony. (/ direction)");
		//I have decided not to adopt cowsays depenceny of the number of lines in the baloon.
		System.out.println("  Note that the are no spaces; the text followed by the colon on");
		System.out.println("  such a line sets the attribute's value. Hashes (#) by be used");
		System.out.println("  as the first character on a line to make it a comment. e:, w:,");
		System.out.println("  \\: and /: must be single lines; nw, n and ne must, however,");
		System.out.println("  just consist of the same number of lines, same thing goes for");
		System.out.println("  se, s and sw; additional lines must start with just a colon.");
		    
		    
		System.out.print("\n\n");
		System.out.println("THE IMAGE FILE FORMAT:");
		System.out.print("\n\n");
		System.out.println("  The image files are text files, as the output of the program");
		System.out.println("  is text for terminals. ANSI esacape sequences in the text are");
		System.out.println("  allowed and by be used drawing new ponies.");
		System.out.println("  The image files describes the entire output of the program,");
		System.out.println("  except the ballon's and its link's style and the message.");
		System.out.println("  Anything written in the file will be printed, except things");
		System.out.println("  surrounded by dollar signs ($), two dollar signes (nothing");
		System.out.println("  surrounded by dollar sign, i.e. $$, produces one dollar sign.");
		System.out.println("  additionally, $\\$ produces the \\ directional link line character");
		System.out.println("  sequence between the ballon and the pony, $/$ produces the /");
		System.out.println("  directional version of this, while $baloon#$ produces the baloon");
		System.out.println("  with the message inside it, the hash (#) must be either removed");
		System.out.println("  or replaced by an integer specifying the minimum allowed width of");
		System.out.println("  the entire ballon. You may add ,# to the end of the tag (before");
		System.out.println("  the second $) where # is an integer specifying the minimum allowed");
		System.out.println("  height of the baloon, this option is independent of the width");
		System.out.println("  option, and may create additional line in the end of the output,");
		System.out.println("  the baloon will then be padded into place with blank spaces.");
		System.out.println("  You may also create your own tags, this is done by adding,");
		System.out.println("  anywhere before used, a $name=text$ tag, the name of the tag");
		System.out.println("  is not allowed to be / or \\, start with ballon or include =.");
	    }
		
		
	    System.out.println("\n\n");
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
	    System.out.println("Neither cowsay or ponysay nor way other program was cannibalised,");
	    System.out.println("but I did cannibalise all ponies and eat some delicous pancake\"let\"s.");
	    System.out.println();
	    System.out.println();
		
	    return;
	}
	
	start(args);
    }
    
    
    /**
     * The is actually the main method of the program, {@link #main(String...)} just wraps this
     * methods and takes care of the <code>--help</code> argument.
     * 
     * @param  args  Startup arguments, start the program with </code>--help</code> for details
     * 
     * @throws  IOException  On I/O exception
     */
    private static void start(final String... args) throws IOException
    {
	String nw =  "/-", n = "-", ne = "-\\",
	        w =  "| ",           e = " |",
	       sw = "\\-", s = "-", se = "-/", l = "\\", L = "/";
	
	nw += "\n| ";      ne += "\n |";
	sw = "| \n" + sw;  se = " |\n" + se;
	n += "\n ";        s = " \n" + s;
	
	final ArrayList<String> format = new ArrayList<String>();
	final ArrayList<String> pony = new ArrayList<String>();
	final ArrayList<String> say = new ArrayList<String>();
	
	boolean random = false;
	boolean dash = false;
	for (int i = 0, m = args.length; i < m; i++)
        {
	    final String arg = args[i];
	    
	    if (dash)
		pony.add(arg);
	    else if (Util.equalsAny(arg, "--"))
		dash = true;
	    else if (Util.equalsAny(arg, "--random", "-r"))
		if (random)
		    System.err.println("--random (-r) should only be used once.");
		else
		    random = true;
	    else if (Util.equalsAny(arg, "--format", "-p"))
		format.add(args[++i]);
	    else if (Util.equalsAny(arg, "--say", "-s"))
		say.add(args[++i]);
	    else if (arg.startsWith("-"))
	    {
		System.err.println("Unrecognised option, assuming it is a pony file: " + arg);
		pony.add(arg);
	    }
	    else
		pony.add(arg);
	}
	
	final String oneSay;
	final String onePony;
	String oneFormat;
	
	if (say.isEmpty() == false)
	    oneSay = say.get((int)(Math.random() * say.size()));
	else
	    oneSay = null; //we will catch it later
	
	if (pony.isEmpty() == false)
	    onePony = pony.get((int)(Math.random() * pony.size()));
	else
	{
	    final String privateDir = "~/.local/share/unisay/pony/".replace("~", Util.getProperty("HOME"));
	    final String publicDir  = "/usr/local/share/unisay/pony/";
	    
	    final String privateDefault = privateDir + "default";
	    final String publicDefault  = publicDir  + "default";
	    
	    if ((new File(privateDefault)).exists() && !random)
		onePony = privateDefault;
	    else if ((new File(publicDefault)).exists() && !random)
		onePony = publicDefault;
	    else
	    {
		pony.clear();
		
		if ((new File(privateDir)).exists())
		    for (final String file : (new File(privateDir)).list())
			if (file.equals("default") == false)
			    pony.add(privateDir + file);
		
		if ((new File(publicDir)).exists())
		    for (final String file : (new File(publicDir)).list())
			if (file.equals("default") == false)
			    pony.add(publicDir + file);
		
		if (pony.isEmpty())
		    onePony = null;
		else
		    onePony = pony.get((int)(Math.random() * pony.size()));
	    }
	}
	
	if (format.isEmpty() == false)
	    oneFormat = format.get((int)(Math.random() * format.size()));
	else
	    oneFormat = null;
	
	if (onePony == null)
	{
	    System.err.println("No pony file was specified.");
	    return;
	}
	if ((new File(onePony)).exists() == false)
	{
	    System.err.println("The selected (or choosen) pony file does not exist.");
	    return;
	}
	
	if (oneFormat != null)
	    if ((new File(oneFormat)).exists() == false)
	    {
		System.err.println("The selected (or choosen) format file does not exist.");
		oneFormat = null;
	    }
	
	final int[][] mne, me, mse, ms, msw, mw, mnw, mn, ml, mL;
	if (oneFormat == null)
	{
	    final String[] nes, ns, nws, sws, ss, ses;
	    mne = new int[(nes = ne.split("\n")).length][];
	    mn  = new int[(ns  = n .split("\n")).length][];
	    mnw = new int[(nws = nw.split("\n")).length][];
	    msw = new int[(sws = sw.split("\n")).length][];
	    ms  = new int[(ss  = s .split("\n")).length][];
	    mse = new int[(ses = se.split("\n")).length][];
	    me = new int[1][e.length()];
	    mw = new int[1][w.length()];
	    ml = new int[1][l.length()];
	    mL = new int[1][L.length()];
	    for (int i = 0, m = e.length(); i < m; i++)  me[0][i] = (int)(e.charAt(i));
	    for (int i = 0, m = w.length(); i < m; i++)  mw[0][i] = (int)(w.charAt(i));
	    for (int i = 0, m = l.length(); i < m; i++)  ml[0][i] = (int)(l.charAt(i));
	    for (int i = 0, m = L.length(); i < m; i++)  mL[0][i] = (int)(L.charAt(i));
	    String t;
	    for (int i = 0, mi = nes.length; i < mi; i++)    for (int j = 0, mj = (mne[i] = new int[(t = nes[i]).length()]).length; j < mj; j++)    mne[i][j] = (int)(t.charAt(j));
	    for (int i = 0, mi =  ns.length; i < mi; i++)    for (int j = 0, mj = (mn [i] = new int[(t = ns [i]).length()]).length; j < mj; j++)    mn [i][j] = (int)(t.charAt(j));
	    for (int i = 0, mi = nws.length; i < mi; i++)    for (int j = 0, mj = (mnw[i] = new int[(t = nws[i]).length()]).length; j < mj; j++)    mnw[i][j] = (int)(t.charAt(j));
	    for (int i = 0, mi = sws.length; i < mi; i++)    for (int j = 0, mj = (msw[i] = new int[(t = sws[i]).length()]).length; j < mj; j++)    msw[i][j] = (int)(t.charAt(j));
	    for (int i = 0, mi =  ss.length; i < mi; i++)    for (int j = 0, mj = (ms [i] = new int[(t = ss [i]).length()]).length; j < mj; j++)    ms [i][j] = (int)(t.charAt(j));
	    for (int i = 0, mi = ses.length; i < mi; i++)    for (int j = 0, mj = (mse[i] = new int[(t = ses[i]).length()]).length; j < mj; j++)    mse[i][j] = (int)(t.charAt(j));
	}
	else
	{
	    final InputStream is = new BufferedInputStream(new FileInputStream(new File(oneFormat)));
	    
	    final int SIZE = 32;
	    final ArrayList<int[]> front = new ArrayList<int[]>(); //Using List instead of Deque to make it work with Java 5.
	    int[] buf = new int[SIZE];
	    int ptr = 0;
	    int state = 0;
	    String dir = null;
	    
	    final HashMap<String, ArrayList<int[]>> map = new HashMap<String, ArrayList<int[]>>();
	    map.put("nw:", new ArrayList<int[]>());
	    map.put("n:",  new ArrayList<int[]>());
	    map.put("ne:", new ArrayList<int[]>());
	    map.put( "e:", new ArrayList<int[]>());
	    map.put("se:", new ArrayList<int[]>());
	    map.put("s:",  new ArrayList<int[]>());
	    map.put("sw:", new ArrayList<int[]>());
	    map.put( "w:", new ArrayList<int[]>());
	    map.put("\\:", new ArrayList<int[]>());
	    map.put("/:",  new ArrayList<int[]>());
	    
	    for (int d; (d = is.read()) != -1;)
	    {
		if ((d & 128) == 128)
		{
		    int dn = 0;
		    while ((d & 128) == 128)
		    {
			dn++;
			d <<= 1;
		    }
		    if (dn == 1)
			continue;
		    d &= 0xFF;
		    d >>>= dn;
		    for (int i = 1; i < dn; i++)
		    {
			int pd = is.read();
			if (pd == -1)
			    break;
			d <<= 6;
			d |= pd & 0x3F;
		    }
		}
		
		if ((state != 1) || (d != (int)'\n'))
		    buf[ptr++] = d;
		if (ptr == SIZE)
		{
		    front.add(buf);
		    buf = new int[SIZE];
		    ptr = 0;
		}
		
		if ((state == 0) && (d == (int)':'))
		{
		    state = 1;
		    front.add(buf);
		    buf = front.get(0);
		    front.clear();
		    final char[] cbuf = new char[ptr];
		    for (int i = 0; i < ptr; i++)
			cbuf[i] = (char)(buf[i]);
		    String tmp = new String(cbuf);
		    if (Util.equalsAny(tmp, "nw:", "n:", "ne:", "e:", "se:", "s:", "sw:", "w:", "\\:", "/:"))
			dir = tmp;
		    else if (tmp.equals(":") == false)
		    {
			dir = null;
			System.err.println("Unrecognised format part: " + tmp.substring(0, tmp.length() - 1));
		    }
		    ptr = 0;
		}
		else if ((state == 1) && (d == (int)'\n'))
		{
		    state = 0;
		    final int[] line = new int[front.size() * SIZE + ptr];
		    for (int i = 0, m = front.size(); i < m; i++)
			System.arraycopy(front.get(i), 0, line, i * SIZE, SIZE);
		    front.clear();
		    System.arraycopy(buf, 0, line, front.size() * SIZE, ptr);
		    ptr = 0;
		    map.get(dir).add(line);
		}
	    }
	    
	    is.close();
	    
	    
	    mne = new int[map.get("ne:").size()][];
	    mn  = new int[map.get( "n:").size()][];
	    mnw = new int[map.get("nw:").size()][];
	    msw = new int[map.get("sw:").size()][];
	    ms  = new int[map.get( "s:").size()][];
	    mse = new int[map.get("se:").size()][];
	    me = new int[1][];
	    mw = new int[1][];
	    ml = new int[1][];
	    mL = new int[1][];
	    
	    map.get("nw:").toArray(mnw);  map.get("n:").toArray(mn);  map.get("ne:").toArray(mne);
	    map.get( "w:").toArray(mw);                               map.get( "e:").toArray(me);
	    map.get("sw:").toArray(msw);  map.get("s:").toArray(ms);  map.get("se:").toArray(mse);
	    map.get("\\:").toArray(ml);   map.get("/:").toArray(mL);
	}
	
	final ArrayList<int[]> lens = new ArrayList<int[]>();
	final ArrayList<ArrayList<byte[]>> lines = new ArrayList<ArrayList<byte[]>>();
	if (oneSay != null)
	    for (final String line : oneSay.split("\n"))
	    {
		final byte[] bs = line.getBytes("UTF-8");
		final ArrayList<byte[]> list = new ArrayList<byte[]>();
		list.add(bs);
		lines.add(list);
		lens.add(new int[] { line.length() });
	    }
	else
	{
	    ArrayList<byte[]> list = new ArrayList<byte[]>();
	    final int SIZE = 64;
	    byte[] buf = new byte[SIZE];
	    int ptr = 0;
	    int len = 0;
	    
	    boolean esc = false;
	    for (int d; (d = System.in.read()) != -1;)
	    {
		if (d == '\n')
		{
		    if (ptr > 0)
		    {
			byte[] app = new byte[ptr];
			System.arraycopy(buf, 0, app, 0, ptr);
			list.add(app);
		    }
		    lines.add(list);
		    list = new ArrayList<byte[]>();
		    lens.add(new int[] { len });
		    ptr = 0;
		    len = 0;
		}
		else
		{
		    if ((d & 0xC0) != 0xC0)
			if (d == '\033')
			    esc = true;
			else if (!esc)
			    len++;
			else if (d == 'm')
			    esc = false;
		    buf[ptr++] = (byte)d;
		    if (ptr == SIZE)
		    {
			list.add(buf);
			buf = new byte[SIZE];
			ptr = 0;
		    }
		}
	    }
	    
	    if (ptr > 0)
	    {
		byte[] app = new byte[ptr];
		System.arraycopy(buf, 0, app, 0, ptr);
		list.add(app);
	    }
	    lens.add(new int[] { len });
	    lines.add(list);
	}
	
	while ((lines.size() > 1) && (lines.get(lines.size() - 1).isEmpty()))
	    lines.remove(lines.size() - 1);
	
	int maxlen = 0;
	for (final int[] len : lens)
	    if (maxlen < len[0])
		maxlen = len[0];
	
	final Baloon baloon = new Baloon(lens, maxlen, lines, mnw, mn, mne, me[0], mse, ms, msw, mw[0]);
	say(onePony, baloon, ml[0], mL[0]);
    }
    
    
    /**
     * Performs the speaking!
     *
     * @param  ponyFile  The image file
     * @parma  baloon    The baloon object
     * @param  l         \ directional link symbol
     * @param  L         / directional link symbol
     * 
     * @throws  IOException  On I/O exception
     */
    private static void say(final String ponyFile, final Baloon baloon, final int[] l, final int[] L) throws IOException
    {
	final HashMap<String, byte[]> variables = new HashMap<String, byte[]>();
	final InputStream is = new BufferedInputStream(new FileInputStream(new File(ponyFile)));
	variables.put("\\", Util.toBytes(l));
	variables.put("/", Util.toBytes(L));
	variables.put("", Util.toBytes((int)'$'));
	int indent = 0;
	
	boolean esc = false;
	int eq = 0;
	byte[] buf = new byte[16];
	int ptr = 0;
	
	for (int d; (d = is.read()) != -1;)
	    if (d == '$')
	    {
		if ((esc ^= true) == false)
		{
		    String var = new String(buf, 0, eq == 0 ? ptr : eq, "UTF-8");
		    if (eq != 0)
		    {
			final byte[] val = new byte[ptr - eq - 1];
			System.arraycopy(val, 0, buf, eq + 1, val.length);
			variables.put(var, val);
		    }
		    else if (var.startsWith("baloon"))
		    {
			int w = 0;
			int h = 0;
			final String props = var.substring("baloon".length());
			if (props.isEmpty() == false)
			    if (props.contains(","))
			    {
				if (props.startsWith(",") == false)
				    w = Integer.parseInt(props.substring(0, props.indexOf(",")));
				h = Integer.parseInt(props.substring(1 + props.indexOf(",")));
			    }
			    else
				w = Integer.parseInt(props);
			
			baloon.print(w, h, indent);
			
			indent = 0;
		    }
		    else
			System.out.write(variables.get(var));
		    eq = 0;
		    ptr = 0;
		}
	    }
	    else if (esc == false)
	    {
		System.out.write(d);
		indent++;
		if (d == '\n')
		    indent = 0;
	    }
	    else
	    {
		if ((buf[ptr++] = (byte)d) == (byte)'=')
		    if (eq == 0)
			eq = ptr - 1;
		if (ptr == buf.length)
		{
		    final byte[] nbuf = new byte[ptr + 16];
		    System.arraycopy(nbuf, 0, buf, 0, ptr);
		    buf = nbuf;
		}
	    }
	
	is.close();
    }    
    
}
