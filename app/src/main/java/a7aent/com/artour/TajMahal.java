package a7aent.com.artour;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class TajMahal extends AppCompatActivity {


    private ArFragment arFragment;
    private ModelRenderable andyRenderable, head, rightdownarm, hip;
    ViewRenderable viewRenderable, viewRenderable2;

    private WebView mWebview, mWebview2 ;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taj_mahal);


        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        // When you build a Renderable, Sceneform loads its resources in the background while returning
        // a CompletableFuture. Call thenAccept(), handle(), or check isDone() before calling get().
        ModelRenderable.builder()
                .setSource(this, R.raw.taj)
                .build()
                .thenAccept(renderable -> andyRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });


        ViewRenderable.builder()
                .setView(this, R.layout.skeleton_webview)
                .build()
                .thenAccept(renderable -> viewRenderable = renderable);



        ViewRenderable.builder()
                .setView(this, R.layout.skeleton_webview)
                .build()
                .thenAccept(renderable -> viewRenderable2 = renderable);


        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (andyRenderable == null) {
                        return;
                    }

                    // Create the Anchor.
                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    // Create the transformable andy and add it to the anchor.
                    TransformableNode headNode = new TransformableNode(arFragment.getTransformationSystem());
                    headNode.setParent(anchorNode);
                    headNode.setRenderable(andyRenderable);
                    headNode.select();

                    TransformableNode hipNode = new TransformableNode(arFragment.getTransformationSystem());


                    hipNode.setLocalScale(new Vector3(0.5f, 0.5f, 0.5f));

                    hipNode.setParent(headNode);
                    hipNode.setRenderable(viewRenderable);
                    hipNode.select();


                    hipNode.setLocalPosition(new Vector3(0.9f, 0.0f, 0.0f));
                    hipNode.setLocalRotation(Quaternion.axisAngle(new Vector3(0, 1f, 0), -30f));

                    View v = viewRenderable.getView();

                    mWebview = v.findViewById(R.id.skeletonweb);
                    mWebview.getSettings().setJavaScriptEnabled(true);
                    mWebview.loadUrl("https://www.tajmahal.gov.in/");


                    TransformableNode hipnode2 = new TransformableNode(arFragment.getTransformationSystem());
                    hipnode2.setLocalScale(new Vector3(0.5f, 0.5f, 0.5f));

                    hipnode2.setParent(headNode);
                    hipnode2.setRenderable(viewRenderable2);
                    hipnode2.select();


                    hipnode2.setLocalPosition(new Vector3(-0.9f, 0.0f, 0.0f));
                    hipnode2.setLocalRotation(Quaternion.axisAngle(new Vector3(0, 1f, 0), 30f));

                    View v2 = viewRenderable2.getView();

                    mWebview2 = v2.findViewById(R.id.skeletonweb);
                    mWebview2.getSettings().setJavaScriptEnabled(true);
                    mWebview2.loadUrl("https://www.britannica.com/topic/Taj-Mahal");


                }
        );

    }
}
