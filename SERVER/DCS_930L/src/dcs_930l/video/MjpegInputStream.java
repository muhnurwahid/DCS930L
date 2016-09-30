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
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MjpegInputStream extends DataInputStream {

    protected int mSequence = 0;
    protected int mContentLength = -1;
    protected boolean isContentLengthAvailable = false;
    protected boolean isFirstPass = true;

    public MjpegInputStream(InputStream in) {
        super(new BufferedInputStream(in, MjpegFormat.FRAME_MAX_LENGTH));
    }

    public MjpegFrame readMjpegFrame() throws IOException {
        mark(MjpegFormat.FRAME_MAX_LENGTH);
        int headerLen = MjpegFormat.getStartOfSequence(this,
                JpegFormat.SOI_MARKER);
        if (isFirstPass) {
            isFirstPass = false;
            reset();
            byte[] header = new byte[headerLen];
            readFully(header);
            try {
                mContentLength = MjpegFormat.parseContentLength(header);
                isContentLengthAvailable = true;
            } catch (NumberFormatException nfe) {

            }
        }

        reset();

        if (isContentLengthAvailable) {
            byte[] header = new byte[headerLen];
            readFully(header);

            try {
                mContentLength = MjpegFormat.parseContentLength(header);
            } catch (NullPointerException npe) {
            }

        } else {
            mContentLength = MjpegFormat.getEndOfSeqeunce(this,
                    JpegFormat.EOF_MARKER);
        }
        byte[] frameData = new byte[headerLen + mContentLength];
        reset();
        readFully(frameData);

        return new MjpegFrame(frameData, mContentLength, mSequence++);
    }
}
