/*
 * Perform Caching logic
 */
package webproxyserver;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author Rajiv K
 */
public class CachePage {

    public CachePage() {
    }

    private static HashMap<String, String> cacheDict = new HashMap<String, String>();

    public static void cacheIt(HttpRequest request, HttpResponse response) throws IOException {
        
        DataOutputStream fileWriter;
        int random=0,Min=10000,Max=99999999;
        random = Min + (int)(Math.random() * ((Max - Min) + 1));
        //Generating a random name with the requested URL and storing it locally
        File cachedFile = new File("cache/" +request.getHost() +"."+ random);
        //Create DataOutputStream object to write the responses to the file
        fileWriter = new DataOutputStream(new FileOutputStream(cachedFile));
        //writeBytes method to take the response in string format and write it to the file
        fileWriter.writeBytes(response.toString());
        //writeBytes method to take the response in byte input format and write it to the file
        //and write the body of the response message
        fileWriter.write(response.body);
        //Create a HashMap with Url and file name, to perform lookups for the cached file
        cacheDict.put(request.URI, cachedFile.getAbsolutePath());
        System.out.println("Caching from: " + request.URI + " to " + cachedFile.getAbsolutePath());
        //Close the DataOutputStream object
        fileWriter.close();
    }

    public static byte[] isCached(String requestUrl) throws IOException {
        File fileCached;
        FileInputStream fileReader;
        String localCachedFile;
        //Array of bytes which are cached in the local file
        byte[] bytesCached;
        //Search the requested URL in HashMap
        if ((localCachedFile = cacheDict.get(requestUrl)) != null) {
            //if a file with the requested URL was sound create a FileInputStream object and load the contents from the
            //locally cached file
            fileCached = new File(localCachedFile);
            fileReader = new FileInputStream(fileCached);
            bytesCached = new byte[(int) fileCached.length()];
            fileReader.read(bytesCached);
            System.out.println("Local Cache found on: '" + requestUrl + "' returning reponse from cache to user");
            return bytesCached;
        } else {
            //if the file is not cached return a zero byte cached array 
            System.out.println("No Local Cache found on the requested URL: '" + requestUrl+"'");
            return bytesCached = new byte[0];
        }

    }
}
