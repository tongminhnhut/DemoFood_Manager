package com.tongminhnhut.orderfood_manager;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import com.tongminhnhut.orderfood_manager.Common.Common;
import com.tongminhnhut.orderfood_manager.Interface.ItemClickListener;
import com.tongminhnhut.orderfood_manager.ViewHolder.FoodViewHolder;
import com.tongminhnhut.orderfood_manager.model.Category;
import com.tongminhnhut.orderfood_manager.model.Foods;

import java.util.UUID;

import info.hoang8f.widget.FButton;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FoodListActivity extends AppCompatActivity {
    RecyclerView recyclerView ;
    RecyclerView.LayoutManager layoutManager ;
    FirebaseDatabase mData ;
    FirebaseStorage storage ;
    DatabaseReference foodsList ;
    StorageReference storageReference ;

    FloatingActionButton btnAdd ;
    String categoryId = "";
    FirebaseRecyclerAdapter<Foods, FoodViewHolder> adapter ;
    Foods foods ;

    MaterialEditText edtName, edtDescription, edtDiscount, edtPrice ;
    FButton btnSelect, btnUpload ;
    Uri saveUri;
    private final int PICK_IMAGE_REQUEST = 123;

    RelativeLayout relativeLayout ;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set Default font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fs.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        setContentView(R.layout.activity_food_list);
        mData = FirebaseDatabase.getInstance() ;
        foodsList = mData.getReference("Foods");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        recyclerView = findViewById(R.id.recylerFoodList) ;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        relativeLayout = findViewById(R.id.relativelayoutFood);

        btnAdd = findViewById(R.id.btnAdd_FoodList);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAddFoods();

            }
        });
        if (getIntent() != null)
            categoryId = getIntent().getStringExtra("ID");
        if (!categoryId.isEmpty())
            loadListFood(categoryId);


    }

    private void showDialogAddFoods() {
        AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
        mDialog.setTitle("Add new food");
        mDialog.setMessage("Fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View food_layout = inflater.inflate(R.layout.add_new_food, null);

        // init View
        btnSelect = food_layout.findViewById(R.id.btnSelect_AddFood);
        btnUpload = food_layout.findViewById(R.id.btnUpload_AddFood);
        edtName = food_layout.findViewById(R.id.edtName_AddFood);
        edtDescription = food_layout.findViewById(R.id.edtDescription_AddFood);
        edtDiscount = food_layout.findViewById(R.id.edtDiscount_AddFood);
        edtPrice = food_layout.findViewById(R.id.edtPrice_AddFood);




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
        mDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                if (foods !=null)
                {
                    foodsList.push().setValue(foods);
                    Snackbar.make(relativeLayout,"New food "+foods.getName()+ "was added ", Snackbar.LENGTH_LONG ).show();

                }

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

    private void loadListFood(String categoryId) {
        Query query = foodsList.orderByChild("menuId").equalTo(categoryId);
        FirebaseRecyclerOptions<Foods> options = new FirebaseRecyclerOptions.Builder<Foods>()
                .setQuery(query, Foods.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Foods, FoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder viewHolder, int position, @NonNull Foods model) {
                viewHolder.txtName.setText(model.getName());
                Picasso.with(FoodListActivity.this).load(model.getImage()).into(viewHolder.imgHinh);
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });
            }

            @Override
            public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
                return new FoodViewHolder(view);
            }
        };


        adapter.notifyDataSetChanged();
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select picture"), PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.startListening();
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
                    Toast.makeText(FoodListActivity.this, "Upload Success !", Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
//                            foods = new Category(edtName.getText().toString().trim(), String.valueOf(taskSnapshot.getDownloadUrl()));
                            String name = edtName.getText().toString().trim();
                            String description = edtDescription.getText().toString().trim();
                            String discount = edtDiscount.getText().toString().trim();
                            String price = edtPrice.getText().toString().trim() ;
                            foods = new Foods();
                            foods.setName(name);
                            foods.setDescription(description);
                            foods.setDiscount(discount);
                            foods.setPrice(price);
                            foods.setMenuId(categoryId);
                            foods.setImage(String.valueOf(taskSnapshot.getDownloadUrl()));
                        }

                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Log.d("AAA", e.getMessage());
                    Toast.makeText(FoodListActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode ==RESULT_OK && data!=null && data.getData()!=null)
        {
            saveUri=data.getData();
            btnSelect.setText("Image Selected");
        }
    }

    // code Context Menu
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


    // dialog Cập nhật sản ph
    private void showUpDateDialog(final String key, final Foods item) {
        AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
        mDialog.setTitle("Update category");
        mDialog.setMessage("Fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View food_layout = inflater.inflate(R.layout.add_new_food, null);
        edtName = food_layout.findViewById(R.id.edtName_AddFood);
        edtDescription = food_layout.findViewById(R.id.edtDescription_AddFood);
        edtDiscount = food_layout.findViewById(R.id.edtDiscount_AddFood);
        edtPrice = food_layout.findViewById(R.id.edtPrice_AddFood);
        btnSelect = food_layout.findViewById(R.id.btnSelect_AddFood);
        btnUpload = food_layout.findViewById(R.id.btnUpload_AddFood);

        edtName.setText(item.getName());
        edtDiscount.setText(item.getDiscount());
        edtDescription.setText(item.getDescription());
        edtPrice.setText(item.getPrice());

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
        mDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();



                    // Cập nhật lại thông tin loại menu
                    item.setName(edtName.getText().toString());
                    item.setDiscount(edtDiscount.getText().toString());
                    item.setDescription(edtDescription.getText().toString());
                    item.setPrice(edtPrice.getText().toString());
                    foodsList.child(key).setValue(item);
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

    // chọn hình ảnh trong  Upd
    private void changeImage(final Foods item) {
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
                    Toast.makeText(FoodListActivity.this, "Upload Success !", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(FoodListActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
