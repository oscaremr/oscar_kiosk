package org.oscarehr.oscar_apps.util;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.content.AbstractContentBody;

public class ByteArrayBody extends AbstractContentBody
{

	private byte[] byteArray;
	private String fileName;

	public ByteArrayBody(byte[] byteArray, String fileName)
	{
		super("application/octet-stream");
		this.byteArray = byteArray;
		this.fileName = fileName;
	}

	@Override
	public void writeTo(OutputStream outputStream) throws IOException
	{
		outputStream.write(byteArray);
		outputStream.flush();
	}

	@Override
	public String getFilename()
	{
		return(fileName);
	}

	@Override
	public String getCharset()
	{
		return null;
	}

	@Override
	public long getContentLength()
	{
		return(byteArray.length);
	}

	@Override
	public String getTransferEncoding()
	{
		return(MIME.ENC_BINARY);
	}

}
