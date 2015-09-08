package net.forkforge.cubemapassembler;

import java.io.File;

public abstract class Exporter{
	protected String[] EXTENSIONS;
	protected boolean DEFAULT;
	public void setDefault(boolean d){
		DEFAULT=d;
	}
	
	public boolean isDefault(){
		return DEFAULT;
	}
	
	public abstract void doExport(int cube_dimension,Area[] images,File output) throws Exception; 
	
	protected void setExtensions(String ...exts){
		EXTENSIONS=exts;
	}
	
	public String[] getExtensions(){
		return EXTENSIONS;
	}
	
	public abstract String getName();

	
}
