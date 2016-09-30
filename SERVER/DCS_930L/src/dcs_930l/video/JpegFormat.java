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
import java.io.DataInputStream;
import java.io.IOException;

public class JpegFormat {

    public static final byte[] SOI_MARKER = {(byte) 0xFF, (byte) 0xD8};
    public static final byte[] APP0_MARKER = {(byte) 0xFF, (byte) 0xE0};
    public static final byte[] COM_MARKER = {(byte) 0xFF, (byte) 0xFE};
    public static final byte[] EOF_MARKER = {(byte) 0xFF, (byte) 0xD9};
    public static final byte[] DQT_MARKER = {(byte) 0xFF, (byte) 0xDB};
    public static final byte[] SOF_MARKER = {(byte) 0xFF, (byte) 0xC0};
    public static final byte[] DHT_MARKER = {(byte) 0xFF, (byte) 0xC4};
    public static final byte[] SOS_MARKER = {(byte) 0xFF, (byte) 0xDA};
    public static int JPEG_MAX_LENGTH = 3 * 240 * 352;

    public static int getEndOfSeqeunce(DataInputStream in, byte[] sequence)
            throws IOException {
        int seqIndex = 0; 
        byte c;

        for (int i = 0; i < (MjpegFormat.FRAME_MAX_LENGTH); i++) {
            c = (byte) in.readUnsignedByte(); //read next byte
            if (c == sequence[seqIndex]) {
                seqIndex++;
                if (seqIndex == sequence.length) {
                    return i + 1;
                }
            } else {
                seqIndex = 0;
            }
        }
        return -1;
    }

    /**
     * @param in
     * @param sequence
     * @return
     * @throws java.io.IOException
     */
    public static int getStartOfSequence(DataInputStream in, byte[] sequence)
            throws IOException {
        int end = getEndOfSeqeunce(in, sequence);

        return (end < 0) ? (-1) : (end - sequence.length);
    }
}
