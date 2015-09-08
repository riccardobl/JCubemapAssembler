package net.forkforge.cubemapassembler;

import java.io.File;

import net.forkforge.cubemapassembler.exporters.AWTExporter;
import net.forkforge.cubemapassembler.exporters.KTXExporter;
import net.forkforge.cubemapassembler.exporters.dds.DDSExporter;
import net.forkforge.cubemapassembler.fx_gui.WinFx;

public class jCubemapAssembler{

	public static final String _VERSION="v2.0";

	public static void main(String _a[]) {
		ExportersManager.addExporter(DDSExporter.class,true,"dds");
		ExportersManager.addExporter(KTXExporter.class,"ktx");
		ExportersManager.addExporter(AWTExporter.class,"png","jpg","bmp");
		WinFx.start(_a);
	}
	
	public static String getFileExtension(String file) {
		String ext="";
		if(file.contains(File.separator)){ 
			String parts[]=file.split(File.separator);
			file=parts[parts.length-1];
			String exts[]=file.split("\\.");
			boolean x=true;
			for(String e:exts)
				if(x){
					x=false;
					continue;
				}else ext+=(ext.isEmpty()?"":".")+e;
			return ext;
		}
		return "png";
		
	}
	
}
