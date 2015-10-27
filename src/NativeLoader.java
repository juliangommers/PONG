//package org.newdawn.slick;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.jar.JarFile;

import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.util.Log;

/**
 * Solves the long standing issue of managing the native linking libraries by the developer by auto-magically
 * loading them into the Classpath at runtime. This class absolutely <b>must</b> be called before any of the native
 * library code is expected, or a {@link java.lang.UnsatisfiedLinkError}s will not be prevented. The loader can be disabled
 * by setting the property designated by {@link #DISABLE_AUTO_LOADING_PROPERTY} to the String value "true".
 * @author Joshua Mabrey
 * Jul 21, 2012
 */
public class NativeLoader {
	
	/**
	 * Property to disable Native loading by this class
	 */
	public static final String DISABLE_AUTO_LOADING_PROPERTY = "org.newdawn.slick.NativeLoader.load";
	
	/**
	 * The file name of the Linux Natives JAR
	 */
	private static final String LINUX_NATIVE_JAR = "natives-linux.jar";

	/**
	 * The file name of the Mac Natives JAR
	 */
	private static final String MAC_NATIVE_JAR = "natives-mac.jar";
	
	/**
	 * The file name of the Windows Natives JAR
	 */
	private static final String WINDOWS_NATIVE_JAR = "natives-windows.jar";
	
	/**
	 * The default package for a JAR is the name of the JAR without the extension suffix.
	 */
	private static final String LINUX_NATIVES_DEFAULT_PACKAGE = LINUX_NATIVE_JAR.substring(0,LINUX_NATIVE_JAR.indexOf(".jar"));

	/**
	 * The default package for a JAR is the name of the JAR without the extension suffix.
	 */
	private static final String MAC_NATIVES_DEFAULT_PACKAGE = MAC_NATIVE_JAR.substring(0,MAC_NATIVE_JAR.indexOf(".jar"));
	
	/**
	 * The default package for a JAR is the name of the JAR without the extension suffix.
	 */
	private static final String WINDOWS_NATIVES_DEFAULT_PACKAGE = WINDOWS_NATIVE_JAR.substring(0,WINDOWS_NATIVE_JAR.indexOf(".jar"));
	
	/**
	 * The system property to set for the library path; it points to the temp directory
	 * the natives are unpacked into.
	 */
	private static final String LIBRARY_SYSTEM_PROPERTY = "java.library.path";
	
	/**
	 * The system property to set for the LWJGL library path; it points to the temp directory
	 * the natives are unpacked into. This is redundant in reality, but it can't hurt.
	 */
	private static final String LWJGL_LIBRARY_PROPERTY = "org.lwjgl.librarypath";
	
	/**
	 * The system property to set for the Jinput library path; it points to the temp directory
	 * the natives are unpacked into. This is redundant in reality, but it can't hurt.
	 */
	private static final String JINPUT_LIBRARY_PROPERTY = "net.java.games.input.librarypath";
	
	/**
	 * Listing of the file names of the Linux natives
	 */
	private static final String[] LINUX_LIBRARIES = {
		"libjinput-linux.so",
		"libjinput-linux64.so",
		"liblwjgl.so",
		"liblwjgl64.so",
		"libopenal.so",
		"libopenal64.so"
	};
	
	/**
	 * Listing of the file names of the Macintosh natives
	 */
	private static final String[] MAC_LIBRARIES = {
		"libjinput-osx.dylib",
		"liblwjgl.dylib",
		"openal.dylib"
	};
	
	/**
	 * Listing of the file names of the Windows natives
	 */
	private static final String[] WINDOWS_LIBRARIES = {
		"jinput-dx8.dll",
		"jinput-dx8_64.dll",
		"jinput-raw.dll",
		"jinput-raw_64.dll",
		"lwjgl.dll",
		"lwjgl64.dll",
		"OpenAL32.dll",
		"OpenAL64.dll"
	};
	
	/**
	 * Flag used by the loader to prevent double loading. If a loading call succeeds then this flag will be set to
	 * true, and any further calls result in a single boolean comparison/no-op.
	 */
	private static boolean alreadyLoaded = false;
	
	
	/**
	 * The temp directory that the files are placed in. Held in memory so that calls to {@link #destroy()} are able
	 * to delete the directory and it's content libraries. 
	 */
	private static File outputDirectoryHandle = null;
	
	/**
	 * Number of times to try to create temporary directory. Set to a reasonably high number. This insures that we get
	 * a temporary directory in a safe manner, as this algorithm for directory creation prevents several well known
	 * issues involving deadlock with temporary directory creation.
	 */
	private static final int TEMP_DIR_CREATE_ATTEMPTS = 10000;
	
	
	/**
	 * Determines the current operating system, extracts the OS dependent native libraries to a temp directory, and
	 * adds that temporary directory to the classpath. Once the load call succeeds any furthur calls are short-circuted
	 * for performance reasons. 
	 */
	public static final void load(){
			if(!librariesShouldBeLoaded()){
				
				Log.info("Loading native libraries automatically");
				
				int OS = LWJGLUtil.getPlatform();
				try {
					NativeLoader.loadLibraries(OS);
					alreadyLoaded = true;					
				} catch (Exception e) {
					Log.error("Unable to load native libraries. They must be set manually with '-Djava.library.path'",e);
					//We failed. We shouldn't kill the application however, linking *may* have succeeded because of user manually setting location
				}
			}else {
				Log.info("Not automatically loading native libraries.");
			}
	}
	
	/**
	 * If the class has successfully loaded the native libraries, this method attempts to delete the libraries
	 * and the temporary directory, and then reset the class to the unloaded state. Calling this method is absolutely
	 * not guaranteed to remove the files from the file system, and should under no condition be assumed that it does.
	 * 
	 * @return A boolean that will be false if deletion was a definite failure, but true otherwise. True != success
	 */
	public static final boolean destroy(){
		if(alreadyLoaded && outputDirectoryHandle != null){
			//We have to clear the directory or we cannot delete it
			boolean successFlag = true;
			File[] children = outputDirectoryHandle.listFiles();
			for(int iter = 0; iter < children.length; iter++){
				File child = children[iter];
				
				try{
					child.delete();//delete all children
				}catch(Exception e){
					Log.error("Unable to delete temp file from native library temp folder");
					successFlag = false;
				}
				
			}
			
			try{
				outputDirectoryHandle.delete();
			}catch(Exception e){
				Log.error("Unable to delete native library temp folder");
				successFlag = false;
			}
			
			//Reset the class to starting state
			alreadyLoaded = false;
			outputDirectoryHandle = null;
			
			return successFlag;
		}
		
		return false;
	}
	
	/**
	 * Attempts to call the native libraries this class is supposed to automatically load. If it successfully uses the classes
	 * requiring the native libraries, it returns true, otherwise it returns false. This prevents the introduction of NativeLoader
	 * from being a compatibility problem. Also uses the class level loaded flag to short circuit the expensive evaluation
	 * of the loading state of the native libraries.
	 * @return whether the libraries NativeLoader should be loaded.
	 */
	private static final boolean librariesShouldBeLoaded(){
		if(alreadyLoaded){
			return true;//Short circuit evaluation
		}
		
		if(System.getProperty(DISABLE_AUTO_LOADING_PROPERTY, "true").equalsIgnoreCase("false")){
			return true;//Respect user configuration to disable loading
		}
		
		boolean lwjglAvailable = false;
		
		try{
			lwjglAvailable = testLibraryAvailable("lwjgl","lwjgl64");
		}catch(SecurityException e){
			return false;//We shouldn't load libraries automatically if the SecurityManager won't let us
		}
		
		if(lwjglAvailable){
			return true;
		}
		else{
			return false;
		}		
	}
	
	/**
	 * Tests a libraries current availability. If a library is load-able, either in 32 or 64 bit versions, this
	 * method returns true. If libName64 is null then it is assumed that a 64 bit version should not be checked.
	 * Do not attempt to split checks across two different calls if a 64 bit version is being examined.
	 * @param libName The system independent 32 bit library name
	 * @param libName64 The system independent 64 bit library name (optional)
	 * @return The availability of the library, in 32 or 64 bit.
	 * @throws SecurityException if a SecurityManager is preventing library loading
	 */
	private static final boolean testLibraryAvailable(String libName,String libName64) throws SecurityException{
		try{
			System.loadLibrary(libName);
			Log.debug("Library " + libName + " is already available.");
			return true;
		}catch(UnsatisfiedLinkError e){//library not found
			Log.debug("Library " + libName + " is not already available.");
			if(libName64 != null){
				return testLibraryAvailable(libName64, null);
			}
			return false;
		}catch(SecurityException e1){
			SecurityException stopLoadingException = new SecurityException("Unable to load libraries because of a SecurityManager",e1);
			throw stopLoadingException;
		}
	}
	
	/**
	 * Constructs the platform dependent path and names of the native libraries, and passes the loaded streams to 
	 * {@link #writeResourceToFile(InputStream,File)} for output onto the file system.
	 * 
	 * @param platform An integer representing the platform.
	 * @throws IOException If writing the stream to file fails
	 */
	private static final void loadLibraries(int platform) throws IOException{
		outputDirectoryHandle = createTempDir();
		
		String[] libraries;
		String prefix;
		JarFile nativeResourceJar = null;
		
		switch(platform){//switch to get the system dependent library names
			case LWJGLUtil.PLATFORM_LINUX:
					libraries = LINUX_LIBRARIES;
					prefix = LINUX_NATIVES_DEFAULT_PACKAGE + "/";
					nativeResourceJar = new JarFile(new File("libraries/slick/lib/"+LINUX_NATIVE_JAR));
					break;
			case LWJGLUtil.PLATFORM_MACOSX:
					libraries = MAC_LIBRARIES;
					prefix = MAC_NATIVES_DEFAULT_PACKAGE + "/";
					nativeResourceJar = new JarFile(new File("libraries/slick/lib/"+MAC_NATIVE_JAR));
					break;
			case LWJGLUtil.PLATFORM_WINDOWS:
					libraries = WINDOWS_LIBRARIES;
					prefix = WINDOWS_NATIVES_DEFAULT_PACKAGE + "/";
					nativeResourceJar = new JarFile(new File("libraries/slick/lib/"+WINDOWS_NATIVE_JAR));
					break;
			default:
					//If future developers add support for more operating systems without updating the switch then:
					throw new IllegalStateException("Encountered an unknown platform while loading native libraries");
		}
		
		for(int iter = 0; iter < libraries.length;iter++){//Loop through all the libraries and load them
			String fileName = libraries[iter];
			InputStream resourceStream = null;
			try{
				resourceStream = nativeResourceJar.getInputStream(nativeResourceJar.getJarEntry(fileName));
				writeResourceToFile(resourceStream,new File(outputDirectoryHandle,fileName));
			}finally{
				if(resourceStream != null){
					//InputStreams don't need to be closed, but for code consistancy, lets go ahead and close
					resourceStream.close();
				}
			}
		}
		
		String outputDir = outputDirectoryHandle.getAbsolutePath();//Where we put the libraries
		
		//Set the properties to alert the JVM where to find the natives
		System.setProperty(LIBRARY_SYSTEM_PROPERTY,
				outputDir + File.pathSeparator + System.getProperty(LIBRARY_SYSTEM_PROPERTY));//Additive to not overwrite other library paths
		
		//These two aren't additive because we should only point to one version of the libraries
		System.setProperty(LWJGL_LIBRARY_PROPERTY, outputDir);
		System.setProperty(JINPUT_LIBRARY_PROPERTY, outputDir);
				
		Log.info("LIBRARY_SYSTEM_PROPERTY: " + outputDir);
	}
	
	
	/**
	 * Creates a unique directory in the location specified by the java.io.tmpdir system property.
	 * Appends nanosecond precise timestamp for directory name uniqueness, and attempts to create the directory
	 * {@link #TEMP_DIR_CREATE_ATTEMPTS} number of times.
	 * 
	 * @return A File that is the unique temporary directory
	 */
	private static final File createTempDir() {
		  File baseDir = new File(System.getProperty("java.io.tmpdir"));
		  String baseName = "slick-natives-" + System.nanoTime() + "-";

		  for (int counter = 0; counter < TEMP_DIR_CREATE_ATTEMPTS; counter++) {
		    File tempDir = new File(baseDir, baseName + counter);
		    if (tempDir.mkdir()) {
		      return tempDir;
		    }
		  }

		  throw new IllegalStateException("Unable to create temporary directory");
	}
	
	/**
	 * Takes an input stream, intended to be the stream acquired through the Classloader of the native libraries JAR,
	 * and writes it to the specified outputFile. This method will create outputFile if it does not exist on the file system.
	 * This method does not close the InputStream it is given, and leaves that responsibility to the caller.
	 * 
	 * @param resource The InputStream acquired from the JarEntry for the native library
	 * @param outputFile The File to write that stream to.
	 * @throws IOException In the event that the write fails for any reason, or if either input is null.
	 */
	private static final void writeResourceToFile(InputStream resource, File outputFile) throws IOException{
		if(resource == null){
			throw new IOException("Class resource is invalid and null.");
		}
		
		if(outputFile == null){
			throw new IOException("File out handle is invalid and null.");
		}
		
		if(outputFile.isDirectory()){
			throw new IOException("Cannot write native library resource to file because file is a directory.");
		}
		
		if(!outputFile.exists()){
			outputFile.createNewFile();
		}
		
		//Create the things we need to transfer the data from stream to file
		byte[] readBuffer = new byte[1024];
		FileOutputStream fos = null;
		
		try{
			
			fos = new FileOutputStream(outputFile);
			
			while(true){
				int bytesRead = resource.read(readBuffer);
				//By keeping track of the bytes buffered this read, we can avoid clearing the buffer each time
				if(bytesRead == -1){
					//We have reached the end of the file
					break;
				}
				fos.write(readBuffer, 0, bytesRead);//writes the buffer to the file
			}
			
		}finally{
			if(fos != null){
				fos.flush();
				fos.close();//We have to release the stream handle
			}
		}		
	}
}