package com.example.PlantAI;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.PlantAI.databinding.ActivityDiseaseBinding;
import com.example.PlantAI.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Disease extends AppCompatActivity {
    TextView result;
    ImageView imageView;
    Button picture;
    int imageSize=224;
    //private AppBarConfiguration appBarConfiguration;
    private ActivityDiseaseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDiseaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        result=findViewById(R.id.result);
        imageView=findViewById(R.id.imageView);
        picture=findViewById(R.id.btn);

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkSelfPermission(Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED){
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void classifyImage(Bitmap image){
        try {
            Model model = Model.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer=ByteBuffer.allocateDirect(4 * imageSize * imageSize *3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int [] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;
            for(int i=0; i< imageSize; i++){
                for(int j=0;j<imageSize; j++){
                    int val = intValues[pixel++];
                    byteBuffer.putFloat(((val>>16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val>>8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val& 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences= outputFeature0.getFloatArray();
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i=0;i<confidences.length;i++){
                if(confidences[i]>maxConfidence){
                    maxConfidence=confidences[i];
                    maxPos=i;
                }
            }
            String[] classes={"Apple leaf","Apple Rust leaf","Apple Scab Leaf","Bell Pepper Leaf","Bell Pepper Leaf Spot","Blueberry Leaf","Cherry Leaf","Corn Gray Leaf Spot","Corn Leaf Blight","Corn Rust Leaf", "Grape Leaf","Grape Leaf Black Rot","Peach Leaf", "Potato Leaf Early Blight", "Potato Leaf Late Blight", "Raspberry Leaf", "Soyabean Leaf", "Squash Powdery Mildew Leaf", "Strawberry Leaf","Tomato Early Blight Leaf", "Tomato Leaf", "Tomato Leaf Bacterial Spot", "Tomato Leaf Late Blight","Tomato Leaf Mosaic Virus", "Tomato Leaf Yellow Virus", "Tomato Mold Leaf", "Tomato Septoria Leaf Spot"};
            result.setText(classes[maxPos]);

            generatePDFReport(classes[maxPos], maxConfidence);

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void generatePDFReport(String resultText, float confidence) {
        // Load disease image from drawable
        Bitmap diseaseImage;
        diseaseImage = BitmapFactory.decodeResource(getResources(), R.drawable.logo_no_background);

        // Create a new PdfDocument
        PdfDocument document = new PdfDocument();

        // Create a PageInfo
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(210, 297, 1).create();

        // Start a new page
        PdfDocument.Page page = document.startPage(pageInfo);

        // Get the canvas for drawing
        Canvas canvas = page.getCanvas();

        // Create a Paint object for styling
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(12);

        // Create a Paint object for styling
        Paint paint1 = new Paint();
        paint1.setColor(Color.BLUE);
        paint1.setTextSize(8);

        // Create a Paint object for styling
        Paint paint2 = new Paint();
        paint2.setColor(Color.BLUE);
        paint2.setTextSize(4);

        // Create a Paint object for styling
        Paint paint3 = new Paint();
        paint3.setColor(Color.BLACK);
        paint3.setTextSize(8);

        int pageWidth = canvas.getWidth();
        int pageHeight = canvas.getHeight();


        int textStartY = 10; // Initial top margin

        // Add disease image to the PDF
        if (diseaseImage != null) {
            int desiredWidth = 50; // Adjust the width as needed
            int desiredHeight = 50; // Adjust the height as needed
            int centerX = (canvas.getWidth() - desiredWidth) / 2;
            int top = 5; // Adjust the top margin as needed
            Rect destRect = new Rect(centerX, top, centerX + desiredWidth, top + desiredHeight);
            canvas.drawBitmap(diseaseImage, null, destRect, paint);

            // Adjust textStartY to start text below the image
            textStartY += desiredHeight + 10; // Adjust the margin between image and text
        }
        // Draw "Disease Classification Report" centered horizontally on the canvas
        int reportTextWidth = (int) paint.measureText("Disease Classification Report");
        int reportTextStartX = (pageWidth - reportTextWidth) / 2;
        canvas.drawText("Disease Classification Report", reportTextStartX, textStartY+10, paint);

        // Draw other text on the canvas after the report text, centered horizontally
        canvas.drawText("Result: " + resultText, 10, textStartY + 40, paint1);
        canvas.drawText("Confidence: " + String.format(Locale.getDefault(), "%.2f", confidence), 10, textStartY + 50, paint1);

        String timestamp1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        canvas.drawText("Date: " + timestamp1, 130, textStartY + 70, paint1);

        // Add image and text to the bottom-right corner
        Bitmap yourImage = BitmapFactory.decodeResource(getResources(), R.drawable.signature_2023); // Replace R.drawable.your_image with the actual image resource

        int imageWidth = 90; // Adjust the width as needed
        int imageHeight = 25; // Adjust the height as needed

        // Calculate the position for the image and text in the bottom-right corner
        int imageLeft = pageWidth - imageWidth - 10; // Adjust the margin as needed
        int imageTop = pageHeight - imageHeight - 10; // Adjust the margin as needed

        Rect destRectImage = new Rect(imageLeft, imageTop, imageLeft + imageWidth, imageTop + imageHeight);
        canvas.drawBitmap(yourImage, null, destRectImage, paint);

        // Draw text just below the image
        String additionalText = "PlantAI - Developer";
        float textWidth = paint1.measureText(additionalText);
        float textLeft = imageLeft + (imageWidth - textWidth) / 2;
        float textTop = (imageTop + imageHeight) - 10; // Adjust the margin as needed
        canvas.drawText(additionalText, textLeft, textTop + paint3.getTextSize(), paint3);



        // Finish the page
        document.finishPage(page);

        // Save the PDF to external storage
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String fileName = "DiseaseReport_" + timestamp + ".pdf";

            // Save the PDF to the "Downloads" folder using MediaStore API
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Downloads.DISPLAY_NAME, fileName);
            contentValues.put(MediaStore.Downloads.MIME_TYPE, "application/pdf");

            Uri uri = getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);

            try (OutputStream outputStream = getContentResolver().openOutputStream(uri)) {
                document.writeTo(outputStream);

                // Display a Toast message for successful PDF creation
                Toast.makeText(this, "PDF successfully created: " + fileName, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creating PDF. Please try again.", Toast.LENGTH_SHORT).show();
        }

        // Close the document
        document.close();
    }


    @Override
    public void onActivityResult(int requestCode,int resultCode, @Nullable Intent data) {
        if (requestCode==1 && resultCode==RESULT_OK){
            Bitmap image=(Bitmap) data.getExtras().get("data");
            int dimension = Math.min(image.getWidth(), image.getHeight());
            image=ThumbnailUtils.extractThumbnail(image, dimension, dimension);
            imageView.setImageBitmap(image);

            image=Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                classifyImage(image);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //use onBackPressed function
    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }
}