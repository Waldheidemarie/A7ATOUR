package a7aent.com.artour;

import android.net.Uri;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.RequiresApi;

public class hackDTU extends AppCompatActivity {


    private ArFragment arFragment;
    private ImageView fitToScanView;
    ModelRenderable andyRenderable;
    private DatabaseReference ref;
    private ModelRenderable box;
    private ModelRenderable campfire;
    private ViewRenderable ourPoint, ourPoint1, ourPoint2, nav;
    private Button mButton;
    TextToSpeech tts;
    private ImageView trans, vol;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hack_dtu2);



        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment2);




        ViewRenderable
                .builder()
                .setView(this, R.layout.about_place)
                .build()
                .thenAccept(renderable -> ourPoint = renderable);

        ViewRenderable
                .builder()
                .setView(this, R.layout.about_place)
                .build()
                .thenAccept(renderable -> ourPoint1 = renderable);

        ViewRenderable
                .builder()
                .setView(this, R.layout.about_place)
                .build()
                .thenAccept(renderable -> ourPoint2 = renderable);

        ViewRenderable
                .builder()
                .setView(this, R.layout.nav_layout)
                .build()
                .thenAccept(renderable -> nav = renderable);


        ModelRenderable.builder().setSource(this, Uri.parse("tree01.sfb"))

                .build()
                .thenAccept(renderable -> box = renderable);



        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (ourPoint == null) {
                        return;
                    }

                    Toast.makeText(getApplicationContext(), "READY", Toast.LENGTH_LONG).show();
                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());


                    TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
                    node.setParent(anchorNode);
                    node.setRenderable(ourPoint);

                    View v = ourPoint.getView();
                    TextView title = v.findViewById(R.id.placeTitle);
                    TextView desc = v.findViewById(R.id.placeDesc);
                    TextView productDesc = v.findViewById(R.id.productdesc);

                    TextView deals = v.findViewById(R.id.dealstext);
                    Button dealbtn = v.findViewById(R.id.deals);
                    Button products = v.findViewById(R.id.products);
                    ImageView productPic = v.findViewById(R.id.productpic);
                    ScrollView productList = v.findViewById(R.id.productList);
                    TextView nutT = v.findViewById(R.id.nutCrackers);

                    desc.setText("BIO FRESH VEG, PACKETS, COLDRINKS AVAILABLE HERE");
                    Button hide = v.findViewById(R.id.hidebtn);

                   /* hide.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            productPic.setVisibility(View.GONE);
                        }
                    });*/
                    products.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            productList.setVisibility(View.VISIBLE);

                            nutT.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    productPic.setVisibility(View.VISIBLE);
                                    Picasso.get().load(R.drawable.nutcrackers).into(productPic);

                                    productDesc.setVisibility(View.VISIBLE);
                                    productDesc.setText("BRAND : Haldirams\nWeight : 0.4 Pounds\nProduct Dimensions : 12 x 12 x 12\nEnergy : 634kcal\nFats : 50gm\nCarbohydrates : 26g\nPrice : 45 INR");
                                    productPic.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            productPic.setVisibility(View.GONE);
                                            productDesc.setVisibility(View.GONE);
                                        }
                                    });
                                }
                            });
                        }
                    });

                    title.setText("GROCERY SHOP");

                    dealbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deals.setText("50% off on");
                            deals.setVisibility(View.VISIBLE);
                        }
                    });


                    Node nav1 = new Node();
                    nav1.setParent(node);
                    nav1.setLocalPosition(new Vector3(0.0f, -3.0f, -5.0f));
                    Quaternion rotation2 = Quaternion.axisAngle(new Vector3(-1.0f, 0.0f, 0.0f), 90); // rotate Y axis 90 degrees
                    nav1.setLocalRotation(rotation2);
                    nav1.setRenderable(nav);

                    Node nav2 = new Node();
                    nav2.setParent(node);
                    nav2.setLocalPosition(new Vector3(0.0f, -3.0f, -10.0f));
                    Quaternion rotation3 = Quaternion.axisAngle(new Vector3(-1.0f, 0.0f, 0.0f), 90); // rotate Y axis 90 degrees
                    nav2.setLocalRotation(rotation3);
                    nav2.setRenderable(nav);


                        Node nav3 = new Node();
                        nav3.setParent(node);
                        nav3.setLocalPosition(new Vector3(0.0f, -3.0f, -15.0f));
                        Quaternion rotation4 = Quaternion.axisAngle(new Vector3(-1.0f, 0.0f, 0.0f), 90); // rotate Y axis 90 degrees
                        nav3.setLocalRotation(rotation4);
                        nav3.setRenderable(nav);

                    Node nav4 = new Node();
                    nav4.setParent(node);
                    nav4.setLocalPosition(new Vector3(0.0f, -3.0f, -20.0f));
                    Quaternion rotation5 = Quaternion.axisAngle(new Vector3(-1.0f, 0.0f, 0.0f), 90); // rotate Y axis 90 degrees
                    nav4.setLocalRotation(rotation5);
                    nav4.setRenderable(nav);

                    Node nav5 = new Node();
                    nav5.setParent(node);
                    nav5.setLocalPosition(new Vector3(0.0f, -3.0f, -25.0f));
                    Quaternion rotation6 = Quaternion.axisAngle(new Vector3(-1.0f, 0.0f, 0.0f), 90); // rotate Y axis 90 degrees
                    nav5.setLocalRotation(rotation6);
                    nav5.setRenderable(nav);

                    Node nav6 = new Node();
                    nav6.setParent(node);
                    nav6.setLocalPosition(new Vector3(0.0f, -3.0f, -30.0f));
                    Quaternion rotation7 = Quaternion.axisAngle(new Vector3(-1.0f, 0.0f, 0.0f), 90); // rotate Y axis 90 degrees
                    nav6.setLocalRotation(rotation7);
                    nav4.setRenderable(nav);

                    Node nav7 = new Node();
                    nav7.setParent(node);
                    nav7.setLocalPosition(new Vector3(0.0f, -3.0f, -35.0f));
                    Quaternion rotation8 = Quaternion.axisAngle(new Vector3(-1.0f, 0.0f, 0.0f), 90); // rotate Y axis 90 degrees
                    nav7.setLocalRotation(rotation8);
                    nav7.setRenderable(nav);

                    //////////////Second Shop/////////////////////////////////
                    Node node1 = new Node();
                    node1.setParent(node);
                    node1.setLocalPosition(new Vector3(0.0f, 0.0f,-14.0f));
                    node1.setRenderable(ourPoint1);



                    View v1 = ourPoint1.getView();
                    TextView title1 = v1.findViewById(R.id.placeTitle);
                    TextView desc1 = v1.findViewById(R.id.placeDesc);
                    TextView productDesc1 = v1.findViewById(R.id.productdesc);

                    TextView deals1 = v1.findViewById(R.id.dealstext);
                    Button dealbtn1 = v1.findViewById(R.id.deals);
                    Button products1 = v1.findViewById(R.id.products);
                    ImageView productPic1 = v1.findViewById(R.id.productpic);
                    ScrollView productList1 = v1.findViewById(R.id.productList);
                    TextView nutT1 = v1.findViewById(R.id.nutCrackers);

                    nutT1.setText("FUNSKOOL CHESS CLASSIC");
                    productDesc1.setText("MRP : 350 INR, deal price 30% off\nAGE: 6 yrs and above\nTYPE : STRATEGY AND WAR GAME\nSKILLSET : CURIOSITY BUILDING, PROBLEM SOLVING, SOCIAL SKILLS");
                    title1.setText("TOYS SHOP");

                    dealbtn1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deals1.setText("30% Off FUNSKOOL CHESS CLASSIC");
                            deals1.setVisibility(View.VISIBLE);
                        }
                    });

                    products1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            productList1.setVisibility(View.VISIBLE);
                            nutT1.setText("FUNSKOOL CHESS CLASSIC");

                            nutT1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    productPic1.setVisibility(View.VISIBLE);
                                    productDesc1.setVisibility(View.VISIBLE);
                                    Picasso.get().load(R.drawable.ches).into(productPic1);
                                }
                            });
                        }
                    });



                    Node node2 = new Node();
                    node2.setParent(node1);
                    node2.setLocalPosition(new Vector3(8.8f, 0.0f, 0.0f));
                    node2.setRenderable(ourPoint2);


                    View v2 = ourPoint2.getView();
                    TextView title2 = v2.findViewById(R.id.placeTitle);
                    TextView desc2 = v2.findViewById(R.id.placeDesc);
                    TextView deals2 = v2.findViewById(R.id.dealstext);
                    Button dealbtn2 = v2.findViewById(R.id.deals);


                    title2.setText("CLOTHS SHOP");

                    dealbtn2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deals2.setVisibility(View.VISIBLE);
                            deals2.setText("30% off on mens clothing");

                        }
                    });



                    node.select();


                });


    }
}
