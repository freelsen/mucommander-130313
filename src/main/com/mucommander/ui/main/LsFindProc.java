package com.mucommander.ui.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JOptionPane;

public class LsFindProc {

	private HashMap<String,ArrayList<String>> mfindmap;
	public ArrayList<String> mfindlist;
	
	private String mworkdir = "";//2013-10-20;-1120;
	char mbackslash = '/'; //@150330, '/'for mac; //'\\'; for win;
	String mbackslashstr="/";

	LsFindProc()
	{
		mfindmap = new HashMap<String,ArrayList<String>>();
		mfindlist = new ArrayList<String>();	
	}
	public void init()
	{
		mworkdir = this.getCurdir();
		addPath(mworkdir);
	}
	// 2013-10-20;-1124;
	private String getCurdir() {
		return System.getProperty("user.dir");
		// example: C:\muCommander\tmp
	}
	
	private String mpath="";
	public int addPath(String path)
	{
		mpath = path;
		String name = getFolderName();
		if( name == "")
			return 0;
		return addPath(mpath, name);
	}
	public int addPath(String path, String name)
	{
		// add name and path to hashmap;
				// search if exist?
		if( !mfindmap.containsKey(name))// not contain ,then add one;
		{
			ArrayList<String> al = new ArrayList<String>();
			al.add(path);
			mfindmap.put(name, al);
			return 1;
		}
		else//name has already exist;
		{
			ArrayList<String> al = mfindmap.get(name);
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
	private String getFolderName()
	{
		mpath = mpath.replaceAll("\\\\", "/");
		//JOptionPane.showMessageDialog(null, "after "+mpath);
		if(mpath.charAt(0) == mbackslash)				// 131020-1406;
			mpath = mworkdir +mpath;
		
		char c = mpath.charAt(mpath.length()-1);
		//System.out.println(c);
		if( c==mbackslash)
		{
			//System.out.println(path);
			mpath = mpath.substring(0,mpath.length()-1);
			//System.out.println(path);
		}
		if(mpath.isEmpty())
			return "";
		//System.out.println(path);
		
		// 2013-03-18; if folder exist?
//		File file =new File(mpath);    
//		if(!file.exists() && !file.isDirectory())      
//		{
//			return "";
//		}  
				
		// get file name from path;
		String name="";
		int p = mpath.lastIndexOf(mbackslash);
		if( p < 0 ) // =-1 not found;
			return "";
		else if (p >= mpath.length()-1)// no char after \;
			return "";
		else
			name = mpath.substring(p+1,mpath.length());
		
		return name;
	}
	private String procSaveRelativePath(String path)
	{
		String rpath = "";
		int len = mworkdir.length();
		if( path.length() > len )
		{
			rpath = path.substring(0,len);
			if( rpath.equalsIgnoreCase(mworkdir) )
			{
				rpath = path.substring(len);
				return rpath;
			}
		}
		return "";
	}
	public void savePath() throws IOException
	{
		String path="";
		String rpath="";
		Iterator<String> iterator = mfindmap.keySet().iterator();                
        while (iterator.hasNext()) 
        {    
        	String key = iterator.next();
        	ArrayList<String> als = mfindmap.get(key);
        	for(int i=0; i <als.size();i++)
        	{
        		path = als.get(i);
			rpath = procSaveRelativePath(path);
        		if( rpath != "")
        			LsFastFoder.mthis.onWritePath(rpath);
        		else
        			LsFastFoder.mthis.onWritePath(path);
        	}
        }  
	}
	
	public void delPath( String path)
	{
		ArrayList<String> als = null;
		Iterator<String> iterator = mfindmap.keySet().iterator();                
        while (iterator.hasNext()) 
        {    
        	String key = iterator.next();
        	//System.out.println(key);
        	
        	//if( (key.toLowerCase()).contains(fstr.toLowerCase()))
        	//{
    		als = mfindmap.get(key);
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
	
	public ArrayList<String> FindStr( String fstr )
	{
		//System.out.println("findstr..");
		
		this.mfindlist = new ArrayList<String>();
		
	// 2013-04-01; modify to multi keyword search version;
		// split fstr by space;
		// first result is find folder;
		// other is in path;
		String[] sps = fstr.split(" ");
		if( sps.length < 1) 
			return mfindlist;
		//System.out.println(sps.length);
		
	// find folder keyword in database;
		fstr = sps[0];
		//ArrayList<ArrayList<String>> falal = new ArrayList<ArrayList<String>>();
		ArrayList<String> als = null;
		
		// iterator fmap;
		Iterator<String> iterator = mfindmap.keySet().iterator();                
        while (iterator.hasNext()) 
        {    
        	String key = iterator.next();
        	//System.out.println(key);
        	if( (key.toLowerCase()).contains(fstr.toLowerCase()))
        	{
        		als = mfindmap.get(key);
        		//falal.add(als);
        		for(int i=0;i <als.size();i++)
        		{
        			mfindlist.add(als.get(i));
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
	        	for( int j=0; j<mfindlist.size();j++)
	        	{
	        		s2 = mfindlist.get(j);
	        		//System.out.println(s2);
	        		if((s2.toLowerCase()).contains(s.toLowerCase()))
	        			findlist2.add(s2);
	        	}
	        	mfindlist = findlist2;
	        }
        }
        
        //return findlist;        
    // 2013-04-13; sort result list;
        return sortResult( mfindlist, fstr);
	}
// <2013-04-13; sort find result;	
	public ArrayList<String> sortResult( ArrayList<String> als, String fstr )
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
}
