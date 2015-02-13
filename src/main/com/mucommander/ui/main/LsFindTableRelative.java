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


public class LsFindTableRelative {

	private String fname = "ls_path_rel.txt";
	private String fname2 = "ls_path_rel.txt";
	public Boolean isFind = false;
	
	private HashMap<String,ArrayList<String>> fmap;
	private ArrayList<String> findlist;
	public String selstr; // store user select path in frame's table;
	
	public static LsFindTableRelative lsfind= null;
	public String findstr;
	public int  findstate = 0;
	
	private int state = 0;
	// =1,file read ok;
	
	//2013-10-20;-1120;
	private String workdir = ""; // dir of exe;
	

	public MainFrame mfrm;
	
	public LsFindTableRelative()
	{
		fmap = new HashMap<String,ArrayList<String>>();
		findlist = new ArrayList<String>();
		selstr = new String();
		selstr = "";
		
		mfrm = null;
		
	}
	public void init()
	{
		workdir = getCurdir();
		//System.out.println(workdir);
		//JOptionPane.showMessageDialog(null, workdir);
		
		Load();
	}
	public void Close()
	{
		Save();
	}
	
// 2013-10-20;-1124;
	private String getCurdir() {
		return System.getProperty("user.dir");
		// example: C:\lsen\prog\muCommander\trunk-2013-03-13\tmp
	}
	
// 2013-04-06;
	public void DelPath( String path){
		if( path == null)
			return ;
		//
		// iterator fmap;
		ArrayList<String> als = null;
		Iterator<String> iterator = fmap.keySet().iterator();                
        while (iterator.hasNext()) 
        {    
        	String key = iterator.next();
        	//System.out.println(key);
        	
        	//if( (key.toLowerCase()).contains(fstr.toLowerCase()))
        	//{
    		als = fmap.get(key);
    		//falal.add(als);
    		for(int i=0;i <als.size();i++)
    		{
    			if( path == als.get(i))
    			{
    				als.remove(i);
    				return;
    			}
    		}
        	//}
        }
	}
// 131020-1153;
	public int AddPathRel(String path)
	{
		path = workdir + path;
		return AddPath( path );
	}
	public int AddPath(String path)
	{
		// check;
		if( path == null )
			return -1;
		else if( path.isEmpty())
			return 0;
		
		char c = path.charAt(path.length()-1);
		//System.out.println(c);
		if( c=='\\')
		{
			//System.out.println(path);
			path = path.substring(0,path.length()-1);
			//System.out.println(path);
		}
		if(path.isEmpty())
			return 0;
		//System.out.println(path);
		
		// 2013-03-18; if folder exist?
		File file =new File(path);    
		if(!file.exists() && !file.isDirectory())      
		{
			return 0;
		}  
				
		// get file name from path;
		String name;
		int p = path.lastIndexOf('\\');
		if( p < 0 ) // =-1 not found;
			return 0;
		else if (p >= path.length()-1)// no char after \;
			return 0;
		else
			name = path.substring(p+1,path.length());
		
		// add name and path to hashmap;
		// search if exist?
		if( !fmap.containsKey(name))// not contain ,then add one;
		{
			ArrayList<String> al = new ArrayList<String>();
			al.add(path);
			fmap.put(name, al);
			return 1;
		}
		else//name has already exist;
		{
			ArrayList<String> al = fmap.get(name);
			// check path exist?
			for(int i=0; i< al.size();i++)
			{
				String s = al.get(i);
				if( s.equals(path))
					return 1;

			}
			al.add(path);
			return 1;		
		}
		
	}
	
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
		if( CheckFile(fname) < 0 )
			return;
		
		try {
			FileInputStream fis = new FileInputStream(fname); 
			BufferedReader br=new BufferedReader(new InputStreamReader(fis));
			do
			{
				// read a line;
				String path =  br.readLine();
				if( path == null )
					break;
				// add to hashmap;
				AddPathRel(path);	// 131020-1154;
				
			}while(true);
			
			br.close();
			
			state = 1;  // read file ok;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private void relPath()
	{
		String str;
		String s;
		int len = workdir.length();
		
		// iterator fmap;
		Iterator<String> iterator = fmap.keySet().iterator();                
        while (iterator.hasNext()) 
        {    
        	String key = iterator.next();
        	ArrayList<String> als = fmap.get(key);
        	for(int i=0; i <als.size();i++)
        	{
        		str = als.get(i);
        		// remove curdir;
        		// find if contains curdir first;
        		// get file name from path;
        		if( str.length() > len )
        		{
        			s = str.substring(len);
        			if( s == workdir)
        				als.set(i, s);
        			else
        				als.set(i, "");
        		}
        		else
        			als.set(i, "");	
            }
        }
	}
	public void Save()
	{
		if( CheckFile(fname2) < 0)
			return;
		
		relPath();
		
		//
		try {
			FileOutputStream fos = new FileOutputStream(fname2); 
			BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(fos));
			
			//FileWriter fw = new FileWriter(fname);

			bw.write("\\\r\n");
			
			// iterator fmap;
			String str;
			Iterator<String> iterator = fmap.keySet().iterator();                
            while (iterator.hasNext()) 
            {    
            	String key = iterator.next();
            	ArrayList<String> als = fmap.get(key);
            	for(int i=0; i <als.size();i++)
            	{
            		str = als.get(i);
            		if( str.length() > 0 )
            		{
            			bw.write(str);
            			bw.write("\r\n");
            		}            		
            	}
   
            }  
			bw.close();
			//fw.close();
			
			state = 2;  // write file ok;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> GetFindList()
	{
		return findlist;
	}
	public ArrayList<String> FindStr0( String fstr )
	{
	// single keyword search version;
		//System.out.println("findstr..");
		
		this.findlist = new ArrayList<String>();
		
		//ArrayList<ArrayList<String>> falal = new ArrayList<ArrayList<String>>();
		ArrayList<String> als = null;
		
		// iterator fmap;
		Iterator<String> iterator = fmap.keySet().iterator();                
        while (iterator.hasNext()) 
        {    
        	String key = iterator.next();
        	//System.out.println(key);
        	if( (key.toLowerCase()).contains(fstr.toLowerCase()))
        	{
        		als = fmap.get(key);
        		//falal.add(als);
        		for(int i=0;i <als.size();i++)
        		{
        			findlist.add(als.get(i));
        		}
        	}
        } 
        return findlist;
	}
// <2013-04-13; sort find result;	
	public ArrayList<String> FindStr3( ArrayList<String> als, String fstr )
	{		
		int len = als.size();
		if( len < 1)
			return als;
		
		ArrayList<String> als1 = new ArrayList<String>();
		int[] ai1= new int[len];
		//ArrayList<Integer> ali1 = new ArrayList<Integer>();
		ArrayList<String> als2 = new ArrayList<String>();
		int[] ai2= new int[len];
		
		String s,s2;
		int idx1=0,idx2=0;
		String[] sps;
		// get 2 lists;
		for( int i=0; i < als.size();i++)
		{
			s2 = als.get(i);
			// split;
			sps =  s2.split("\\\\");
			for( int j=sps.length-1; j>=0 ; j--)
			{
				s = sps[j];
				// compare;
				if(s.equalsIgnoreCase(fstr)) // add to list 1;
				{
					als1.add(s2);
					ai1[idx1++]=j;
					break;
				}
				else if( (s.toLowerCase()).contains(s.toLowerCase())) // add to list 2;
				{
					als2.add(s2);
					ai2[idx2++]=j;
					break;
				}
			}
		}
		
		// sort;
		ArrayList<String> ass1 = new ArrayList<String>();
		ArrayList<String> ass2 = new ArrayList<String>();
		
		len=als1.size();
		int[] ai;
		if( len >0)
		{
			ai = SortIdx( ai1, len);
			for( int i=0;i < len; i++)
				ass1.add(als1.get(ai[i]));
		}
		
		len=als2.size();
		if( len >0)
		{
			ai = SortIdx( ai2, len);
			for( int i=0;i < len; i++)
				ass2.add(als2.get(ai[i]));
		}
		// add 1,2 to list;
		als.clear();
		for( int i=0; i< ass1.size();i++)
			als.add(ass1.get(i));
		for( int i=0; i< ass2.size();i++)
			als.add(ass2.get(i));
		
		return als;
	}
	private int[] SortIdx( int[] av, int len)
	{
		int[] ai = new int[len];
		
		// as=idx; av=value;
		for( int i=0; i < len; i++)
			ai[i] =i;
		
		// rec idx & val both;
		// popup sort algo;
		int m,v;
		int c=len;
		for( int i=0; i<len-1; i++)
		{
			c--;
			for(int j=0; j< c;j++)
			{
				if( av[j]>av[j+1] ) // big one down;
				{
					v = av[j];
					av[j] = av[j+1];
					av[j+1]=v;
					
					m=ai[j];
					ai[j]=ai[j+1];
					ai[j+1]=m;
				}
			}
		}
		return ai;
	}
// 2013-04-13; sort find result;>
	
	public ArrayList<String> FindStr( String fstr )
	{
		//System.out.println("findstr..");
		
		this.findlist = new ArrayList<String>();
		
	// 2013-04-01; modify to multi keyword search version;
		// split fstr by space;
		// first result is find folder;
		// other is in path;
		String[] sps = fstr.split(" ");
		if( sps.length < 1) 
			return findlist;
		//System.out.println(sps.length);
		
	// find folder keyword in database;
		fstr = sps[0];
		//ArrayList<ArrayList<String>> falal = new ArrayList<ArrayList<String>>();
		ArrayList<String> als = null;
		
		// iterator fmap;
		Iterator<String> iterator = fmap.keySet().iterator();                
        while (iterator.hasNext()) 
        {    
        	String key = iterator.next();
        	//System.out.println(key);
        	if( (key.toLowerCase()).contains(fstr.toLowerCase()))
        	{
        		als = fmap.get(key);
        		//falal.add(als);
        		for(int i=0;i <als.size();i++)
        		{
        			findlist.add(als.get(i));
        		}
        	}
        } 
        
    //2013-04-01; find path keywork in findlist;    
        if( sps.length > 1)
        {
	        String s;
	        String s2;
	        for( int i=1; i <sps.length; i++)
	        {
	        	s=sps[i];
	        	//System.out.println(i);
	        	//System.out.println(s);
	        	ArrayList<String> findlist2 = new ArrayList<String>();
	        	for( int j=0; j<findlist.size();j++)
	        	{
	        		s2 = findlist.get(j);
	        		//System.out.println(s2);
	        		if((s2.toLowerCase()).contains(s.toLowerCase()))
	        			findlist2.add(s2);
	        	}
	        	findlist = findlist2;
	        }
        }
        
        //return findlist;        
    // 2013-04-13; sort result list;
        return FindStr3( findlist, fstr);
	}
	
	public void ShowFrame()
	{
		//
		LsFindFrame ff = new LsFindFrame();
		ff.mfrm= this.mfrm;
		ff.findtm.flist = findlist;
		ff.setLocationRelativeTo(this.mfrm);
		
		ff.findtb.setRowSelectionInterval(0, 0);
		ff.setVisible(true);
		
		return;
		//
		/*
		javax.swing.JFrame jf=new javax.swing.JFrame("table test");
		jf.setSize(300,400);
		java.awt.FlowLayout fl=new java.awt.FlowLayout();
		jf.setLayout(fl);
		javax.swing.JTable table=new javax.swing.JTable();
		MyTableModel    tm=new MyTableModel();
		tm.flist = findlist;
		
		table.setModel(tm);
		jf.add(table);
		jf.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		jf.setVisible(true);
*/
	}
}
