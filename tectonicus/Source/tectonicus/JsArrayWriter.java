/*
 * Source code from Tectonicus, http://code.google.com/p/tectonicus/
 *
 * Tectonicus is released under the BSD license (below).
 *
 *
 * Original code John Campbell / "Orangy Tang" / www.triangularpixels.com
 *
 * Copyright (c) 2012, John Campbell
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright notice, this list
 *     of conditions and the following disclaimer.
 *
 *   * Redistributions in binary form must reproduce the above copyright notice, this
 *     list of conditions and the following disclaimer in the documentation and/or
 *     other materials provided with the distribution.
 *   * Neither the name of 'Tecctonicus' nor the names of
 *     its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package tectonicus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;

public class JsArrayWriter
{
	private OutputStream out;
	private PrintWriter writer;
	
	private boolean hasWritenEntry;
	
	public JsArrayWriter(File file, String arrayName) throws FileNotFoundException, IOException
	{
		if (file.exists())
			file.delete();
		
		out = new FileOutputStream(file);
		writer = new PrintWriter(out);
		
		writer.println("var "+arrayName+"=[");
	}
	
	public void write(Map<String, String> vars)
	{
		if (hasWritenEntry)
			writer.println(",");
		writer.println("\t{");
		
		boolean hasWrittenLine = false;
		
		for (String name : vars.keySet())
		{
			String value = vars.get(name);
			
			if (hasWrittenLine)
				writer.println(",");
			
			writer.print("\t\t");
			writer.print(name);
			writer.print(": ");
			writer.print(value);
			
			
			// ..
			
			hasWrittenLine = true;
		}
		
		writer.println();
		
		writer.print("\t}");
		hasWritenEntry = true;
	}
	
	public void close()
	{
		writer.println();
		writer.println("];");
		
		try
		{
			if (writer != null)
				writer.close();
		
			if (out != null)
				out.close();
		}
		catch (IOException e) {}
	}
}
