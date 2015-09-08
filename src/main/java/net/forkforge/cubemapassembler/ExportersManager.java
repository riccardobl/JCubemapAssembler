package net.forkforge.cubemapassembler;

import java.util.HashMap;
import java.util.Map.Entry;

public class ExportersManager{
	private static HashMap<String[],Exporter> EXPORTERS=new HashMap<String[],Exporter>();
	
	public static synchronized void addExporter(Class<? extends Exporter> ex,String... exts) {
		addExporter(ex,false,exts);
	}
	
	public static synchronized void addExporter(Class<? extends Exporter> ex,boolean d,String... exts) {
		try{
			Exporter exp=ex.newInstance();
			exp.setDefault(d);
			EXPORTERS.put(exts,exp);
			exp.setExtensions(exts);
		}catch(InstantiationException e){
			e.printStackTrace();
		}catch(IllegalAccessException e){
			e.printStackTrace();
		}
	}
	
	public static synchronized Exporter getExporter(String ext){
		for(Entry<String[],Exporter> x:EXPORTERS.entrySet()){
			String exts[]=x.getKey();
			for(String e:exts){
				if(e.equals(ext))return x.getValue();
			}
		}
		return null;
	}

	public static  HashMap<String[],Exporter> getExporters() {
		return EXPORTERS;
	}
	
	
}
