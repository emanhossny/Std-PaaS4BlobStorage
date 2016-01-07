# Std-PaaS4BlobStorage
This is an open source for Std-PaaS generic API for Blob Storage.
It provides a set of generic methods to help cloud developers to implement generic apps which can be 
deployed on any one of the supported PaaS platforms.
At the current state, Std-Paas generic API supports Google App Engine (GAE) and Windows Azue.
The currently available genereic methods are:
- configureStorageService method which takes, as an input, the given XML
manifest. It parses the manifest and stores the parsed data in an instance of
StorageServiceType class. Finally, the StorageServiceType instance is returned as
an output.
- createContainer method takes, as an input, the previously returned
StorageServiceType instance and creates a container using a specific PaaS API
and the parameters defined in the given StorageServiceType instance. The created
container instance is stored in the given StorageServiceType instance. Finally,
this instance is returned as an output.
- createBlob method takes, as an input, the previously returned StorageServiceType
instance and creates a blob using a specific PaaS API and the previously created
container. The created blob is stored in the given StorageServiceType instance.
Finally, this instance is returned as an output.
- uploadBlob method takes, as an input, the previously returned StorageServiceType
instance and uploads the given input stream using a specific PaaS API and the
previously created blob. A URL of the uploaded blob is stored in the given
StorageServiceType instance. Finally, this instance is returned as an output.
- accessBlob method takes, as an input, the previously returned StorageServiceType
instance and uses the URL of an uploaded blob to access it.
