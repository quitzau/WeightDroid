package eu.quitzau.android.weightdroid.utilities;

/**
 * Exception used when writing/reading external file system/sdcard fails.
 * 
 * @author jimmy
 */
public class ExternalFileException extends Exception {

	private static final long serialVersionUID = 1L;

	public ExternalFileException(String detailMessage) {
		super(detailMessage);
	}

	public ExternalFileException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

}
