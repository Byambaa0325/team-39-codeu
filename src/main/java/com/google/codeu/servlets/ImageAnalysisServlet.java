package com.google.codeu.servlets;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * When the user submits the form, Blobstore processes the file upload
 * and then forwards the request to this servlet. This servlet can then
 * analyze the image using the Vision API.
 */
@WebServlet("/image-analysis")
public class ImageAnalysisServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        PrintWriter out = response.getWriter();

        // Get the message entered by the user.
        String message = request.getParameter("message");

        // Get the BlobKey that points to the image uploaded by the user.
        BlobKey blobKey = getBlobKey(request, "image");

        // User didn't upload a file, so render an error message.
        if (blobKey == null) {
            out.println("Please upload an image file.");
            return;
        }

        // Get the URL of the image that the user uploaded.
        String imageUrl = getUploadedFileUrl(blobKey);

        // Get the labels of the image that the user uploaded.
        byte[] blobBytes = getBlobBytes(blobKey);

        //Analyze the picture by specified type
        //The default is labeling tasg
        String labelsToPrint = "";
        if ("SafeSearch".equals(request.getParameter("type"))) {
            SafeSearchAnnotation explicityLabels = doSafeSearch(blobBytes);
            labelsToPrint = String.format("<li>adult: %s</li><li>medical: %s</li><li>spoofed: %s</li><li>violence: %s</li><li>racy: %s</li>",
                    explicityLabels.getAdult(),
                    explicityLabels.getMedical(),
                    explicityLabels.getSpoof(),
                    explicityLabels.getViolence(),
                    explicityLabels.getRacy());
        } else {
            List<EntityAnnotation> imageLabels = getImageLabels(blobBytes);
            for (EntityAnnotation label : imageLabels) {
                labelsToPrint += "<li>" + label.getDescription() + " " + label.getScore() + "\n";
            }

        }

        // Output some HTML that shows the data the user entered.
        // A real codebase would probably store these in Datastore.
        response.setContentType("text/html");
        out.println("<p>Here's the image you uploaded:</p>");
        out.println("<a href=\"" + imageUrl + "\">");
        out.println("<img src=\"" + imageUrl + "\" />");
        out.println("</a>");
        out.println("<p>Here are the labels:</p>");
        out.println("<ul>");
        out.println(labelsToPrint);
        out.println("</ul>");
    }

    /**
     * Returns the BlobKey that points to the file uploaded by the user, or null if the user didn't upload a file.
     */
    private BlobKey getBlobKey(HttpServletRequest request, String formInputElementName) {
        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
        List<BlobKey> blobKeys = blobs.get("image");

        // User submitted form without selecting a file, so we can't get a BlobKey. (devserver)
        if (blobKeys == null || blobKeys.isEmpty()) {
            return null;
        }

        // Our form only contains a single file input, so get the first index.
        BlobKey blobKey = blobKeys.get(0);

        // User submitted form without selecting a file, so the BlobKey is empty. (live server)
        BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
        if (blobInfo.getSize() == 0) {
            blobstoreService.delete(blobKey);
            return null;
        }

        return blobKey;
    }

    /**
     * Blobstore stores files as binary data. This function retrieves the
     * binary data stored at the BlobKey parameter.
     */
    private byte[] getBlobBytes(BlobKey blobKey) throws IOException {
        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        ByteArrayOutputStream outputBytes = new ByteArrayOutputStream();

        int fetchSize = BlobstoreService.MAX_BLOB_FETCH_SIZE;
        long currentByteIndex = 0;
        boolean continueReading = true;
        while (continueReading) {
            // end index is inclusive, so we have to subtract 1 to get fetchSize bytes
            byte[] b = blobstoreService.fetchData(blobKey, currentByteIndex, currentByteIndex + fetchSize - 1);
            outputBytes.write(b);

            // if we read fewer bytes than we requested, then we reached the end
            if (b.length < fetchSize) {
                continueReading = false;
            }

            currentByteIndex += fetchSize;
        }

        return outputBytes.toByteArray();
    }

    /**
     * Uses the Google Cloud Vision API to generate a list of labels that apply to the image
     * represented by the binary data stored in imgBytes.
     */
    private List<EntityAnnotation> getImageLabels(byte[] imgBytes) throws IOException {
        ByteString byteString = ByteString.copyFrom(imgBytes);
        Image image = Image.newBuilder().setContent(byteString).build();

        Feature feature = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feature).setImage(image).build();
        List<AnnotateImageRequest> requests = new ArrayList<>();
        requests.add(request);

        ImageAnnotatorClient client = ImageAnnotatorClient.create();
        BatchAnnotateImagesResponse batchResponse = client.batchAnnotateImages(requests);
        client.close();
        List<AnnotateImageResponse> imageResponses = batchResponse.getResponsesList();
        AnnotateImageResponse imageResponse = imageResponses.get(0);

        if (imageResponse.hasError()) {
            System.err.println("Error getting image labels: " + imageResponse.getError().getMessage());
            return null;
        }

        return imageResponse.getLabelAnnotationsList();
    }

    /**
     * Uses the Google Cloud Vision API to detect level of inappropriateness that apply to the image
     * represented by the binary data stored in imgBytes.
     */
    private SafeSearchAnnotation doSafeSearch(byte[] imgBytes) throws IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ByteString byteString = ByteString.copyFrom(imgBytes);

        Image img = Image.newBuilder().setContent(byteString).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.SAFE_SEARCH_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        ImageAnnotatorClient client = ImageAnnotatorClient.create();
        BatchAnnotateImagesResponse batchResponse = client.batchAnnotateImages(requests);
        client.close();
        List<AnnotateImageResponse> imageResponses = batchResponse.getResponsesList();
        AnnotateImageResponse imageResponse = imageResponses.get(0);

        if (imageResponse.hasError()) {
            System.err.println("Error getting image labels: " + imageResponse.getError().getMessage());
            return null;
        }
        return imageResponse.getSafeSearchAnnotation();

    }

    /**
     * Returns a URL that points to the uploaded file.
     */
    private String getUploadedFileUrl(BlobKey blobKey) {
        ImagesService imagesService = ImagesServiceFactory.getImagesService();
        ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(blobKey);
        return imagesService.getServingUrl(options);
    }
}