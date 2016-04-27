package com.example.user.mmc_gamuda;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.github.jorgecastilloprz.FABProgressCircle;
/*
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
*/
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 9/17/15.
 */
public class Util {

    boolean completed;
    String mCurrentPhotoPath;


    public void Util(){


    }


    public boolean writeToDatabase(final String directoryFromRoot, String childNode, String value) {

        Firebase ref = new Firebase("https://sizzling-fire-1548.firebaseio.com/" + directoryFromRoot);
        Firebase.CompletionListener listener = new Firebase.CompletionListener() {

            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {

                if(firebaseError != null){

                    completed = false;
                    writeLog(firebaseError.toString(), directoryFromRoot);

            } else {

                completed = true;
                    writeLog( "Write success", directoryFromRoot);
            }
        }
        };

        //Map<String,Object> map = new HashMap<String,Object>();
        //map.put(childNode,value);
        //ref.updateChildren(map, listener);
        ref.child(childNode).setValue(value,listener);
        //ref.keepSynced(true);
        writeLog(Boolean.toString(completed), directoryFromRoot);
        return completed;
    }

    public void writeToast(String text, Context context){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public void writeLog(String text, String tag){
        Log.i(tag, text);
    }

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    public File createFile() {

        File photoPath = null;

        try {
            photoPath = createImageFile();
        } catch (IOException io){
            Log.e("Capture Img", io.toString());
        }

        return photoPath;
    }

    public String getmCurrentPhotoPath(){
        return mCurrentPhotoPath;
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
/*
    public void writeToGCS(String filename,byte[] content){
        final GcsService gcsService =
                GcsServiceFactory.createGcsService(RetryParams.getDefaultInstance());

        GcsFilename fileName = new GcsFilename("mmc-gamudah.appspot.com",filename);

        GcsOutputChannel outputChannel =
                null;
        try {
            outputChannel = gcsService.createOrReplace(fileName, GcsFileOptions.getDefaultInstance());
            ObjectOutputStream oout =
                    new ObjectOutputStream(Channels.newOutputStream(outputChannel));
            oout.writeObject(ByteBuffer.wrap(content));
            oout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
*/

    public void uploadImg(Context c, String key, File data){


        TransferObserver observer = getAmazonHandle(c).upload(
                "pugapp",     /* The bucket to upload to */
                key,    /* The key for the uploaded object */
                data        /* The file where the data to upload exists */
        );
        observer.setTransferListener(new TransferListener(){

            @Override
            public void onStateChanged(int i, TransferState transferState) {

            }

            @Override
            public void onProgressChanged(int i, long l, long l1) {

            }

            @Override
            public void onError(int i, Exception e) {

            }
        });
    }

    public File downloadImg(Context c, String key){

        File imgData = null;

        TransferObserver observer = getAmazonHandle(c).download(
                "pugapp",     /* The bucket to upload to */
                key,    /* The key for the uploaded object */
                imgData        /* The file where the data to upload exists */
        );

        return imgData;

    }


    private TransferUtility getAmazonHandle(Context c){

        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                c,    /* get the context for the application */
                "us-east-1:9da257a2-d80f-4520-bceb-edbfa926c326",    /* Identity Pool ID */
                Regions.US_EAST_1           /* Region for your identity pool--US_EAST_1 or EU_WEST_1*/
        );

        AmazonS3 s3 = new AmazonS3Client(credentialsProvider);

        s3.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_1));

        TransferUtility transferUtility = new TransferUtility(s3, c);

        return transferUtility;
    }

}
