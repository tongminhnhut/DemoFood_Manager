package com.tongminhnhut.orderfood_manager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import com.tongminhnhut.orderfood_manager.Common.Common;
import com.tongminhnhut.orderfood_manager.Interface.ItemClickListener;
import com.tongminhnhut.orderfood_manager.ViewHolder.BannerViewHolder;
import com.tongminhnhut.orderfood_manager.model.Banner;
import com.tongminhnhut.orderfood_manager.model.Foods;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import info.hoang8f.widget.FButton;

public class BannerActivity extends AppCompatActivity {
    RecyclerView recyclerView ;
    RecyclerView.LayoutManager layoutManager ;
    FirebaseDatabase mData ;
    FirebaseStorage storage ;
    DatabaseReference foodsList ;
    StorageReference storageReference ;

    FloatingActionButton btnAdd ;
    FirebaseRecyclerAdapter<Banner, BannerViewHolder> adapter ;

    MaterialEditText edtName, edtID ;
    FButton btnSelect, btnUpload ;
    Uri saveUri;

    Banner newBanner ;
    RelativeLayout relativeLayout ;
    private final int PICK_IMAGE_REQUEST = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        mData = FirebaseDatabase.getInstance();
        foodsList = mData.getReference("Banner");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        recyclerView  = findViewById(R.id.recylerBanner);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        relativeLayout = findViewById(R.id.relativelayoutFood);

        btnAdd = findViewById(R.id.btnAdd_Banner);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAddBanner();

            }
        });

        loadListBanner();


    }

    private void loadListBanner() {
        FirebaseRecyclerOptions<Banner> options = new FirebaseRecyclerOptions.Builder<Banner>()
                .setQuery(foodsList, Banner.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<Banner, BannerViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BannerViewHolder holder, int position, @NonNull Banner model) {
                holder.txtName.setText(model.getName());
                Picasso.with(BannerActivity.this).load(model.getImage()).into(holder.imgHinh);

            }

            @Override
            public BannerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_item, parent, false);
                return new BannerViewHolder(view);
            }
        };
        adapter.notifyDataSetChanged();
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.startListening();
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select picture"), PICK_IMAGE_REQUEST);

    }


    private void showDialogAddBanner() {
        AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
        mDialog.setTitle("Update category");
        mDialog.setMessage("Fill full information");
        mDialog.setIcon(R.drawable.ic_laptop_black_24dp);

        LayoutInflater inflater = this.getLayoutInflater();
        View food_layout = inflater.inflate(R.layout.add_banner_layout, null);
        edtName = food_layout.findViewById(R.id.edtFoodName_AddBanner);
        edtID = food_layout.findViewById(R.id.edtFoodID_AddBanner);
        btnSelect = food_layout.findViewById(R.id.btnSelect_AddBanner);
        btnUpload = food_layout.findViewById(R.id.btnUpload_AddBanner);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upLoadImage();
            }
        });
        mDialog.setView(food_layout);
        mDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        //set Button
        mDialog.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                if (newBanner !=null) {
                    foodsList.push().setValue(newBanner);
                    Snackbar.make(relativeLayout, "New banner " + newBanner.getName() + " was added ", Snackbar.LENGTH_LONG).show();
                }




            }
        });

        mDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                newBanner = null
;

            }
        });

        mDialog.show();

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.UPDATE)){
            showUpDateDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
        }
        else if (item.getTitle().equals(Common.DELETE)){
            deleteFood(adapter.getRef(item.getOrder()).getKey());

        }
        return super.onContextItemSelected(item);
    }

    private void deleteFood(String key) {
        foodsList.child(key).removeValue();
    }

    private void showUpDateDialog(final String key, final Banner item) {
        AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
        mDialog.setTitle("Edit banner");
        mDialog.setMessage("Fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View food_layout = inflater.inflate(R.layout.add_banner_layout, null);
        edtName = food_layout.findViewById(R.id.edtFoodName_AddBanner);
        edtID = food_layout.findViewById(R.id.edtFoodID_AddBanner);
        btnSelect = food_layout.findViewById(R.id.btnSelect_AddBanner);
        btnUpload = food_layout.findViewById(R.id.btnUpload_AddBanner);

        edtName.setText(item.getName());
        edtID.setText(item.getId());
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage(item);
            }
        });

        mDialog.setView(food_layout);
        mDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        //set Button
        mDialog.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();



                // Cập nhật lại thông tin loại menu
                item.setName(edtName.getText().toString());
                item.setId(edtID.getText().toString());

                Map<String, Object> update = new HashMap<>();
                update.put("id", item.getId());
                update.put("name", item.getName());
                update.put("image", item.getImage());
                foodsList.child(key).updateChildren(update);
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                Snackbar.make(relativeLayout,"Updated !", Snackbar.LENGTH_LONG ).show();
//                                loadListBanner();
//                            }
//                        });
                Snackbar.make(relativeLayout,"Food  "+item.getName()+ "  was edited ", Snackbar.LENGTH_LONG ).show();




            }
        });

        mDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();


            }
        });

        mDialog.show();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode ==RESULT_OK && data!=null && data.getData()!=null)
        {
            saveUri=data.getData();
            btnSelect.setText("Image Selected");
        }
    }

    private void upLoadImage() {
        if (saveUri != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading . . . ");
            progressDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(BannerActivity.this, "Upload Success !", Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
//                            foods = new Category(edtName.getText().toString().trim(), String.valueOf(taskSnapshot.getDownloadUrl()));
                            String name = edtName.getText().toString().trim();
                            String id = edtID.getText().toString().trim();
                            newBanner = new Banner();
                            newBanner.setName(name);
                            newBanner.setId(id);
                            newBanner.setImage(String.valueOf(taskSnapshot.getDownloadUrl()));
                        }

                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Log.d("AAA", e.getMessage());
                    Toast.makeText(BannerActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    int progress = (int) (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded "+ progress+ " %");
                }
            });

        }
    }

    // chọn hình ảnh trong  Upd
    private void changeImage(final Banner item) {
        if (saveUri != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading . . . ");
            progressDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(BannerActivity.this, "Upload Success !", Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            item.setImage(String.valueOf(taskSnapshot.getDownloadUrl()));
                        }

                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Log.d("AAA", e.getMessage());
                    Toast.makeText(BannerActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    int progress = (int) (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded "+ progress+ " %");
                }
            });

        }
    }
}
