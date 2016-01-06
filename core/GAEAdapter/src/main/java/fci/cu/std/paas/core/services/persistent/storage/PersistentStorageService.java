package fci.cu.std.paas.core.services.persistent.storage;

import java.nio.channels.Channels;

import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;

import fci.cu.std.paas.api.services.persistent.storage.StdPersistentStorage;
import fci.cu.std.paas.api.xml.manifest.Blob;
import fci.cu.std.paas.api.xml.manifest.Container;
import fci.cu.std.paas.api.xml.manifest.PersistentStorageServiceManifest;
import fci.cu.std.paas.api.xml.manifest.utilities.ManifestUtilities;
import fci.cu.std.paas.api.xml.services.persistent.storage.StorageServiceType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PersistentStorageService implements StdPersistentStorage {

	public StorageServiceType configureStorageService(String cloudStorageDescribtor) {
		
		if(cloudStorageDescribtor!=null && !cloudStorageDescribtor.equals(""))
		{
			//parse the given string into XML manifest
			PersistentStorageServiceManifest manifest=ManifestUtilities.parseManifest(cloudStorageDescribtor);
			StorageServiceType ssType=new StorageServiceType();
			Blob blob=new Blob();
			ssType.setBlob(blob);
			Container container=manifest.getContainer();
			ssType.setContainer(container);
			System.out.println("container info:"+container.getName()+"    "+container.getProvider());
			return ssType;			
		}
		return null;
	}
	/**
	 * com.google.appengine.tools.cloudstorage.GcsService createContainer(java.lang.String containerName,java.lang.String blobName, java.lang.String contentType)
	 * - containerName and blobName --> they will be used to create GCSFileName 
	 * - content type of this file -->it will be used to create GCSFileOptions
	 */

	public boolean createContainer(StorageServiceType ssType) {
		System.out.println("No code for this fun in GAE, bcos the container"
				+ " must be created from the dashboard\n no GAE APIs available to create container");
		System.out.println("we will assume that the container is available");
		
		return true;
	}
	/**
	 * the createBlob fun needs the following i/ps:
	 * - containerName 
	 * - blobName (the name of the file to be uploaded)
	 * - content type of this file -->also, I can get it using my fun ManifestUtilities.getMimeType
	 * & it should return the output stream of the uploaded blob
	 * com.google.appengine.tools.cloudstorage.GcsOutputChannel createBlob(com.google.appengine.tools.cloudstorage.GcsService physicalContainer,java.lang.String BlobName)
	 * 
	 */
	public boolean createBlob(StorageServiceType ssType) {
		String containerName=ssType.getContainer().getName();
		String blobName=ssType.getBlob().getName();
		String sCType=ssType.getBlob().getsCType();
		System.out.println("from create blob fn.:blobname="+blobName+" blob type="+sCType);
		//1. create GCS file with container name and blob name
		GcsFilename gcsfileName = new GcsFilename(containerName, blobName);
		//2. specify the content type and ACL of ur file
		//you can get a default instance using  GcsFileOptions.getDefaultInstance()-->however, this method didn't specify any default value
		// the default ACL is project-private --> by this way, the file can't be accessed after it is uploaded. 
		GcsFileOptions options = new GcsFileOptions.Builder()
        .acl("public-read").mimeType(sCType).build();
		
		//3. create Google Cloud Service
		//you can get a default instance from RetryParams using RetryParams.getDefaultInstance()
		GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
	    .initialRetryDelayMillis(10)
	    .retryMaxAttempts(10)
	    .totalRetryPeriodMillis(15000)
	    .build());

		try {
			GcsOutputChannel outputChannel =
			        gcsService.createOrReplace(gcsfileName, options);
			OutputStream osBlob=Channels.newOutputStream(outputChannel);
			//store the outputstream of the created blob in the ssType to be used by
			// the uploadBlob method.
			ssType.getBlob().setUploadedBlob(osBlob);
			System.out.println("the blob is created successfuly & its o/p stream="+osBlob);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return false;
	}

	/**
	 * the uploadBlob fun needs the following i/ps:
	 * - input stream which will copy the data from it 
	 * - output stream which will copy the data to it
	 * & it should return the URL of the uploaded file 
	 */
	public boolean uploadBlob(StorageServiceType ssType) {
		System.out.println("from inside the uploadBlob fun.");
		//System.out.println((ssType.getContainer().getProvider().toUpperCase().toString())+(ssType.getContainer().getProvider().toUpperCase().equals("GAE")));
		if(ssType.getContainer().getProvider().toUpperCase().equals("GAE"))
		{
			OutputStream osBlob=(OutputStream)ssType.getBlob().getUploadedBlob();
			InputStream isBlob=(InputStream)ssType.getBlob().getPhysicalBlob();
			System.out.println("the outputstream of the created blob="+osBlob +" & its input stream="+isBlob);
			//get the blob physical path on the local PC
			try {
					
					return ManifestUtilities.copy(isBlob, osBlob);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println(e.getMessage());
				}
		}
		
		return false;
	}

	public void accessBlob(StorageServiceType ssType) {
		// TODO Auto-generated method stub
		
	}

	
}
