package fci.cu.std.paas.client.services.persistent.storage;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fci.cu.std.paas.api.xml.services.persistent.storage.StorageServiceType;
import fci.cu.std.paas.core.services.persistent.storage.PersistentStorageService;


public class StdPersistentStorageClient extends HttpServlet {

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
	    out.println("YaRab");
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String storageManifest=req.getParameter("storage_manifest");
		PrintWriter out = resp.getWriter();
	    out.println("YaRab, YaRab, YaRab");
	    out.println(storageManifest);
	    PersistentStorageService ssObj=new PersistentStorageService();
	    StorageServiceType ssType=ssObj.configureStorageService(storageManifest);
	    boolean result=ssObj.createContainer(ssType);// has no effect in case of GAE provider
	    result=ssObj.createBlob(ssType);
	    result=ssObj.uploadBlob(ssType);
	    out.println("SStype is created "+ssType);
	    out.println("the upload result is "+result);
	}
	
	

}
