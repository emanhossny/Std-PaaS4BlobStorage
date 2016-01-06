package fci.cu.std.paas.api.services.persistent.storage;

import fci.cu.std.paas.api.xml.services.persistent.storage.StorageServiceType;

/**********************************************************************************
 * this interface aims to provide a set of standard methods to satisfy the 
 * functionalities of the persistent storage service over one of the heterogeneous 
 * PaaS platforms
 * 
 * @date 25/12/2014
 * @author Eman Hossny, FCI-CU
 ********************************************************************************/

public interface StdPersistentStorage {
	
	/**
	 * 
	 * @param cloudStorageDescribtor
	 * 			 A Cloud Stroage Descriptor (manifest) must be provided.
	 * @return XML file An: enriched Cloud Storage Descriptor (manifest).
	 *         The storage_service_ID (storId) will be added to the Manifest.
	 */
     StorageServiceType configureStorageService(String cloudStorageDescribtor);
     boolean createContainer(StorageServiceType ssType);
     boolean createBlob(StorageServiceType ssType);
     boolean uploadBlob(StorageServiceType ssType);
     void accessBlob(StorageServiceType ssType);
     

}
