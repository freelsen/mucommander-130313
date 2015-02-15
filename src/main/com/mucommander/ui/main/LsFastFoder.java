package com.mucommander.ui.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

public class LsFastFoder 
{
	public static LsFastFoder mthis= null;
	
	private String mloadfilename = "ls_path.txt";
	private String msavefilename = "ls_path.txt";
	public Boolean misfind = false;
	
	public String mselectpath; // store user select path in frame's table;
	
	public LsFindProc mfindproc = new LsFindProc();
	public String mfindstr;
	public int  mfindstate = 0;
	
	private int state = 0;
	// =1,file read ok;

	public MainFrame mainframe;
	
	public LsFastFoder()
	{
		mthis = this;
		
		mselectpath = new String();
		mselectpath = "";
		
		mainframe = null;	
	}
	public void init()
	{
		mfindproc.init();
		
		Load();
	}
	public void Close()
	{
		Save();
	}
		
// 2013-04-06;
	public int AddPath(String path)
	{
		if( path == null )
			return -1;
		else if( path.isEmpty())
			return 0;
		
		return mfindproc.addPath(path);	
	}
	public void DelPath( String path){
		if( path == null)
			return ;

		mfindproc.delPath(path);
	}
	
	// -------- file operation; ------------------
	private int CheckFile(String name)
	{
		int ret = -1;
		
		File f = new File(name);
		if( !f.exists())
		{
			try {
				f.createNewFile();
				ret = 1;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			ret = 1;
		}
		return ret;
	}
	public void Load()
	{
		// do the file operation;
		if( CheckFile(mloadfilename) < 0 )
			return;
		
		try {
			FileInputStream fis = new FileInputStream(mloadfilename); 
			BufferedReader br=new BufferedReader(new InputStreamReader(fis));
			do
			{
				// read a line;
				String path =  br.readLine();
				if( path == null )
					break;
				// add to hashmap;
				AddPath(path);
				
			}while(true);
			
			br.close();
			
			state = 1;  // read file ok;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private BufferedWriter mwriter;
	public void Save()
	{
		if( CheckFile(msavefilename) < 0)
			return;
		//
		try {
			FileOutputStream fos = new FileOutputStream(msavefilename); 
			mwriter = new BufferedWriter(new OutputStreamWriter(fos));
			
			mfindproc.savePath();

			mwriter.close();
			
			state = 2;  // write file ok;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void onWritePath(String s) throws IOException
	{
		mwriter.write(s);
		mwriter.write("\r\n");
	}
	
	// ------------mainframe event; ------------------------------
	public void gotoPath(String path)
	{
		mselectpath = path;
		if(mainframe!=null)
		{
			mainframe.getActivePanel().getLocationTextField().setText(path);
			//mfrm.getRightPanel().getLocationTextField().setText(str);
		}
	}
	
	public boolean onTextField( String location)
	{
		if(location.startsWith("."))
        {
        	this.mselectpath = "";
        	this.mfindstate = 2;
        	
        	String fstr = location.substring(1, location.length());
        	this.mfindstr = fstr;
        	
        	ArrayList<String> als = mfindproc.FindStr(fstr);
        	if(!als.isEmpty())
        	{
        	    this.showFindFrame();
        	    return true;
        	}
        }
        else
        {
        	this.mfindstate = 0;
        }  
        return false;
	}
	private void showFindFrame()
	{
		LsFindFrame frame = new LsFindFrame();
		
		frame.mfindtablemodel.mfindlist = mfindproc.mfindlist;
		frame.setLocationRelativeTo(this.mainframe);
		
		frame.mfindtable.setRowSelectionInterval(0, 0);
		frame.setVisible(true);
		
		return;
	}
}
