package com.mucommander.ui.main;

import java.util.ArrayList;

import javax.swing.event.TableModelListener;

public class MyTableModel implements javax.swing.table.TableModel{

	public ArrayList<String> flist;
	
	public MyTableModel()
	{
		flist = null;
	}
	
    public int getRowCount(){
    	if(flist!=null)
    		return flist.size();
    	else
    		return 0;
    }

    public int getColumnCount(){
    	return 1;
    }

    public String getColumnName(int columnIndex){
    	return columnIndex+"directory path";
    }

    public Class<?> getColumnClass(int columnIndex){
    	 return String.class;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex){
    	if(columnIndex==0){
    		return false;
    	}
     	return false;
    }

    public Object getValueAt(int rowIndex, int columnIndex){
    	if( flist !=null)
    		return flist.get(rowIndex);
    	else
    		return "";
    	//return rowIndex+"--"+columnIndex;
    }

   public void setValueAt(Object aValue, int rowIndex, int columnIndex){
        //String s="Change at: "+rowIndex+"--- "+columnIndex+" newValue: "+aValue;
        //System.out.println(s);
    }

    public void addTableModelListener(TableModelListener l){}
    public void removeTableModelListener(TableModelListener l){}

}
