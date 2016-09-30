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
import java.awt.Image;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.imageio.ImageIO;

public class MjpegFrame {

    byte[] mData;
    int mJpegLen;
    int mSeq;
    Properties mProps;

    public MjpegFrame(byte[] frame, int jpegLen, int sequence) {
        mData = frame;
        mJpegLen = jpegLen;
        mSeq = sequence;
    }
    
    public MjpegFrame(byte[] jpegBytes, int sequence) {
        mJpegLen = jpegBytes.length;
        mSeq = sequence;
        ByteArrayOutputStream out = new ByteArrayOutputStream(jpegBytes.length
                + 200);
        try {
            out.write(createHeader(mJpegLen).toString().getBytes());
            out.write(jpegBytes);
        } catch (IOException ioe) {
        }

        mData = out.toByteArray();
    }

    private StringBuffer createHeader(int contentLength) {
        StringBuffer header = new StringBuffer(100);
        header.append(
                "\r\n\r\n--myboundary\r\nContent-Type: image/jpeg\r\nContent-Length: ");
        header.append(contentLength);
        header.append("\r\n\r\n");

        return header;
    }

    public byte[] getJpegBytes() {
        byte[] jpeg = new byte[mJpegLen];
        System.arraycopy(mData, mData.length - mJpegLen, jpeg, 0, mJpegLen);

        return jpeg;
    }

    public byte[] getHeaderBytes() {
        byte[] header = new byte[mData.length - mJpegLen];
        System.arraycopy(mData, 0, header, 0, header.length);

        return header;
    }
    
    public byte[] getBytes() {
        return mData;
    }

    public int getLength() {
        return mData.length;
    }

    public int getContentLength() {
        return mJpegLen;
    }

    public int getDeltaTime() {
        return Integer.parseInt(getProperties().getProperty(MjpegFormat.DELTA_TIME));
    }

    public Properties getProperties() {
        if (mProps == null) {
            mProps = new Properties();

            try {
                mProps.load(new ByteArrayInputStream(mData, 0,
                        mData.length - mJpegLen));
            } catch (IOException ioe) {
            }
        }

        return mProps;
    }

    public Image getImage() {
        InputStream is = new ByteArrayInputStream(getJpegBytes());

        try {
            return ImageIO.read(is);
        } catch (IOException ioe) {
            return null;
        }
    }
    
    public int getSequence() {
        return mSeq;
    }
}
