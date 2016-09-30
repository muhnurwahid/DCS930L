/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcs_930l.video;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;
import dcs_930l.socket.MyAuthenticator;
import dcs_930l.view.MainView;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 *
 * @author Dhan
 */
public class RecordIPCamera {

    String address = "";
    String name = "";
    String[] account;
    MainView mainView;
    boolean status_recorded = true;

    public RecordIPCamera(MainView mainView, String name, String address, String[] account) {
        this.address = address;
        this.name = name;
        this.account = account;
        this.mainView = mainView;
        makeDir("video/" + name);
    }

    private void makeDir(String dirname) {
        File directory = new File(dirname);
        if (directory.exists() && directory.isFile()) {
            System.out.println("Directory Exist !!!");
        } else {
            if (!directory.exists()) {
                directory.mkdir();                
            }
        }
    }

    public boolean getStatusRecorded() {
        return status_recorded;
    }

    private void removeFiles() {
        File file = new File("image/" + name + "/");
        String[] myFiles;
        if (file.isDirectory()) {
            myFiles = file.list();
            for (String myFile1 : myFiles) {
                File myFile = new File(file, myFile1);
                myFile.delete();
            }
        }
    }

    BufferedImage convertToType(BufferedImage sourceImage, int targetType) {
        BufferedImage image;
        if (sourceImage.getType() == targetType) {
            image = sourceImage;
        } else {
            image = new BufferedImage(sourceImage.getWidth(),
                    sourceImage.getHeight(), targetType);
            image.getGraphics().drawImage(sourceImage, 0, 0, null);
        }
        return image;
    }

    public void recordFile(int start) {
        Date date;
        Calendar cal = Calendar.getInstance();

        SimpleDateFormat formatter = new SimpleDateFormat("d/M/y h:mm:ss a");
        SimpleDateFormat formatter1 = new SimpleDateFormat("d-M-y");
        SimpleDateFormat formatter2 = new SimpleDateFormat("h-mm-ss__a");

        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));
        formatter1.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));
        formatter2.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));

        makeDir("video/" + name + "/" + formatter1.format(cal.getTime()));
        makeDir("image/" + name);
        Dimension screenBounds;
        screenBounds = Toolkit.getDefaultToolkit().getScreenSize();
        IMediaWriter writer = ToolFactory.makeWriter("video/" + name + "/" + formatter1.format(cal.getTime()) + "/" + formatter2.format(cal.getTime()) + ".mp4");
        writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_MPEG4, screenBounds.width / 2, screenBounds.height / 2);
        long startTime = System.nanoTime();
        Authenticator.setDefault(new MyAuthenticator(account[0], account[1]));
        URL url;
        MjpegInputStream mis;
        MjpegFrame frame;

        try {
            url = new URL(address);
            try {
                mis = new MjpegInputStream(url.openStream());
                while ((frame = mis.readMjpegFrame()) != null) {
                    date = new Date();
                    cal.setTimeInMillis(date.getTime());
                    File f = new File("image/" + name + "/jpeg-"
                            + start + ".jpg");
                    FileOutputStream out = new FileOutputStream(f);
                    out.write(frame.getJpegBytes());
                    out.close();
                    //ImageIO.write(bf, "jpg", new File("image/image-" + count + ".jpg"));
                    BufferedImage screen = ImageIO.read(f);
                    if (screen != null) {
                        BufferedImage bgrScreen = convertToType(screen, BufferedImage.TYPE_3BYTE_BGR);
                        Graphics g = bgrScreen.getGraphics();
                        g.setColor(Color.WHITE);
                        g.drawString(formatter.format(cal.getTime()), 10, 20);
                        writer.encodeVideo(0, bgrScreen, System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
                        if (cal.getTime().getSeconds() == 59) {
                            break;
                        }
                        try {
                            Thread.sleep((long) (1000 / 25));
                        } catch (InterruptedException e) {
                        }
                        start++;
                    }
                }
                writer.close();
                removeFiles();
            } catch (IOException ex) {
                System.out.println("Error read cam "+ex.getMessage());
            }
        } catch (MalformedURLException ex) {
            JOptionPane.showMessageDialog(null, "Cannot connect to Host : " + address);
            writer.close();
            removeFiles();
            status_recorded=false;
            mainView.deleteCamIfNotFound(address,name);
        }
    }
}
