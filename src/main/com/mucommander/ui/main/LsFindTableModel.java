package com.mucommander.ui.main;

import java.util.ArrayList;

import javax.swing.event.TableModelListener;

public class LsFindTableModel implements javax.swing.table.TableModel{

	public ArrayList<String> mfindlist;
	
	public LsFindTableModel()
	{
		mfindlist = null;
	}
	
	public String getCellstr( int row, int col){
		String str = "";
		if( col >= this.getColumnCount() || col < 0)
			return str;
		if( row >= this.getRowCount() || row < 0 )
			return str;
		
		str = this.getValueAt(row, col).toString();
		
		return str;
	}
	public void removeRow(int row)
	{
		mfindlist.remove(row);
	}
	
    public int getRowCount(){
    	if(mfindlist!=null)
    		return mfindlist.size();
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
    	if( mfindlist !=null)
    		return mfindlist.get(rowIndex);
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
