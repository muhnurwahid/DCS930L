/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcs_930l.video;

/**
 *
 * @author Dhan
 */
import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.util.Properties;


public class MjpegFormat extends JpegFormat {
	
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String CONTENT_LENGTH = "Content-Length";
	public static final String DELTA_TIME = "Delta-time";
	public static int HEADER_MAX_LENGTH = 100;
	public static int FRAME_MAX_LENGTH = JpegFormat.JPEG_MAX_LENGTH + HEADER_MAX_LENGTH;
	public static int parseContentLength(byte[] headerBytes)
		throws IOException, NumberFormatException {
		return parseContentLength(new ByteArrayInputStream(headerBytes));
	}

	private static int parseContentLength(ByteArrayInputStream headerIn)
		throws IOException, NumberFormatException {
		Properties props = new Properties();
		props.load(headerIn);

		return Integer.parseInt(props.getProperty(MjpegFormat.CONTENT_LENGTH));
	}
}
