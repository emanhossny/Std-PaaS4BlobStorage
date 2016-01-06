package fci.cu.std.paas.api.xml.manifest.utilities;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.FileNameMap;
import java.net.URLConnection;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import fci.cu.std.paas.api.xml.manifest.ObjectFactory;
import fci.cu.std.paas.api.xml.manifest.PersistentStorageServiceManifest;

public class ManifestUtilities {
	private static final int BUFFER_SIZE = 2 * 1024 *1024;
	/**
	 * this method takes a string to parse it as a JAXB object to be converted to XML
	 * manifest file
	 * @param cloudStorageDescribtor
	 * @return
	 */
	public static PersistentStorageServiceManifest parseManifest(String cloudStorageDescribtor)
	{
		InputStream is = new ByteArrayInputStream(cloudStorageDescribtor.getBytes());
		JAXBContext jaxbContext;
		PersistentStorageServiceManifest manifest=new PersistentStorageServiceManifest();
		//convert the given string into JAXB element to represent the manifest
		try {
			jaxbContext = JAXBContext.newInstance(ObjectFactory.class.getPackage().getName(),
					ObjectFactory.class.getClassLoader());
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			JAXBElement<PersistentStorageServiceManifest> root = jaxbUnmarshaller
					.unmarshal(new StreamSource(is),PersistentStorageServiceManifest.class);
			manifest=root.getValue();
			return manifest;
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * this method take a file URL on the local host and return its content type
	 * @param fileUrl
	 * @return
	 */
	public static String getMimeType(String fileUrl) 
    {
      FileNameMap fileNameMap = URLConnection.getFileNameMap();
      String type = fileNameMap.getContentTypeFor(fileUrl);
      return type;
    }
	
	/**
	 * this method aims to copy data of an input stream into an output stream
	 * @param input
	 * @param output
	 * @throws IOException
	 */
	public static boolean copy(InputStream input, OutputStream output) throws IOException {
       // System.out.println("start copying the file");
		try {
		//	System.out.println("1st line in try");
          byte[] buffer = new byte[BUFFER_SIZE];
          if(input!=null && output!=null)
          {
        	  int bytesRead = input.read(buffer);
        	//  System.out.println("no. of bytes read:"+bytesRead);
	          while (bytesRead > 0) {
	        	output.write(buffer, 0, bytesRead);
		        bytesRead = input.read(buffer);
		      //  System.out.println((new String(buffer)).toString());
		       // System.out.println("no. of bytes read:"+bytesRead);
	          }
	          return true;	        
	      }
          else
        	  System.out.println("the blob input stream or output stream is null");
        }catch(Exception e)
        {
        	System.out.println(e.getMessage());
        }
		finally {
          input.close();
          output.close();
        }
		return false;
      }
	
	public static long getInputStreamLength(InputStream is)
	{
		int count=0;
		byte [] buff=new byte[1024];
		try {
			long temp=is.read(buff);
			while(temp>-1)
			{
				count+=temp;
				temp=is.read(buff);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}


}
