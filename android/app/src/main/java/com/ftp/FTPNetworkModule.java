package com.ftp;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.uimanager.IllegalViewOperationException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;

public class FTPNetworkModule extends ReactContextBaseJavaModule {

    public FTPNetworkModule(ReactApplicationContext reactContext) {
        super(reactContext); //required by React Native
    }

    @NonNull
    @Override
    public String getName() {
        return "FTPNetwork";
    }


    @ReactMethod
    public void downloadFile(Callback errorCallback, Callback successCallback) {
        String server = "bilmarkltd.com";
        int port = 21;
        String user = "tester@bilmarkltd.com";
        String pass = "8T.n,hI[x8}O";

        FTPClient ftpClient = new FTPClient();
        try {

            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // APPROACH #1: using retrieveFile(String, OutputStream)
            String remoteFile1 = "t_file_sample_1.sqlite";
            File path = getReactApplicationContext().getFilesDir();
            File downloadFile1 = new File(path, "myDb.sqlite");
            OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
            boolean success = ftpClient.retrieveFile(remoteFile1, outputStream1);
            outputStream1.close();

            if (success) {
                System.out.println("File #1 has been downloaded successfully.");
                successCallback.invoke("Callback : File downloaded!!"+downloadFile1.getAbsolutePath());
            }

        }  catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
            errorCallback.invoke(ex.getMessage());
        }   finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
