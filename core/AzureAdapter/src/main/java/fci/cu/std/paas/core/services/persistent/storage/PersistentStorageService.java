package fci.cu.std.paas.core.services.persistent.storage;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobContainerPermissions;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.BlobOutputStream;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import fci.cu.std.paas.api.services.persistent.storage.StdPersistentStorage;
import fci.cu.std.paas.api.xml.manifest.Blob;
import fci.cu.std.paas.api.xml.manifest.Container;
import fci.cu.std.paas.api.xml.manifest.ObjectFactory;
import fci.cu.std.paas.api.xml.manifest.PersistentStorageServiceManifest;
import fci.cu.std.paas.api.xml.manifest.StorageServiceAccount;
import fci.cu.std.paas.api.xml.manifest.utilities.ManifestUtilities;
import fci.cu.std.paas.api.xml.services.persistent.storage.StorageServiceType;
/**
 * @date 25/12/2014
 * @author Eman Hossny, FCI-CU
 *
 */

public class PersistentStorageService implements StdPersistentStorage {

	/**
	 * in case of Azure, the configuration method should take a string contains
	 * accountName and Key
	 * --> therefore here, we need to find automatically a method which takes
	 * an input string and output an obj of CloudStroageAccount 
	 */
	public StorageServiceType configureStorageService(String cloudStorageDescribtor) {
		if(cloudStorageDescribtor!=null && !cloudStorageDescribtor.equals(""))
		{
			//parse the given string into XML manifest
			PersistentStorageServiceManifest manifest=ManifestUtilities.parseManifest(cloudStorageDescribtor);
			
			StorageServiceAccount ssAccount=manifest.getStorageServiceAccount();
			System.out.println("Storage Account:"+ssAccount.getAccountName()+"    "+ssAccount.getAccountKey());
			Container container=manifest.getContainer();
			System.out.println("container info:"+container.getName()+"    "+container.getProvider());
			StorageServiceType ssType=new StorageServiceType();
			ssType.setContainer(container);
			ssType.setStorageAccount(ssAccount);
			ssType.setDescription(manifest.getDescription());
			ssType.setStorId(1);
			System.out.println("a storage service object is created "+ssType);
			return ssType;
		}
		return null;
	}

	/**
	 * the createBlob (should be combined with createContainer) & it needs the following i/ps:
	 * - accountName
	 * - accountKey--> those will come from CloudStorageAccount, which will be output from configuration method 
	 * - containerName 
	 * - blobName (the name of the file to be uploaded)
	 * & it should return the output stream of the uploaded blob
	 * the prototype of the generic method is 
	 * com.microsoft.azure.storage.blob.CloudBlobContainer createContainer(java.lang.String storageConnectionString,java.lang.String containerName)
	 */
	
	public boolean createContainer(StorageServiceType ssType) {
		//make sure that the current provider is Azure
		String provider=ssType.getContainer().getProvider();
		String accountName=ssType.getStorageAccount().getAccountName();
		String accountKey=ssType.getStorageAccount().getAccountKey();
		provider=provider.toLowerCase();
		if(provider.equals("azure"))  
		{
			//1. create connection String
			String storageConnectionString ="DefaultEndpointsProtocol=http;" + 
				    "AccountName="+accountName+";AccountKey="+accountKey;
			try {
				//2. create storage account
				CloudStorageAccount stroageAccount=CloudStorageAccount.parse(storageConnectionString);
				//3. Create the blob client.
				CloudBlobClient blobClient = stroageAccount.createCloudBlobClient();
				//4. create the blob container
				// The container name must be lower case
				String containerName=ssType.getContainer().getName().toLowerCase();
			    System.out.println("the container name:"+containerName);
				CloudBlobContainer container = blobClient.getContainerReference(containerName);
			    container.createIfNotExists();
			    //5. Optional: Configure a container for public access
			    //the following code allows you to access the uploaded files from a web browser. 
			    BlobContainerPermissions containerPermissions=new BlobContainerPermissions();
			    containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);
			    container.uploadPermissions(containerPermissions);
			    
			    ssType.getContainer().setPhysicalContainer(container);
			    return true;				
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (StorageException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return false;
	}

	/**
	 * com.microsoft.azure.storage.blob.CloudBlockBlob createBlob(com.microsoft.azure.storage.blob.CloudBlobContainer physicalContainer,java.lang.String BlobName)
	 */
	
	public boolean createBlob(StorageServiceType ssType) {
		CloudBlobContainer container=(CloudBlobContainer)ssType.getContainer().getPhysicalContainer();
		try {
			CloudBlockBlob blob = container.getBlockBlobReference(ssType.getBlob().getName());
			ssType.getBlob().setUploadedBlob(blob);
			return true;		    
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StorageException e) {
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
	 * com.microsoft.azure.storage.blob.BlobOutputStrea uploadBlob(com.microsoft.azure.storage.blob.CloudBlockBlob blob)
	 */
	public boolean uploadBlob(StorageServiceType ssType) {
		InputStream isBlob=(InputStream)ssType.getBlob().getPhysicalBlob();
		System.out.println("blob object:"+isBlob);
		//File source = new File(blobPhysicalPath);
		CloudBlockBlob blob=(CloudBlockBlob)ssType.getBlob().getUploadedBlob();
		try {
			BlobOutputStream blobOS=blob.openOutputStream();
			boolean result= ManifestUtilities.copy(isBlob, blobOS);
			ssType.getBlob().setUploadURI(blob.getUri());
			return result;
		} catch (StorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return false;
	}


	public static void main(String[] args)
	{
		PersistentStorageService ss=new PersistentStorageService();
		String manifest=new String("");
		// read the manifest
		String fileName="E:\\uploads\\manifest.xml";
		manifest=readManifest(fileName);
		System.out.println("manifest="+manifest);
		StorageServiceType ssType=ss.configureStorageService(manifest);
		boolean result=ss.createContainer(ssType);
		System.out.println("the container is created?"+result);
		result=ss.createBlob(ssType);
		System.out.println("the blob is created?"+result);
		result=ss.uploadBlob(ssType);
		System.out.println("the blob is uploaded?"+result);
		
	}

	/**
	 * it aims to read the manifest content from a given file
	 * @param fileName
	 * @return
	 */
	private static String readManifest(String fileName) {
		BufferedReader br = null;
		 
		try {
 
			String manifest="";
			String temp="";
			br = new BufferedReader(new FileReader(fileName));
 
			while ((temp = br.readLine()) != null) {
				manifest+=temp;
			}
 
		return manifest;
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		try {
			if (br != null)br.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
		return null;
	}

	public void accessBlob(StorageServiceType ssType) {
		// TODO Auto-generated method stub
		
	}


	
}
