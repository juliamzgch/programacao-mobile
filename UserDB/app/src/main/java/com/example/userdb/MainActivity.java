package com.example.userdb;

import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.userdb.classes.AppDatabase;
import com.example.userdb.classes.Playlist;
import com.example.userdb.classes.User;
import com.example.userdb.classes.UserListDialogFragment;
import com.example.userdb.classes.UserWithPlaylists;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements
UserListDialogFragment.UserListDialogFragmentListener {

    //*** criar a instancia para a BD SQLite
    private AppDatabase db;
    //*** criar o executor service que vai correr as nossas queries a base de dados
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //*** criar a instancia para a BD SQLite
        db = AppDatabase.getInstance(this);

        //vamos associar o método guardarUser( ao clique do botao add)
        Button buttonAdd = findViewById(R.id.btnAdd);
        buttonAdd.setOnClickListener(view -> guardarUser());

        //vamos associar o método guardarUser( ao clique do botao add)
        Button buttonDelete = findViewById(R.id.btnDelete);
        buttonDelete.setOnClickListener(view -> apagarUser());

        //vamos associar o método guardarUser( ao clique do botao add)
        Button buttonSearch = findViewById(R.id.btnSearch);
        buttonSearch.setOnClickListener(view -> pesquisarUser());

        Button buttonList = findViewById(R.id.btnListAll);
        buttonList.setOnClickListener(view -> {
            executorService.execute(() -> {
                ArrayList<User> users = new ArrayList<>(db.userDao().getAll());
                runOnUiThread(() -> {
                    UserListDialogFragment dialogFragment = UserListDialogFragment.newInstance(users);
                    dialogFragment.show(getSupportFragmentManager(), "fragment_user_list_dialog");
                });
            });
        });

        EditText editTextSearch = findViewById(R.id.search);
        editTextSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // call the method to which needed to handle the info inserted on editTextSearch
                String searchTerm = v.getText().toString();
                executorService.execute(() -> {
                    List<User> users = db.userDao().findByName(searchTerm);
                    runOnUiThread(() -> {
                        Toast.makeText(MainActivity.this,
                                getResources().getQuantityString(R.plurals.users_found,
                                        users.size(),
                                        users.size()),
                                Toast.LENGTH_SHORT).show();
                        if (!users.isEmpty()) { mostrarUser(users.get(0)); }
                    });
                });
                return true;
            }
            return false;
        });

    }

    //metodo para inserir um user na DB a partir dos dados das widgets
    private void guardarUser() {
        //obter referencias para widgets
        EditText editNome = findViewById(R.id.editName);
        EditText editEmail = findViewById(R.id.editEmail);
        EditText editPassword = findViewById(R.id.editPassword);

        String nome = editNome.getText().toString();
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        //validar os campos para verficar se estao convenientemente preenchidos
        if (nome.equals("")) {
            Toast.makeText(this, R.string.missing_name, Toast.LENGTH_SHORT).show();
        } else if (email.equals("")) {
            Toast.makeText(this, R.string.missing_email, Toast.LENGTH_SHORT).show();
        } else if (password.equals("")) {
            Toast.makeText(this, R.string.missing_password, Toast.LENGTH_SHORT).show();
        } else {
            // inserir novo User na BD:
            //primeiro cria-se um novo objeto User
            User usr1 = new User(nome, email, password);
            //e depois insere-se ou atualiza-se mesmo no objeto da DB
            executorService.execute(() -> {
                List<User> users = db.userDao().getAll();
                int indiceUpdate = searchUserByName(users, nome, password);
                if (indiceUpdate != -1) {
                    usr1.setId(users.get(indiceUpdate).getId());
                    db.userDao().update(usr1);
                    runOnUiThread(() ->
                            Toast.makeText(this, R.string.user_updated, Toast.LENGTH_SHORT).show()
                    );
                } else {
                    db.userDao().insert(usr1);
                    runOnUiThread(() ->
                            Toast.makeText(this, R.string.user_inserted, Toast.LENGTH_SHORT).show()
                    );
                }
            });
        }
    }

    // metodo para fazer delete a um User na BD caso seja encontrado user com os dados
    // de username e password preenchido nas widgets
    private void apagarUser() {
        // bastará que o username e a password estejam na BD!!
        // é apenas um exemplo hipotético!

        // obter referencias para widgets
        EditText editNome = findViewById(R.id.editName);
        EditText editPassword = findViewById(R.id.editPassword);

        String nome = editNome.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        executorService.execute(() -> {
            List<User> users = db.userDao().getAll();
            int indiceDelete = searchUserByName(users, nome, password);
            if (indiceDelete != -1) {
                // delete one user: the first one with the username and password inserted
                User usr = users.get(indiceDelete);
                db.userDao().delete(usr);
                runOnUiThread(() -> {
                    mostrarUser(null);
                    Toast.makeText(this, R.string.user_deleted, Toast.LENGTH_SHORT).show();
                });
            } else {
                runOnUiThread(() -> {
                    Toast.makeText(this, R.string.user_not_found, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    // metodo que preenche as widgets com os dados do User selecionado, etc.
    // caso o parametro user seja null, entao este metodo serve para limpar as widgets
    // de dados sendo usado assim no apagarUser()
    private void mostrarUser(User user) {
        // obter referencias para widgets
        EditText editNome = findViewById(R.id.editName);
        EditText editEmail = findViewById(R.id.editEmail);
        EditText editPassword = findViewById(R.id.editPassword);

        if (user == null) {
            editNome.setText("");
            editEmail.setText("");
            editPassword.setText("");
        } else {
            editNome.setText(user.getUserName());
            editEmail.setText(user.getEmail());
            editPassword.setText(user.getPassword());
        }
    }

    // metodo para ilustrar apenas como se poderia pesquisar usandos
    // os dados que ja estao nas widgets
    // serve apenas para exemplificar...
    private void pesquisarUser() {
        // é apenas um exemplo hipotético!
        // bastará que o username e a password estejam na BD!

        // obter referencias para widgets
        EditText editNome = findViewById(R.id.editName);
        EditText editEmail = findViewById(R.id.editEmail);
        EditText editPassword = findViewById(R.id.editPassword);

        String nome = editNome.getText().toString();
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        executorService.execute(() -> {
            List<User> users = db.userDao().getAll();
            int indiceDelete = searchUserByName(users, nome, password);
            if (indiceDelete != -1) {
                runOnUiThread(() ->
                    Toast.makeText(this, R.string.user_found1, Toast.LENGTH_SHORT).show()
                );
            } else {
                runOnUiThread(() ->
                        Toast.makeText(this, R.string.user_not_found, Toast.LENGTH_SHORT).show()
                );
            }

            indiceDelete = searchUserByEmail(users, email, password);
            if (indiceDelete != -1) {
                runOnUiThread(() ->
                        Toast.makeText(this, R.string.user_found2, Toast.LENGTH_SHORT).show()
                );
            } else {
                runOnUiThread(() ->
                        Toast.makeText(this, R.string.user_not_found, Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    private int searchUserByName(List<User> list, String username, String password) {
        int indiceDelete = -1;
        for (User usr : list) {
            if (usr.getUserName().equals(username) && usr.getPassword().equals(password))
                indiceDelete = list.indexOf(usr);
        }
        return indiceDelete;
    }

    private int searchUserByEmail(List<User> list, String email, String password) {
        int indiceDelete = -1;
        for (User usr : list) {
            if (usr.getEmail().equals(email) && usr.getPassword().equals(password))
                indiceDelete = list.indexOf(usr);
        }
        return indiceDelete;
    }

    @Override
    public void onUserSelected(User user) {
        mostrarUser(user);
        executorService.execute(() -> {
            UserWithPlaylists userWithPlaylists =
                    db.playlistDao().getUserWithPlaylists(user.getId());

            StringBuilder stringBuilder = new StringBuilder();
            for (Playlist p : userWithPlaylists.getPlaylists()) {
                stringBuilder.append(p.getName());
                stringBuilder.append("\n");
            }
            runOnUiThread(() ->
                    Toast.makeText(this,
                            "User Playlists: " + stringBuilder.toString(),
                            Toast.LENGTH_LONG).show());
        });
    }
}