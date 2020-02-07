package com.rrcc.appcultural20;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FavoritosFragment extends android.support.v4.app.Fragment {

    RecyclerView favoritosRecyclerView;
    post_adapter favoritosAdapter;
    List<Post> PostList;
    //final List<Favoritos> favList = new ArrayList<>();
    final ArrayList<Favoritos> favList = new ArrayList<>();
    boolean vacio;

    //firebase
    FirebaseAuth mAuth;
    FirebaseUser currentUser1;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferencePost;
    DatabaseReference databaseReferenceFav;

    final ArrayList<String> favs = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_favoritos, container, false);
        favoritosRecyclerView = fragmentView.findViewById(R.id.favoritosRV);
        favoritosRecyclerView.setHasFixedSize(true);
        favoritosRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferencePost = firebaseDatabase.getReference("Posts");
        databaseReferenceFav = firebaseDatabase.getReference("Favoritos");

        //iniciamos firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser1 = mAuth.getCurrentUser();

        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        cargarFavoritos();
        alertMultipleChoiceItems();
        //showMessage("aqui"+favList.get(0).getFav1());
        /*if (favList.isEmpty()) {
            alertMultipleChoiceItems();
            cargarPosts();
        } else {
            cargarPosts();
        }*/


        //ocultar el floatingActionButton
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).hideFloatingActionButton();
        }
    }

    private void cargarPosts() {
        // Get List Favoritos from the database
        databaseReferencePost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PostList = new ArrayList<>();
                for (DataSnapshot postsnap : dataSnapshot.getChildren()) {
                    Post post = postsnap.getValue(Post.class);
                    PostList.add(post);
                }
                favoritosAdapter = new post_adapter(getActivity(), PostList);
                favoritosRecyclerView.setAdapter(favoritosAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    // simple metodo para mostrar un Toast general
    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    public void alertMultipleChoiceItems() {
        // where we will store or remove selected items
        final ArrayList<Integer> mSelectedItems = new ArrayList<Integer>();
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Dialog_NoActionBar_MinWidth));
        // set the dialog title
        builder.setTitle("Elige 3 categorias")
                .setMultiChoiceItems(R.array.Categorias, null, new DialogInterface.OnMultiChoiceClickListener() {
                    int count;

                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            // if the user checked the item, add it to the selected items
                            mSelectedItems.add(which);
                            count++;
                        } else if (mSelectedItems.contains(which)) {
                            // else if the item is already in the array, remove it
                            mSelectedItems.remove(Integer.valueOf(which));
                        }
                        if (count == 3) showMessage("gracias");
                        else showMessage("Seleccione solo 3 categorias");
                    }
                })
                // Set the action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String selectedIndex = "";
                        String res = "";

                        for (Integer i : mSelectedItems) {
                            selectedIndex += i + ", ";

                            switch (i) {
                                case 0:
                                    res = "Artesania";
                                    break;
                                case 1:
                                    res = "Cine";
                                    break;
                                case 2:
                                    res = "Comida";
                                    break;
                                case 3:
                                    res = "Danza";
                                    break;
                                case 4:
                                    res = "Deportes";
                                    break;
                                case 5:
                                    res = "Ferias";
                                    break;
                                case 6:
                                    res = "Fiestas";
                                    break;
                                case 7:
                                    res = "Literatura";
                                    break;
                                case 8:
                                    res = "Pintura";
                                    break;
                                case 9:
                                    res = "Teatro";
                                    break;
                                case 10:
                                    res = "Tecnologia";
                                    break;
                                case 11:
                                    res = "Viajes";
                                    break;
                                default:
                                    showMessage("Error al cargar favoritos");
                            }
                            favs.add(res);
                        }
                        addFavoritos();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // removes the AlertDialog in the screen
                    }
                })
                .show();
    }
    private void addFavoritos() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Favoritos").push();
        Favoritos fav = new Favoritos(currentUser1.getUid(),favs.get(0),favs.get(1),favs.get(2));

        showMessage("aqui"+favList.get(0).getFav1());

        // get fav unique ID and upadte fav key
        String key = myRef.getKey();
        fav.setPostKey(key);

        // añade el favoritos al firebase database
        myRef.setValue(fav).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Añadido favoritos correctamente!!");
            }
        });
    }

    private void cargarFavoritos()
    {
        databaseReferenceFav.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot favsnap: dataSnapshot.getChildren()) {
                        Favoritos fav = favsnap.getValue(Favoritos.class);
                        favList.add(fav);
                    }
                    showMessage("aqui1"+favList.get(0).getFav1());
                    showMessage("aqui2"+favList.get(0).getFav2());
                    showMessage("aqui3"+favList.get(0).getFav3());
                }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}